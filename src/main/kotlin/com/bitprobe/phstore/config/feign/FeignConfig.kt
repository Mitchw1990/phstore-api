package com.bitprobe.phstore.config.feign

import feign.Client
import feign.httpclient.ApacheHttpClient
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.TrustAllStrategy
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.ssl.SSLContextBuilder
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import java.security.SecureRandom

@Configuration
class FeignClientConfig {

    @Bean
    @Throws(Exception::class)
    fun feignClient(): Client {

        val sslContext = SSLContextBuilder.create()
                .setSecureRandom(SecureRandom())
                .loadTrustMaterial(TrustAllStrategy())
                .build()

        val httpClient = HttpClientBuilder
                .create()
                .setSSLContext(sslContext)
                .setSSLHostnameVerifier(NoopHostnameVerifier())
                .build()

        return ApacheHttpClient(httpClient)
    }

    @Bean
    fun feignDecoder(): SpringDecoder {
        return SpringDecoder { HttpMessageConverters(MappingJackson2HttpMessageConverter()) }
    }
}