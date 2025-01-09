package net.ironoc.portfolio.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@ToString()
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CoffeeDto {

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
}
