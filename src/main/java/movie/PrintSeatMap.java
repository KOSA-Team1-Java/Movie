package movie;

import java.util.List;

public class PrintSeatMap {
    public static void printSeatMap(List<String> reservedSeats) {
        int rows = 10, cols = 10;
        char[] rowNames = "ABCDEFGHIJ".toCharArray();
        boolean[][] seatMap = new boolean[rows][cols];

        for (String seat : reservedSeats) {
            char rowChar = seat.charAt(0);
            int row = rowChar - 'A';
            int col = Integer.parseInt(seat.substring(1)) - 1;
            if (row >= 0 && row < 10 && col >= 0 && col < 10) {
                seatMap[row][col] = true;
            }
        }
        // 열 번호
        System.out.print("    ");
        for (int c = 1; c <= cols; c++) System.out.printf("%2d ", c);
        System.out.println();
        // 좌석표
        for (int i = 0; i < rows; i++) {
            System.out.printf("%2c |", rowNames[i]);
            for (int j = 0; j < cols; j++) {
                System.out.print(seatMap[i][j] ? " ■ " : " □ ");
            }
            System.out.println();
        }
    }
}
