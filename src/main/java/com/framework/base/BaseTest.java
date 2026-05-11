package com.framework.base;

import com.framework.config.ConfigManager;
import com.framework.specs.SpecBuilder;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeSuite;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    @BeforeSuite
    public void globalSetup() {
        RestAssured.baseURI = ConfigManager.getConfig().baseUrl();
        RestAssured.basePath = ConfigManager.getConfig().basePath();

        List<io.restassured.filter.Filter> filters = new ArrayList<>();
        filters.add(new AllureRestAssured());

        if (ConfigManager.getConfig().logRequest()) {
            filters.add(new RequestLoggingFilter());
        }
        if (ConfigManager.getConfig().logResponse()) {
            filters.add(new ResponseLoggingFilter());
        }

        RestAssured.filters(filters);
        RestAssured.requestSpecification = SpecBuilder.getDefaultRequestSpec();

        logger.info("Framework initialized with base URL: {}", ConfigManager.getConfig().baseUrl());
        logger.info("Environment: {}", ConfigManager.getConfig().environment());
    }

    @BeforeClass
    public void setup() {
        // Override in child classes for specific setup
    }
}
