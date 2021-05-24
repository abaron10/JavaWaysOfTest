package com.abaron.springboot_test;

import com.abaron.springboot_test.models.Cuenta;
import com.abaron.springboot_test.repositories.CuentaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class IntegrationJpaTest {
    @Autowired
    CuentaRepository cuentaRepository;

    @Test
    void testFindById() {
        Optional<Cuenta> cuenta = cuentaRepository.findById(1L);
        assertTrue(cuenta.isPresent());
        assertEquals("Andres",cuenta.orElseThrow().getPersona());
    }

    @Test
    void testFindByPersona() {
        Optional<Cuenta> cuenta = cuentaRepository.findByPersona("Andres");
        assertTrue(cuenta.isPresent());
        assertEquals("Andres",cuenta.orElseThrow().getPersona());
    }

    @Test
    void testFindAll() {
        List<Cuenta> cuentas = cuentaRepository.findAll();
        assertFalse(cuentas.isEmpty());
        assertEquals(2,cuentas.size());
    }


    @Test
    void testSave() {
        Cuenta cuentaPepe = new Cuenta(null,"Pepe",new BigDecimal("3000"));
        cuentaRepository.save(cuentaPepe);

        var cuenta = cuentaRepository.findByPersona("Pepe").orElseThrow();
        assertEquals("Pepe",cuenta.getPersona());
        assertEquals("3000",cuenta.getSaldo().toPlainString());
    }
}
