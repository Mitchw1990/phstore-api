package com.bitprobe.phstore.upload.client

import com.bitprobe.phstore.hash.model.HashRecord
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.RequestMapping

import org.springframework.web.bind.annotation.RequestMethod.POST

@FeignClient(name = "hashClient", url = "\${dev.uris.fireStore}")
interface HashClient {

    @RequestMapping(method = [POST], path = ["/v1/projects/bitprobe-mw/databases/(default)/documents/imageHashes"])
    fun save(photo: HashRecord): String
}