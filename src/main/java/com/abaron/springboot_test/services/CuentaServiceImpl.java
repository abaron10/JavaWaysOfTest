package com.abaron.springboot_test.services;

import com.abaron.springboot_test.Exceptions.DineroInsuficienteException;
import com.abaron.springboot_test.models.Banco;
import com.abaron.springboot_test.models.Cuenta;
import com.abaron.springboot_test.repositories.BancoRepository;
import com.abaron.springboot_test.repositories.CuentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class CuentaServiceImpl implements CuentaService {
    private CuentaRepository cuentaRepository;
    private BancoRepository bancoRepository;

    public CuentaServiceImpl(CuentaRepository cuentaRepository, BancoRepository bancoRepository) {
        this.cuentaRepository = cuentaRepository;
        this.bancoRepository = bancoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Cuenta findById(Long id) {
      return  cuentaRepository.findById(id).orElseThrow(()-> new DineroInsuficienteException("No se encontro"));
    }

    @Override
    @Transactional(readOnly = true)
    public int revisarTotalTransferencia(Long bancoId) {
        Banco banco = bancoRepository.findById(bancoId).orElseThrow();
        return banco.getTotalTransferencias();
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal revisarSaldo(Long cuentaId) {
        Cuenta cuenta = cuentaRepository.findById(cuentaId).orElseThrow();
        return cuenta.getSaldo();
    }

    @Override
    @Transactional
    public void guardarCuenta(Cuenta cuenta) {
        cuentaRepository.save(cuenta);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        cuentaRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void transferir(Long numCuentaOrigen, Long numCuentaDestino, BigDecimal monto,Long bancoId) {
        Banco banco = bancoRepository.findById(1L).orElseThrow();
        int totalTransferencia = banco.getTotalTransferencias();
        banco.setTotalTransferencias(++totalTransferencia);
        bancoRepository.save(banco);

        Cuenta cuentaOrigen = cuentaRepository.findById(numCuentaOrigen).orElseThrow();
        cuentaOrigen.debito(monto);
        cuentaRepository.save(cuentaOrigen);

        Cuenta cuentaDestino = cuentaRepository.findById(numCuentaDestino).orElseThrow();
        cuentaDestino.credito(monto);
        cuentaRepository.save(cuentaDestino);
    }
}
