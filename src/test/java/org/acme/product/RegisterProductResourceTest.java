package org.acme.product;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.ws.rs.core.MediaType;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class RegisterProductResourceTest {

    @Test
    public void testShouldNotRegisterProductWhenCommandIsInvalidAggregateId() {
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(new RegisterProductCommand("invalid-uuid", "12345678", "Product Dummy"))
            .when()
            .post("/products/api/v1")
            .then()
            .statusCode(400)
            .body("title", is("Constraint Violation"))
            .body("status", is(400))
            .body("violations[0].field", is("registerProduct.command.aggregateId"))
            .body("violations[0].message", is("{aggregateId.pattern}"));
    }

    @Test
    public void testShouldNotRegisterProductWhenCommandIsMissingSku() {
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(new RegisterProductCommand("0e353046-31ef-4c1f-a54b-bd70a834948a", null, "Product Dummy"))
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
            .body(new RegisterProductCommand("0e353046-31ef-4c1f-a54b-bd70a834948a", "1234567", "Product Dummy"))
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
            .body(new RegisterProductCommand("0e353046-31ef-4c1f-a54b-bd70a834948a", "12345678", null))
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
            .body(new RegisterProductCommand("0e353046-31ef-4c1f-a54b-bd70a834948a", "12345678", "P"))
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
            .body(new RegisterProductCommand("0e353046-31ef-4c1f-a54b-bd70a834948a", "12345678", "Product@Dummy"))
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
    public void testShouldRegisterProductWhenCommandIsValid() {
        given()
            .contentType(MediaType.APPLICATION_JSON)
            .body(new RegisterProductCommand("0e353046-31ef-4c1f-a54b-bd70a834948a", "12345678", "Product Dummy"))
            .when()
            .post("/products/api/v1")
            .then()
            .statusCode(201);
    }
}
