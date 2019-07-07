package com.bitprobe.phstore.hash.service

import com.bitprobe.phstore.hash.model.HashRecord
import org.springframework.stereotype.Service

@Service
class HashService {

    public fun createHashes(image: ByteArray): HashRecord  {
        return HashRecord("","","","");
    }
}