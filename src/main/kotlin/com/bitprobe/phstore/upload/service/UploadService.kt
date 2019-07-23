package com.bitprobe.phstore.upload.service

import com.bitprobe.phstore.hash.model.HashRecord
import com.bitprobe.phstore.upload.client.HashClient
import com.bitprobe.phstore.upload.client.ImageClient
import com.bitprobe.phstore.upload.model.ImageForm
import com.bitprobe.phstore.upload.model.StorageResponse
import com.bitprobe.phstore.upload.model.UploadResponse
import feign.form.FormData
import org.springframework.stereotype.Service
import java.util.*

@Service
class UploadService(private val hashClient: HashClient,
                    private val imageClient: ImageClient) {


    fun upload(name: String, type: String, image: ByteArray): UploadResponse {

        println("received file: $name")


        //upload image to firebase storage
        //use the returned link to create hash record

        val file = FormData.builder()
                .contentType("image/jpeg")
                .data(image)
                .fileName("${UUID.randomUUID()}.jpeg")
                .build()

        val response: StorageResponse = imageClient.save(ImageForm(file))

        println(response)

        val hashRecord = HashRecord(
                imageLink = "https://firebasestorage.googleapis.com/v0/b/${response.bucket}/o/${response.name}?alt=media&token=${response.downloadTokens}",
                pHash = "pHash",
                dHash = "dHash",
                aHash = "aHash",
                dHashFastFilter = "dHashPreFilter"
        )

        hashClient.save(hashRecord)

        return UploadResponse("url", "message", hashRecord)
    }
}