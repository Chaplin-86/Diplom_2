package OrderTests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.order.OrderClient;
import praktikum.User.Userrequests.User;
import praktikum.User.Userrequests.UserClient;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static praktikum.UserGenerator.randomUser;

public class GetOrderTests {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";
    private User user;
    private UserClient userClient;
    private OrderClient orderClient;
    private String accessToken;
    private int statusCode;


    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URL;

        user = randomUser();
        userClient = new UserClient();

        ValidatableResponse responseToCreateUser = userClient.createUser(user);
        accessToken = responseToCreateUser
                .extract()
                .path("accessToken");
    }

    @After
    public void deleteUser() {
        if(accessToken != null){
            userClient.deleteUser(accessToken);
        }
    }

    @Test
    @DisplayName("Получение заказа авторизованного пользователя")
    @Description("GET/api/orders")
    public void GetAuthUserOrdersTest() {
        orderClient = new OrderClient();

        ValidatableResponse response = orderClient.getAuthOrder(accessToken.replace("Bearer ", ""));
        statusCode = response.extract().statusCode();
        assertThat(statusCode, equalTo(SC_OK));
        assertThat(response.extract().path("success"), equalTo(true));

    }

    @Test
    @DisplayName("Получение заказа неавторизованного пользователя")
    @Description("Get orders without auth user. POST/api/orders")
    public void GetOrdersWithoutAuthUserTest() {
        orderClient = new OrderClient();

        ValidatableResponse response = orderClient.getNotAuthOrder();
        statusCode = response.extract().statusCode();
        assertThat(statusCode, equalTo(SC_UNAUTHORIZED));
        assertThat(response.extract().path("success"), equalTo(false));
        assertThat(response.extract().path("message"), equalTo("You should be authorised"));

    }
}
