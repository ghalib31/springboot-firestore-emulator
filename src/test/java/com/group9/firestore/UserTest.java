package com.group9.firestore;

import com.github.javafaker.Faker;
import com.group9.firestore.dto.AddressDto;
import com.group9.firestore.dto.CreateUserDto;
import io.restassured.RestAssured;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTest {
    private final String rootUrl = "/api/users";
    Faker faker = new Faker();

    @LocalServerPort
    private int randomServerPort = 0;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = randomServerPort;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void should_get_all_users() {
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();

        given()
                .contentType("application/json")
                .body(fakeUser(firstName, lastName))
                .post(rootUrl)
                .then()
                .statusCode(HttpStatus.SC_CREATED);

        given()
                .param("firstName", firstName)
                .param("lastName", lastName)
                .get(rootUrl)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("size()", greaterThan(0));
    }

    @Test
    void should_get_user_by_id() {
        String id = getUserIdOfAnyUser();
        validateUserFoundById(id);
    }

    @Test
    void should_update_user() {
        var id = createUser();
        var userResponse = given()
                .get(rootUrl + "/" + id)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath();
        var firstName = userResponse.get("firstName");
        var streetName = userResponse.get("address.streetName");

        given()
                .contentType("application/json")
                .body(fakeUser())
                .put(rootUrl + "/" + id)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);

        given()
                .get(rootUrl + "/" + id)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(id))
                .body("firstName", not(firstName))
                .body("address.streetName", not(streetName));
    }

    @Test
    void should_delete_by_id() {
        String id = createUser();
        given()
                .delete(rootUrl + "/" + id)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    void should_delete_all_users() {
        createUser();
        given()
                .delete(rootUrl)
                .then()
                .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    private String getUserIdOfAnyUser() {
        return given()
                .get(rootUrl)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .extract()
                .jsonPath()
                .get("[0].id");
    }

    private void validateUserFoundById(String id) {
        given()
                .get(rootUrl + "/" + id)
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body("id", equalTo(id));
    }

    private String createUser() {
        return given()
                .contentType("application/json")
                .body(fakeUser())
                .post(rootUrl)
                .then()
                .log().all()
                .statusCode(HttpStatus.SC_CREATED)
                .extract()
                .asString();
    }


    private CreateUserDto fakeUser() {
        return fakeUser(faker.name().firstName(), faker.name().lastName());
    }

    private CreateUserDto fakeUser(String firstName, String lastName) {
        return new CreateUserDto(firstName, lastName, address());
    }

    private AddressDto address() {
        return new AddressDto(faker.address().streetName(), faker.number().randomDigitNotZero(), faker.address().zipCode(), faker.address().city());
    }
}
