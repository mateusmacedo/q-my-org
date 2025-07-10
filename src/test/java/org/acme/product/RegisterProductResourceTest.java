package org.acme.product;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
@Testcontainers
public class RegisterProductResourceTest {

    static {
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    void shouldReturnBadRequestWhenRegisteringProductWithMissingSku() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"name\":\"Product Dummy\"}")
                .when()
                .post("/products/api/v1")
                .then()
                .statusCode(400)
                .body("title", is("Constraint Violation"))
                .body("status", is(400))
                .body("violations[0].field", is("registerProduct.dto.sku"))
                .body("violations[0].message", is("{sku.obrigatorio}"));
    }

    @Test
    void shouldReturnBadRequestWhenRegisteringProductWithInvalidPatternSku() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"sku\":\"1234567\",\"name\":\"Product Dummy\"}")
                .when()
                .post("/products/api/v1")
                .then()
                .statusCode(400)
                .body("title", is("Constraint Violation"))
                .body("status", is(400))
                .body("violations[0].field", is("registerProduct.dto.sku"))
                .body("violations[0].message", is("{sku.pattern}"));
    }

    @Test
    void shouldReturnBadRequestWhenRegisteringProductWithMissingName() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"sku\":\"12345678\"}")
                .when()
                .post("/products/api/v1")
                .then()
                .statusCode(400)
                .body("title", is("Constraint Violation"))
                .body("status", is(400))
                .body("violations[0].field", is("registerProduct.dto.name"))
                .body("violations[0].message", is("{nome.obrigatorio}"));
    }

    @Test
    void shouldReturnBadRequestWhenRegisteringProductWithInvalidSizeName() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"sku\":\"12345678\",\"name\":\"P\"}")
                .when()
                .post("/products/api/v1")
                .then()
                .statusCode(400)
                .body("title", is("Constraint Violation"))
                .body("status", is(400))
                .body("violations[0].field", is("registerProduct.dto.name"))
                .body("violations[0].message", is("{nome.size}"));
    }

    @Test
    void shouldReturnBadRequestWhenRegisteringProductWithInvalidPatternName() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"sku\":\"12345678\",\"name\":\"Product@Dummy\"}")
                .when()
                .post("/products/api/v1")
                .then()
                .statusCode(400)
                .body("title", is("Constraint Violation"))
                .body("status", is(400))
                .body("violations[0].field", is("registerProduct.dto.name"))
                .body("violations[0].message", is("{nome.pattern}"));
    }

    @Test
    void shouldReturnCreatedWhenRegisteringProductWithValidCommand() {
        given()
                .contentType(MediaType.APPLICATION_JSON)
                .body("{\"sku\":\"12345678\",\"name\":\"Product Dummy\"}")
                .when()
                .post("/products/api/v1")
                .then()
                .statusCode(201);
    }
}
