package com.chronicare.platform.shared.infrastructure.persistence.jpa.configuration.strategy;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import java.util.Locale;

/**
 * Custom naming strategy for converting entity names to pluralized snake_case table names
 * @summary
 * This strategy converts JPA entity class names to database table names by:
 * - Converting camelCase to snake_case
 * - Pluralizing the table name
 * - Using lowercase
 * 
 * Example: PatientHealthSummary -> patient_health_summaries
 */
public class SnakeCaseWithPluralizedTablePhysicalNamingStrategy implements PhysicalNamingStrategy {

    @Override
    public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return identifier;
    }

    @Override
    public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return identifier;
    }

    @Override
    public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        if (identifier == null) {
            return null;
        }
        String newName = identifier.getText()
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .toLowerCase(Locale.ROOT);
        
        // Simple pluralization (add 's' or handle common cases)
        newName = pluralize(newName);
        
        return Identifier.toIdentifier(newName);
    }

    @Override
    public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        return identifier;
    }

    @Override
    public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnvironment) {
        if (identifier == null) {
            return null;
        }
        String newName = identifier.getText()
                .replaceAll("([a-z])([A-Z])", "$1_$2")
                .toLowerCase(Locale.ROOT);
        return Identifier.toIdentifier(newName);
    }

    /**
     * Simple pluralization logic
     */
    private String pluralize(String word) {
        if (word.endsWith("y") && !word.endsWith("ay") && !word.endsWith("ey") 
            && !word.endsWith("iy") && !word.endsWith("oy") && !word.endsWith("uy")) {
            return word.substring(0, word.length() - 1) + "ies";
        } else if (word.endsWith("s") || word.endsWith("sh") || word.endsWith("ch") 
                   || word.endsWith("x") || word.endsWith("z")) {
            return word + "es";
        } else {
            return word + "s";
        }
    }
}
