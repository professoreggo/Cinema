package cinema;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBConnection {
    
     public List<Movie_table> getMovies() {  //return all movies
        List<Movie_table> movies = new ArrayList<>();
        String connectionString = "jdbc:derby://localhost:1527/Cinema";  // Database URL
        String dbuname = "mostafa";  // Database username
        String dbpass = "arrow5557"; // Database password

        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Establishing connection to the database
            conn = DriverManager.getConnection(connectionString, dbuname, dbpass);

            // SQL query to fetch movie data including reservationNumber
            String query = "SELECT movie_id, name, movie_time, (SELECT COUNT(*) FROM reservation WHERE reservation.movie_id = movie.movie_id) AS reservationNumber FROM movie";
            stmt = conn.prepareStatement(query);

            // Execute the query and get the result
            rs = stmt.executeQuery();

            // Loop through the result set and create Movie objects
            while (rs.next()) {
                int movieId = rs.getInt("movie_id");
                String name = rs.getString("name");
                String movieTime = rs.getString("movie_time");
                int reservationNumber = rs.getInt("reservationNumber");

                Movie_table movie = new Movie_table(movieId, name, movieTime, reservationNumber);
                movies.add(movie);  // Add the movie object to the list
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                // Close resources to avoid memory leaks
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return movies;  // Return the list of movies
    }
    

public int getClientId(String username, String password) {  //
    String connectionString = "jdbc:derby://localhost:1527/Cinema";  // Database URL
    String dbuname = "mostafa";  // Database username
    String dbpass = "arrow5557"; // Database password

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        // Establishing connection to the database
        conn = DriverManager.getConnection(connectionString, dbuname, dbpass);

        // SQL query to get the client_id from the client table where email and password match
        String query = "SELECT client_id FROM client WHERE username = ? AND password = ?";
        stmt = conn.prepareStatement(query);
        stmt.setString(1, username);  // Set the username in the query
        stmt.setString(2, password);  // Set the password in the query

        // Execute the query and get the result
        rs = stmt.executeQuery();

        // If a row is returned, retrieve the client_id
        if (rs.next()) {
            return rs.getInt("client_id");  // Return the client_id
        } else {
            return -1; // If no matching credentials, return -1 to indicate failure
        }

    } catch (Exception e) {
        e.printStackTrace();
        return -1; // Return -1 if there is an error
    } finally {
        try {
            // Close the resources to avoid memory leaks
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

    


public boolean login_client(String username, String password) {
    String connectionString = "jdbc:derby://localhost:1527/Cinema";  // Database URL
    String dbuname = "mostafa";  // Database username
    String dbpass = "arrow5557"; // Database password

    Connection conn = null;
    PreparedStatement stmt = null;
    ResultSet rs = null;

    try {
        // Establishing connection to the database
        conn = DriverManager.getConnection(connectionString, dbuname, dbpass);

        // SQL query to check if the provided username and password exist in the client table
        String query = "SELECT * FROM client WHERE username = ? AND password = ?";
        stmt = conn.prepareStatement(query);
        stmt.setString(1, username);  // Set the username in the query
        stmt.setString(2, password);  // Set the password in the query

        // Execute the query and get the result
        rs = stmt.executeQuery();

        // Check if a row was returned, which means the credentials are valid
        if (rs.next()) {
            return true;  // Valid username and password
        } else {
            return false; // Invalid username or password
        }

    } catch (Exception e) {
        e.printStackTrace();
        return false;  
    } finally {
        try {
            
            if (rs != null) {
                rs.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


    public List<Movie_table> browseMovies() throws SQLException { //return list of available movies
        String connectionString = "jdbc:derby://localhost:1527/Cinema";
        String dbuname = "mostafa";
        String dbpass = "arrow5557";

        List<Movie_table> movies = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(connectionString, dbuname, dbpass)) {
            String sqlQuery = "SELECT * FROM Movie WHERE movie_time > CURRENT_TIMESTAMP";
            System.out.println("SQL Query: " + sqlQuery);

            try (PreparedStatement pst = con.prepareStatement(sqlQuery);
                 ResultSet rs = pst.executeQuery()) {
                 
                while (rs.next()) {
                    int id = rs.getInt("Movie_id");
                    String name = rs.getString("Name");
                    String time = rs.getString("Movie_time");
                    int reservationNumber = rs.getInt("reservationNumber");
                    

                    // Add other fields as necessary
                    Movie_table m = new Movie_table(id, name, time,reservationNumber);
                    movies.add(m);
                }
            }
        }

        return movies;
    }
public Reservation_table reserve(int clientId, int movieId) {
    String connectionString = "jdbc:derby://localhost:1527/Cinema";
    String dbuname = "mostafa";
    String dbpass = "arrow5557";

    String checkReservationQuery = "SELECT reservationNumber FROM movie WHERE movie_id = ?";
    String insertReservationQuery = "INSERT INTO reservation (client_id, movie_id, status) VALUES (?, ?, ?)";
    String updateMovieQuery = "UPDATE movie SET reservationNumber = " +
                              "CASE WHEN reservationNumber IS NULL THEN 1 ELSE reservationNumber + 1 END " +
                              "WHERE movie_id = ?";
    String status = "reserved";

    try (Connection con = DriverManager.getConnection(connectionString, dbuname, dbpass)) {
        // Begin transaction
        con.setAutoCommit(false);

        try {
            // Check the reservationNumber in the movie table
            try (PreparedStatement checkPst = con.prepareStatement(checkReservationQuery)) {
                checkPst.setInt(1, movieId);

                try (ResultSet rs = checkPst.executeQuery()) {
                    if (rs.next()) {
                        int reservationNumber = rs.getInt("reservationNumber");

                        // If reservationNumber is >= 50, do not allow reservation
                        if (reservationNumber >= 50) {
                            System.out.println("Reservation limit reached for movie ID: " + movieId);
                            con.rollback(); // Rollback transaction
                            return null;
                        }
                    }
                }
            }

            // Insert into reservation table
            try (PreparedStatement pst = con.prepareStatement(insertReservationQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                pst.setInt(1, clientId);
                pst.setInt(2, movieId);
                pst.setString(3, status);

                int rowsInserted = pst.executeUpdate();
                if (rowsInserted > 0) {
                    // Retrieve the generated ID
                    try (ResultSet generatedKeys = pst.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            int reservationId = generatedKeys.getInt(1);

                            // Update reservationNumber in the movie table
                            try (PreparedStatement updatePst = con.prepareStatement(updateMovieQuery)) {
                                updatePst.setInt(1, movieId);
                                updatePst.executeUpdate();
                            }

                            // Commit transaction
                            con.commit();
                            return new Reservation_table(reservationId, clientId, movieId, status);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            // Rollback transaction in case of any failure
            con.rollback();
            e.printStackTrace();
        } finally {
            // Restore auto-commit
            con.setAutoCommit(true);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return null;
}


public List<Reservation_table> view_current_reservations(int client_id) throws SQLException {
    String connectionString = "jdbc:derby://localhost:1527/Cinema";
    String dbuname = "mostafa";
    String dbpass = "arrow5557";

    List<Reservation_table> reservations = new ArrayList<>();

    String sqlQuery = "SELECT * FROM reservation WHERE client_id = ?";

    try (Connection con = DriverManager.getConnection(connectionString, dbuname, dbpass);
         PreparedStatement pst = con.prepareStatement(sqlQuery)) {

        pst.setInt(1, client_id);

        try (ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("reservation_id");
                int clientID = rs.getInt("client_id");
                int movieId = rs.getInt("movie_id");
                String status = rs.getString("status");

                Reservation_table reservation = new Reservation_table(id, clientID, movieId, status);
                reservations.add(reservation);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    // Display the reservations
    if (reservations.isEmpty()) {
        System.out.println("No current reservations for client ID: " + client_id);
    } else {
        System.out.println("Current Reservations for client ID: " + client_id);
        reservations.forEach(System.out::println);
    }

    return reservations; // Always return the reservations list
}

public boolean deleteReservation(int reservationId) {
    String connectionString = "jdbc:derby://localhost:1527/Cinema";
    String dbuname = "mostafa";
    String dbpass = "arrow5557";

    String deleteReservationQuery = "DELETE FROM reservation WHERE reservation_id = ?";
    String updateMovieQuery = "UPDATE movie SET reservationNumber = " +
                              "CASE WHEN reservationNumber > 0 THEN reservationNumber - 1 ELSE 0 END " +
                              "WHERE movie_id = (SELECT movie_id FROM reservation WHERE reservation_id = ?)";

    try (Connection con = DriverManager.getConnection(connectionString, dbuname, dbpass)) {
        // Begin transaction
        con.setAutoCommit(false);

        try {
            // Decrease the reservationNumber in the movie table
            try (PreparedStatement updatePst = con.prepareStatement(updateMovieQuery)) {
                updatePst.setInt(1, reservationId);
                updatePst.executeUpdate();
            }

            // Delete the reservation
            try (PreparedStatement deletePst = con.prepareStatement(deleteReservationQuery)) {
                deletePst.setInt(1, reservationId);

                int rowsDeleted = deletePst.executeUpdate();
                if (rowsDeleted > 0) {
                    // Commit transaction
                    con.commit();                    // Commit transaction

                    System.out.println("Reservation with ID " + reservationId + " deleted successfully.");
                    return true;
                } else {
                    System.out.println("No reservation found with ID " + reservationId);
                }
            }
        } catch (SQLException e) {
            // Rollback transaction in case of any failure
            con.rollback();
            e.printStackTrace();
        } finally {
            // Restore auto-commit
            con.setAutoCommit(true);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false;
}


 public List<Reservation_table> getAllReservations() {   //retunr all reservation used by server 
        String connectionString = "jdbc:derby://localhost:1527/Cinema";
        String dbuname = "mostafa";
        String dbpass = "arrow5557";

        String sqlQuery = "SELECT * FROM reservation";
        List<Reservation_table> reservations = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(connectionString, dbuname, dbpass);
             PreparedStatement pst = con.prepareStatement(sqlQuery);
             ResultSet rs = pst.executeQuery()) {

            // Iterate over the result set and populate the list of Reservation_table objects
            while (rs.next()) {
                int reservationId = rs.getInt("reservation_id");
                int clientId = rs.getInt("client_id");
                int movieId = rs.getInt("movie_id");
                String status = rs.getString("status");

                // Create a new Reservation_table object and add it to the list
                reservations.add(new Reservation_table(reservationId, clientId, movieId, status));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservations;  // Return the list of all reservations
    }

    public String viewMovie(int movieId) {
        String connectionString = "jdbc:derby://localhost:1527/Cinema";
        String dbuname = "mostafa";
        String dbpass = "arrow5557";

        String reservedSeatsQuery = "SELECT COUNT(*) FROM reservation WHERE movie_id = ? AND status = 'reserved'";
        String confirmedSeatsQuery = "SELECT COUNT(*) FROM reservation WHERE movie_id = ? AND status = 'confirmed'";

        int totalSeats = 50; // Total seats for the movie

        int reservedSeats = 0;
        int confirmedSeats = 0;

        try (Connection con = DriverManager.getConnection(connectionString, dbuname, dbpass)) {
            // Get the reserved seats count
            try (PreparedStatement pst = con.prepareStatement(reservedSeatsQuery)) {
                pst.setInt(1, movieId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        reservedSeats = rs.getInt(1);
                    }
                }
            }

            // Get the confirmed seats count
            try (PreparedStatement pst = con.prepareStatement(confirmedSeatsQuery)) {
                pst.setInt(1, movieId);
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        confirmedSeats = rs.getInt(1);
                    }
                }
            }

            // Calculate empty seats
            int emptySeats = totalSeats - (reservedSeats + confirmedSeats);

            // Return the result in the desired format
            return String.format("Reserved seats = %d, Confirmed seats = %d, Empty seats = %d", reservedSeats, confirmedSeats, emptySeats);

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error retrieving movie seat information.";
        }
    }
    
    public List<log_table> viewTransactionLog() {
        String connectionString = "jdbc:derby://localhost:1527/Cinema";
        String dbuname = "mostafa";
        String dbpass = "arrow5557";

        String sqlQuery = "SELECT * FROM log";
        List<log_table> logEntries = new ArrayList<>();

        try (Connection con = DriverManager.getConnection(connectionString, dbuname, dbpass);
             PreparedStatement pst = con.prepareStatement(sqlQuery);
             ResultSet rs = pst.executeQuery()) {

            // Iterate through the result set and populate the list
            while (rs.next()) {
                int id = rs.getInt("transaction_id");
                String info = rs.getString("info");

                // Create a new LogEntry object and add it to the list
                logEntries.add(new log_table(id, info));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return logEntries;  // Return the list of log entries
    }
    
 public void insertLog(String info) {
        // Database connection details
        String connectionString = "jdbc:derby://localhost:1527/Cinema";
        String dbuname = "mostafa";
        String dbpass = "arrow5557";

        // SQL query to insert log
        String query = "INSERT INTO log (info) VALUES (?)";

        try (Connection conn = DriverManager.getConnection(connectionString, dbuname, dbpass);
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            // Set the value for the INFO column
            stmt.setString(1, info);
            
            // Execute the insert query
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                System.out.println("Log inserted successfully.");
            } else {
                System.out.println("Failed to insert log.");
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
 }
    
    
    
}
