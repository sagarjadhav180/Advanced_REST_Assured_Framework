package com.framework.utils;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Advanced contract validation utility.
 * Validates API responses against expected contracts including:
 * - Status codes
 * - Headers
 * - Response time
 * - Field presence and types
 * - Business rules
 */
public class ContractValidator {

    private static final Logger logger = LogManager.getLogger(ContractValidator.class);

    private final Response response;
    private final List<String> violations = new ArrayList<>();

    public ContractValidator(Response response) {
        this.response = response;
    }

    public static ContractValidator forResponse(Response response) {
        return new ContractValidator(response);
    }

    /**
     * Validate expected status code.
     */
    public ContractValidator expectStatusCode(int expectedStatusCode) {
        int actualStatusCode = response.getStatusCode();
        if (actualStatusCode != expectedStatusCode) {
            violations.add(String.format("Status code mismatch: expected %d, got %d",
                    expectedStatusCode, actualStatusCode));
        }
        return this;
    }

    /**
     * Validate expected content type.
     */
    public ContractValidator expectContentType(ContentType expectedContentType) {
        String actualContentType = response.getContentType();
        if (actualContentType == null || !actualContentType.contains(expectedContentType.toString())) {
            violations.add(String.format("Content-Type mismatch: expected %s, got %s",
                    expectedContentType, actualContentType));
        }
        return this;
    }

    /**
     * Validate that a header exists with expected value.
     */
    public ContractValidator expectHeader(String headerName, String expectedValue) {
        String actualValue = response.getHeader(headerName);
        if (actualValue == null) {
            violations.add(String.format("Missing header: %s", headerName));
        } else if (!actualValue.equals(expectedValue)) {
            violations.add(String.format("Header '%s' mismatch: expected '%s', got '%s'",
                    headerName, expectedValue, actualValue));
        }
        return this;
    }

    /**
     * Validate that a header exists (regardless of value).
     */
    public ContractValidator expectHeaderPresent(String headerName) {
        if (response.getHeader(headerName) == null) {
            violations.add(String.format("Missing required header: %s", headerName));
        }
        return this;
    }

    /**
     * Validate response time is within threshold.
     */
    public ContractValidator expectResponseTimeBelow(long maxMillis) {
        long actualTime = response.getTime();
        if (actualTime > maxMillis) {
            violations.add(String.format("Response time %dms exceeds threshold %dms",
                    actualTime, maxMillis));
        }
        return this;
    }

    /**
     * Validate that a JSON field exists in response body.
     */
    public ContractValidator expectFieldPresent(String jsonPath) {
        Object value = response.jsonPath().get(jsonPath);
        if (value == null) {
            violations.add(String.format("Missing required field: %s", jsonPath));
        }
        return this;
    }

    /**
     * Validate that multiple JSON fields exist in response body.
     */
    public ContractValidator expectFieldsPresent(String... jsonPaths) {
        for (String jsonPath : jsonPaths) {
            expectFieldPresent(jsonPath);
        }
        return this;
    }

    /**
     * Validate that a JSON field has the expected type.
     */
    public ContractValidator expectFieldType(String jsonPath, Class<?> expectedType) {
        Object value = response.jsonPath().get(jsonPath);
        if (value == null) {
            violations.add(String.format("Field '%s' is null, expected type %s",
                    jsonPath, expectedType.getSimpleName()));
        } else if (!expectedType.isInstance(value)) {
            violations.add(String.format("Field '%s' type mismatch: expected %s, got %s",
                    jsonPath, expectedType.getSimpleName(), value.getClass().getSimpleName()));
        }
        return this;
    }

    /**
     * Validate that a JSON field has the expected value.
     */
    public ContractValidator expectFieldValue(String jsonPath, Object expectedValue) {
        Object actualValue = response.jsonPath().get(jsonPath);
        if (actualValue == null && expectedValue != null) {
            violations.add(String.format("Field '%s' is null, expected '%s'", jsonPath, expectedValue));
        } else if (actualValue != null && !actualValue.equals(expectedValue)) {
            violations.add(String.format("Field '%s' value mismatch: expected '%s', got '%s'",
                    jsonPath, expectedValue, actualValue));
        }
        return this;
    }

    /**
     * Validate that a list field has a minimum size.
     */
    public ContractValidator expectListMinSize(String jsonPath, int minSize) {
        List<?> list = response.jsonPath().getList(jsonPath);
        if (list == null) {
            violations.add(String.format("Field '%s' is null, expected list with min size %d",
                    jsonPath, minSize));
        } else if (list.size() < minSize) {
            violations.add(String.format("Field '%s' list size %d is less than minimum %d",
                    jsonPath, list.size(), minSize));
        }
        return this;
    }

    /**
     * Validate each item in a list matches expected fields.
     */
    public ContractValidator expectListItemsHaveFields(String listPath, String... fields) {
        List<Map<String, Object>> items = response.jsonPath().getList(listPath);
        if (items == null || items.isEmpty()) {
            violations.add(String.format("List '%s' is null or empty", listPath));
            return this;
        }
        for (int i = 0; i < items.size(); i++) {
            Map<String, Object> item = items.get(i);
            for (String field : fields) {
                if (!item.containsKey(field) || item.get(field) == null) {
                    violations.add(String.format("Item[%d] in '%s' missing field: %s",
                            i, listPath, field));
                }
            }
        }
        return this;
    }

    /**
     * Validate JSON schema.
     */
    public ContractValidator expectSchema(String schemaFileName) {
        if (!SchemaValidator.isSchemaValid(response, schemaFileName)) {
            violations.add(String.format("Schema validation failed for: %s", schemaFileName));
        }
        return this;
    }

    /**
     * Get all contract violations.
     */
    public List<String> getViolations() {
        return new ArrayList<>(violations);
    }

    /**
     * Check if the contract is valid (no violations).
     */
    public boolean isValid() {
        return violations.isEmpty();
    }

    /**
     * Assert the contract is valid. Throws AssertionError if violations exist.
     */
    public void assertContract() {
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder("Contract violations found:\n");
            for (int i = 0; i < violations.size(); i++) {
                sb.append(String.format("  %d. %s%n", i + 1, violations.get(i)));
            }
            logger.error(sb.toString());
            throw new AssertionError(sb.toString());
        }
        logger.info("Contract validation PASSED - no violations found");
    }
}
