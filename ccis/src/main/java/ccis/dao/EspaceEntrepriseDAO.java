package ccis.dao;

import ccis.models.EspaceEntreprise;
import ccis.config.JDBCConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EspaceEntrepriseDAO {

    // Create operation
    public void create(EspaceEntreprise espaceEntreprise) {
        String sql = "INSERT INTO espace_entreprise (date_contact, heure_contact, objet_visite, nom_prenom, statut, telephone_fixe, telephone_gsm, email, accepte_envoi_ccis, adresse, ville, denomination, code_ice, nom_representant_legal, site_web, forme_juridique, taille_entreprise, secteur_activite, activite, nom_prenom_conseiller_ccis, qualite_conseiller_ccis, date_depart, heure_depart) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = JDBCConnection.getConnection(); 
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
                stmt.setString(1, espaceEntreprise.getDateContact());
                stmt.setString(2, espaceEntreprise.getHeureContact());
                stmt.setString(3, espaceEntreprise.getObjetVisite());
                stmt.setString(4, espaceEntreprise.getNomPrenom());
                stmt.setString(5, espaceEntreprise.getStatut());
                stmt.setString(6, espaceEntreprise.getFixe());
                stmt.setString(7, espaceEntreprise.getGsm());
                stmt.setString(8, espaceEntreprise.getEmail());
                stmt.setBoolean(9, Boolean.parseBoolean(espaceEntreprise.getAccepteEnvoi())); // convert String to boolean
                stmt.setString(10, espaceEntreprise.getAdresse());
                stmt.setString(11, espaceEntreprise.getVille());
                stmt.setString(12, espaceEntreprise.getDenomination());
                stmt.setString(13, espaceEntreprise.getCodeICE());
                stmt.setString(14, espaceEntreprise.getNomRepLegal());
                stmt.setString(15, espaceEntreprise.getSiteWeb());
                stmt.setString(16, espaceEntreprise.getFormeJuridique());
                stmt.setString(17, espaceEntreprise.getTailleEntreprise());
                stmt.setString(18, espaceEntreprise.getSecteurActivite());
                stmt.setString(19, espaceEntreprise.getActivite());
                stmt.setString(20, espaceEntreprise.getNomPrenomCCIS());
                stmt.setString(21, espaceEntreprise.getQualiteCCIS());
                stmt.setString(22, espaceEntreprise.getDateDepart());
                stmt.setString(23, espaceEntreprise.getHeureDepart());
              
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Read operation
    public EspaceEntreprise getById(int id) {
        String sql = "SELECT * FROM espace_entreprise WHERE id = ?";
        EspaceEntreprise espaceEntreprise = null;
        
        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                espaceEntreprise = new EspaceEntreprise();
                espaceEntreprise.setId(rs.getInt("id"));
                espaceEntreprise.setDateContact(rs.getString("date_contact"));
                espaceEntreprise.setObjetVisite(rs.getString("objet_visite"));
                espaceEntreprise.setNomPrenom(rs.getString("nom_prenom"));
                espaceEntreprise.setStatut(rs.getString("statut"));
                espaceEntreprise.setFixe(rs.getString("telephone_fixe"));
                espaceEntreprise.setGsm(rs.getString("telephone_gsm"));
                espaceEntreprise.setEmail(rs.getString("email"));
                espaceEntreprise.setAccepteEnvoi(rs.getString("accepte_envoi_ccis"));
                espaceEntreprise.setAdresse(rs.getString("adresse"));
                espaceEntreprise.setVille(rs.getString("ville"));
                espaceEntreprise.setDenomination(rs.getString("denomination"));
                espaceEntreprise.setCodeICE(rs.getString("code_ice"));
                espaceEntreprise.setNomRepLegal(rs.getString("nom_representant_legal"));
                espaceEntreprise.setSiteWeb(rs.getString("site_web"));
                espaceEntreprise.setFormeJuridique(rs.getString("forme_juridique"));
                espaceEntreprise.setTailleEntreprise(rs.getString("taille_entreprise"));
                espaceEntreprise.setSecteurActivite(rs.getString("secteur_activite"));
                espaceEntreprise.setActivite(rs.getString("activite"));
                espaceEntreprise.setNomPrenomCCIS(rs.getString("nom_prenom_conseiller_ccis"));
                espaceEntreprise.setQualiteCCIS(rs.getString("qualite_conseiller_ccis"));
                espaceEntreprise.setDateDepart(rs.getString("date_depart"));
                espaceEntreprise.setHeureContact(rs.getString("heure_contact"));
                espaceEntreprise.setHeureDepart(rs.getString("heure_depart"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return espaceEntreprise;
    }

    // Update operation
    public void update(EspaceEntreprise espaceEntreprise) {
        String sql = "UPDATE espace_entreprise SET date_contact = ?, heure_contact = ?, objet_visite = ?, nom_prenom = ?, statut = ?, telephone_fixe = ?, telephone_gsm = ?, email = ?, accepte_envoi_ccis = ?, adresse = ?, ville = ?, denomination = ?, code_ice = ?, nom_representant_legal = ?, site_web = ?, forme_juridique = ?, taille_entreprise = ?, secteur_activite = ?, activite = ?, nom_prenom_conseiller_ccis = ?, qualite_conseiller_ccis = ?, date_depart = ?, heure_depart = ? WHERE id = ?";

        try (Connection connection = JDBCConnection.getConnection(); 
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            
                stmt.setString(1, espaceEntreprise.getDateContact());
                stmt.setString(2, espaceEntreprise.getHeureContact());
                stmt.setString(3, espaceEntreprise.getObjetVisite());
                stmt.setString(4, espaceEntreprise.getNomPrenom());
                stmt.setString(5, espaceEntreprise.getStatut());
                stmt.setString(6, espaceEntreprise.getFixe());
                stmt.setString(7, espaceEntreprise.getGsm());
                stmt.setString(8, espaceEntreprise.getEmail());
                stmt.setBoolean(9, Boolean.parseBoolean(espaceEntreprise.getAccepteEnvoi()));
                stmt.setString(10, espaceEntreprise.getAdresse());
                stmt.setString(11, espaceEntreprise.getVille());
                stmt.setString(12, espaceEntreprise.getDenomination());
                stmt.setString(13, espaceEntreprise.getCodeICE());
                stmt.setString(14, espaceEntreprise.getNomRepLegal());
                stmt.setString(15, espaceEntreprise.getSiteWeb());
                stmt.setString(16, espaceEntreprise.getFormeJuridique());
                stmt.setString(17, espaceEntreprise.getTailleEntreprise());
                stmt.setString(18, espaceEntreprise.getSecteurActivite());
                stmt.setString(19, espaceEntreprise.getActivite());
                stmt.setString(20, espaceEntreprise.getNomPrenomCCIS());
                stmt.setString(21, espaceEntreprise.getQualiteCCIS());
                stmt.setString(22, espaceEntreprise.getDateDepart());
                stmt.setString(23, espaceEntreprise.getHeureDepart());
                stmt.setInt(26, espaceEntreprise.getId());
                
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete operation
    public void delete(int id) {
        String sql = "DELETE FROM espace_entreprise WHERE id = ?";

        try (Connection connection = JDBCConnection.getConnection(); 
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // List all records
    public List<EspaceEntreprise> getAll() {
        List<EspaceEntreprise> espaceEntreprises = new ArrayList<>();
        String sql = "SELECT * FROM espace_entreprise";

        try (Connection connection = JDBCConnection.getConnection(); 
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                EspaceEntreprise espaceEntreprise = new EspaceEntreprise();
                espaceEntreprise.setId(rs.getInt("id"));
                espaceEntreprise.setDateContact(rs.getString("date_contact"));
                espaceEntreprise.setObjetVisite(rs.getString("objet_visite"));
                espaceEntreprise.setNomPrenom(rs.getString("nom_prenom"));
                espaceEntreprise.setStatut(rs.getString("statut"));
                espaceEntreprise.setFixe(rs.getString("telephone_fixe"));
                espaceEntreprise.setGsm(rs.getString("telephone_gsm"));
                espaceEntreprise.setEmail(rs.getString("email"));
                espaceEntreprise.setAccepteEnvoi(rs.getString("accepte_envoi_ccis"));
                espaceEntreprise.setAdresse(rs.getString("adresse"));
                espaceEntreprise.setVille(rs.getString("ville"));
                espaceEntreprise.setDenomination(rs.getString("denomination"));
                espaceEntreprise.setCodeICE(rs.getString("code_ice"));
                espaceEntreprise.setNomRepLegal(rs.getString("nom_representant_legal"));
                espaceEntreprise.setSiteWeb(rs.getString("site_web"));
                espaceEntreprise.setFormeJuridique(rs.getString("forme_juridique"));
                espaceEntreprise.setTailleEntreprise(rs.getString("taille_entreprise"));
                espaceEntreprise.setSecteurActivite(rs.getString("secteur_activite"));
                espaceEntreprise.setActivite(rs.getString("activite"));
                espaceEntreprise.setNomPrenomCCIS(rs.getString("nom_prenom_conseiller_ccis"));
                espaceEntreprise.setQualiteCCIS(rs.getString("qualite_conseiller_ccis"));
                espaceEntreprise.setDateDepart(rs.getString("date_depart"));
                espaceEntreprise.setHeureContact(rs.getString("heure_contact"));
                espaceEntreprise.setHeureDepart(rs.getString("heure_depart"));
                espaceEntreprises.add(espaceEntreprise);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return espaceEntreprises;
    }

    public long count() {
        String sql = "SELECT COUNT(*) FROM espace_entreprise";
        long count = 0;

        try (Connection connection = JDBCConnection.getConnection(); 
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                count = rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return count;
    }
}
