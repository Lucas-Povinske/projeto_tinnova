package br.com.tinnova.veiculos;

import br.com.tinnova.veiculos.dto.VeiculoDto;
import br.com.tinnova.veiculos.model.Veiculo;
import br.com.tinnova.veiculos.repository.VeiculoRepository;
import br.com.tinnova.veiculos.service.VeiculoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VeiculoServiceTest {

    @Mock
    private VeiculoRepository veiculoRepository;

    @InjectMocks
    private VeiculoService veiculoService;

    private Veiculo entity;
    private VeiculoDto dto;

    @BeforeEach
    void setUp() {
        entity = new Veiculo();
        entity.setId(1L);
        entity.setVeiculo("Civic");
        entity.setMarca("Honda");
        entity.setAno(2020);
        entity.setCor("Preto");
        entity.setDescricao("Sedan");
        entity.setVendido(false);

        dto = new VeiculoDto();
        dto.setId(1L);
        dto.setVeiculo("Civic");
        dto.setMarca("Honda");
        dto.setAno(2020);
        dto.setCor("Preto");
        dto.setDescricao("Sedan");
        dto.setVendido(false);
    }

    @Test
    void create_deveSalvarERetornarDto() {
        when(veiculoRepository.save(any(Veiculo.class))).thenAnswer(inv -> {
            Veiculo v = inv.getArgument(0);
            v.setId(1L);
            return v;
        });

        VeiculoDto novo = new VeiculoDto();
        novo.setVeiculo("Civic");
        novo.setMarca("Honda");
        novo.setAno(2020);
        novo.setVendido(false);

        VeiculoDto salvo = veiculoService.createVeiculo(novo);

        assertThat(salvo.getId()).isEqualTo(1L);
        assertThat(salvo.getMarca()).isEqualTo("Honda");
        verify(veiculoRepository).save(any(Veiculo.class));
    }

    @Test
    void get_deveTrazerPorId() {
        when(veiculoRepository.findById(1L)).thenReturn(Optional.of(entity));

        VeiculoDto out = veiculoService.getVeiculo(1L);

        assertThat(out.getId()).isEqualTo(1L);
        assertThat(out.getVeiculo()).isEqualTo("Civic");
        verify(veiculoRepository).findById(1L);
    }

    @Test
    void list_semFiltros_deveTrazerTodos() {
        when(veiculoRepository.search(null, null, null)).thenReturn(List.of(entity));

        List<VeiculoDto> lista = veiculoService.listVeiculos(null, null, null);

        assertThat(lista).hasSize(1);
        verify(veiculoRepository).search(null, null, null);
    }

    @Test
    void delete_deveExcluir() {
        veiculoService.deleteVeiculo(1L);
        verify(veiculoRepository).deleteById(1L);
    }
}
