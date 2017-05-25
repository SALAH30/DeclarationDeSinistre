package innotech.td3exo3;

/**
 * Created by Mon pc on 14/04/2017.
 */

public class Dossier {
    private String name;
    private String prename;
    private String lieu;
    private String date;
    private String photo;
    private String video;
    private String information;
    private String etat;
    private float montant;

    public Dossier(String name, String prename, String lieu, String date, String photo, String video, String information, String etat, float montant) {
        this.name = name;
        this.prename = prename;
        this.lieu = lieu;
        this.date = date;
        this.photo = photo;
        this.video = video;
        this.information = information;
        this.etat = etat;
        this.montant = montant;
    }

    public String getName() {
        return name;
    }

    public String getPrename() {
        return prename;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setPrename(String prename) {
        this.prename = prename;
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
