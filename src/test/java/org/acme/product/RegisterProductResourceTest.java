package org.acme.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import jakarta.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class RegisterProductResourceTest {

    static {
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    public void testShouldNotRegisterProductWhenCommandIsInvalidId() {
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body("{\"id\":\"invalid-uuid\",\"sku\":\"12345678\",\"name\":\"Product Dummy\"}")
            .when()
            .post("/products/api/v1")
            .then()
            .statusCode(400)
            .body("title", is("Constraint Violation"))
            .body("status", is(400))
            .body("violations[0].field", is("registerProduct.command.id"))
            .body("violations[0].message", is("{id.pattern}"));
    }

    @Test
    public void testShouldNotRegisterProductWhenCommandIsMissingSku() {
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body("{\"id\":\"0e353046-31ef-4c1f-a54b-bd70a834948a\",\"name\":\"Product Dummy\"}")
            .when()
            .post("/products/api/v1")
            .then()
            .statusCode(400)
            .body("title", is("Constraint Violation"))
            .body("status", is(400))
            .body("violations[0].field", is("registerProduct.command.sku"))
            .body("violations[0].message", is("{sku.obrigatorio}"));
    }

    @Test
    public void testShouldNotRegisterProductWhenCommandIsInvalidPatternSku() {
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body("{\"id\":\"0e353046-31ef-4c1f-a54b-bd70a834948a\",\"sku\":\"1234567\",\"name\":\"Product Dummy\"}")
            .when()
            .post("/products/api/v1")
            .then()
            .statusCode(400)
            .body("title", is("Constraint Violation"))
            .body("status", is(400))
            .body("violations[0].field", is("registerProduct.command.sku"))
            .body("violations[0].message", is("{sku.pattern}"));
    }

    @Test
    public void testShouldNotRegisterProductWhenCommandIsMissingName() {
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body("{\"id\":\"0e353046-31ef-4c1f-a54b-bd70a834948a\",\"sku\":\"12345678\"}")
            .when()
            .post("/products/api/v1")
            .then()
            .statusCode(400)
            .body("title", is("Constraint Violation"))
            .body("status", is(400))
            .body("violations[0].field", is("registerProduct.command.name"))
            .body("violations[0].message", is("{nome.obrigatorio}"));
    }


    @Test
    public void testShouldNotRegisterProductWhenCommandIsInvalidSizeName() {
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body("{\"id\":\"0e353046-31ef-4c1f-a54b-bd70a834948a\",\"sku\":\"12345678\",\"name\":\"P\"}")
            .when()
            .post("/products/api/v1")
            .then()
            .statusCode(400)
            .body("title", is("Constraint Violation"))
            .body("status", is(400))
            .body("violations[0].field", is("registerProduct.command.name"))
            .body("violations[0].message", is("{nome.size}"));
    }

    @Test
    public void testShouldNotRegisterProductWhenCommandIsInvalidPatternName() {
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body("{\"id\":\"0e353046-31ef-4c1f-a54b-bd70a834948a\",\"sku\":\"12345678\",\"name\":\"Product@Dummy\"}")
            .when()
            .post("/products/api/v1")
            .then()
            .statusCode(400)
            .body("title", is("Constraint Violation"))
            .body("status", is(400))
            .body("violations[0].field", is("registerProduct.command.name"))
            .body("violations[0].message", is("{nome.pattern}"));
    }

    @Test
    public void testShouldRegisterProductWhenCommandIsValidWithId() {
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body("{\"id\":\"0e353046-31ef-4c1f-a54b-bd70a834948a\",\"sku\":\"12345678\",\"name\":\"Product Dummy\"}")
            .when()
            .post("/products/api/v1")
            .then()
            .statusCode(201);
    }

    @Test
    public void testShouldRegisterProductWhenCommandIsValidWithoutId() {
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body("{\"sku\":\"12345678\",\"name\":\"Product Dummy\"}")
            .when()
            .post("/products/api/v1")
            .then()
            .statusCode(201);
    }
}
