package com.abaron.springboot_test.repositories;

import com.abaron.springboot_test.models.Banco;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BancoRepository extends JpaRepository<Banco,Long> {

}
