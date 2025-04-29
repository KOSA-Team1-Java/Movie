package movie;

public class Theater {
    private int id;
    private String location;
    private int roomNumber;

    public Theater(int id, String location, int roomNumber) {
        this.id = id;
        this.location = location;
        this.roomNumber = roomNumber;
    }

    public int getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public int getRoomNumber() {
        return roomNumber;
    }
}
