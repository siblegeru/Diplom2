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

public class LoginUserTest {
    private String email;
    private String password;
    private String name;
    private CreateUserStep createUserStep = new CreateUserStep();

    @Before
    @DisplayName("Создание пользователя")
    public void userData(){
        email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(10);
        name = RandomStringUtils.randomAlphabetic(10);
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        createUserStep
                .createUser(email, password, name);
    }

    @Test
    @DisplayName("Логин под авторизованным пользователем")
    public void possibilityCreateNewUser() {
        createUserStep
                .loginUser(email, password)
                .statusCode(200)
                .body("success", is(true))
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .body("user", notNullValue())
                .body("user.email", equalTo(email.toLowerCase()))
                .body("user.name", equalTo(name));
    }


    @Test
    @DisplayName("Логин под неавторизованным пользователем")
    public void errorNoAutorizedUserLoginTest() {
        createUserStep
                .loginUser(email, "password")
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("email or password are incorrect"));
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
