package praktikum.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder

public class OrderModel {
    private List<String> ingredients;
    private List<Ingredients> data;


    public OrderModel(List<Ingredients> data, boolean success) {
        this.data = data;

    }
    public OrderModel(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public OrderModel() {

    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }
}
