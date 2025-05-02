package movie;


public class SeatRequest {
    private char row;
    private int col;

    public SeatRequest(char row, int col) {
        this.row = row;
        this.col = col;
    }
    public char getRow() { return row; }
    public int getCol() { return col; }
}