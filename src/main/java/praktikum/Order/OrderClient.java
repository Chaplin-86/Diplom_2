package praktikum.Order;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient {

    private static final String ORDERS = "/api/orders";
    @Step("Создание заказа авторизованным пользователем")
    public ValidatableResponse createAuthOrder (OrderModel order, String accessToken) {
        return given()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .body(order)
                .post(ORDERS)
                .then().log().all();
    }

    @Step("Создание заказа неавторизованным пользователем")
    public ValidatableResponse createNotAuthOrder (OrderModel order) {
        return given()
                .contentType(ContentType.JSON)
                .body(order)
                .post(ORDERS)
                .then().log().all();
    }

    @Step("Получение заказа авторизованного пользователя")
    public ValidatableResponse getAuthOrder (String accessToken) {
        return given()
                .auth().oauth2(accessToken)
                .contentType(ContentType.JSON)
                .get(ORDERS)
                .then().log().all();
    }

    @Step("Получение заказа неавторизованного пользователя")
    public ValidatableResponse getNotAuthOrder () {
        return given()
                .contentType(ContentType.JSON)
                .get(ORDERS)
                .then().log().all();
    }
}
