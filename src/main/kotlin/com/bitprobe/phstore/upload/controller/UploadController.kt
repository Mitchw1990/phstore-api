package com.bitprobe.phstore.upload.controller

import com.bitprobe.phstore.upload.model.UploadResponse
import com.bitprobe.phstore.upload.service.UploadService
import org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController


@RestController("/api/v1/upload")
class UploadController(private val uploadService: UploadService) {

    @PostMapping(consumes = [MULTIPART_FORM_DATA_VALUE])
    fun upload(@RequestPart name: String,
               @RequestPart type: String,
               @RequestPart file: ByteArray): UploadResponse {

        return uploadService.upload(name, type, file)
    }
}