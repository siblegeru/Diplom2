import diplom.CreateUserStep;
import diplom.GetOrder;
import diplom.UserData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;


public class GetOrdersChoisenUserTest {
    private String email;
    private String password;
    private String name;
    private String response;
    UserData user;
    CreateUserStep createUserStep = new CreateUserStep();
    GetOrder getOrder = new GetOrder();
    UserData userData;

    @Before
    @DisplayName("Создание пользователя")
    public void userDataForTets(){
        email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(10);
        name = RandomStringUtils.randomAlphabetic(10);
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        userData = new UserData(name, email, password);
        createUserStep
                .createUser(userData);
    }


    @Test
    @DisplayName("Список заказов авторизованного пользователя")
    public void autorizedUserOrderListTest() {
        response = createUserStep
                .loginUser(userData).extract().body().path("accessToken");
        getOrder
                .getUsersOrder(response, user)
                .statusCode(200)
                .body("success", is(true))
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Список заказов неавторизованного пользователя")
    public void noAutorizedUserOrderListTest() {
        response = createUserStep
                .loginUser(userData).extract().body().path("accessToken");
        getOrder
                .getUsersOrder("response", user)
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @After
    @DisplayName("Удаление пользователя")
    public void deleteCash(){
        response = createUserStep
                .loginUser(userData)
                .extract().body()
                .path("accessToken");
        if (response != null){
            createUserStep.deleteUser(response);
        }
    }
}
