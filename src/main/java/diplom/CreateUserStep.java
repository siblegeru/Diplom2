package diplom;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CreateUserStep {

    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static final String REGISTR_USER = "/api/auth/register";
    private static final String LOGIN_USER = "/api/auth/login";
    private static final String DELETE_USER = "/api/auth/user";


    @Step("Создание уникального пользователя")
    public ValidatableResponse createUser(UserData userData){
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body(userData)
                .when()
                .post(REGISTR_USER)
                .then();
    }

    @Step("Логин юзера в системе")
    public ValidatableResponse loginUser(String email, String password){
        return given()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URI)
                .body("{\n" +
                        "   \"email\": \"" + email + "\",\n" +
                        "   \"password\": \"" + password + "\"\n" +
                        "}")
                .when()
                .post(LOGIN_USER)
                .then();
    }

    @Step("Удаление юзера")
    public ValidatableResponse deleteUser(String accessToken){
        return given()
                .baseUri(BASE_URI)
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .when()
                .delete(DELETE_USER)
                .then();
    }
}