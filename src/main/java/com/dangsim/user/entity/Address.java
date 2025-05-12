package com.dangsim.user.entity;

import java.util.Objects;

import com.dangsim.common.exception.errorcode.AddressErrorCode;
import com.dangsim.common.exception.runtime.BaseException;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

	@Column(name = "city")
	private String city;
	@Column(name = "street")
	private String street;
	@Column(name = "zipcode")
	private String zipcode;

	public static Address from(String address) {
		if (Objects.isNull(address) || address.isBlank()) {
			throw new BaseException(AddressErrorCode.ADDRESS_EMPTY);
		}

		String[] addressParts = address.trim().split(" ");
		if (addressParts.length != 3) {
			throw new BaseException(AddressErrorCode.INVALID_ADDRESS_FORMAT);
		}

		return new Address(addressParts[0], addressParts[1], addressParts[2]);
	}

	@Override
	public String toString() {
		return city + " " + street + " " + zipcode;
	}
}
