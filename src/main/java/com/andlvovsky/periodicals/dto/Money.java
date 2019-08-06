package com.andlvovsky.periodicals.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Money {

    private Integer dollars;

    private Integer cents;

    public static Money fromDouble(Double number) {
        Integer dollars = number.intValue();
        Integer cents = (int)Math.round((number - dollars) * 100);
        return new Money(dollars, cents);
    }

    @JsonProperty("doubleValue")
    public double toDouble() {
        return dollars + (double)cents / 100;
    }

}
