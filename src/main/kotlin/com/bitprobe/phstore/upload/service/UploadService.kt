package com.bitprobe.phstore.upload.service

import com.bitprobe.phstore.hash.model.HashRecord
import com.bitprobe.phstore.upload.model.UploadResponse
import org.springframework.stereotype.Service

@Service
class UploadService {

    public fun upload(name: String, type: String, image: ByteArray): UploadResponse {

        println("received file: $name")

        val hashRecord = HashRecord(
                pHash = "pHash",
                dHash = "dHash",
                aHash = "aHash",
                dHashFastFilter = "dHashPreFilter"
        )

        return UploadResponse("url", "message", hashRecord)
    }
}