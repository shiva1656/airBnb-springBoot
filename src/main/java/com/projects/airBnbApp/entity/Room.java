package com.projects.airBnbApp.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter

public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    //Here it automatically hotel name or id will be kept by the backend and column name will be "hotel_id"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="hotel_id",nullable = false)

    private Hotel hotel;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false,precision = 10,scale = 2)
    private BigDecimal basePrice;

    // This will store URL of images in the bottom field, and actual images will be stored else where in the server
    // We have mention the columnDefination as its smart enough guess what values will be store, this will not happen upper.
    @Column(columnDefinition = "TEXT[]")
    private String[] photos;


    @Column(columnDefinition = "TEXT[]")
    private String[] amenities;

    @Column(nullable = false)
    private Integer totalCount;

    @Column(nullable = false)
    private Integer capacity;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;


}
