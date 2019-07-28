package com.andlvovsky.periodicals.model.subscription;

import com.andlvovsky.periodicals.model.publication.Publication;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Basket {

    private Publication publication;

    private Integer number;

}
