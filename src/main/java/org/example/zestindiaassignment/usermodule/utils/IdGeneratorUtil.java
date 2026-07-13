package org.example.zestindiaassignment.usermodule.utils;

import jakarta.persistence.EntityManager;
import org.example.zestindiaassignment.myprofilemodule.util.SimpleIdGenerator;

/**
 * Utility for generating year-based unique IDs.
 * Format: PREFIX + YEAR + "-" + RANDOM_4_DIGIT_NUMBER
 * Example: USR2025-0042 , EMP2025-1234
 */
public class IdGeneratorUtil {

    public static String generate(EntityManager em,
                                  String prefix,
                                  String tableName,
                                  String columnName,
                                  int startRange,
                                  int endRange) {

        int year = java.time.Year.now().getValue();

        String query = String.format(
                "SELECT 1 FROM %s WHERE %s = ? LIMIT 1",
                tableName,
                columnName
        );

        return SimpleIdGenerator.generate(
                em,
                query,
                prefix + year + "-",
                startRange,
                endRange
        );
    }
}