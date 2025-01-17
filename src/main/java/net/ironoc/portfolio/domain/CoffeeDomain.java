package net.ironoc.portfolio.domain;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CoffeeDomain {

    @Schema(name= "title", description = "Coffee Name/Type.", example = "Cold Brew",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String title;

    @Schema(name= "description", description = "Drink Description.", example = "The trendiest of the iced coffee bunch",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private String description;

    @Schema(name= "ingredients", description = "Main Ingredients.", example = "Long steeped coffee, Ice",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private List<String> ingredients;

    @Schema(name= "image", description = "Image URL.",
            example = "https://upload.wikimedia.org/640px-ColdBrewCoffeein_Cans.png",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String image;

    @Schema(name= "id", description = "ID of Coffee Details Object.", example = "3",
            requiredMode = Schema.RequiredMode.AUTO)
    private Integer id;

    @Override
    public String toString() {
        return "name: '" + this.title + "'" + " id: '" + this.id + "'";
    }
}
