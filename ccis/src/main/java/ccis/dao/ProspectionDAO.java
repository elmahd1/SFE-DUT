package ccis.dao;

import ccis.models.Prospection;
import ccis.config.JDBCConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
             
public class ProspectionDAO {

    // Create (Insert)
    public void insertProspection(Prospection prospection) {
        String query = "INSERT INTO prospection (" +
                "nom_etp, nom_etp_arabic, adresse, adresse_arabic, telephone_etp, email_etp, " +
                "nom_prenom, nom_prenom_arabic, fonction, fonction_arabic, telephone, email, " +
                "type_prospection, secteur_activite, psa_prospecter, marche_cible, periode_prospection, particularite, date" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, prospection.getNomETP());
            ps.setString(2, prospection.getNomETPArabic());
            ps.setString(3, prospection.getAdresse());
            ps.setString(4, prospection.getAdresseArabic());
            ps.setString(5, prospection.getTelephoneETP());
            ps.setString(6, prospection.getEmailETP());
            ps.setString(7, prospection.getNomPrenom());
            ps.setString(8, prospection.getNomPrenomArabic());
            ps.setString(9, prospection.getFonction());
            ps.setString(10, prospection.getFonctionArabic());
            ps.setString(11, prospection.getTelephone());
            ps.setString(12, prospection.getEmail());
            ps.setString(13, prospection.getTypeProspection());
            ps.setString(14, prospection.getSecteurActivite());
            ps.setString(15, prospection.getPSAProspecter());
            ps.setString(16, prospection.getMarcheCible());
            ps.setString(17, prospection.getPeriodeProspection());
            ps.setString(18, prospection.getParticularite());
            ps.setString(19, prospection.getDate());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
public Long count() {
        String query = "SELECT COUNT(*) FROM prospection";
        long count = 0;
        try (Connection connection = JDBCConnection.getConnection();
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            if (rs.next()) {
                count = rs.getLong(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    // Read (Select by ID)
    public Prospection getProspectionById(int id) {
        String query = "SELECT * FROM prospection WHERE id = ?";
        Prospection prospection = null;
        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                prospection = mapResultSetToProspection(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prospection;
    }

    // Update
    public void updateProspection(Prospection prospection) {
        String query = "UPDATE prospection SET " +
                "nom_etp=?, nom_etp_arabic=?, adresse=?, adresse_arabic=?, telephone_etp=?, email_etp=?, " +
                "nom_prenom=?, nom_prenom_arabic=?, fonction=?, fonction_arabic=?, telephone=?, email=?, " +
                "type_prospection=?, secteur_activite=?, psa_prospecter=?, marche_cible=?, periode_prospection=?,"+
                " particularite=?, date=? , resultat=? , commentaire=? , sad=?" +
                "WHERE id=?";
        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, prospection.getNomETP());
            ps.setString(2, prospection.getNomETPArabic());
            ps.setString(3, prospection.getAdresse());
            ps.setString(4, prospection.getAdresseArabic());
            ps.setString(5, prospection.getTelephoneETP());
            ps.setString(6, prospection.getEmailETP());
            ps.setString(7, prospection.getNomPrenom());
            ps.setString(8, prospection.getNomPrenomArabic());
            ps.setString(9, prospection.getFonction());
            ps.setString(10, prospection.getFonctionArabic());
            ps.setString(11, prospection.getTelephone());
            ps.setString(12, prospection.getEmail());
            ps.setString(13, prospection.getTypeProspection());
            ps.setString(14, prospection.getSecteurActivite());
            ps.setString(15, prospection.getPSAProspecter());
            ps.setString(16, prospection.getMarcheCible());
            ps.setString(17, prospection.getPeriodeProspection());
            ps.setString(18, prospection.getParticularite());
            ps.setString(19, prospection.getDate());
            ps.setString(20, prospection.getResultat());
            ps.setString(21, prospection.getCommentaire());
            ps.setString(22, prospection.getSuiteADonner());
            ps.setInt(23, prospection.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete
    public void deleteProspection(int id) {
        String query = "DELETE FROM prospection WHERE id = ?";
        try (Connection connection = JDBCConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Get All
    public List<Prospection> getAllProspections() {
        List<Prospection> list = new ArrayList<>();
        String query = "SELECT * FROM prospection";
        try (Connection connection = JDBCConnection.getConnection();
             Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(query)) {
            while (rs.next()) {
                list.add(mapResultSetToProspection(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Helper to map ResultSet to Prospection object
    private Prospection mapResultSetToProspection(ResultSet rs) throws SQLException {
        Prospection p = new Prospection();
        p.setId(rs.getInt("id"));
        p.setNomETP(rs.getString("nom_etp"));
        p.setNomETPArabic(rs.getString("nom_etp_arabic"));
        p.setAdresse(rs.getString("adresse"));
        p.setAdresseArabic(rs.getString("adresse_arabic"));
        p.setTelephoneETP(rs.getString("telephone_etp"));
        p.setEmailETP(rs.getString("email_etp"));
        p.setNomPrenom(rs.getString("nom_prenom"));
        p.setNomPrenomArabic(rs.getString("nom_prenom_arabic"));
        p.setFonction(rs.getString("fonction"));
        p.setFonctionArabic(rs.getString("fonction_arabic"));
        p.setTelephone(rs.getString("telephone"));
        p.setEmail(rs.getString("email"));
        p.setTypeProspection(rs.getString("type_prospection"));
        p.setSecteurActivite(rs.getString("secteur_activite"));
        p.setPSAProspecter(rs.getString("psa_prospecter"));
        p.setMarcheCible(rs.getString("marche_cible"));
        p.setPeriodeProspection(rs.getString("periode_prospection"));
        p.setParticularite(rs.getString("particularite"));
        p.setDate(rs.getString("date"));
        p.setResultat(rs.getString("resultat"));
        p.setCommentaire(rs.getString("commentaire"));
        p.setSuiteADonner(rs.getString("sad"));
        return p;
    }
    public void deleteAll() {
    String sql = "DELETE FROM prospection";
    try (Connection conn = JDBCConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.executeUpdate();
    } catch (Exception e) {
        e.printStackTrace();
    }
}
}