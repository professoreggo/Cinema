package cinema;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

public class Client {

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    // Constructor to connect to the server
    public Client(String address, int port) {
        try {
            socket = new Socket(address, port);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Connected to the server");
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e);
        }
    }

    // Browse movies
    public List<Movie_table> browseMovies() {
        try {
            HashMap<String, Object> request = new HashMap<>();
            request.put("command", "browse");
            request.put("client", true);

            out.writeObject(request);
            out.flush();

            Object response = in.readObject();
            if (response instanceof List<?>) {
                @SuppressWarnings("unchecked")
                List<Movie_table> movies = (List<Movie_table>) response;
                System.out.println("Available movies:");
                for (Movie_table movie : movies) {
                    System.out.println("Movie Name: " + movie.getName());
                }
                return movies;
            } else {
                System.out.println("Unexpected response: " + response);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error browsing movies: " + e);
        }
        return null;
    }

   
public boolean reserveMovie(int clientId, int movieId) {
    DBConnection dbConnection = new DBConnection(); // Create an instance of DBConnection

    try {
        HashMap<String, Object> request = new HashMap<>();
        request.put("command", "reserve");
        request.put("client", true);
        request.put("clientID", clientId);
        request.put("movieID", movieId);

        out.writeObject(request);
        out.flush();

        Object response = in.readObject();
        if (response instanceof Reservation_table) {
            System.out.println("Reservation successful: " + response);

            // Insert log for successful reservation
            String logInfo = "Reservation successful: Client ID " + clientId + 
                             " reserved Movie ID " + movieId + ".";
            dbConnection.insertLog(logInfo);

            return true;
        } else {
            System.out.println("Unexpected response: " + response);

            // Insert log for unexpected response
            String logInfo = "Unexpected response while reserving: Client ID " + clientId + 
                             ", Movie ID " + movieId + ". Response: " + response;
            dbConnection.insertLog(logInfo);
        }
    } catch (IOException | ClassNotFoundException e) {
        System.out.println("Error reserving movie: " + e);

        // Insert log for exception
        String logInfo = "Error while reserving movie: Client ID " + clientId + 
                         ", Movie ID " + movieId + ". Error: " + e.getMessage();
        dbConnection.insertLog(logInfo);
    }

    return false;
}


    // View current reservations
    public List<Reservation_table> viewCurrentReservations(int clientId) {
        try {
            HashMap<String, Object> request = new HashMap<>();
            request.put("command", "view_reservation_id");
            request.put("client", true);
            request.put("clientID", clientId);

            out.writeObject(request);
            out.flush();

            Object response = in.readObject();
            if (response instanceof List<?>) {
                @SuppressWarnings("unchecked")
                List<Reservation_table> reservations = (List<Reservation_table>) response;
                System.out.println("Current reservations:");
                for (Reservation_table reservation : reservations) {
                    System.out.println("Reservation ID: " + reservation.getReservation_id() +
                                       ", Movie ID: " + reservation.getMovie_id() +
                                       ", Status: " + reservation.getStatus());
                }
                return reservations;
            } else {
                System.out.println("Unexpected response: " + response);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error viewing reservations: " + e);
        }
        return null;
    }

    // Delete a reservation
    public boolean deleteReservation(int reservationId) {
        try {
            HashMap<String, Object> request = new HashMap<>();
            request.put("command", "deleteReservation");
            request.put("client", true);
            request.put("reservationID", reservationId);

            out.writeObject(request);
            out.flush();

            Object response = in.readObject();
            if (response instanceof String) {
                String message = (String) response;
                System.out.println(message);
                return message.equals("Deleted successfully");
            } else {
                System.out.println("Unexpected response: " + response);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error deleting reservation: " + e);
        }
        return false;
    }

    // Close the connection
    public void closeConnection() {
        try {
            out.writeObject("Over");
            out.flush();
            in.close();
            out.close();
            socket.close();
            System.out.println("Disconnected from the server");
        } catch (IOException e) {
            System.out.println("Error closing connection: " + e);
        }
    }

    public static void main(String[] args) {
        Client client = new Client("127.0.0.1", 5000);

        // Perform client operations
        List<Movie_table> movies = client.browseMovies();
        if (movies != null && !movies.isEmpty()) {
            client.reserveMovie(202, movies.get(0).getId());
        }
        List<Reservation_table> reservations = client.viewCurrentReservations(202);
        if (reservations != null && !reservations.isEmpty()) {
            client.deleteReservation(reservations.get(0).getReservation_id());
        }

        client.closeConnection();
    }
}
