package com.bitprobe.phstore.hash.model

data class HashRecord(
        val imageLink: String,
        val pHash: String,
        val aHash: String,
        val dHash: String,
        val dHashFastFilter: String,
        var pHashScore: Long? = 0,
        var aHashScore: Long? = 0,
        var dHashScore: Long? = 0,
        var dHashFastFilterScore: Long? = 0
)