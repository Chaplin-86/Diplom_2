package praktikum.Order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder

public class IngredientsData {
    private String _id;
    private String name;
    private String type;
    private float proteins;
    private float fat;
    private float carbohydrates;
    private float calories;
    private float price;
    private String image;
    private String image_mobile;
    private String image_large;
    private String __v;

    public IngredientsData(String _id, String name, String type, String proteins, String fat,
                       String carbohydrates, String calories, String price, String image,
                       String image_mobile, String image_large, String __v) {
        this._id = _id;
        this.name = name;
        this.type = type;
        this.proteins = Float.parseFloat(proteins);
        this.fat = Float.parseFloat(fat);
        this.carbohydrates = Float.parseFloat(carbohydrates);
        this.calories = Float.parseFloat(calories);
        this.price = Float.parseFloat(price);
        this.image = image;
        this.image_mobile = image_mobile;
        this.image_large = image_large;
        this.__v = __v;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

}
