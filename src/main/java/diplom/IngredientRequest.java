package diplom;

import java.util.List;

public class IngredientRequest {
    @lombok.Getter
    private List<String> ingredients;

    public IngredientRequest(List<String> ingredients) {
        this.ingredients = ingredients;
    }

}
