import diplom.CreateUserStep;
import diplom.UpdateUserData;
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
import static org.hamcrest.CoreMatchers.equalTo;

public class UpdateUserDataTest {
    private String email;
    private String password;
    private String name;
    private String response;
    private CreateUserStep createUserStep = new CreateUserStep();
    private UpdateUserData updateUserData = new UpdateUserData();
    UserData user = new UserData(name, email, password);

    @Before
    @DisplayName("Создание пользователя")
    public void userData(){
        email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(10);
        name = RandomStringUtils.randomAlphabetic(10);
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        createUserStep
                .createUser(user);
    }


    @Test
    @DisplayName("Изменение имени пользователя")
    public void updateUserNameTest() {
        response = createUserStep
                .loginUser(email, password).extract().body().path("accessToken");
        user.setName(RandomStringUtils.randomAlphabetic(10));
        user.setPassword(password);
        user.setEmail(email);

        updateUserData.updateUser(response, user)
                .statusCode(200)
                .body("success", is(true))
                .body("user", notNullValue())
                .body("user.email", equalTo(email.toLowerCase()))
                .body("user.name", equalTo(name));
    }


    @Test
    @DisplayName("Изменение пароля пользователя")
    public void updateUserPasswordTest() {
        response = createUserStep
                .loginUser(email, password).extract().body().path("accessToken");
        user.setName(name);
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setEmail(email);

        updateUserData.updateUser(response, user)
                .statusCode(200)
                .body("success", is(true))
                .body("user", notNullValue())
                .body("user.email", equalTo(email.toLowerCase()))
                .body("user.name", equalTo(name));
    }

    @Test
    @DisplayName("Изменение email пользователя")
    public void updateUserEmailTest() {
        response = createUserStep
                .loginUser(email, password).extract().body().path("accessToken");
        user.setName(name);
        user.setPassword(password);
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");

        updateUserData.updateUser(response, user)
                .statusCode(200)
                .body("success", is(true))
                .body("user", notNullValue())
                .body("user.email", equalTo(email.toLowerCase()))
                .body("user.name", equalTo(name));;
    }

    @Test
    @DisplayName("Изменение имени для неавторизованного пользователя")
    public void updateNoAutorizedUserNameTest() {
        createUserStep
                .loginUser(email, password).extract().body().path("accessToken");
        user.setName(RandomStringUtils.randomAlphabetic(10));
        user.setPassword(password);
        user.setEmail(email);

        updateUserData.updateUser("response", user)
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение имени для неавторизованного пользователя")
    public void updateNoAutorizedUserEmailTest() {
        createUserStep
                .loginUser(email, password).extract().body().path("accessToken");
        user.setName(name);
        user.setPassword(password);
        user.setEmail(RandomStringUtils.randomAlphabetic(10) + "@yandex.ru");

        updateUserData.updateUser("response", user)
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("Изменение имени для неавторизованного пользователя")
    public void updateNoAutorizedUserPasswordTest() {
        createUserStep
                .loginUser(email, password).extract().body().path("accessToken");
        user.setName(name);
        user.setPassword(RandomStringUtils.randomAlphabetic(10));
        user.setEmail(email);

        updateUserData.updateUser("response", user)
                .statusCode(401)
                .body("success", is(false))
                .body("message", is("You should be authorised"));
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
