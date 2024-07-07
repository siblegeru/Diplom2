package diplom;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class CreateUserStep {

    //метод создания уник. юзера
    public ValidatableResponse createUser(String email, String password, String name){
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://stellarburgers.nomoreparties.site")
                .body("{\n" +
                        "   \"email\": \"" + email + "\",\n" +
                        "   \"password\": \"" + password + "\",\n" +
                        "    \"name\": \"" + name + "\"\n" +
                        "}")
                .when()
                .post("/api/auth/register")
                .then();
    }

    //метод логина юзера в системе
    public ValidatableResponse loginUser(String email, String password){
        return given()
                .contentType(ContentType.JSON)
                .baseUri("https://stellarburgers.nomoreparties.site")
                .body("{\n" +
                        "   \"email\": \"" + email + "\",\n" +
                        "   \"password\": \"" + password + "\"\n" +
                        "}")
                .when()
                .post("/api/auth/login")
                .then();
    }

    public ValidatableResponse deleteUser(String accessToken){
        return given()
                .baseUri("https://stellarburgers.nomoreparties.site")
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .when()
                .delete("/api/auth/user")
                .then();
    }
}