package br.com.tinnova.veiculos;

import br.com.tinnova.veiculos.controller.VeiculoController;
import br.com.tinnova.veiculos.dto.VeiculoDto;
import br.com.tinnova.veiculos.service.VeiculoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.annotation.Resource;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = VeiculoController.class)
class VeiculoControllerTest {

    @Resource
    private MockMvc mvc;

    @MockBean
    private VeiculoService veiculoService;

    @Test
    void getAll_deveRetornar200ComLista() throws Exception {
        VeiculoDto d = new VeiculoDto();
        d.setId(1L); d.setVeiculo("Civic"); d.setMarca("Honda"); d.setAno(2020); d.setVendido(false);

        when(veiculoService.listVeiculos(null, null, null)).thenReturn(List.of(d));

        mvc.perform(get("/veiculos"))
           .andExpect(status().isOk())
           .andExpect(content().contentType(MediaType.APPLICATION_JSON))
           .andExpect(jsonPath("$[0].id").value(1))
           .andExpect(jsonPath("$[0].marca").value("Honda"));
    }

    @Test
    void create_deveRetornar201Ou200ComBody() throws Exception {
        VeiculoDto out = new VeiculoDto();
        out.setId(1L); out.setVeiculo("Civic"); out.setMarca("Honda"); out.setAno(2020); out.setVendido(false);

        when(veiculoService.createVeiculo(org.mockito.ArgumentMatchers.any(VeiculoDto.class))).thenReturn(out);

        String payload = """
          {"veiculo":"Civic","marca":"Honda","ano":2020,"vendido":false}
        """;

        mvc.perform(post("/veiculos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
           .andExpect(status().isCreated())
           .andExpect(jsonPath("$.id").value(1))
           .andExpect(jsonPath("$.veiculo").value("Civic"));
    }

    @Test
    void update_deveRetornar200() throws Exception {
        VeiculoDto out = new VeiculoDto();
        out.setId(1L); out.setVeiculo("Civic EX"); out.setMarca("Honda"); out.setAno(2021); out.setVendido(true);

        when(veiculoService.updateVeiculo(org.mockito.ArgumentMatchers.eq(1L), org.mockito.ArgumentMatchers.any(VeiculoDto.class)))
                .thenReturn(out);

        String payload = """
          {"veiculo":"Civic EX","marca":"Honda","ano":2021,"vendido":true}
        """;

        mvc.perform(put("/veiculos/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.ano").value(2021))
           .andExpect(jsonPath("$.vendido").value(true));
    }

    @Test
    void delete_deveRetornar204() throws Exception {
        mvc.perform(delete("/veiculos/1"))
           .andExpect(status().isNoContent());
    }
}
