package com.subodh.airbnb.Repositories;

import com.subodh.airbnb.Dto.GuestDTO;
import com.subodh.airbnb.Entities.GuestEntity;
import com.subodh.airbnb.Entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestRepository extends JpaRepository<GuestEntity, Long> {
    List<GuestDTO> findByUser(UserEntity user);
}