package com.toptal.soccer.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

//@Converter(autoApply = true)
public class CurrencyConverter implements AttributeConverter<Currency, String> {


    @Override
    public String convertToDatabaseColumn(Currency currency) {
        return currency == null ? null : currency.getSign();
    }

    @Override
    public Currency convertToEntityAttribute(String code) {
        if (code == null) {
            return null;
        }

        return Stream.of(Currency.values())
                .filter(c -> c.getSign().equals(code))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}

