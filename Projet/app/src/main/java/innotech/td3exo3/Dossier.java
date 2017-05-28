package innotech.td3exo3;

import java.io.Serializable;

/**
 * Created by Mon pc on 14/04/2017.
 */

public class Dossier implements Serializable{
    private String type;
    private String id;
    private String lieu;
    private String date;
    private String photo;
    private String video;
    private String information;
    private String etat;
    private float montant;

    public Dossier(String id, String type, String lieu, String date, String photo, String video, String information, String etat, float montant) {
        this.id = id;
        this.type = type;
        this.lieu = lieu;
        this.date = date;
        this.photo = photo;
        this.video = video;
        this.information = information;
        this.etat = etat;
        this.montant = montant;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getLieu() {
        return lieu;
    }

    public String getDate() {
        return date;
    }

    public String getPhoto() {
        return photo;
    }

    public String getVideo() {
        return video;
    }

    public String getInformation() {
        return information;
    }

    public String getEtat() {
        return etat;
    }

    public float getMontant() {
        return montant;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }
}
