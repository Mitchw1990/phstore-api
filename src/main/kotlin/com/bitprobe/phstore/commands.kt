package com.bitprobe.phstore

import com.github.kilianB.hashAlgorithms.AverageHash
import com.github.kilianB.hashAlgorithms.PerceptiveHash
import com.github.kilianB.matcher.exotic.SingleImageMatcher
import org.springframework.boot.CommandLineRunner
import java.io.File


fun startUpCommand() = CommandLineRunner {

    val img0 = File("classpath:images/file.png")
    val img1 = File("classpath:images/secondFile.jpg")

    val hasher = PerceptiveHash(32)

    val hash0 = hasher.hash(img0)
    val hash1 = hasher.hash(img1)

    val similarityScore = hash0.normalizedHammingDistance(hash1)

    println("Score = $similarityScore")

    if (similarityScore < .2) {

    }

    val matcher = SingleImageMatcher()
    matcher.addHashingAlgorithm(AverageHash(64), .3)
    matcher.addHashingAlgorithm(PerceptiveHash(32), .2)

    println(matcher.checkSimilarity(img0, img1))

    if (matcher.checkSimilarity(img0, img1)) {

    }
}