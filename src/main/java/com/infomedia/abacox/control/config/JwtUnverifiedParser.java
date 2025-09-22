package com.infomedia.abacox.control.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

/**
 * A utility class to parse JWT tokens without verifying their signature.
 * <p>
 * <b>WARNING:</b> The data returned by this class is unverified and should NOT be trusted
 * for any security-sensitive operations. Use it only for logging, debugging, or
 * extracting information like the 'kid' header before verification.
 */
public final class JwtUnverifiedParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> MAP_TYPE_REFERENCE = new TypeReference<>() {};

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private JwtUnverifiedParser() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Parses the JWT and extracts the header claims as a Map.
     *
     * @param jwtToken The JWT string.
     * @return A Map containing the header claims.
     * @throws IllegalArgumentException if the token is invalid or cannot be parsed.
     */
    public static Map<String, Object> getHeaders(String jwtToken) {
        String headerJson = getDecodedJson(jwtToken, 0, "header");
        try {
            return objectMapper.readValue(headerJson, MAP_TYPE_REFERENCE);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not parse JWT header JSON", e);
        }
    }

    /**
     * Parses the JWT and extracts the payload claims as a Map.
     *
     * @param jwtToken The JWT string.
     * @return A Map containing the payload claims.
     * @throws IllegalArgumentException if the token is invalid or cannot be parsed.
     */
    public static Map<String, Object> getClaims(String jwtToken) {
        String payloadJson = getDecodedJson(jwtToken, 1, "payload");
        try {
            return objectMapper.readValue(payloadJson, MAP_TYPE_REFERENCE);
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not parse JWT payload JSON", e);
        }
    }

    /**
     * Splits the JWT, decodes the specified part, and returns it as a JSON string.
     *
     * @param token The JWT string.
     * @param partIndex The index of the part to decode (0 for header, 1 for payload).
     * @param partName The name of the part for error messages.
     * @return The decoded JSON string from the specified part.
     */
    private static String getDecodedJson(String token, int partIndex, String partName) {
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("JWT token cannot be null or empty.");
        }

        String[] parts = token.split("\\.");
        if (parts.length < 2) {
            throw new IllegalArgumentException(
                "Invalid JWT: The token must have at least a header and a payload. Found " + parts.length + " parts."
            );
        }

        try {
            byte[] decodedBytes = Base64.getUrlDecoder().decode(parts[partIndex]);
            return new String(decodedBytes, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalArgumentException("Could not decode Base64Url-encoded JWT " + partName, e);
        }
    }
}