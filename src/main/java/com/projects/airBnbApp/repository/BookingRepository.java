package com.projects.airBnbApp.repository;

import com.projects.airBnbApp.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Long> {
    Optional<Booking> findByPaymentSessionId(String sessionId);
}
