package br.com.facens.academia.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AlunoRequest {

    @NotBlank(message = "O nome do aluno é obrigatório")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido")
    private String email;

    @NotBlank(message = "O telefone é obrigatório")
    private String telefone;

    @NotNull(message = "A data de nascimento é obrigatória")
    @Past(message = "A data de nascimento deve ser no passado")
    private LocalDate dataNascimento;

    @NotNull(message = "O plano é obrigatório")
    private Long planoId;
}
