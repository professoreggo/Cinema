package cinema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ClientGUI {
    
    private int client_id;
    private Client client;

    public ClientGUI() {
        client = new Client("127.0.0.1", 5000);
        createLoginFrame();
    }

private void createLoginFrame() {
    JFrame loginFrame = new JFrame("Cinema Login");
    loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    loginFrame.setSize(400, 300);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(4, 2, 10, 10));  // Adjusted grid layout to accommodate the warning label

    JLabel usernameLabel = new JLabel("Username:");
    JTextField usernameField = new JTextField();

    JLabel passwordLabel = new JLabel("Password:");
    JPasswordField passwordField = new JPasswordField();

    // Warning label for invalid login
    JLabel warningLabel = new JLabel("");
    warningLabel.setForeground(Color.RED);  // Set the text color to red
    warningLabel.setHorizontalAlignment(SwingConstants.CENTER);

    DBConnection db = new DBConnection();

    JButton loginButton = new JButton("Login");
    loginButton.addActionListener(e -> {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        // Check login credentials
        if (db.login_client(username, password)) {
            createMainFrame();
            loginFrame.dispose();
        } else {
            // Display warning message if login fails
            warningLabel.setText("Invalid username or password.");
        }
        client_id = db.getClientId(username, password);
        System.out.println("client id is "+client_id);
    });

    panel.add(usernameLabel);
    panel.add(usernameField);
    panel.add(passwordLabel);
    panel.add(passwordField);
    panel.add(warningLabel);  // Add the warning label to the panel
    panel.add(new JLabel());  // Empty space
    panel.add(loginButton);

    loginFrame.add(panel);
    loginFrame.setLocationRelativeTo(null);
    loginFrame.setVisible(true);
}


    private void createMainFrame() {
        JFrame mainFrame = new JFrame("Cinema Client");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(400, 300);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1, 10, 10));

        JButton browseMoviesButton = new JButton("Browse Movies");
        browseMoviesButton.addActionListener(e -> createBrowseMoviesFrame());

        JButton viewReservationsButton = new JButton("View Current Reservations");
        viewReservationsButton.addActionListener(e -> createViewReservationsFrame());
        
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> {
        mainFrame.dispose(); // Close the current main frame
        createLoginFrame();  // Show the login frame
    });

        panel.add(browseMoviesButton);
        panel.add(viewReservationsButton);
        panel.add(logoutButton);

        mainFrame.add(panel);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setVisible(true);
    }

    private void createBrowseMoviesFrame() {
        JFrame browseFrame = new JFrame("Browse Movies");
        browseFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        browseFrame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));

        List<Movie_table> movies = client.browseMovies(); // Adjusted to return a list
        for (Movie_table movie : movies) {
            JPanel moviePanel = new JPanel();
            moviePanel.setLayout(new BorderLayout());
            moviePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JLabel movieName = new JLabel(movie.getName());
            JButton reserveButton = new JButton("Reserve");
            reserveButton.addActionListener(e -> client.reserveMovie(client_id, movie.getId())); //////////////////////////////////////////////

            moviePanel.add(movieName, BorderLayout.CENTER);
            moviePanel.add(reserveButton, BorderLayout.EAST);
            panel.add(moviePanel);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        browseFrame.add(scrollPane);
        browseFrame.setLocationRelativeTo(null);
        browseFrame.setVisible(true);
    }

    private void createViewReservationsFrame() {
        JFrame reservationsFrame = new JFrame("Current Reservations");
        reservationsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reservationsFrame.setSize(600, 400);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 10, 10));
        
        List<Reservation_table> reservations = client.viewCurrentReservations(client_id); // Adjusted to return a list
        for (Reservation_table reservation : reservations) {
            JPanel reservationPanel = new JPanel();
            reservationPanel.setLayout(new BorderLayout());
            reservationPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            JLabel reservationDetails = new JLabel("Reservation ID: " + reservation.getReservation_id() +
                    ", Movie ID: " + reservation.getMovie_id() + ", Status: " + reservation.getStatus());
            JButton deleteButton = new JButton("Delete");
            deleteButton.addActionListener(e -> client.deleteReservation(reservation.getReservation_id()));

            reservationPanel.add(reservationDetails, BorderLayout.CENTER);
            reservationPanel.add(deleteButton, BorderLayout.EAST);
            panel.add(reservationPanel);
        }

        JScrollPane scrollPane = new JScrollPane(panel);
        reservationsFrame.add(scrollPane);
        reservationsFrame.setLocationRelativeTo(null);
        reservationsFrame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientGUI::new);
    }
}
