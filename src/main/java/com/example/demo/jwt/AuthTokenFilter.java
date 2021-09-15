package com.example.demo.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthTokenFilter extends OncePerRequestFilter {

    private JwtUtils jwtUtils;

    public AuthTokenFilter(UserDetailsService userDetailsService, JwtUtils jwtUtils) {
        this.userDetailsService = userDetailsService;
        this.jwtUtils = jwtUtils;
    }

    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            System.out.printf("UserDetailsService isNull %s%n", (userDetailsService == null));
            System.out.printf("JwtUtils isNull %s%n", (jwtUtils == null));
            System.out.println(request);
            String jwtToken = parseAuthHeader(request);
            if (jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {
//                final String username = new JwtToken(jwtToken).getSubject();
                final String username = jwtUtils.getUserNameFromJwtToken(jwtToken);
                System.out.printf("Username: %s%n", username);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                System.out.printf("UserDetails: %s%n", userDetails);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null,
                        userDetails.getAuthorities());
                System.out.printf("UserPassAuthToken: %s%n", authentication);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                System.err.println("JWT: token not found");
            }
        } catch (Exception e) {
            System.err.println("Cannot set user authentication");
            System.err.println(e.toString());
            e.printStackTrace();
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * @return [username, password]
     */
    private String parseAuthHeader(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");
        System.out.printf("Auth Header: %s%n", headerAuth);

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}

