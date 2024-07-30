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
import static org.junit.Assert.*;
import static praktikum.UserGenerator.randomUser;

public class UpdateUserTest {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private User user;
    private UserClient userClient;
    private UserLogin userLogin;
    private String accessToken;

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
    @DisplayName("Изменение почты авторизованного пользователя")
    @Description("PATCH-запрос на ручку /api/auth/user")
    public void changeAuthUserEmail() {
        user = randomUser();
        userClient = new UserClient();
        userLogin = new UserLogin();

        ValidatableResponse createUserResponse = userClient.createUser(user);
        accessToken = createUserResponse.extract().path("accessToken");

        User createdUser = new User(user.getEmail(), user.getPassword(),user.getName());

        ValidatableResponse loginResponse = userClient.loginUser(userLogin.creds(createdUser));
        accessToken = loginResponse.extract().path("accessToken");


        String newEmail = "Sirius_Black@yandex.ru";
        createdUser.setEmail(newEmail);

        ValidatableResponse updateUserEmailResponse = userClient.updateUser(createdUser, accessToken);

        assertEquals("Status code is wrong", SC_OK, updateUserEmailResponse.extract().statusCode());
        Boolean isUpdateSuccessful = updateUserEmailResponse.extract().path("success");
        assertTrue(isUpdateSuccessful);
    }

    @Test
    @DisplayName("Изменение пароля авторизованного пользователя")
    @Description("PATCH-запрос на ручку /api/auth/user")
    public void changeAuthUserPassword() {
        user = randomUser();
        userClient = new UserClient();

        ValidatableResponse createUserResponse = userClient.createUser(user);
        accessToken = createUserResponse.extract().path("accessToken");

        User createdUser = new User(user.getEmail(), user.getPassword(),user.getName());

        ValidatableResponse loginResponse = userClient.loginUser(userLogin.creds(createdUser));
        accessToken = loginResponse.extract().path("accessToken");

        String newPassword = "egjtfiohvl";
        createdUser.setPassword(newPassword);

        ValidatableResponse updateUserPasswordResponse = userClient.updateUser(createdUser, accessToken);

        assertEquals("Status code is wrong", SC_OK, updateUserPasswordResponse.extract().statusCode());
        Boolean isUpdateSuccessful = updateUserPasswordResponse.extract().path("success");
        assertTrue(isUpdateSuccessful);
    }

    @Test
    @DisplayName("Изменение имени авторизованного пользователя")
    @Description("PATCH-запрос на ручку /api/auth/user")
    public void changeAuthUserName() {
        user = randomUser();
        userClient = new UserClient();

        ValidatableResponse createUserResponse = userClient.createUser(user);
        accessToken = createUserResponse.extract().path("accessToken");

        User createdUser = new User(user.getEmail(), user.getPassword(),user.getName());

        ValidatableResponse loginResponse = userClient.loginUser(userLogin.creds(createdUser));
        accessToken = loginResponse.extract().path("accessToken");

        String newName = "Sirius";
        createdUser.setEmail(newName);

        ValidatableResponse updateUserNameResponse = userClient.updateUser(createdUser, accessToken);

        assertEquals("Status code is wrong", SC_OK, updateUserNameResponse.extract().statusCode());
        Boolean isUpdateSuccessful = updateUserNameResponse.extract().path("success");
        assertTrue(isUpdateSuccessful);
    }

    @Test
    @DisplayName("Изменение почты неавторизованного пользователя")
    @Description("PATCH-запрос на ручку /api/auth/user")
    public void changeNotAuthUserEmail() {
        user = randomUser();
        userClient = new UserClient();

        ValidatableResponse createUserResponse = userClient.createUser(user);
        accessToken = createUserResponse.extract().path("accessToken");

        User notAuthUser = new User(user.getEmail(), user.getPassword(),user.getName());


        String newEmail = "Sirius_Black@yandex.ru";
        notAuthUser.setEmail(newEmail);

        ValidatableResponse updateNotAuthUserEmailResponse = userClient.updateUser(notAuthUser, accessToken);

        assertEquals("Status code is wrong", SC_UNAUTHORIZED, updateNotAuthUserEmailResponse.extract().statusCode());
        Boolean isUpdateNotSuccessful = updateNotAuthUserEmailResponse.extract().path("success");
        assertFalse(isUpdateNotSuccessful);
    }

    @Test
    @DisplayName("Изменение пароля неавторизованного пользователя")
    @Description("PATCH-запрос на ручку /api/auth/user")
    public void changeNotAuthUserPassword() {
        user = randomUser();
        userClient = new UserClient();

        ValidatableResponse createUserResponse = userClient.createUser(user);
        accessToken = createUserResponse.extract().path("accessToken");

        User notAuthUser = new User(user.getEmail(), user.getPassword(),user.getName());


        String newPassword = "password";
        notAuthUser.setPassword(newPassword);

        ValidatableResponse updateNotAuthUserPasswordResponse = userClient.updateUser(notAuthUser, accessToken);

        assertEquals("Status code is wrong", SC_UNAUTHORIZED, updateNotAuthUserPasswordResponse.extract().statusCode());
        Boolean isUpdateNotSuccessful = updateNotAuthUserPasswordResponse.extract().path("success");
        assertFalse(isUpdateNotSuccessful);
    }

    @Test
    @DisplayName("Изменение имени неавторизованного пользователя")
    @Description("PATCH-запрос на ручку /api/auth/user")
    public void changeNotAuthUserName() {
        user = randomUser();
        userClient = new UserClient();

        ValidatableResponse createUserResponse = userClient.createUser(user);
        accessToken = createUserResponse.extract().path("accessToken");

        User notAuthUser = new User(user.getEmail(), user.getPassword(),user.getName());


        String newName = "Sirius";
        notAuthUser.setName(newName);

        ValidatableResponse updateNotAuthUserNameResponse = userClient.updateUser(notAuthUser, accessToken);

        assertEquals("Status code is wrong", SC_UNAUTHORIZED, updateNotAuthUserNameResponse.extract().statusCode());
        Boolean isUpdateNotSuccessful = updateNotAuthUserNameResponse.extract().path("success");
        assertFalse(isUpdateNotSuccessful);

    }


}
