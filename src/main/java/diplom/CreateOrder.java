package diplom;

import io.restassured.http.ContentType;

import io.restassured.response.ValidatableResponse;


import static io.restassured.RestAssured.given;

public class CreateOrder {

    //получение данных ингридиентов
    public ValidatableResponse getIngridientList(){
        return given()
                .baseUri("https://stellarburgers.nomoreparties.site")
                .contentType(ContentType.JSON)
                .when()
                .get("/api/ingredients")
                .then();
    }


    public ValidatableResponse createOrderUser(String accessToken, String ingredientIds) {
        return given()
                .header("Authorization", accessToken)
                .baseUri("https://stellarburgers.nomoreparties.site")
                .contentType(ContentType.JSON)
                .body("{\"ingredients\": [\"" + ingredientIds + "\"]}")
                .when()
                .post("/api/orders")
                .then();
    }
}
