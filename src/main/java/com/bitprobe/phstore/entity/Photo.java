package com.bitprobe.phstore.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Data
@Table(name = "PHOTO")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Photo {

    @Id
    @SequenceGenerator(name = "photoHashSequence", sequenceName = "PHOTO_IDSEQ", allocationSize = 1)
    @GeneratedValue(generator = "photoHashSequence", strategy = SEQUENCE)
    private Long id;
    private byte[] photoData;
    private String md5Hash;

    @CreationTimestamp
    private Date createdDt;

    @UpdateTimestamp
    private Date updatedDt;
}
