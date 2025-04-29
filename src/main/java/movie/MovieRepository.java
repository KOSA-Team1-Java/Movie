package movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static JDBC.ConnectionConst.*;

public class MovieRepository {
    public List<String> getMovies() {
        String sql = "SELECT title FROM movie";

        try(Connection con = DriverManager.getConnection(URL,USERNAME,PASSWORD)) {
            Statement statement = con.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            List<String> movies = new ArrayList<>();
            while(resultSet.next()) {
                movies.add(resultSet.getString("title"));
            }
            return movies;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

//    public List<Screening> getScreenings() {
//    }
}
