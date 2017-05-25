package com.mobile.myapplication;

import java.util.List;

/**
 * Created by Mon pc on 23/04/2017.
 */

public class Offre {
    private int numero;
    private String date;
    private String fournisseur;
    private float montant;
    private String[] cles;

    public Offre(int numero, String date, String fournisseur, float montant, String[] cles) {
        this.numero = numero;
        this.date = date;
        this.fournisseur = fournisseur;
        this.montant = montant;
        this.cles = cles;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFournisseur() {
        return fournisseur;
    }

    public void setFournisseur(String fournisseur) {
        this.fournisseur = fournisseur;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public String[] getCles() {
        return cles;
    }

    public void setCles(String[] cles) {
        this.cles = cles;
    }
}
