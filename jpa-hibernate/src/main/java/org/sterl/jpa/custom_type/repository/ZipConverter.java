package org.sterl.jpa.custom_type.repository;

import org.sterl.jpa.custom_type.model.Zip;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ZipConverter implements AttributeConverter<Zip, String> {

    @Override
    public String convertToDatabaseColumn(Zip attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public Zip convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Zip(dbData);
    }
}
