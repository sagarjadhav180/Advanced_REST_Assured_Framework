package com.framework.utils;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hamcrest.Matcher;

import java.io.File;
import java.io.InputStream;

/**
 * Utility class for JSON Schema validation in contract testing.
 * Validates API responses against predefined JSON schemas.
 */
public final class SchemaValidator {

    private static final Logger logger = LogManager.getLogger(SchemaValidator.class);
    private static final String SCHEMA_BASE_PATH = "schemas/";

    private SchemaValidator() {
    }

    /**
     * Validates a response body against a JSON schema file from classpath.
     */
    public static Matcher<?> matchesSchema(String schemaFileName) {
        logger.info("Validating response against schema: {}", schemaFileName);
        return JsonSchemaValidator.matchesJsonSchemaInClasspath(SCHEMA_BASE_PATH + schemaFileName);
    }

    /**
     * Validates a response body against a JSON schema from a File object.
     */
    public static Matcher<?> matchesSchema(File schemaFile) {
        logger.info("Validating response against schema file: {}", schemaFile.getAbsolutePath());
        return JsonSchemaValidator.matchesJsonSchema(schemaFile);
    }

    /**
     * Validates a response body against a JSON schema from an InputStream.
     */
    public static Matcher<?> matchesSchema(InputStream schemaStream) {
        return JsonSchemaValidator.matchesJsonSchema(schemaStream);
    }

    /**
     * Validates a response body against an inline JSON schema string.
     */
    public static Matcher<?> matchesSchemaString(String schemaContent) {
        logger.info("Validating response against inline schema");
        return JsonSchemaValidator.matchesJsonSchema(schemaContent);
    }

    /**
     * Performs schema validation on a Response object and returns validation result.
     */
    public static boolean isSchemaValid(Response response, String schemaFileName) {
        try {
            response.then().assertThat().body(matchesSchema(schemaFileName));
            logger.info("Schema validation PASSED for: {}", schemaFileName);
            return true;
        } catch (AssertionError e) {
            logger.error("Schema validation FAILED for: {} - {}", schemaFileName, e.getMessage());
            return false;
        }
    }
}
