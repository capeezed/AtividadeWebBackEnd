package br.com.facens.academia.repository;

import br.com.facens.academia.entity.Plano;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlanoRepository extends JpaRepository<Plano, Long> {

    List<Plano> findByAtivoTrue();

    boolean existsByNome(String nome);
}
