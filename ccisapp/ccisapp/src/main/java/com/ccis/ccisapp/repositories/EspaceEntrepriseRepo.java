package com.ccis.ccisapp.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ccis.ccisapp.models.EspaceEntreprise;

@Repository
public interface EspaceEntrepriseRepo extends JpaRepository<EspaceEntreprise, Long> {
  
    
}
