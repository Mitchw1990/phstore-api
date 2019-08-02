package com.bitprobe.phstore.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import oracle.sql.NUMBER;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;

import java.math.BigInteger;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Data
@Table(name = "PHOTO_HASH")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PhotoHash {

    @Id
    @SequenceGenerator(name = "photoHashSequence", sequenceName = "PHOTO_HASH_IDSEQ", allocationSize = 1)
    @GeneratedValue(generator = "photoHashSequence", strategy = SEQUENCE)
    private Long id;
    private Long photoId;
    private byte[] pHash;
    private byte[] aHash;
    private byte[] dHash64;
    private byte[] dHash16;

    @CreationTimestamp
    private Date createdDt;

    @UpdateTimestamp
    private Date updatedDt;
}
