package praktikum.order;

import io.qameta.allure.Step;

import static io.restassured.RestAssured.given;

public class IngredientsClient {
    private final String GET_INGREDIENTS = "/api/ingredients";

    @Step("Получение списка ингредиентов")
    public Ingredients getIngredients() {
        return given()
                .get(GET_INGREDIENTS)
                .then().log().all()
                .extract()
                .body()
                .as(Ingredients.class);
    }
}
