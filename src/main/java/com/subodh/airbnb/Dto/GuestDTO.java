package com.subodh.airbnb.Dto;

import com.subodh.airbnb.Entities.UserEntity;
import com.subodh.airbnb.Enums.Gender;
import lombok.Data;

@Data
public class GuestDTO {
    private Long id;
    private String name;
    private Gender gender;
    private Integer age;
}
