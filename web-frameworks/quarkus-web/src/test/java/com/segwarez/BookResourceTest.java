package com.segwarez;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

@QuarkusTest
class BookResourceTest {
    @Test
    void verify() {
        RestAssured.get("/books")
                .then()
                .statusCode(200);
    }
}