package com.bitprobe.phstore.image.service

import com.bitprobe.phstore.extensions.collectBytes
import com.bitprobe.phstore.extensions.collectToString
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Profile
import org.springframework.core.io.Resource
import org.springframework.core.io.WritableResource
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
@Profile("!cloud")
class ImageService {

    @Value("\${dev.uris.storage}")
    private val remoteResourceDir: Resource? = null

    @PostConstruct
    fun readFile(): String? {
        val stringResult = this.remoteResourceDir!!.inputStream.collectToString()
        println(stringResult)

        return stringResult
    }

    fun writeFile(file: ByteArray): String? {
        (remoteResourceDir as WritableResource).outputStream.collectBytes(file)
        return "Wrote file successfully. I think..."
    }


    fun readFireBaseStorageFile() {

//        StorageClient.getInstance()

    }
}