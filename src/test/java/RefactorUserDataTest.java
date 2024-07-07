import diplom.CreateUserStep;
import diplom.UpdateUserData;
import diplom.UserData;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.CoreMatchers.equalTo;

public class RefactorUserDataTest {
    private String email;
    private String password;
    private String name;
    private String response;
    private CreateUserStep createUserStep = new CreateUserStep();
    private UpdateUserData updateUserData = new UpdateUserData();
    UserData user = new UserData();

    @Before
    public void userData(){
        email = RandomStringUtils.randomAlphabetic(10) + "@yandex.ru";
        password = RandomStringUtils.randomAlphabetic(10);
        name = RandomStringUtils.randomAlphabetic(10);
        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
        createUserStep
                .createUser(email, password, name);
    }

    //изменение имени
    @Test
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

    //изменение пароля
    @Test
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
    //изменение email
    @Test
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
    //изменение имени для неавторизованного пользователя
    @Test
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
    //изменение имени для неавторизованного пользователя
    @Test
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
    //изменение имени для неавторизованного пользователя
    @Test
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
