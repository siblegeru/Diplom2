package diplom;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class GetOrder {

    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static final String ORDERS_LIST = "/api/orders";

    @Step("Получение списка заказов пользователя")
    public ValidatableResponse getUsersOrder(String accessToken, UserData user){
        return given()
                .baseUri(BASE_URI)
                .header("Authorization", accessToken)
                .contentType(ContentType.JSON)
                .when()
                .get(ORDERS_LIST)
                .then();
    }

}
