package org.example.zestindiaassignment.myprofilemodule.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

import java.util.Random;

/**
 * Core ID generation logic.
 * Generates a unique prefixed numeric ID and verifies uniqueness via DB query.
 */
public class SimpleIdGenerator {

    private static final Random RANDOM = new Random();

    /**
     * Generates a unique ID.
     *
     * @param em         EntityManager for DB check
     * @param sqlQuery   Native SQL with one ? placeholder for the ID value
     * @param prefix     Prefix string e.g. "EMP2025-"
     * @param startRange Start of random number range (inclusive)
     * @param endRange   End of random number range (inclusive)
     * @return Unique generated ID string
     */
    public static String generate(
            EntityManager em,
            String sqlQuery,
            String prefix,
            int startRange,
            int endRange) {

        String generatedId;
        int maxAttempts = 100;
        int attempts = 0;

        do {
            if (attempts++ > maxAttempts) {
                throw new RuntimeException(
                        "Failed to generate unique ID after " + maxAttempts +
                        " attempts. Please expand range [" + startRange + " - " + endRange + "]."
                );
            }
            int randomNumber = startRange + RANDOM.nextInt(endRange - startRange + 1);
            generatedId = prefix + String.format("%04d", randomNumber);

        } while (isIdExists(em, sqlQuery, generatedId));

        return generatedId;
    }

    private static boolean isIdExists(EntityManager em, String sqlQuery, String id) {
        Query query = em.createNativeQuery(sqlQuery);
        query.setParameter(1, id);
        return !query.getResultList().isEmpty();
    }
}