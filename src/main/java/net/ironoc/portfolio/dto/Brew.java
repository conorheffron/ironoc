package net.ironoc.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Brew {

    private String title;

    private String description;

    private String[] ingredients;

    private String image;

    private Integer id;

    @Override
    public String toString() {
        return "title: '" + this.title + "'";
    }
}
