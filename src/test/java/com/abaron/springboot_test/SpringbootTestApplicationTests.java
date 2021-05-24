package com.abaron.springboot_test;

import com.abaron.springboot_test.models.Cuenta;
import com.abaron.springboot_test.repositories.BancoRepository;
import com.abaron.springboot_test.repositories.CuentaRepository;
import com.abaron.springboot_test.services.CuentaService;
import com.abaron.springboot_test.services.CuentaServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@SpringBootTest
class SpringbootTestApplicationTests {
	@Mock
	CuentaRepository cuentaRepository;

	@Mock
	BancoRepository bancoRepository;

	@InjectMocks
	CuentaServiceImpl service;


	@Test
	void contextLoads() {
		when(cuentaRepository.findById(1L)).thenReturn(java.util.Optional.of(Datos.CUENTA_001));
		when(cuentaRepository.findById(2L)).thenReturn(java.util.Optional.of(Datos.CUENTA_002));
		when(bancoRepository.findById(1L)).thenReturn(java.util.Optional.of(Datos.BANCO));

		BigDecimal saldoOrigen = service.revisarSaldo(1L);
		BigDecimal saldoDestino = service.revisarSaldo(2L);
		assertEquals("1000",saldoOrigen.toPlainString());
		assertEquals("2000",saldoDestino.toPlainString());

		service.transferir(1L,2L,new BigDecimal("100"),1L);

		saldoOrigen = service.revisarSaldo(1L);
		saldoDestino = service.revisarSaldo(2L);
		assertEquals("900",saldoOrigen.toPlainString());
		assertEquals("2100",saldoDestino.toPlainString());

		verify(cuentaRepository,atLeast(3)).findById(1L);
		verify(cuentaRepository,atLeast(3)).findById(2L);
		verify(cuentaRepository,atLeast(2)).save(any(Cuenta.class));

		verify(cuentaRepository,never()).findAll();
	}

	@Test
	void testCuenta() {
		when(cuentaRepository.findById(1L)).thenReturn(java.util.Optional.of(Datos.CUENTA_001));
		Cuenta cuenta1 = service.findById(1L);
		Cuenta cuenta2 = service.findById(1L);
		assertSame(cuenta1,cuenta2);


	}
}
