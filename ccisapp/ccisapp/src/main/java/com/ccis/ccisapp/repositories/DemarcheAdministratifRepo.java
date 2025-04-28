package com.ccis.ccisapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ccis.ccisapp.models.DemarcheAdministratif;

@Repository
public interface  DemarcheAdministratifRepo extends JpaRepository<DemarcheAdministratif, Long> {

}