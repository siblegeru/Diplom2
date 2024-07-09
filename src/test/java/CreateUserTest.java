import diplom.CreateUserStep;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;

public class CreateUserTest {
    private String email;
    private String password;
    private String name;
    private CreateUserStep createUserStep = new CreateUserStep();

    @Before
    @DisplayName("Генерация пользователя")
    public void userData(){
        email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(10);
        name = RandomStringUtils.randomAlphabetic(10);
    }


    @Test
    @DisplayName("Создание уникального пользователя")
    public void possibilityCreateNewUser() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        createUserStep
                .createUser(email, password, name)
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(email.toLowerCase()))
                .body("user.name", equalTo(name))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }


    @Test
    @DisplayName("Создание одинаковых юзеров 403")
    public void recCreatingUserTest() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        createUserStep
                .createUser(email, password, name);
        createUserStep
                .createUser(email, password, name)
                .statusCode(403)
                .body("success", is(false))
                .body("message", is("User already exists"));

    }

    @Test
    @DisplayName("Пропуск одного из обязательных полей 403")
    public void errorMissingRequiredFieldOnCreateUserTest() {
        password = "";
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        createUserStep
                .createUser(email, password, name)
                .statusCode(403)
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }

    @After
    @DisplayName("Удаление пользователя")
    public void deleteCash(){
        String response = createUserStep
                .loginUser(email, password)
                .extract().body()
                .path("accessToken");
        if (response != null){
        createUserStep.deleteUser(response);
        }
    }

}











