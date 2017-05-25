package innotech.td3exo3;

/**
 * Created by Mon pc on 09/04/2017.
 */

public class Type {
    private String type;
    private String description;

    public Type(String type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
