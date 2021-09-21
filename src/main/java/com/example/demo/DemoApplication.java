package com.example.demo;

import org.bouncycastle.jcajce.provider.asymmetric.RSA;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.nativex.hint.AccessBits;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.TypeHint;

@NativeHint(types = {
        @TypeHint(
                typeNames = "org.bouncycastle.jcajce.provider.asymmetric.rsa.KeyFactorySpi",
                access = AccessBits.PUBLIC_CONSTRUCTORS | AccessBits.PUBLIC_METHODS
        ),
        @TypeHint(
                types = {RSA.class},
                typeNames = "org.bouncycastle.jcajce.provider.asymmetric.RSA$Mappings",
                access = AccessBits.PUBLIC_CONSTRUCTORS | AccessBits.PUBLIC_METHODS
        ),
        @TypeHint(
                types = {BouncyCastleProvider.class},
                access = AccessBits.PUBLIC_CONSTRUCTORS | AccessBits.PUBLIC_METHODS
        ),
        @TypeHint(
                typeNames = "org.bouncycastle.jcajce.provider.asymmetric.rsa.PSSSignatureSpi$SHA512withRSA",
                access = AccessBits.PUBLIC_CONSTRUCTORS | AccessBits.PUBLIC_METHODS
        )
})
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, SecurityFilterAutoConfiguration.class})
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

  /*  @Bean
    public BouncyCastleProvider bouncyCastleBean(){
        return new BouncyCastleProvider();
    }*/
}
