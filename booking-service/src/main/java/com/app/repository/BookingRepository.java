package com.app.repository;

import com.app.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer>{

  Optional< List<Booking>> findByPnr(String pnr);

  List<Booking> findByEmail(String email);


}
