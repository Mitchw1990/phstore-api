package com.bitprobe.phstore.upload.controller

import com.bitprobe.phstore.upload.model.UploadResponse
import com.bitprobe.phstore.upload.service.UploadService
import org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/upload")
@CrossOrigin(origins = ["\${dev.uris.client}"], allowedHeaders = ["*"], methods = [RequestMethod.POST])
class UploadController(private val uploadService: UploadService) {


    @PostMapping(consumes = [MULTIPART_FORM_DATA_VALUE])
    fun upload(@RequestPart name: String,
               @RequestPart type: String,
               @RequestPart image: ByteArray): UploadResponse {

        return uploadService.upload(name, type, image)
    }
}