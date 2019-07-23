package com.bitprobe.phstore.extensions

import org.springframework.util.StreamUtils
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset


fun InputStream.collectToString(): String {
    return use { StreamUtils.copyToString(this, Charset.defaultCharset()) }
}

fun OutputStream.collectBytes(byteArray: ByteArray) {
    use { write(byteArray) }
}