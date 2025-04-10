package net.ironoc.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Donate {

    private String donate;

    private String link;

    private String img;

    private String alt;

    private String name;

    private String overview;

    private Integer founded;

    private String phone;

    @Override
    public String toString() {
        return "name: '" + this.name + "'";
    }
}
