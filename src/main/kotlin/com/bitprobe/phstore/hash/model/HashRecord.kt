package com.bitprobe.phstore.hash.model

data class HashRecord(val pHash: String,
                      val aHash: String,
                      val dHash: String,
                      val dHashPreFiler: String,
                      var pHashScore: Long? = 0,
                      var aHashScore: Long? = 0,
                      var dHashPreFilterScore: Long? = 0,
                      var dHashScore: Long? = 0
)