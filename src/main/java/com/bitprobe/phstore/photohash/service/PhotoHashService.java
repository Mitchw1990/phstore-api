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
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.util.Arrays.stream;
import static org.apache.commons.io.FileUtils.readFileToByteArray;
import static org.springframework.util.DigestUtils.md5DigestAsHex;

@Log4j2
@Configuration
@AllArgsConstructor
public class PhotoHashService {

    ApplicationContext context;
    HashRepository hashRepository;
    PhotoRepository photoRepository;

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            populatePhotos();
            hashPhotos();
        };
    }

    private void hashPhotos() {

        log.info("Starting photo hashing process...");

        var pageNumber = 0;
        var totalPages = getPage(pageNumber).getTotalPages();

        while (pageNumber <= totalPages) {
            getPage(pageNumber++).getContent().forEach(photo -> {

                val image = ImageIO.read(new ByteArrayInputStream(photo.getPhotoData());

                val dHash16 = new DifferenceHash(16, DifferenceHash.Precision.Simple).hash(image);
                val dHash64 = new DifferenceHash(64, DifferenceHash.Precision.Triple).hash(image);
                val pHash = new PerceptiveHash(64).hash(image);
                val aHash = new AverageHash(64).hash(image);

                val hash = PhotoHash.builder()
                        .photoId(photo.getId())
                        .dHash16(dHash16.getHashValue().toByteArray())
                        .dHash64(dHash64.getHashValue().toByteArray())
                        .pHash(pHash.getHashValue().toByteArray())
                        .aHash(aHash.getHashValue().toByteArray())
                        .build();

                 hashRepository.save(hash);

                log.info("Created hash for photo with md5: {}", photo.getMd5Hash());
            });
        }
    }

    private Page<Photo> getPage(int pageNumber) {
        return photoRepository.findAll(PageRequest.of(pageNumber, 20));
    }

    private void populatePhotos() throws IOException {

        log.info("Starting photo population process...");

        stream(context.getResources("classpath:images/*"))
                .forEach(image -> {
                            try {
                                val file = readFileToByteArray(image.getFile());
                                val hash = md5DigestAsHex(file);

                                if (!photoRepository.existsByMd5Hash(hash)) {

                                    var photo = Photo.builder()
                                            .md5Hash(hash)
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
    }
}
