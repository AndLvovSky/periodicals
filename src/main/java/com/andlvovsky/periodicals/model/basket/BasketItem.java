package com.andlvovsky.periodicals.model.basket;

import com.andlvovsky.periodicals.model.publication.Publication;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasketItem {

    private Publication publication;

    private Integer number;

}
