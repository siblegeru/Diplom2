package diplom;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;

import io.restassured.response.ValidatableResponse;


import java.util.List;

import static io.restassured.RestAssured.given;

public class CreateOrder {

    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static final String INGRID_LIST = "/api/ingredients";
    private static final String CREATE_ORDER_PATH = "/api/orders";



    @Step("Получение данных ингридиентов")
    public ValidatableResponse getIngridientList(){
        return given()
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .when()
                .get(INGRID_LIST)
                .then();
    }

    @Step("Создание заказа юзером")
    public ValidatableResponse createOrderUser(String accessToken, List<String> ingredientIds) {
        IngredientRequest requestBody = new IngredientRequest(ingredientIds);
        return given()
                .header("Authorization", accessToken)
                .baseUri(BASE_URI)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post(CREATE_ORDER_PATH)
                .then();
    }
}
