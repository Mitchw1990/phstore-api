package com.bitprobe.phstore.photohash.service;

import com.bitprobe.phstore.entity.Photo;
import com.bitprobe.phstore.entity.PhotoHash;
import com.bitprobe.phstore.repository.HashRepository;
import com.bitprobe.phstore.repository.PhotoRepository;
import com.github.kilianB.hashAlgorithms.AverageHash;
import com.github.kilianB.hashAlgorithms.DifferenceHash;
import com.github.kilianB.hashAlgorithms.PerceptiveHash;
import lombok.AllArgsConstructor;
import lombok.experimental.var;
import lombok.extern.log4j.Log4j2;
import lombok.val;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.UUID;

import static java.util.Arrays.stream;
import static org.apache.commons.io.FileUtils.readFileToByteArray;
import static org.apache.commons.io.FileUtils.writeByteArrayToFile;
import static org.springframework.util.DigestUtils.md5DigestAsHex;

@Log4j2
@Configuration
@AllArgsConstructor
public class PhotoHashService {

    private static final int DISTANCE_THRESHOLD = 15;
    private static final double NORMALIZED_DISTANCE_THRESHOLD = 0.42;
    private static final String OUT_PATH = "/Users/mitch/Desktop/";

    ApplicationContext context;£
    HashRepository hashRepository;
    PhotoRepository photoRepository;

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            findPhoto();
//            seedPhotos();
//            hashPhotos();
        };
    }

    private void hashPhotos() {

        log.info("Starting photo hashing process...");

        var pageNumber = 0;
        var totalPages = getPhotoPage(pageNumber).getTotalPages();

        while (pageNumber <= totalPages) {
            getPhotoPage(pageNumber++).getContent().forEach(photo -> {

                try {
                    val image = ImageIO.read(new ByteArrayInputStream(photo.getPhotoData()));

                    hashRepository.save(hashPhoto(image, photo.getId()));

                    log.info("Created hash for photo with md5: {}", photo.getMd5());
                } catch (IOException e) {
                    log.error("Exception encountered hashing file: {}", e.getMessage());
                }
            });
        }
        log.info("Completed photo hashing process.");
    }

    private PhotoHash hashPhoto(BufferedImage image, Long photoId) {

        val dHash16 = new DifferenceHash(16, DifferenceHash.Precision.Simple).hash(image);
        val dHash64 = new DifferenceHash(32, DifferenceHash.Precision.Simple).hash(image);
        val pHash = new PerceptiveHash(64).hash(image);
        val aHash = new AverageHash(64).hash(image);

        return PhotoHash.builder()
                .photoId(photoId)
                .dHash16(dHash16.getHashValue().toByteArray())
                .dHash64(dHash64.getHashValue().toByteArray())
                .pHash(pHash.getHashValue().toByteArray())
                .aHash(aHash.getHashValue().toByteArray())
                .build();
    }

    private void seedPhotos() throws IOException {

        log.info("Starting photo seeding process...");

        stream(context.getResources("classpath:images/vehicles/*"))
                .forEach(image -> {
                            try {
                                val file = readFileToByteArray(image.getFile());
                                val hash = md5DigestAsHex(file);

                                if (!photoRepository.existsByMd5(hash)) {

                                    var photo = Photo.builder()
                                            .md5(hash)
                                            .photoData(file)
                                            .build();

                                    photoRepository.save(photo);

                                    log.info("Created photo with md5: {}", hash);
                                }

                            } catch (Exception e) {
                                log.error("Exception encountered while writing file to db: {}", e.getMessage());
                            }
                        }
                );
        log.info("Completed photo seeding process.");
    }

    private void findPhoto() throws IOException {

        var pageNumber = 0;
        var totalPages = getHashPage(pageNumber).getTotalPages();
        val matches = new ArrayList<Long>();

        val image = ImageIO.read(context.getResources("classpath:search/*")[0].getFile());

        while (pageNumber <= totalPages) {
            getHashPage(pageNumber++).getContent().forEach(hash -> {

                try {
                    val dHash16Diff = new DifferenceHash(16, DifferenceHash.Precision.Simple).hash(image).hammingDistanceFast(new BigInteger(hash.getDHash16())) / (double) 16;
                    val dHash64Diff = new DifferenceHash(32, DifferenceHash.Precision.Simple).hash(image).hammingDistanceFast(new BigInteger(hash.getDHash64())) / (double) 32;
                    val pHashDiff = new PerceptiveHash(64).hash(image).hammingDistanceFast(new BigInteger(hash.getPHash())) / (double) 64;
                    val aHashDiff = new AverageHash(64).hash(image).hammingDistanceFast(new BigInteger(hash.getAHash())) / (double) 64;

                    if (hasMatchNormalized(dHash16Diff, dHash64Diff, pHashDiff, aHashDiff)) {
                        matches.add(hash.getPhotoId());
                        log.info("Match => {} {} {} {}", dHash16Diff, dHash64Diff, pHashDiff, aHashDiff);
                    }

                } catch (Exception e) {
                    log.error("Exception encountered while searching for file: {}", e.getMessage());
                }
            });
        }

        photoRepository.findAllByIdIn(matches).forEach(photo -> {
            try {
                writeByteArrayToFile(new File(OUT_PATH + UUID.randomUUID().toString() + ".jpeg"), photo.getPhotoData());
            } catch (IOException e) {
                log.error("Exception encountered while writing file.");
            }
        });
    }

    private boolean hasMatch(int... distances) {
        return stream(distances).anyMatch(distance -> distance <= DISTANCE_THRESHOLD);
    }

    private boolean hasMatchNormalized(double... distances) {
        return stream(distances).allMatch(distance -> distance <= NORMALIZED_DISTANCE_THRESHOLD);
    }

    private Page<Photo> getPhotoPage(int pageNumber) {
        return photoRepository.findAll(PageRequest.of(pageNumber, 20));
    }

    private Page<PhotoHash> getHashPage(int pageNumber) {
        return hashRepository.findAll(PageRequest.of(pageNumber, 20));
    }
}
