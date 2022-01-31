
package com.example.main.repos;

import com.example.main.domain.Rooms;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepo extends JpaRepository<Rooms, String > {
    Rooms findById(Integer Id);
}
