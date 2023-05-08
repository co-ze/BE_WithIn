package com.example.within.config;

import lombok.RequiredArgsConstructor;
import org.jasypt.encryption.StringEncryptor;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.SimpleStringPBEConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class JasyptConfig {

    @Value("${jasypt.password}") // withinbest
    private String encryptKey;

    @Bean("jasyptStringEncryptor")
    public StringEncryptor stringEncryptor(){
        PooledPBEStringEncryptor encryptor = new PooledPBEStringEncryptor();
        SimpleStringPBEConfig config = new SimpleStringPBEConfig();
        config.setPassword(encryptKey);
        config.setAlgorithm("PBEWithMD5AndDES");
        config.setKeyObtentionIterations("1000");
        config.setPoolSize("1");

        config.setSaltGeneratorClassName("org.jasypt.salt.RandomSaltGenerator");
        config.setIvGeneratorClassName("org.jasypt.iv.NoIvGenerator");
        config.setStringOutputType("base64");
        encryptor.setConfig(config);
        return encryptor;
    }

//    @Autowired
//    @Qualifier("jasyptStringEncryptor")
//    private StringEncryptor encryptor;
//
//    public void decryptJwtSecretKey() {
//        String encryptedKey = "XrlocOAEi5gbjlTI3IEGWD/c7WcPvWnATDXVh3T5echxbUGUl2TvOyf2KDzvlKBotMXKS1IxIKBwOZ+5SgllaE4mnmrA2Iakfp4+ABhALqNzBigTzAZ7zJ9OHV3dOAxbgiS2jM9YXYFX6JtBpEW7mft7C6xBKnFfvi61uBca+MM=";
//        String decryptedKey = encryptor.decrypt(encryptedKey);
//        System.out.println("Decrypted JWT Secret Key: " + decryptedKey);
//    }
}
