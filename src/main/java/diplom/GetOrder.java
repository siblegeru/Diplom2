package diplom;

import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class GetOrder {
    //получение списка заказов пользователя
    public ValidatableResponse getUsersOrder(String accessToken, UserData user){
        return given()
                .baseUri("https://stellarburgers.nomoreparties.site")
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .when()
                .get("/api/orders")
                .then();
    }

}
