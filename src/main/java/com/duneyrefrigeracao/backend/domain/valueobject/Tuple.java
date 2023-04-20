package com.duneyrefrigeracao.backend.domain.valueobject;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tuple<T,J> {
    private T firstValue;
    private J secondValue;
}
