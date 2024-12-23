package cinema;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class Server {

    private ServerSocket server = null;

    // Constructor with port
    public Server(int port) {
        try {
            // Start server and wait for a connection
            server = new ServerSocket(port);
            System.out.println("Server started");
            System.out.println("Waiting for a client...");

            // Continuously listen for client connections
            while (true) {
                Socket socket = server.accept();  // Accept client connection
                System.out.println("Client accepted");

                // Handle the client in a separate thread
                new ClientHandler(socket).start();
            }

        } catch (IOException i) {
            System.out.println("Error in server: " + i);
        }
    }

    // ClientHandler class to process requests from clients
    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private ObjectInputStream in;
        private ObjectOutputStream out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try {
                // Initialize input and output streams
                in = new ObjectInputStream(clientSocket.getInputStream());
                out = new ObjectOutputStream(clientSocket.getOutputStream());

                Object input;

                // Continuously read input until "Over" is received
                while ((input = in.readObject()) != null) {
                    if (input instanceof HashMap) {
                        // Cast the input to a HashMap
                        @SuppressWarnings("unchecked")
                        HashMap<String, Object> reservationData = (HashMap<String, Object>) input;

                        // Check if the client is valid
                        boolean isClient = (boolean) reservationData.get("client");
                        if (isClient) {
                            // Extract command and process accordingly
                            String command = (String) reservationData.get("command");
                            switch (command) {
                                case "browse":
                                    // Call browse() and send the result back
                                    List<Movie_table> movies = browse();
                                    out.writeObject(movies);
                                    break;

                                case "reserve":
                                    // Retrieve clientID and movieID and reserve movie
                                    int clientId = (int) reservationData.get("clientID");
                                    int movieId = (int) reservationData.get("movieID");
                                    Reservation_table reservation = reserveMovie(clientId, movieId);
                                    out.writeObject(reservation);
                                    break;

                                case "deleteReservation":
                                    // Retrieve reservationID and delete reservation
                                    int reservationId = (int) reservationData.get("reservationID");
                                    deleteReservation(reservationId);
                                    System.out.println("Deleted successfully");
                                    out.writeObject("Deleted successfully");
                                    break;
                                    
                                case "view_reservation_id":
                                    int client_Id = (int) reservationData.get("clientID");
                                    List<Reservation_table> reserves = view_current_reservations(client_Id);
                                    out.writeObject(reserves);
                                    break;
                                    
                                default:
                                    // Send an error message for unrecognized commands
                                    out.writeObject("Unknown command: " + command);
                                    break;
                            }
                        } else {
                            // Respond with an error if the client flag is false
                            out.writeObject("Unauthorized client");
                        }
                        out.flush();
                    } else if ("Over".equals(input)) {
                        // Break the loop if "Over" is received
                        break;
                    }
                }
            } catch (IOException | SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    out.close();
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private List<log_table> viewTransactionLog() throws SQLException {
            DBConnection db = new DBConnection();
            return db.viewTransactionLog();
        }

        private List<Movie_table> browse() throws SQLException {
            DBConnection db = new DBConnection();
            return db.browseMovies();
        }

        private Reservation_table reserveMovie(int clientId, int movieId) {
            DBConnection db = new DBConnection();
            return db.reserve(clientId, movieId);
        }

        private void deleteReservation(int reservationID) {
            DBConnection db = new DBConnection();
            db.deleteReservation(reservationID);
        }
        
        private List<Reservation_table> view_current_reservations(int client_id) throws SQLException{
            DBConnection db = new DBConnection();
            return db.view_current_reservations(client_id);
        }
    }

    public static void main(String args[]) {
        // Start the server on port 5000
        Server server = new Server(5000);
    }
}
