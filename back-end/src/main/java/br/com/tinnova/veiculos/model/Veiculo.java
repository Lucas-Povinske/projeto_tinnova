package br.com.tinnova.veiculos.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.OffsetDateTime;

// Entidade JPA para Veículo
@Data // Gera getters, setters, toString, equals e hashCode automaticamente
@Entity
@Table(name = "veiculos")
public class Veiculo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String veiculo; // Identificação do veículo

  @Column(nullable = false)
  private String marca;

  @Column(nullable = false)
  private Integer ano;

  @Column(nullable = true)
  private String cor;

  @Column(columnDefinition = "text")
  private String descricao;

  @Column(nullable = false)
  private Boolean vendido = false;

  @Column(nullable = false)
  private OffsetDateTime created;

  @Column(nullable = false)
  private OffsetDateTime updated;

  @PrePersist
  protected void onCreate() {
    OffsetDateTime now = OffsetDateTime.now();
    this.created = now;
    this.updated = now;
  }

  @PreUpdate
  protected void onUpdate() {
    this.updated = OffsetDateTime.now();
  }
}