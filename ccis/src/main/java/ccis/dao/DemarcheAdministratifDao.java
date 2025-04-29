package ccis.dao;

import ccis.models.DemarcheAdministratif;
import ccis.config.JDBCConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DemarcheAdministratifDao {

    // Create (Insert)
    public void insertDemarche(DemarcheAdministratif demarche) {
        String query = "INSERT INTO demarche_administratif (" +
            "forme_juridique, date_depot, heure_depot, secteur_activite, activite, " +
            "nom_representant_legal, qualite_conseiller_ccis, etat_dossier_fournit, " +
            "suite_accordee_commande, observation, date_delivrance, heure_delivrance, " +
            "objet_visite, heure_contact, date_contact, denomination, type_demande, " +
            "type, fixe, gsm, siege_sociale_adresse, ville_communite, interlocuteur, " +
            "email, montant, nom_prenom, accepte_envoi_ccis, site_web, " +
            "nom_prenom_conseiller_ccis) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, demarche.getFormeJuridique());
            preparedStatement.setString(2, demarche.getDateDepot());
            preparedStatement.setString(3, demarche.getHeureDepot());
            preparedStatement.setString(4, demarche.getSecteurActivite());
            preparedStatement.setString(5, demarche.getActivite());
            preparedStatement.setString(6, demarche.getNomRepLegal());
            preparedStatement.setString(7, demarche.getQualiteCCIS());
            preparedStatement.setString(8, demarche.getEtatDossier());
            preparedStatement.setString(9, demarche.getSuiteDemande());
            preparedStatement.setString(10, demarche.getObservation());
            preparedStatement.setString(11, demarche.getDateDelivrance());
            preparedStatement.setString(12, demarche.getHeureDelivrance());
            preparedStatement.setString(13, demarche.getObjetVisite());
            preparedStatement.setString(14, demarche.getHeureContact());
            preparedStatement.setString(15, demarche.getDateContact());
            preparedStatement.setString(16, demarche.getDenomination());
            preparedStatement.setString(17, demarche.getTypeDemande());
            preparedStatement.setString(18, demarche.getStatut());
            preparedStatement.setString(19, demarche.getFixe());
            preparedStatement.setString(20, demarche.getGsm());
            preparedStatement.setString(21, demarche.getAdresse());
            preparedStatement.setString(22, demarche.getVille());
            preparedStatement.setString(24, demarche.getEmail());
            preparedStatement.setFloat(25, demarche.getMontant());
            preparedStatement.setString(26, demarche.getNomPrenom());
            preparedStatement.setString(27, demarche.getAccepteEnvoi());
            preparedStatement.setString(28, demarche.getSiteWeb());
            preparedStatement.setString(29, demarche.getNomPrenomCCIS());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read (Select)
    public DemarcheAdministratif getDemarcheById(int id) {
        String query = "SELECT * FROM demarche_administratif WHERE id = ?";
        DemarcheAdministratif demarche = null;

        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                demarche = new DemarcheAdministratif();
                demarche.setId(resultSet.getInt("id"));
                demarche.setFormeJuridique(resultSet.getString("forme_juridique"));
                demarche.setDateDepot(resultSet.getString("date_depot"));
                demarche.setHeureDepot(resultSet.getString("heure_depot"));
                demarche.setSecteurActivite(resultSet.getString("secteur_activite"));
                demarche.setActivite(resultSet.getString("activite"));
                demarche.setNomRepLegal(resultSet.getString("nom_representant_legal"));
                demarche.setQualiteCCIS(resultSet.getString("qualite_conseiller_ccis"));
                demarche.setEtatDossier(resultSet.getString("etat_dossier_fournit"));
                demarche.setSuiteDemande(resultSet.getString("suite_accordee_commande"));
                demarche.setObservation(resultSet.getString("observation"));
                demarche.setDateDelivrance(resultSet.getString("date_delivrance"));
                demarche.setHeureDelivrance(resultSet.getString("heure_delivrance"));
                demarche.setObjetVisite(resultSet.getString("objet_visite"));
                demarche.setHeureContact(resultSet.getString("heure_contact"));
                demarche.setDateContact(resultSet.getString("date_contact"));
                demarche.setDenomination(resultSet.getString("denomination"));
                demarche.setTypeDemande(resultSet.getString("type_demande"));
                demarche.setStatut(resultSet.getString("type"));
                demarche.setFixe(resultSet.getString("fixe"));
                demarche.setGsm(resultSet.getString("gsm"));
                demarche.setAdresse(resultSet.getString("siege_sociale_adresse"));
                demarche.setVille(resultSet.getString("ville_communite"));
                demarche.setEmail(resultSet.getString("email"));
                demarche.setMontant(resultSet.getFloat("montant"));
                demarche.setNomPrenom(resultSet.getString("nom_prenom"));
                demarche.setAccepteEnvoi(resultSet.getString("accepte_envoi_ccis"));
                demarche.setSiteWeb(String.valueOf(resultSet.getDouble("site_web")));
                demarche.setNomPrenomCCIS(resultSet.getString("nom_prenom_conseiller_ccis"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return demarche;
    }

    // Update (Modify)
    public void updateDemarche(DemarcheAdministratif demarche) {
        String query = "UPDATE demarche_administratif SET " +
            "forme_juridique = ?, date_depot = ?, heure_depot = ?, secteur_activite = ?, " +
            "activite = ?, nom_representant_legal = ?, qualite_conseiller_ccis = ?, " +
            "etat_dossier_fournit = ?, suite_accordee_commande = ?, observation = ?, " +
            "date_delivrance = ?, heure_delivrance = ?, objet_visite = ?, heure_contact = ?, " +
            "date_contact = ?, denomination = ?, type_demande = ?, type = ?, fixe = ?, " +
            "gsm = ?, siege_sociale_adresse = ?, ville_communite = ?, interlocuteur = ?, " +
            "email = ?, montant = ?, nom_prenom = ?, accepte_envoi_ccis = ?, site_web = ?, " +
            "nom_prenom_conseiller_ccis = ? WHERE id = ?";

        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setString(1, demarche.getFormeJuridique());
            preparedStatement.setString(2, demarche.getDateDepot());
            preparedStatement.setString(3, demarche.getHeureDepot());
            preparedStatement.setString(4, demarche.getSecteurActivite());
            preparedStatement.setString(5, demarche.getActivite());
            preparedStatement.setString(6, demarche.getNomRepLegal());
            preparedStatement.setString(7, demarche.getQualiteCCIS());
            preparedStatement.setString(8, demarche.getEtatDossier());
            preparedStatement.setString(9, demarche.getSuiteDemande());
            preparedStatement.setString(10, demarche.getObservation());
            preparedStatement.setString(11, demarche.getDateDelivrance());
            preparedStatement.setString(12, demarche.getHeureDelivrance());
            preparedStatement.setString(13, demarche.getObjetVisite());
            preparedStatement.setString(14, demarche.getHeureContact());
            preparedStatement.setString(15, demarche.getDateContact());
            preparedStatement.setString(16, demarche.getDenomination());
            preparedStatement.setString(17, demarche.getTypeDemande());
            preparedStatement.setString(18, demarche.getStatut());
            preparedStatement.setInt(19, Integer.parseInt(demarche.getFixe()));
            preparedStatement.setInt(20, Integer.parseInt(demarche.getGsm()));
            preparedStatement.setString(21, demarche.getAdresse());
            preparedStatement.setString(22, demarche.getVille());
            preparedStatement.setString(24, demarche.getEmail());
            preparedStatement.setFloat(25, demarche.getMontant());
            preparedStatement.setString(26, demarche.getNomPrenom());
            preparedStatement.setString(27, demarche.getAccepteEnvoi());
            preparedStatement.setDouble(28, Double.parseDouble(demarche.getSiteWeb()));
            preparedStatement.setString(29, demarche.getNomPrenomCCIS());
            preparedStatement.setInt(30, demarche.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete
    public void deleteDemarche(int id) {
        String query = "DELETE FROM demarche_administratif WHERE id = ?";

        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get All Records
    public List<DemarcheAdministratif> getAllDemarches() {
        List<DemarcheAdministratif> demarches = new ArrayList<>();
        String query = "SELECT * FROM demarche_administratif";

        try (Connection connection = JDBCConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                DemarcheAdministratif demarche = new DemarcheAdministratif();
                demarche.setId(resultSet.getInt("id"));
                demarche.setFormeJuridique(resultSet.getString("forme_juridique"));
                demarche.setDateDepot(resultSet.getString("date_depot"));
                demarche.setHeureDepot(resultSet.getString("heure_depot"));
                demarche.setSecteurActivite(resultSet.getString("secteur_activite"));
                demarche.setActivite(resultSet.getString("activite"));
                demarche.setNomRepLegal(resultSet.getString("nom_representant_legal"));
                demarche.setQualiteCCIS(resultSet.getString("qualite_conseiller_ccis"));
                demarche.setEtatDossier(resultSet.getString("etat_dossier_fournit"));
                demarche.setSuiteDemande(resultSet.getString("suite_accordee_commande"));
                demarche.setObservation(resultSet.getString("observation"));
                demarche.setDateDelivrance(resultSet.getString("date_delivrance"));
                demarche.setHeureDelivrance(resultSet.getString("heure_delivrance"));
                demarche.setObjetVisite(resultSet.getString("objet_visite"));
                demarche.setHeureContact(resultSet.getString("heure_contact"));
                demarche.setDateContact(resultSet.getString("date_contact"));
                demarche.setDenomination(resultSet.getString("denomination"));
                demarche.setTypeDemande(resultSet.getString("type_demande"));
                demarche.setStatut(resultSet.getString("type"));
                demarche.setFixe(resultSet.getString("fixe"));
                demarche.setGsm(resultSet.getString("gsm"));
                demarche.setAdresse(resultSet.getString("siege_sociale_adresse"));
                demarche.setVille(resultSet.getString("ville_communite"));
                demarche.setEmail(resultSet.getString("email"));
                demarche.setMontant(resultSet.getFloat("montant"));
                demarche.setNomPrenom(resultSet.getString("nom_prenom"));
                demarche.setAccepteEnvoi(resultSet.getString("accepte_envoi_ccis"));
                demarche.setSiteWeb(resultSet.getString("site_web"));
                demarche.setNomPrenomCCIS(resultSet.getString("nom_prenom_conseiller_ccis"));
                
                demarches.add(demarche);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return demarches;
    }

    public long count() {
        String query = "SELECT COUNT(*) FROM demarche_administratif";
        long count = 0;

        try (Connection connection = JDBCConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            
            if (resultSet.next()) {
                count = resultSet.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

 
}