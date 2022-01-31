package com.example.main.repos;

import com.example.main.domain.Videofiles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepo extends JpaRepository<Videofiles, String > {
    Videofiles findByMask(String Mask);
}
