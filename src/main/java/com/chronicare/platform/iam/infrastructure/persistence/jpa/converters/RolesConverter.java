package com.chronicare.platform.iam.infrastructure.persistence.jpa.converters;

import com.chronicare.platform.iam.domain.model.valueobjects.Roles;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class RolesConverter implements AttributeConverter<Roles, String> {

    @Override
    public String convertToDatabaseColumn(Roles role) {
        if (role == null) {
            return null;
        }
        return role.getName();
    }

    @Override
    public Roles convertToEntityAttribute(String name) {
        if (name == null) {
            return null;
        }
        return Stream.of(Roles.values())
                .filter(r -> r.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown role: " + name));
    }
}
