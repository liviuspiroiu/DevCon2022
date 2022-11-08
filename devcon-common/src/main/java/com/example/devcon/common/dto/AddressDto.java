package com.example.devcon.common.dto;

import com.example.devcon.common.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private String address1;
    private String address2;
    private String city;
    private String postcode;
    private String country;

    public Address createFromDto() {
        return new Address(
                this.getAddress1(),
                this.getAddress2(),
                this.getCity(),
                this.getPostcode(),
                this.getCountry()
        );
    }
}
