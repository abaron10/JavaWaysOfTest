package com.abaron.springboot_test;

import com.abaron.springboot_test.controllers.CuentaController;
import com.abaron.springboot_test.models.TransaccionDto;
import com.abaron.springboot_test.services.CuentaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CuentaController.class)
public class CuentaControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    CuentaService cuentaService;

    @Test
    void detalle() throws Exception {
        when(cuentaService.findById(1L)).thenReturn(Datos.crearCuenta001());
        this.mvc.perform(get("/api/cuentas/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.persona").value("Andres"))
                .andReturn();
        verify(cuentaService,atLeast(1)).findById(1L);
    }

    @Test
    void testTransferir() throws Exception {
        TransaccionDto dto = new TransaccionDto();
        dto.setCuentaDestinoId(2L);
        dto.setCuentaOrigenId(1l);
        dto.setMonto(new BigDecimal("100"));
        dto.setBancoId(1L);
        this.mvc.perform(post("/api/cuentas/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("ok"))
                .andExpect(jsonPath("$.mensaje").value("Transferencia realizada con exito"));

    }

    @Test
    void testTransferirJson() throws Exception {
        TransaccionDto dto = new TransaccionDto();
        dto.setCuentaDestinoId(2L);
        dto.setCuentaOrigenId(1l);
        dto.setMonto(new BigDecimal("100"));
        dto.setBancoId(1L);

        ObjectMapper objectMapper = new ObjectMapper();

        Map<String,Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status","ok");
        response.put("mensaje","Transferencia realizada con exito");

        String res = objectMapper.writeValueAsString(response);

        this.mvc.perform(post("/api/cuentas/transferir")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.status").value("ok"))
                .andExpect(jsonPath("$.mensaje").value("Transferencia realizada con exito"))
                .andExpect(content().json(res));


    }
}
