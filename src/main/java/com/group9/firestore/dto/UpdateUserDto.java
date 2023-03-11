package com.group9.firestore.dto;

public record UpdateUserDto(
        String firstName,
        String lastName,
        AddressDto address
) {
}
