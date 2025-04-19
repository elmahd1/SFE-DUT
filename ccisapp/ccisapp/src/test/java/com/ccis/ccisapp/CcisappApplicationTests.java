package com.ccis.ccisapp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import com.ccis.ccisapp.models.DemarcheAdministratif;
import com.ccis.ccisapp.models.EspaceEntreprise;
import com.ccis.ccisapp.repositories.DemarcheAdministratifRepo;
import com.ccis.ccisapp.repositories.EspaceEntrepriseRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
@SpringBootTest
class CcisappApplicationTests {

    // @Test
    // public void testData() {

	// 	public static void main(String[] args) {
	// 		SpringApplication.run(CcisappApplication.class, args);
	// 	}
	
	// 	@Bean
	// 	CommandLineRunner init(DemarcheAdministratifRepo repo) {
	// 		return args -> {
	// 			DemarcheAdministratif d = new DemarcheAdministratif();
	
	// 			d.setDateContact("2025-04-18");
	// 			d.setHeureContact("10:30");
	// 			d.setObjetVisite("Demande d'information");
	// 			d.setStatut("En cours");
	// 			d.setMontant("1500");
	// 			d.setNomPrenom("ELMHADRI Elmahdi");
	// 			d.setFixe("0520123456");
	// 			d.setGsm("0612345678");
	// 			d.setEmail("exemple@mail.com");
	// 			d.setAccepteEnvoi("Oui");
	// 			d.setSiteWeb("www.example.com");
	// 			d.setAdresse("Essaouira, Maroc");
	// 			d.setVille("Essaouira");
	// 			d.setDenomination("EntrepriseX");
	// 			d.setNomRepLegal("Ahmed El Baz");
	// 			d.setFormeJuridique("SARL");
	// 			d.setDateDepot("2025-04-18");
	// 			d.setHeureDepot("11:00");
	// 			d.setSecteurActivite("Tourisme");
	// 			d.setActivite("Agence de voyages");
	// 			d.setNomPrenomCCIS("Mohamed Amine");
	// 			d.setQualiteCCIS("Responsable");
	// 			d.setEtatDossier("Complet");
	// 			d.setSuiteDemande("À traiter");
	// 			d.setObservation("RAS");
	// 			d.setDateDelivrance("2025-04-19");
	// 			d.setHeureDelivrance("14:00");
	
	// 			repo.save(d);
	
	// 			System.out.println("✅ Démarche enregistrée !");
	// 			System.out.println("📄 Liste actuelle dans la base :");
	// 			repo.findAll().forEach(entry -> {
	// 				System.out.println("Nom: " + entry.getNomPrenom() + ", Objet: " + entry.getObjetVisite());
	// 			});
	// 		};
	// 	}
	
	// }

}
