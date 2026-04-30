package br.com.facens.academia.service;

import br.com.facens.academia.dto.request.PlanoRequest;
import br.com.facens.academia.dto.response.PlanoResponse;
import br.com.facens.academia.entity.Plano;
import br.com.facens.academia.exception.RecursoNaoEncontradoException;
import br.com.facens.academia.exception.RegraNegocioException;
import br.com.facens.academia.repository.PlanoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlanoService {

    private final PlanoRepository planoRepository;

    public PlanoResponse cadastrar(PlanoRequest request) {
        // Regra de negócio: não permitir planos com mesmo nome
        if (planoRepository.existsByNome(request.getNome())) {
            throw new RegraNegocioException("Já existe um plano com o nome: " + request.getNome());
        }

        Plano plano = new Plano();
        plano.setNome(request.getNome());
        plano.setPreco(request.getPreco());
        plano.setDuracaoMeses(request.getDuracaoMeses());
        plano.setAtivo(request.getAtivo() != null ? request.getAtivo() : true);

        Plano salvo = planoRepository.save(plano);
        return toResponse(salvo);
    }

    public List<PlanoResponse> listarTodos() {
        return planoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<PlanoResponse> listarAtivos() {
        return planoRepository.findByAtivoTrue()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public PlanoResponse buscarPorId(Long id) {
        Plano plano = planoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Plano não encontrado com ID: " + id));
        return toResponse(plano);
    }

    public PlanoResponse atualizar(Long id, PlanoRequest request) {
        Plano plano = planoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Plano não encontrado com ID: " + id));

        plano.setNome(request.getNome());
        plano.setPreco(request.getPreco());
        plano.setDuracaoMeses(request.getDuracaoMeses());
        plano.setAtivo(request.getAtivo() != null ? request.getAtivo() : plano.getAtivo());

        Plano atualizado = planoRepository.save(plano);
        return toResponse(atualizado);
    }

    public void excluir(Long id) {
        Plano plano = planoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Plano não encontrado com ID: " + id));

        // Regra de negócio: não excluir plano com alunos vinculados
        if (plano.getAlunos() != null && !plano.getAlunos().isEmpty()) {
            throw new RegraNegocioException("Não é possível excluir o plano pois existem alunos vinculados a ele.");
        }

        planoRepository.delete(plano);
    }

    // Método auxiliar para converter entidade em DTO de resposta
    private PlanoResponse toResponse(Plano plano) {
        return new PlanoResponse(
                plano.getId(),
                plano.getNome(),
                plano.getPreco(),
                plano.getDuracaoMeses(),
                plano.getAtivo()
        );
    }

    // Método usado internamente por AlunoService
    public Plano buscarEntidadePorId(Long id) {
        return planoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Plano não encontrado com ID: " + id));
    }
}
