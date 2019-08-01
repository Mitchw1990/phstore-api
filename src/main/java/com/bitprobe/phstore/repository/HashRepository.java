package com.bitprobe.phstore.repository;

import com.bitprobe.phstore.entity.PhotoHash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface HashRepository extends JpaRepository<PhotoHash, Long> {

}
