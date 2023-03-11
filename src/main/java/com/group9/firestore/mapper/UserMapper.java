package com.group9.firestore.mapper;

import com.group9.firestore.document.Address;
import com.group9.firestore.document.User;
import com.group9.firestore.dto.AddressDto;
import com.group9.firestore.dto.CreateUserDto;
import com.group9.firestore.dto.UpdateUserDto;

import java.util.UUID;

public class UserMapper {

    public static User toUser(CreateUserDto dto) {
        return new User()
                .setId(UUID.randomUUID().toString())
                .setFirstName(dto.firstName())
                .setLastName(dto.lastName())
                .setAddress(toAddress(dto.address()));
    }

    public static User toUser(UpdateUserDto dto, User user) {
        return user
                .setFirstName(dto.firstName())
                .setLastName(dto.lastName())
                .setAddress(toAddress(dto.address()));
    }

    private static Address toAddress(AddressDto dto) {
        return new Address()
                .setCity(dto.city())
                .setHouseNumber(dto.houseNumber())
                .setStreetName(dto.streetName())
                .setZipCode(dto.zipCode());
    }
}
