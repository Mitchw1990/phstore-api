package com.bitprobe.phstore.repository;

import com.bitprobe.phstore.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface PhotoRepository extends JpaRepository<Photo, Long> {
    boolean existsByMd5(String md5Hash);
    List<Photo> findAllByIdIn(List<Long> ids);
}