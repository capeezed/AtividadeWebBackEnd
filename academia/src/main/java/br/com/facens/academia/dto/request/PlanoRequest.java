package br.com.facens.academia.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PlanoRequest {

    @NotBlank(message = "O nome do plano é obrigatório")
    private String nome;

    @NotNull(message = "O preço é obrigatório")
    @Positive(message = "O preço deve ser maior que zero")
    private BigDecimal preco;

    @NotNull(message = "A duração em meses é obrigatória")
    @Min(value = 1, message = "A duração deve ser de no mínimo 1 mês")
    private Integer duracaoMeses;

    private Boolean ativo = true;
}
