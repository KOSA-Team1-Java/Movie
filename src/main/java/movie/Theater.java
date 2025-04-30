package movie;

public class Theater {
    private int id;
    private String location;

    public Theater(int id, String location) {
        this.id = id;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

}
