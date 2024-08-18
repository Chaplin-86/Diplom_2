package OrderTests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import praktikum.order.*;
import praktikum.User.Userrequests.User;
import praktikum.User.Userrequests.UserClient;

import java.util.ArrayList;
import java.util.List;


import static org.apache.http.HttpStatus.*;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static praktikum.UserGenerator.randomUser;

public class CreateOrderTests {
    private static final String BASE_URL = "https://stellarburgers.nomoreparties.site";

    private Ingredients ingredients;
    private IngredientsClient ingredientsClient;
    private OrderClient orderClient;
    private OrderModel order;
    private User user;
    private UserClient userClient;
    private String accessToken;
    private List<String> ingredientId;
    private int statusCode;

    private final String HASH_1 = "987Vrt";
    private final String HASH_2 = "357Ujk";

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
    @DisplayName("Создание заказа с ингредиентами авторизованным пользователем")
    @Description("POST/api/orders")
    public void CreateAuthOrderWithIngredientsTest() {
        ingredientId = new ArrayList<>();
        order = new OrderModel(ingredientId);
        orderClient = new OrderClient();
        ingredientsClient = new IngredientsClient();


        ingredients = ingredientsClient.getIngredients();
        ingredientId.add(ingredients.getData().get(0).get_id());
        ingredientId.add(ingredients.getData().get(1).get_id());


        ValidatableResponse responseToCreateOrder = orderClient.createAuthOrder(order, accessToken.replace("Bearer ", ""));
        statusCode = responseToCreateOrder
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_OK));
        assertThat(responseToCreateOrder.extract().path("success"), equalTo(true));
        assertThat(responseToCreateOrder.extract().path("order "), notNullValue());
    }

    @Test
    @DisplayName("Создание заказа с ингредиентами неавторизованным пользователем")
    @Description("POST/api/orders")
    public void CreateNotAuthOrderWithIngredientsTest() {
        ingredientId = new ArrayList<>();
        order = new OrderModel(ingredientId);
        orderClient = new OrderClient();
        ingredientsClient = new IngredientsClient();


        ingredients = ingredientsClient.getIngredients();
        ingredientId.add(ingredients.getData().get(0).get_id());
        ingredientId.add(ingredients.getData().get(1).get_id());


        ValidatableResponse responseToCreateOrder = orderClient.createNotAuthOrder(order);
        statusCode = responseToCreateOrder
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_OK));
        assertThat(responseToCreateOrder.extract().path("success"), equalTo(true));
        assertThat(responseToCreateOrder.extract().path("order "), notNullValue());
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов авторизованным пользователем")
    @Description("POST/api/orders")
    public void CreateAuthOrderWithoutIngredientsTest() {
        ingredientId = new ArrayList<>();
        order = new OrderModel(ingredientId);
        orderClient = new OrderClient();

        ValidatableResponse responseToCreateOrder = orderClient.createAuthOrder(order, accessToken);
        statusCode = responseToCreateOrder
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_BAD_REQUEST));
        assertThat(responseToCreateOrder.extract().path("success"), equalTo(false));
        assertThat(responseToCreateOrder.extract().path("message "), equalTo("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("Создание заказа без ингредиентов неавторизованным пользователем")
    @Description("POST/api/orders")
    public void CreateNotAuthOrderWithoutIngredientsTest() {
        ingredientId = new ArrayList<>();
        order = new OrderModel(ingredientId);
        orderClient = new OrderClient();

        ValidatableResponse responseToCreateOrder = orderClient.createNotAuthOrder(order);
        statusCode = responseToCreateOrder
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_BAD_REQUEST));
        assertThat(responseToCreateOrder.extract().path("success"), equalTo(false));
        assertThat(responseToCreateOrder.extract().path("message "), equalTo("Ingredient ids must be provided"));
    }



    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов авторизованным пользователем")
    @Description("POST/api/orders")
    public void CreateAuthOrderWitInvalidIngredientsHashTest() {
        ingredientId = new ArrayList<>();
        order = new OrderModel(ingredientId);
        orderClient = new OrderClient();
        ingredientsClient = new IngredientsClient();

        ingredients = ingredientsClient.getIngredients();
        ingredientId.add(ingredients.getData().get(0).get_id() + HASH_1);
        ingredientId.add(ingredients.getData().get(1).get_id() + HASH_2);


        ValidatableResponse responseToCreateOrder = orderClient.createAuthOrder(order, accessToken);
        statusCode = responseToCreateOrder
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_INTERNAL_SERVER_ERROR));

    }


    @Test
    @DisplayName("Создание заказа с неверным хешем ингредиентов неавторизованным пользователем")
    @Description("POST/api/orders")
    public void CreateNotAuthOrderWithInvalidIngredientsHashTest() {
        ingredientId = new ArrayList<>();
        order = new OrderModel(ingredientId);
        orderClient = new OrderClient();
        ingredientsClient = new IngredientsClient();

        ingredients = ingredientsClient.getIngredients();
        ingredientId.add(ingredients.getData().get(0).get_id() + HASH_1);
        ingredientId.add(ingredients.getData().get(1).get_id() + HASH_2);


        ValidatableResponse responseToCreateOrder = orderClient.createNotAuthOrder(order);
        statusCode = responseToCreateOrder
                .extract()
                .statusCode();
        assertThat(statusCode, equalTo(SC_INTERNAL_SERVER_ERROR));

    }






}
