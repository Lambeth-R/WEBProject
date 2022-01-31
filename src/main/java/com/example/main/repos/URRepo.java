package com.example.main.repos;

import com.example.main.domain.UR;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface URRepo extends JpaRepository<UR, Integer> {
    UR findByUserID(Integer userID);
}
