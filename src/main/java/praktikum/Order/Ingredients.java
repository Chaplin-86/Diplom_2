package praktikum.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder

public class Ingredients {
    private String success;
    private List<IngredientsData> data;
    public Ingredients(List<IngredientsData> data, String success) {
        this.data = data;
        this.success = success;
    }

    public Ingredients() {

    }
    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public List<IngredientsData> getData() {
        return data;
    }

    public void setData(List<IngredientsData> data) {
        this.data = data;
    }
}
