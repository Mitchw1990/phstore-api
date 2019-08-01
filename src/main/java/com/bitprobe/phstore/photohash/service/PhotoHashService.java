package com.bitprobe.phstore.photohash.service;

import com.bitprobe.phstore.entity.Photo;
import com.bitprobe.phstore.entity.PhotoHash;
import com.bitprobe.phstore.repository.HashRepository;
import com.bitprobe.phstore.repository.PhotoRepository;
import com.github.kilianB.hashAlgorithms.AverageHash;
import com.github.kilianB.hashAlgorithms.DifferenceHash;
import com.github.kilianB.hashAlgorithms.PerceptiveHash;
import lombok.val;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import static org.apache.commons.io.FileUtils.readFileToByteArray;

import static java.util.Arrays.stream;

@Configuration
public class PhotoHashService {


    @Bean
    public ApplicationRunner runner(ApplicationContext context,
                                    HashRepository hashRepository,
                                    PhotoRepository photoRepository) {
        return args -> {

            stream(context.getResources("classpath:images/*"))
                    .forEach(image -> {

                                Photo photo;

                                try {

                                    val file = image.getFile();

                                    photo = Photo.builder()
                                            .photoData(readFileToByteArray(file))
                                            .build();
                                    photo = photoRepository.save(photo);

                                    val dHash16 = new DifferenceHash(16, DifferenceHash.Precision.Simple).hash(file);
                                    val dHash64 = new DifferenceHash(64, DifferenceHash.Precision.Triple).hash(file);
                                    val pHash = new PerceptiveHash(64).hash(file);
                                    val aHash = new AverageHash(64).hash(file);

                                    val hash = PhotoHash.builder()
                                            .photoId(photo.getId())
                                            .dHash16(dHash16.getHashValue())
                                            .dHash64(dHash64.getHashValue())
//                                            .pHash(pHash.getHashValue())
                                            .aHash(aHash.getHashValue())
                                            .build();

                                    hashRepository.save(hash);

                                    System.out.println(hash.getDHash16());

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                    );
        };
    }
}
