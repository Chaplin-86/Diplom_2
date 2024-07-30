import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.User.Userrequests.User;
import praktikum.User.Userrequests.UserClient;
import praktikum.User.Userrequests.UserLogin;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static praktikum.UserGenerator.randomUser;

public class LoginUserTest {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private User user;
    private UserClient userClient;
    private UserLogin userLogin;
    private String accessToken;
    private String refreshToken;


    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            userClient.deleteUser(accessToken);
        }

    }

    @Test
    @DisplayName("Логин под существующим пользователем")
    @Description("POST-запрос на ручку /api/auth/login")
    public void realUserLogin() {
        user = randomUser();
        userClient = new UserClient();
        userLogin = new UserLogin();

        userClient.createUser(user);

        ValidatableResponse responseToLogin = userClient.loginUser(userLogin.creds(user));

        assertEquals("Status code is wrong", SC_OK, responseToLogin.extract().statusCode());
        Boolean isLoginSuccessful = responseToLogin.extract().path("success");
        assertTrue(isLoginSuccessful);
        accessToken = responseToLogin.extract().path("accessToken");
        assertThat(accessToken, notNullValue());
        refreshToken = responseToLogin.extract().path("refreshToken");
        assertThat(refreshToken, notNullValue());
        assertThat(responseToLogin.extract().path("user"), notNullValue());


    }

    @Test
    @DisplayName("Логин с неверным логином")
    @Description("POST-запрос на ручку /api/auth/login")
    public void loginWithInvalidEmail() {
        User user = new User("HarryP@yandex.ru", "password", "Harry");
        userClient = new UserClient();

        user.setEmail("harry@yandex.ru");

        ValidatableResponse responseToLogin = userClient.loginUser(userLogin.creds(user));

        assertEquals("Status code is wrong", SC_UNAUTHORIZED, responseToLogin.extract().statusCode());
        Boolean isLoginNotSuccessful = responseToLogin.extract().path("success");
        assertFalse(isLoginNotSuccessful);
        String messageExpected = "email or password are incorrect";
        String messageActual = responseToLogin.extract().path("message");
        assertEquals("The message is wrong", messageExpected, messageActual);


    }

    @Test
    @DisplayName("Логин с неверным паролем")
    @Description("POST-запрос на ручку /api/auth/login")
    public void loginWithInvalidPassword() {
        User user = new User("Harry1980@yandex.ru", "password", "Harry");
        userClient = new UserClient();

        user.setPassword("pass");

        ValidatableResponse responseToLogin = userClient.loginUser(userLogin.creds(user));

        assertEquals("Status code is wrong", SC_UNAUTHORIZED, responseToLogin.extract().statusCode());
        Boolean isLoginNotSuccessful = responseToLogin.extract().path("success");
        assertFalse(isLoginNotSuccessful);
        String messageExpected = "email or password are incorrect";
        String messageActual = responseToLogin.extract().path("message");
        assertEquals("The message is wrong", messageExpected, messageActual);


    }


}
