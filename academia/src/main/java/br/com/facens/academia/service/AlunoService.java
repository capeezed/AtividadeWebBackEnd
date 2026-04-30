package br.com.facens.academia.service;

import br.com.facens.academia.dto.request.AlunoRequest;
import br.com.facens.academia.dto.response.AlunoResponse;
import br.com.facens.academia.entity.Aluno;
import br.com.facens.academia.entity.Plano;
import br.com.facens.academia.exception.RecursoNaoEncontradoException;
import br.com.facens.academia.exception.RegraNegocioException;
import br.com.facens.academia.repository.AlunoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlunoService {

    private final AlunoRepository alunoRepository;
    private final PlanoService planoService;

    public AlunoResponse cadastrar(AlunoRequest request) {
        // Regra de negócio: não permitir e-mail duplicado
        if (alunoRepository.existsByEmail(request.getEmail())) {
            throw new RegraNegocioException("Já existe um aluno cadastrado com o e-mail: " + request.getEmail());
        }

        // Regra de negócio: buscar plano e verificar se está ativo
        Plano plano = planoService.buscarEntidadePorId(request.getPlanoId());
        if (!plano.getAtivo()) {
            throw new RegraNegocioException("Não é possível matricular alunos em um plano inativo.");
        }

        Aluno aluno = new Aluno();
        aluno.setNome(request.getNome());
        aluno.setEmail(request.getEmail());
        aluno.setTelefone(request.getTelefone());
        aluno.setDataNascimento(request.getDataNascimento());
        aluno.setDataMatricula(LocalDate.now());
        aluno.setPlano(plano);

        Aluno salvo = alunoRepository.save(aluno);
        return toResponse(salvo);
    }

    public List<AlunoResponse> listarTodos() {
        return alunoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public AlunoResponse buscarPorId(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com ID: " + id));
        return toResponse(aluno);
    }

    public AlunoResponse buscarPorEmail(String email) {
        Aluno aluno = alunoRepository.findByEmail(email)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com e-mail: " + email));
        return toResponse(aluno);
    }

    public AlunoResponse atualizar(Long id, AlunoRequest request) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com ID: " + id));

        // Regra: se o e-mail mudou, verificar se o novo e-mail já está em uso
        if (!aluno.getEmail().equals(request.getEmail()) && alunoRepository.existsByEmail(request.getEmail())) {
            throw new RegraNegocioException("Já existe um aluno cadastrado com o e-mail: " + request.getEmail());
        }

        Plano plano = planoService.buscarEntidadePorId(request.getPlanoId());
        if (!plano.getAtivo()) {
            throw new RegraNegocioException("Não é possível vincular alunos em um plano inativo.");
        }

        aluno.setNome(request.getNome());
        aluno.setEmail(request.getEmail());
        aluno.setTelefone(request.getTelefone());
        aluno.setDataNascimento(request.getDataNascimento());
        aluno.setPlano(plano);

        Aluno atualizado = alunoRepository.save(aluno);
        return toResponse(atualizado);
    }

    public void excluir(Long id) {
        Aluno aluno = alunoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Aluno não encontrado com ID: " + id));
        alunoRepository.delete(aluno);
    }

    // Método auxiliar para converter entidade em DTO de resposta
    private AlunoResponse toResponse(Aluno aluno) {
        return new AlunoResponse(
                aluno.getId(),
                aluno.getNome(),
                aluno.getEmail(),
                aluno.getTelefone(),
                aluno.getDataNascimento(),
                aluno.getDataMatricula(),
                aluno.getPlano().getNome()
        );
    }
}
