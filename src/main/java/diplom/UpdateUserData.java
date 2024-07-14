package diplom;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;

import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class UpdateUserData {
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static final String DATA_USER = "/api/auth/user";

    @Step("Изменение данных пользователя")
    public ValidatableResponse updateUser(String accessToken, UserData user) {
        return given()
                .baseUri(BASE_URI)
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .patch(DATA_USER)
                .then();
    }
}
