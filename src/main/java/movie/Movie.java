package movie;

public class Movie {
    private int id;
    private String title;
    private int price;
    private int age;

    public Movie(int id, String title, int price, int age) {
        this.id = id;
        this.title = title;
        this.price = price;
        this.age = age;
    }

    // 게터/세터
    public int getId() { return id; }
    public String getTitle() { return title; }
    public int getPrice() { return price; }
    public int getAge() { return age; }
    public void setPrice(int price) { this.price = price; }
    public void setAge(int age) { this.age = age; }
}
