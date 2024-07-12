import diplom.CreateOrder;
import diplom.CreateUserStep;

import diplom.UserData;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;


public class CreateOrderTest {
    private String email;
    private String password;
    private String name;
    private String response;
    private CreateUserStep createUserStep = new CreateUserStep();
    CreateOrder createOrder = new CreateOrder();
    UserData userData;

    @Before
    @DisplayName("Генерация пользователя")
    public void userData(){
        email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(10);
        name = RandomStringUtils.randomAlphabetic(10);
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        userData = new UserData(name, email, password);
        createUserStep
                .createUser(userData);
    }

    @Test
    @DisplayName("создание заказа под авторизированным пользователем")
    public void createAutorizedUserOrderTest(){
        response = createUserStep
                .loginUser(email, password)
                .extract()
                .body()
                .path("accessToken");
        List<String> listIngridient = createOrder
                .getIngridientList().extract().body().path("data._id");
        createOrder
                .createOrderUser(response, listIngridient)
                .statusCode(200)
                .body("name", notNullValue())
                .body("order", notNullValue())
                .body("order.number", notNullValue())
                .body("success", is(true));
    }

    @Test
    @DisplayName("создание заказа под неавторизированным пользователем")
    public void createNoAutorizedUserOrderTest(){
        List<String> listIngridient = createOrder
                .getIngridientList().extract().body().path("data._id");
        createOrder
                .createOrderUser("response", listIngridient)
                .statusCode(200)
                .body("name", notNullValue())
                .body("order", notNullValue())
                .body("order.number", notNullValue())
                .body("success", is(true));
    }

    @Test
    @DisplayName("Передача невалидного хэша 500")
    public void createAutorizedUserOrderWhithInvalideHashIngridientsTest(){
        response = createUserStep
                .loginUser(email, password)
                .extract()
                .body()
                .path("accessToken");
        List<String> listIngridient = Arrays.asList("orderIngridient", "orderIngridient");
        createOrder
                .createOrderUser(response, listIngridient)
                .statusCode(500);
    }

    @Test
    @DisplayName("создание заказа без параметров проваливается, должна быть ошибка 400, но выдает 500 БАГ")
    public void createAutorizedUserOrderWhithoutIngridientsTest(){
        response = createUserStep
                .loginUser(email, password)
                .extract()
                .body()
                .path("accessToken");
        List<String> listIngridient = Arrays.asList();
        createOrder
                .createOrderUser(response, listIngridient)
                .statusCode(400);
    }


    @After
    @DisplayName("Удаление пользователя")
    public void deleteCash(){
        response = createUserStep
                .loginUser(email, password)
                .extract().body()
                .path("accessToken");
        if (response != null){
            createUserStep.deleteUser(response);
        }
    }
}
