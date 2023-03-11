package com.group9.firestore.dto;

public record AddressDto(
        String streetName,
        int houseNumber,
        String zipCode,
        String city
) {
}
