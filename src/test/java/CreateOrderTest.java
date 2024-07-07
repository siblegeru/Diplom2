import diplom.CreateOrder;
import diplom.CreateUserStep;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


import java.util.List;

import static org.hamcrest.CoreMatchers.*;


public class CreateOrderTest {
    private String email;
    private String password;
    private String name;
    private String response;
    private CreateUserStep createUserStep = new CreateUserStep();
    CreateOrder createOrder = new CreateOrder();

    @Before
    public void userData(){
        email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(10);
        name = RandomStringUtils.randomAlphabetic(10);
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        createUserStep
                .createUser(email, password, name);
    }
    //создание заказа под авторизированным пользователем
    @Test
    public void createAutorizedUserOrderTest(){
        response = createUserStep
                .loginUser(email, password)
                .extract()
                .body()
                .path("accessToken");
        List<String> listIngridient = createOrder
                .getIngridientList().extract().body().path("data._id");
        String orderIngridient = listIngridient.get(1);
        createOrder
                .createOrderUser(response, orderIngridient)
                .statusCode(200)
                .body("name", notNullValue())
                .body("order", notNullValue())
                .body("order.number", notNullValue())
                .body("success", is(true));
    }
    //создание заказа под неавторизированным пользователем
    @Test
    public void createNoAutorizedUserOrderTest(){
        List<String> listIngridient = createOrder
                .getIngridientList().extract().body().path("data._id");
        String orderIngridient = listIngridient.get(1);
        createOrder
                .createOrderUser("response", orderIngridient)
                .statusCode(200)
                .body("name", notNullValue())
                .body("order", notNullValue())
                .body("order.number", notNullValue())
                .body("success", is(true));
    }
    //передача невалидного хэша 500
    @Test
    public void createAutorizedUserOrderWhithInvalideHashIngridientsTest(){
        response = createUserStep
                .loginUser(email, password)
                .extract()
                .body()
                .path("accessToken");
        createOrder
                .createOrderUser(response, "orderIngridient")
                .statusCode(500);
    }
    //создание заказа без параметров проваливается, должна быть ошибка 400, но выдает 500 БАГ
    @Test
    public void createAutorizedUserOrderWhithoutIngridientsTest(){
        response = createUserStep
                .loginUser(email, password)
                .extract()
                .body()
                .path("accessToken");
        createOrder
                .createOrderUser(response, "")
                .statusCode(400);
    }


    @After
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
