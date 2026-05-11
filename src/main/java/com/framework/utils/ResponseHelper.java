package com.framework.utils;

import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Helper utility for extracting and working with API responses.
 */
public final class ResponseHelper {

    private static final Logger logger = LogManager.getLogger(ResponseHelper.class);

    private ResponseHelper() {
    }

    public static <T> T deserialize(Response response, Class<T> clazz) {
        return response.as(clazz);
    }

    public static <T> List<T> deserializeList(Response response, String jsonPath, Class<T> clazz) {
        return response.jsonPath().getList(jsonPath, clazz);
    }

    public static String getJsonValue(Response response, String jsonPath) {
        return response.jsonPath().getString(jsonPath);
    }

    public static int getIntValue(Response response, String jsonPath) {
        return response.jsonPath().getInt(jsonPath);
    }

    public static int getStatusCode(Response response) {
        return response.getStatusCode();
    }

    public static String getHeader(Response response, String headerName) {
        return response.getHeader(headerName);
    }

    public static long getResponseTime(Response response) {
        long time = response.getTime();
        logger.info("Response time: {}ms", time);
        return time;
    }

    public static void logResponse(Response response) {
        logger.info("Status Code: {}", response.getStatusCode());
        logger.info("Response Body: {}", response.getBody().asPrettyString());
        logger.info("Response Time: {}ms", response.getTime());
    }
}
