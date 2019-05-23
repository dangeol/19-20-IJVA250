package com.example.demo.repository;

import com.example.demo.entity.Facture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FactureRepository extends JpaRepository<Facture, Long> {
    @Query("select f from Facture f where f.client.id = :clientId")
    List<Facture> findAllFacturesByClientId(@Param("clientId") Long clientId);

    @Query("select f from Facture f where f.id = :Id")
    Facture findAllFacturesById(@Param("Id") Long Id);
}
