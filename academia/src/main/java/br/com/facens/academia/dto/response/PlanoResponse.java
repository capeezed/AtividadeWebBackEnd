package br.com.facens.academia.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanoResponse {

    private Long id;
    private String nome;
    private BigDecimal preco;
    private Integer duracaoMeses;
    private Boolean ativo;
}
