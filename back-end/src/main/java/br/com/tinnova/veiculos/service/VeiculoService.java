package br.com.tinnova.veiculos.service;

import br.com.tinnova.veiculos.dto.VeiculoDto;
import br.com.tinnova.veiculos.model.Veiculo;
import br.com.tinnova.veiculos.repository.VeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class VeiculoService {

  private static final Set<String> MARCAS_VALIDAS = Set.of(
      "Volkswagen","Ford","Honda","Chevrolet","Fiat","Toyota",
      "Hyundai","Renault","Nissan","Peugeot","BMW","Mercedes-Benz","Audi","Kia"
  );

  @Autowired
  private VeiculoRepository veiculoRepository;

  public VeiculoService(VeiculoRepository veiculoRepository) {
    this.veiculoRepository = veiculoRepository;
  }

  // ---------------------- CRUD ----------------------

  // Listar Veículos - Lista todos se não existe nenhum parâmetro de busca
  public List<VeiculoDto> listVeiculos(String marca, Integer ano, String cor) {
    // Usando lower() para comparação case-insensitive
    String m = (marca == null) ? null : marca.toLowerCase(Locale.ROOT);
    String c = (cor   == null) ? null : cor.toLowerCase(Locale.ROOT);
    return veiculoRepository.search(m, ano, c).stream().map(VeiculoService::setVeiculoDto).toList();
  }

  // Retorna o detalhe de um veículo
  public VeiculoDto getVeiculo(Long id) {
    Veiculo v = veiculoRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));
    return setVeiculoDto(v);
  }

  // Adiciona um novo veículo
  @Transactional
  public VeiculoDto createVeiculo(VeiculoDto dto) {
    validarMarca(dto.getMarca());
    if (dto.getVeiculo() == null || dto.getVeiculo().isBlank())
      throw new ValidationException("Campo 'veiculo' é obrigatório.");
    if (dto.getAno() == null)
      throw new ValidationException("Campo 'ano' é obrigatório.");

    Veiculo v = new Veiculo();
    setVeiculoFromDto(v, dto); // A criação e update são setados pelas callbacks @PrePersist/@PreUpdate no model
    v = veiculoRepository.save(v);
    return setVeiculoDto(v);
  }

  // Atualiza os dados de um veículo
  @Transactional
  public VeiculoDto updateVeiculo(Long id, VeiculoDto dto) {
    validarMarca(dto.getMarca());
    if (dto.getVeiculo() == null || dto.getVeiculo().isBlank())
      throw new ValidationException("Campo 'veiculo' é obrigatório.");
    if (dto.getAno() == null)
      throw new ValidationException("Campo 'ano' é obrigatório.");

    Veiculo v = veiculoRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));
    setVeiculoFromDto(v, dto);
    return setVeiculoDto(v);
  }

  // Atualiza apenas alguns dados de um veículo
  @Transactional
  public VeiculoDto patch(Long id, VeiculoDto dto) {
    if (dto.getMarca() != null) validarNomeMarca(dto.getMarca());
    Veiculo v = veiculoRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Veículo não encontrado"));
    applyNonNullFromDto(v, dto);
    return setVeiculoDto(v);
  }

  // Apaga um veículo
  public void delete(Long id) {
    veiculoRepository.deleteById(id);
  }

  // ---------------------- Funções auxiliares ----------------------

  // Valida se a Marca foi enviada pelo front
  private void validarMarca(String marca) {
    if (marca == null || marca.isBlank())
      throw new ValidationException("Marca é obrigatória.");
    validarNomeMarca(marca);
  }

  //Valida se a Marca está dentre os nomes válidos
  private void validarNomeMarca(String marca) {
    if (marca == null) return;
    boolean marcaValida = MARCAS_VALIDAS.stream().anyMatch(m -> m.equalsIgnoreCase(marca));
    if (!marcaValida) throw new ValidationException("Marca inválida: " + marca);
  }

  // Transforma Veículo -> Dto
  private static VeiculoDto setVeiculoDto(Veiculo v) {
    VeiculoDto dto = new VeiculoDto();
    dto.setId(v.getId());
    dto.setVeiculo(v.getVeiculo());
    dto.setMarca(v.getMarca());
    dto.setAno(v.getAno());
    dto.setCor(v.getCor());
    dto.setDescricao(v.getDescricao());
    dto.setVendido(v.getVendido());
    dto.setCreated(v.getCreated());
    dto.setUpdated(v.getUpdated());
    return dto;
  }

  // Transform Dto -> Veículo
  private static void setVeiculoFromDto(Veiculo veiculo, VeiculoDto dto) {
    veiculo.setVeiculo(dto.getVeiculo());
    veiculo.setMarca(dto.getMarca());
    veiculo.setAno(dto.getAno());
    veiculo.setCor(dto.getCor());
    veiculo.setDescricao(dto.getDescricao());
    veiculo.setVendido(Boolean.TRUE.equals(dto.getVendido()));
  }

  // Utilizado para o Patch: Atualiza somente os campos alterados
  private static void applyNonNullFromDto(Veiculo veiculo, VeiculoDto dto) {
    if (dto.getVeiculo() != null)   veiculo.setVeiculo(dto.getVeiculo());
    if (dto.getMarca() != null)     veiculo.setMarca(dto.getMarca());
    if (dto.getAno() != null)       veiculo.setAno(dto.getAno());
    if (dto.getCor() != null)       veiculo.setCor(dto.getCor());
    if (dto.getDescricao() != null) veiculo.setDescricao(dto.getDescricao());
    if (dto.getVendido() != null)   veiculo.setVendido(dto.getVendido());
  }
}