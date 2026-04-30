package br.com.facens.academia.config;

import br.com.facens.academia.dto.request.AlunoRequest;
import br.com.facens.academia.dto.request.PlanoRequest;
import br.com.facens.academia.service.AlunoService;
import br.com.facens.academia.service.PlanoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final PlanoService planoService;
    private final AlunoService alunoService;

    @Override
    public void run(String... args) {
        try {
            log.info("=== Iniciando carga de dados de exemplo ===");

            // Cadastrar planos
            PlanoRequest planoBasico = new PlanoRequest();
            planoBasico.setNome("Plano Básico");
            planoBasico.setPreco(new BigDecimal("89.90"));
            planoBasico.setDuracaoMeses(1);
            planoBasico.setAtivo(true);
            var basico = planoService.cadastrar(planoBasico);
            log.info("Plano cadastrado: {}", basico.getNome());

            PlanoRequest planoMensal = new PlanoRequest();
            planoMensal.setNome("Plano Mensal");
            planoMensal.setPreco(new BigDecimal("129.90"));
            planoMensal.setDuracaoMeses(3);
            planoMensal.setAtivo(true);
            var mensal = planoService.cadastrar(planoMensal);
            log.info("Plano cadastrado: {}", mensal.getNome());

            PlanoRequest planoAnual = new PlanoRequest();
            planoAnual.setNome("Plano Anual");
            planoAnual.setPreco(new BigDecimal("899.90"));
            planoAnual.setDuracaoMeses(12);
            planoAnual.setAtivo(true);
            var anual = planoService.cadastrar(planoAnual);
            log.info("Plano cadastrado: {}", anual.getNome());

            // Cadastrar alunos
            AlunoRequest aluno1 = new AlunoRequest();
            aluno1.setNome("João Silva");
            aluno1.setEmail("joao.silva@email.com");
            aluno1.setTelefone("(15) 99999-0001");
            aluno1.setDataNascimento(LocalDate.of(1995, 3, 15));
            aluno1.setPlanoId(basico.getId());
            var a1 = alunoService.cadastrar(aluno1);
            log.info("Aluno cadastrado: {}", a1.getNome());

            AlunoRequest aluno2 = new AlunoRequest();
            aluno2.setNome("Maria Oliveira");
            aluno2.setEmail("maria.oliveira@email.com");
            aluno2.setTelefone("(15) 99999-0002");
            aluno2.setDataNascimento(LocalDate.of(2000, 7, 22));
            aluno2.setPlanoId(mensal.getId());
            var a2 = alunoService.cadastrar(aluno2);
            log.info("Aluno cadastrado: {}", a2.getNome());

            AlunoRequest aluno3 = new AlunoRequest();
            aluno3.setNome("Carlos Souza");
            aluno3.setEmail("carlos.souza@email.com");
            aluno3.setTelefone("(15) 99999-0003");
            aluno3.setDataNascimento(LocalDate.of(1998, 11, 5));
            aluno3.setPlanoId(anual.getId());
            var a3 = alunoService.cadastrar(aluno3);
            log.info("Aluno cadastrado: {}", a3.getNome());

            log.info("=== Carga de dados finalizada com sucesso! ===");

        } catch (Exception e) {
            log.warn("Dados já cadastrados ou erro ao inicializar: {}", e.getMessage());
        }
    }
}
