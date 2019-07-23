package com.bitprobe.phstore

import com.github.kilianB.hash.Hash
import com.github.kilianB.hashAlgorithms.AverageHash
import com.github.kilianB.hashAlgorithms.DifferenceHash
import com.github.kilianB.hashAlgorithms.PerceptiveHash
import org.springframework.boot.CommandLineRunner
import org.springframework.core.io.ClassPathResource

//aHash (also called Average Hash or Mean Hash). This approach crunches the image into a grayscale 8x8 image and sets the 64 bits in the hash based on whether the pixel's value is greater than the average color for the image.
//pHash (also called "Perceptive Hash"). This algorithm is similar to aHash but use a discrete cosine transform (DCT) and compares based on frequencies rather than color values.
//While aHash focuses on average values and pHash evaluates frequency patterns, dHash tracks gradients.

//The hash values represent the relative change in brightness intensity. To compare two hashes, just count the number of bits that are different. (This is the Hamming distance.) A value of 0 indicates the same hash and likely a similar picture. A value greater than 10 is likely a different image, and a value between 1 and 10 is potentially a variation.
//If the haystack image differs from the needle image by more than 10 bits, then it is assumed to not match

//Results (64 bits for used in all 3)

//aHash results:
//In general, aHash is fast but as accurate.
//aHash generates a huge number of false positives. It matched all of the expected images, but also matched about 10x more false-positives.
//pHash results:
//This algorithm definitely performed the best with regards to accuracy. No false positives, no false negatives, and every match has a score of 2 or less
//note -> evaluate fast vs slow dct
//dHash results:
// 16 bits can be used a fast filter for pHash - looks for a hamming score of zero (0 number of bits that are different)
// can also use 1 to be safe and still filter out a large percentage of comparisons prior to comparing the pHashes
//same speed as aHash and almost as accurate as pHash (4 false positives)
//Correctly found the two expected matches in the db (both had a score of 0)
//the resulting matches: 10, 0, 8, 10, 0
//note -> lowering the threshold to 7 would yield perfect results in this case


//combo approaches:
//I've also combined pHash with dHash. Basically, I use the really fast dHash as a fast filter. If dHash matches, then I compute the more expensive pHash value. This gives me all the speed of dHash with all the accuracy of pHash.
//At a billion images, it would be worth storing the 16-bit dHash, 64-bit dHash, and 64-bit pHash values.
//But a million images? I'm still ahead just by pre-computing the 16-bit dHash and 64-bit pHash values.


fun startUpCommand() = CommandLineRunner {

    val img0 = ClassPathResource("images/steve.jpg").file
//    val img1 = ClassPathResource("images/jesse.png").file
    val img1 = ClassPathResource("images/jesse2.png").file

    listOf(
            AverageHash(64),
            PerceptiveHash(64),
            DifferenceHash(64, DifferenceHash.Precision.Triple),
            DifferenceHash(16, DifferenceHash.Precision.Simple)
    ).map {
        val hash1 = it.hash(img0)
        val hash2 = it.hash(img1)

        println("$it score = ${hash1.normalizedHammingDistance(hash2)}")
    }
}

private fun Hash.toHexString(): String {
    return hashValue.toString(16);
}

//fun main(args: Array<String>) {
//    val commandLineRunner = startUpCommand()
//    commandLineRunner.run()
//}