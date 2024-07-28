import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.User.Userrequests.User;
import praktikum.User.Userrequests.UserClient;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.*;
import static praktikum.UserGenerator.randomUser;

public class CreateUserTest {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private UserClient userClient;
    private User user;
    private String accessToken;
    private String bearerToken;

    private int statusCode;

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;
    }

    @After
    public void deleteUser() {
        if(accessToken != null){
            userClient.deleteUser(accessToken);
        }


//        ValidatableResponse deleteResponse = userClient.deleteUser(accessToken);
//        assertEquals(SC_ACCEPTED, deleteResponse.extract().statusCode());
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("POST-запрос на ручку /api/auth/register")
    public void createUser() {
    user = randomUser();
    userClient = new UserClient();

    ValidatableResponse responseToCreateUser = userClient.createUser(user);

    assertEquals("Status code is wrong", SC_OK, responseToCreateUser.extract().statusCode());
    Boolean isUserCreated = responseToCreateUser.extract().path("success");
    assertTrue(isUserCreated);
    assertThat(responseToCreateUser.extract().path("user"), notNullValue());
    accessToken = responseToCreateUser.extract().path("accessToken");

    }

    @Test
    @DisplayName("Повторное создание уже зарегистрированного пользователя")
    @Description("POST-запрос на ручку /api/auth/register")
    public void createNotUniqueUser() {
        user = new User("Harry_P@yandex.ru", "password", "Harry");
        userClient = new UserClient();

        ValidatableResponse response = userClient.createUser(user);
        ValidatableResponse response1 = userClient.createUser(user);

        assertEquals("Status code is wrong", SC_OK, response.extract().statusCode());
        Boolean isUserCreated = response.extract().path("success");
        assertTrue(isUserCreated);
        assertThat(response.extract().path("user"), notNullValue());
        accessToken = response.extract().path("accessToken");

        assertEquals("Status code id wrong", SC_FORBIDDEN, response1.extract().statusCode());
        Boolean isUserNotCreated = response1.extract().path("success");
        assertFalse(isUserNotCreated);
        String messageExpected = "User already exists";
        String messageActual = response1.extract().path("message");
        assertEquals("The message is wrong", messageExpected, messageActual);

    }


    @Test
    @DisplayName("Создание пользователя без поля email")
    @Description("POST-запрос на ручку /api/auth/register")
    public void createUserWithoutEmail() {
        user = new User("", "password", "Harry");
        userClient = new UserClient();

        ValidatableResponse responseWithoutEmail = userClient.createUser(user);

        assertEquals("Status code id wrong", SC_FORBIDDEN, responseWithoutEmail.extract().statusCode());
        Boolean isUserNotCreated = responseWithoutEmail.extract().path("success");
        assertFalse(isUserNotCreated);
        String messageExpected = "Email, password and name are required fields";
        String messageActual = responseWithoutEmail.extract().path("message");
        assertEquals("The message is wrong", messageExpected, messageActual);

    }


    @Test
    @DisplayName("Создание пользователя без поля password")
    @Description("POST-запрос на ручку /api/auth/register")
    public void createUserWithoutPassword() {
        user = new User("Harry_J_P@yandex.ru", "", "Harry");
        userClient = new UserClient();

        ValidatableResponse responseWithoutPassword = userClient.createUser(user);

        assertEquals("Status code id wrong", SC_FORBIDDEN, responseWithoutPassword.extract().statusCode());
        Boolean isUserNotCreated = responseWithoutPassword.extract().path("success");
        assertFalse(isUserNotCreated);
        String messageExpected = "Email, password and name are required fields";
        String messageActual = responseWithoutPassword.extract().path("message");
        assertEquals("The message is wrong", messageExpected, messageActual);

    }


    @Test
    @DisplayName("Создание пользователя без поля name")
    @Description("POST-запрос на ручку /api/auth/register")
    public void createUserWithoutName() {
        user = new User("HarryJP@yandex.ru", "password", "");
        userClient = new UserClient();

        ValidatableResponse responseWithoutName = userClient.createUser(user);

        assertEquals("Status code id wrong", SC_FORBIDDEN, responseWithoutName.extract().statusCode());
        Boolean isUserNotCreated = responseWithoutName.extract().path("success");
        assertFalse(isUserNotCreated);
        String messageExpected = "Email, password and name are required fields";
        String messageActual = responseWithoutName.extract().path("message");
        assertEquals("The message is wrong", messageExpected, messageActual);

    }


}
