package com.ccis.ccisapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;

@Entity
@Table(name = "espace_entreprise")
public class EspaceEntreprise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "date_contact")
    private String dateContact;

    @Column(name = "heure_contact")
    private String heureContact;

    @Column(name = "objet_visite")
    private String objetVisite;

    @Column(name = "nom_prenom")
    private String nomPrenom;

    @Column(name = "statut")
    private String statut;

    @Column(name = "fixe")
    private String fixe;

    @Column(name = "gsm")
    private String gsm;

    @Column(name = "email")
    private String email;

    @Column(name = "accepte_envoi")
    private String accepteEnvoi;

    @Column(name = "adresse")
    private String adresse;

    @Column(name = "ville")
    private String ville;

    @Column(name = "denomination")
    private String denomination;

    @Column(name = "code_ice")
    private String codeIce;

    @Column(name = "nom_rep_legal")
    private String nomRepLegal;

    @Column(name = "site_web")
    private String siteWeb;

    @Column(name = "forme_juridique")
    private String formeJuridique;

    @Column(name = "date_depot")
    private String dateDepot;

    @Column(name = "heure_depot")
    private String heureDepot;

    @Column(name = "taille_entreprise")
    private String tailleEntreprise;

    @Column(name = "secteur_activite")
    private String secteurActivite;

    @Column(name = "avtivite")
    private String avtivite;

    @Column(name = "nom_prenom_c_ccis")
    private String nomPrenomCCIS;

    @Column(name = "qualite_c_ccis")
    private String qualiteCCIS;

    @Column(name = "date_depart")
    private String dateDepart;

    @Column(name = "heure_depart")
    private String heureDepart;
    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateContact() {
        return dateContact;
    }

    public void setDateContact(String dateContact) {
        this.dateContact = dateContact;
    }

    public String getHeureContact() {
        return heureContact;
    }

    public void setHeureContact(String heureContact) {
        this.heureContact = heureContact;
    }

    public String getObjetVisite() {
        return objetVisite;
    }

    public void setObjetVisite(String objetVisite) {
        this.objetVisite = objetVisite;
    }

    public String getNomPrenom() {
        return nomPrenom;
    }
    public void setNomPrenom(String nomPrenom) {
        this.nomPrenom = nomPrenom;
    }       
    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }
    public String getFixe() {
        return fixe;
    }
    public void setFixe(String fixe) {
        this.fixe = fixe;
    }
    public String getGsm() {
        return gsm;
    }
    public void setGsm(String gsm) {
        this.gsm = gsm;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getAccepteEnvoi() {
        return accepteEnvoi;
    }
    public void setAccepteEnvoi(String accepteEnvoi) {
        this.accepteEnvoi = accepteEnvoi;
    }
    public String getAdresse() {
        return adresse;
    }
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }
    public String getVille() {
        return ville;
    }
    public void setVille(String ville) {
        this.ville = ville;
    }
    public String getDenomination() {
        return denomination;
    }
    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }
    public String getCodeIce() {
        return codeIce;
    }
    public void setCodeIce(String codeIce) {
        this.codeIce = codeIce;
    }
    public String getNomRepLegal() {
        return nomRepLegal;
    }
    public void setNomRepLegal(String nomRepLegal) {
        this.nomRepLegal = nomRepLegal;
    }
    public String getSiteWeb() {
        return siteWeb;
    }
    public void setSiteWeb(String siteWeb) {
        this.siteWeb = siteWeb;
    }
    public String getFormeJuridique() {
        return formeJuridique;
    }
    public void setFormeJuridique(String formeJuridique) {
        this.formeJuridique = formeJuridique;
    }
    public String getDateDepot() {
        return dateDepot;
    }
    public void setDateDepot(String dateDepot) {
        this.dateDepot = dateDepot;
    }
    public String getHeureDepot() {
        return heureDepot;
    }
    public void setHeureDepot(String heureDepot) {
        this.heureDepot = heureDepot;
    }
    public String getTailleEntreprise() {
        return tailleEntreprise;
    }
    public void setTailleEntreprise(String tailleEntreprise) {
        this.tailleEntreprise = tailleEntreprise;
    }
    public String getSecteurActivite() {
        return secteurActivite;
    }
    public void setSecteurActivite(String secteurActivite) {
        this.secteurActivite = secteurActivite;
    }
    public String getAvtivite() {
        return avtivite;
    }
    public void setAvtivite(String avtivite) {
        this.avtivite = avtivite;
    }
    public String getNomPrenomCCIS() {
        return nomPrenomCCIS;
    }
    public void setNomPrenomCCIS(String nomPrenomCCIS) {
        this.nomPrenomCCIS = nomPrenomCCIS;
    }
    public String getQualiteCCIS() {
        return qualiteCCIS;
    }
    public void setQualiteCCIS(String qualiteCCIS) {
        this.qualiteCCIS = qualiteCCIS;
    }
    public String getDateDepart() {
        return dateDepart;
    }
    public void setDateDepart(String dateDepart) {
        this.dateDepart = dateDepart;
    }
    public String getHeureDepart() {
        return heureDepart;
    }
    public void setHeureDepart(String heureDepart) {
        this.heureDepart = heureDepart;
    }
    
}
