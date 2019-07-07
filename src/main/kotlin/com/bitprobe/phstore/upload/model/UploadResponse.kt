package com.bitprobe.phstore.upload.model

import com.bitprobe.phstore.hash.model.HashRecord

data class UploadResponse(
        val url: String,
        val message: String,
        val hashRecord: HashRecord
)