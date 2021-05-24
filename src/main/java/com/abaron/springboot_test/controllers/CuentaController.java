package com.abaron.springboot_test.controllers;

import com.abaron.springboot_test.models.Cuenta;
import com.abaron.springboot_test.models.TransaccionDto;
import com.abaron.springboot_test.repositories.CuentaRepository;
import com.abaron.springboot_test.services.CuentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping(path = "/api/cuentas")
public class CuentaController {

    private CuentaService cuentaService;

    public CuentaController(CuentaService cuentaService) {
        this.cuentaService = cuentaService;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Cuenta detalle(@PathVariable Long id){
        return cuentaService.findById(id);
    }

    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Cuenta cuenta){
        cuentaService.guardarCuenta(cuenta);

        return new ResponseEntity(cuenta,HttpStatus.CREATED);
    }

    @PostMapping("/transferir")
    public ResponseEntity<?> transferir(@RequestBody TransaccionDto dto){
        cuentaService.transferir(dto.getCuentaOrigenId(),
                                 dto.getCuentaDestinoId(),
                                 dto.getMonto(),dto.getBancoId());
        Map<String,Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status","ok");
        response.put("mensaje","Transferencia realizada con exito");


        return new ResponseEntity(response,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        cuentaService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
