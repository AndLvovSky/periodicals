package com.andlvovsky.periodicals.model.basket;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasketItemDto {

    @NonNull
    private Long publicationId;

    @NotNull
    @Min(1)
    private Integer number;

}
