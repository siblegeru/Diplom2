package diplom;

import io.restassured.http.ContentType;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UpdateUserData {
    public ValidatableResponse updateUser(String accessToken, UserData user) {
        return given()
                .baseUri("https://stellarburgers.nomoreparties.site")
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .patch("/api/auth/user")
                .then();
    }
}
