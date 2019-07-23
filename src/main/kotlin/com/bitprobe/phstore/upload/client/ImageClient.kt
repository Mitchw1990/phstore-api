package com.bitprobe.phstore.upload.client

import com.bitprobe.phstore.upload.model.ImageForm
import com.bitprobe.phstore.upload.model.StorageResponse
import feign.Client
import feign.Feign
import feign.RequestLine
import feign.form.FormEncoder
import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


interface ImageClient {

    @RequestLine("POST /v0/b/bitprobe-mw.appspot.com/o/image")
    fun save(formData: ImageForm): StorageResponse

    @Configuration
    class DcfClientConfig {

        @Bean
        fun imageClient(feignClient: Client,
                        feignDecoder: SpringDecoder): ImageClient {

            return Feign.builder()
                    .client(feignClient)
                    .decoder(feignDecoder)
                    .encoder(FormEncoder())
                    .target(ImageClient::class.java, "https://firebasestorage.googleapis.com")
        }
    }
}