package br.com.tinnova.veiculos.repository;

import br.com.tinnova.veiculos.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

// Repositório JPA para Veículo
public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

  // Filtro básico (qualquer parâmetro pode vir nulo)
  // Usando lower() para comparação case-insensitive e @Param para mapear parâmetros
  @Query("""
    select v from Veiculo v
    where (:marca is null or lower(v.marca) = :marca)
      and (:ano   is null or v.ano = :ano)
      and (:cor   is null or lower(v.cor)   = :cor)
  """)
  List<Veiculo> search(@Param("marca") String marcaLower,
                       @Param("ano") Integer ano,
                       @Param("cor") String corLower);
}