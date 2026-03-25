package com.subodh.airbnb.Service.Interfaces;

import com.subodh.airbnb.Dto.ProfileUpdateRequestDTO;
import com.subodh.airbnb.Dto.UserDTO;
import com.subodh.airbnb.Entities.UserEntity;

public interface UserService {
    UserEntity getUserById(Long id);

    void updateProfile(ProfileUpdateRequestDTO profileUpdateRequestDto);

    UserDTO getMyProfile();
}
