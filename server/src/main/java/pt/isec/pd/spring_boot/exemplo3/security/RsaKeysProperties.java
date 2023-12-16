package pt.isec.pd.spring_boot.exemplo3.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
public record RsaKeysProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey) { }