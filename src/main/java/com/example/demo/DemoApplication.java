package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.AccessBits;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.TypeHint;


/*@NativeHint(
        types = {
                @TypeHint(
                        typeNames = "org.bouncycastle.jcajce.provider.asymmetric.RSA$Mappings",
                        access= AccessBits.PUBLIC_CONSTRUCTORS | AccessBits.PUBLIC_METHODS
                ),
                @TypeHint(
                        typeNames = "org.bouncycastle.jcajce.provider.asymmetric.rsa.KeyFactorySpi",
                        access= AccessBits.PUBLIC_CONSTRUCTORS | AccessBits.PUBLIC_METHODS
                )
        })*/
@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

}
