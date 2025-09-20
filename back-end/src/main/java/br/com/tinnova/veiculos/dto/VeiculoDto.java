package br.com.tinnova.veiculos.dto;

import lombok.Data;

import java.time.OffsetDateTime;

// Data Transfer Object (DTO) para Veículo
@Data
public class VeiculoDto{
    private Long id;
    private String veiculo;
    private String marca;
    private Integer ano;
    private String cor;
    private String descricao;
    private Boolean vendido;
    private OffsetDateTime created;
    private OffsetDateTime updated;
}
