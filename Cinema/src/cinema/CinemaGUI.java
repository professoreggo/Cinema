/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cinema;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CinemaGUI extends JFrame {

    public CinemaGUI() {
        // Set up the main frame
	Server s = new Server(5000);
        setTitle("Cinema Management");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10));

        // Create buttons
        JButton btnShowReservations = new JButton("Show All Reservations");
        JButton btnViewTransactionLog = new JButton("View Transaction Log");
        JButton btnShowMovies = new JButton("Show Movies");

        // Button actions
        btnShowReservations.addActionListener(e -> showAllReservations());
        btnViewTransactionLog.addActionListener(e -> viewTransactionLog());
        btnShowMovies.addActionListener(e -> showMovies());

        // Add buttons to the frame
        add(btnShowReservations);
        add(btnViewTransactionLog);
        add(btnShowMovies);

        // Modern styling
        setBackground(Color.DARK_GRAY);
        btnShowReservations.setBackground(new Color(60, 179, 113));
        btnShowReservations.setForeground(Color.WHITE);
        btnViewTransactionLog.setBackground(new Color(30, 144, 255));
        btnViewTransactionLog.setForeground(Color.WHITE);
        btnShowMovies.setBackground(new Color(255, 165, 0));
        btnShowMovies.setForeground(Color.WHITE);
        
    }

    private void showAllReservations() {
        DBConnection db = new DBConnection();
        JFrame frame = new JFrame("All Reservations");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        

        // Fetch data
        List<Reservation_table> reservations = db.getAllReservations();

        // Table setup
        String[] columns = {"Reservation ID", "Client ID", "Movie ID", "Status"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (Reservation_table res : reservations) {
            model.addRow(new Object[]{res.getReservation_id(), res.getClient_id(), res.getMovie_id(), res.getStatus()});
        }
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(scrollPane);
        frame.setVisible(true);
    }

    private void viewTransactionLog() {
        
        JFrame frame = new JFrame("Transaction Log");
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Fetch data
        List<log_table> logs = new DBConnection().viewTransactionLog();

        // Table setup
        String[] columns = {"Log ID", "Information"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        for (log_table log : logs) {
            model.addRow(new Object[]{log.getTransaction_id(), log.getInfo()});
        }
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);

        frame.add(scrollPane);
        frame.setVisible(true);
    }

   private void showMovies()  {
    JFrame frame = new JFrame("Movies");
    frame.setSize(600, 400);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    // Fetch data
    List<Movie_table> movies = new DBConnection().getMovies();

    // Table setup
    String[] columns = {"Movie ID", "Title", "Number of Reservations"};
    DefaultTableModel model = new DefaultTableModel(columns, 0);

    JTable table = new JTable(model);

    // Add data to table
    for (Movie_table movie : movies) {
        model.addRow(new Object[]{movie.getId(), movie.getName(), movie.getReservationNumber(), "View"});
    }


    // Add the table to a scroll pane
    JScrollPane scrollPane = new JScrollPane(table);
    frame.add(scrollPane);
    frame.setVisible(true);
}



    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            CinemaGUI gui = new CinemaGUI();
            gui.setVisible(true);
        });
    }

    private String String(int reservationNumber) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
