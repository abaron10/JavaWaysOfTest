package com.abaron.springboot_test.controllers;

import com.abaron.springboot_test.models.Cuenta;
import com.abaron.springboot_test.models.TransaccionDto;
import com.abaron.springboot_test.repositories.BancoRepository;
import com.abaron.springboot_test.repositories.CuentaRepository;
import com.abaron.springboot_test.services.CuentaService;
import com.abaron.springboot_test.services.CuentaServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class CuentaControllerWebTestClientTests {
    private ObjectMapper objectMapper;
    @Autowired
    private WebTestClient client;
    @Autowired
    CuentaRepository cuentaRepository;
    @Autowired
    BancoRepository bancoRepository;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

    }


    @Test
    @Order(1)
    void  testTranferir(){
        TransaccionDto dto = new TransaccionDto();
        var service = new CuentaServiceImpl(cuentaRepository, bancoRepository);
        dto.setCuentaDestinoId(2L);
        dto.setCuentaOrigenId(1l);
        dto.setMonto(new BigDecimal("100"));
        dto.setBancoId(1L);
        //given
        Map<String,Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status","ok");
        response.put("mensaje","Transferencia realizada con exito");
        Cuenta cuenta = service.findById(1L);
        System.out.println(cuenta.getSaldo());

        //when
        try {
            client.post().uri("api/cuentas/transferir")
                    .contentType(MediaType.APPLICATION_JSON).bodyValue(dto)
                    .exchange().expectStatus().isAccepted()
                    //then
                    .expectBody()
                    .jsonPath("$.mensaje").isNotEmpty()
                    .jsonPath("$.mensaje").value(is("Transferencia realizada con exito"))
                    .jsonPath("$.mensaje").value(mensaje->{
                        assertEquals(mensaje,"Transferencia realizada con exito");
                    })
                    .json(objectMapper.writeValueAsString(response));
            System.out.println(cuenta.getSaldo());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    void testDetalle() {
        client.get().uri("/api/cuentas/1").exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.persona").isEqualTo("Andres")
                .jsonPath("$.saldo").isEqualTo(1000);
    }


    @Test
    @Order(3)
    void testGuardar() {
        Cuenta cuenta = new Cuenta(null,"pepe",new BigDecimal("3000"));
        client.post().uri("/api/cuentas")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cuenta)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.id").isEqualTo(3);

    }

    @Test

    void testDelete() {

        client.get().uri("/api/cuentas/3")
                .exchange()
                .expectStatus().isOk();

        client.delete().uri("/api/cuentas/3")
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();

        /*client.get().uri("/api/cuentas/3")
                .exchange()
                .expectStatus().is5xxServerError();*/

        client.get().uri("/api/cuentas/3")
                .exchange()
                .expectStatus().isNotFound().expectBody().isEmpty();

    }
}