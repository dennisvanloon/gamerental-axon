package com.gamerental.query;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

@Configuration
public class ElasticsearchTestConfiguration extends ElasticsearchConfiguration {

    public static final String ES_USERNAME = "elastic";
    public static final String ES_PASSWORD = "password";

    @Value("${elasticsearch.endpoint}")
    String elasticsearchEndpoint;

    @Value("${elasticsearch.certificate}")
    String elasticsearchCertificate;

    @Override
    public @NotNull ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(elasticsearchEndpoint)
                .usingSsl(getSSLContext())
                .withBasicAuth(ES_USERNAME, ES_PASSWORD)
                .build();
    }

    private SSLContext getSSLContext()  {
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            byte[] decode = elasticsearchCertificate.getBytes();

            Certificate ca;
            try (InputStream certificateInputStream = new ByteArrayInputStream(decode)) {
                ca = cf.generateCertificate(certificateInputStream);
            }

            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
            return context;
        } catch (CertificateException | IOException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }
}