package com.example.restaurant.repo;

import com.example.restaurant.entity.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {
    List<Reserve> findAllByRestaurantId(Long id);
    List<Reserve> findAllByRestaurantIdAndDate(Long restId, Integer date);
}
