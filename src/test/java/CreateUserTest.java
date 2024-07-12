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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.is;

public class CreateUserTest {
    private String email;
    private String password;
    private String name;
    private CreateUserStep createUserStep = new CreateUserStep();
    UserData userData;

    @Before
    @DisplayName("Генерация пользователя")
    public void userData(){
        email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(10);
        name = RandomStringUtils.randomAlphabetic(10);
        userData = new UserData(name, email, password);
    }


    @Test
    @DisplayName("Создание уникального пользователя")
    public void possibilityCreateNewUser() {
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        createUserStep
                .createUser(userData)
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
                .createUser(userData);
        createUserStep
                .createUser(userData)
                .statusCode(403)
                .body("success", is(false))
                .body("message", is("User already exists"));

    }

    @Test
    @DisplayName("Пропуск одного из обязательных полей 403")
    public void errorMissingRequiredFieldOnCreateUserTest() {
        password = "";
        email = RandomStringUtils.randomAlphabetic(10) + "yandex.ru";
        name = RandomStringUtils.randomAlphabetic(10);
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        UserData failUserData = new UserData(name, email, password);
        createUserStep
                .createUser(failUserData)
                .statusCode(403)
                .body("success", is(false))
                .body("message", is("Email, password and name are required fields"));
    }

    @After
    @DisplayName("Удаление пользователя")
    public void deleteCash(){
        String response = createUserStep
                .loginUser(userData)
                .extract().body()
                .path("accessToken");
        if (response != null){
        createUserStep.deleteUser(response);
        }
    }

}











