package br.com.tinnova.veiculos.controller;

import br.com.tinnova.veiculos.dto.VeiculoDto;
import br.com.tinnova.veiculos.service.VeiculoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins="http://localhost:4200")
@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

  @Autowired
  private final VeiculoService veiculoService;

  public VeiculoController(VeiculoService veiculoService) {
    this.veiculoService = veiculoService;
  }

  // GET /veiculos  e  /veiculos?marca=&ano=&cor=
  // Retorna todos os veículos e os veículos que possuem os parâmetros passados
  @GetMapping
  public List<VeiculoDto> list(
      @RequestParam(name = "marca", required = false) String marca,
      @RequestParam(name = "ano", required = false) Integer ano,
      @RequestParam(name = "cor", required = false) String cor) {
    return veiculoService.listVeiculos(marca, ano, cor);
  }

  // GET /veiculos/{id}
  // Retorna os detalhes de um veículo
  @GetMapping("/{id}")
  public VeiculoDto get(@PathVariable("id") Long id) {
    return veiculoService.getVeiculo(id);
  }

  // POST /veiculos
  // Adiciona um novo veículo
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public VeiculoDto create(@RequestBody VeiculoDto dto) {
    return veiculoService.createVeiculo(dto);
  }

  // PUT /veiculos/{id}
  // Atualiza os dados de um veículo
  @PutMapping("/{id}")
  public VeiculoDto update(@PathVariable("id") Long id, @RequestBody VeiculoDto dto) {
    return veiculoService.updateVeiculo(id, dto);
  }

  // PATCH /veiculos/{id}
  // Atualiza apenas alguns dados de um veículo
  @PatchMapping("/{id}")
  public VeiculoDto patch(@PathVariable("id") Long id, @RequestBody VeiculoDto dto) {
    return veiculoService.patch(id, dto);
  }

  // DELETE /veiculos/{id}
  // Apaga um veículo
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable("id") Long id) {
    veiculoService.delete(id);
  }
}