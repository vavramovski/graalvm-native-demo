/*
 * Copyright (C) 2014 jsonwebtoken.io
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.demo.jsonwebtoken.impl.crypto;

import com.example.demo.jsonwebtoken.SignatureAlgorithm;
import com.example.demo.jsonwebtoken.lang.Assert;

import java.security.Key;

public class DefaultSignerFactory implements SignerFactory {

    public static final SignerFactory INSTANCE = new DefaultSignerFactory();

    @Override
    public Signer createSigner(SignatureAlgorithm alg, Key key) {
        Assert.notNull(alg, "SignatureAlgorithm cannot be null.");
        Assert.notNull(key, "Signing Key cannot be null.");

        if (alg == SignatureAlgorithm.HS256) {
            return new MacSigner(alg, key);
        }
        throw new IllegalArgumentException("The '" + alg.name() + "' algorithm cannot be used for signing.");
    }
}
