package net.ironoc.portfolio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.ironoc.portfolio.enums.SortingOrder;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonateItemOrder {

    private SortingOrder founded;

    private SortingOrder name;
}
