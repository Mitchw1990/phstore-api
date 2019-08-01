package com.bitprobe.phstore.repository;

import com.bitprobe.phstore.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface PhotoRepository extends JpaRepository<Photo, Long> {

}
