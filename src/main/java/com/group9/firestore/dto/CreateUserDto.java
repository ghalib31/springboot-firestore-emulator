package com.group9.firestore.dto;

public record CreateUserDto(
        String firstName,
        String lastName,
        AddressDto address
) {
}
