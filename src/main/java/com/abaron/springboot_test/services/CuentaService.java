package com.abaron.springboot_test.services;

import com.abaron.springboot_test.models.Cuenta;

import java.math.BigDecimal;

public interface CuentaService {
    Cuenta findById(Long id);
    int revisarTotalTransferencia(Long bancoId);
    BigDecimal revisarSaldo(Long cuentaId);
    void transferir(Long numCuentaOrigen, Long numCuentaDestino,BigDecimal monto,Long bancoId);
    void guardarCuenta(Cuenta cuenta);
    void deleteById(Long id);
}
