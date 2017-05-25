package innotech.td3exo3;

/**
 * Created by Mon pc on 14/04/2017.
 */

public class Personne {
    private String name;
    private String prename;

    public Personne(String name, String prename) {
        this.name = name;
        this.prename = prename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrename() {
        return prename;
    }

    public void setPrename(String prename) {
        this.prename = prename;
    }
}
