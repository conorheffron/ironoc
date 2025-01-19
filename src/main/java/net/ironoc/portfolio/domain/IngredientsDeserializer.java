package net.ironoc.portfolio.domain;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import net.ironoc.portfolio.exception.IronocJsonException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IngredientsDeserializer extends JsonDeserializer<List<String>> {

    @Override
    public List<String> deserialize(JsonParser p, DeserializationContext context) throws IOException, IronocJsonException {
        JsonNode node = p.getCodec().readTree(p);
        List<String> ingredients = new ArrayList<>();

        if (node.isArray()) {
            for (JsonNode element : node) {
                ingredients.add(element.asText());
            }
        } else {
            throw new IronocJsonException("Unexpected exception occurred deserializing ingredients");
        }
        return ingredients;
    }
}
