package interfaces;

import db.connection.DatabaseConnector;
import demandes.Demand;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AdminInterface extends JFrame {
    private DefaultListModel<String> demandListModel;
    private JList<String> demandList;
    private DefaultListModel<String> acceptedListModel;
    private JList<String> acceptedList;
    private DefaultListModel<String> declinedListModel;
    private JList<String> declinedList;

    public AdminInterface() {
        // Initialize components and layout for the interface
        // You can use Swing or any other UI framework of your choice

        // Example: Using Swing
        demandListModel = new DefaultListModel<>();
        demandList = new JList<>(demandListModel);

        acceptedListModel = new DefaultListModel<>();
        acceptedList = new JList<>(acceptedListModel);

        declinedListModel = new DefaultListModel<>();
        declinedList = new JList<>(declinedListModel);

// Add action listeners for accept and decline buttons
        JButton acceptButton = new JButton("Accept");
        acceptButton.addActionListener(e -> acceptDemand());

        JButton declineButton = new JButton("Decline");
        declineButton.addActionListener(e -> declineDemand());

// Create a panel to hold buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(acceptButton);
        buttonPanel.add(declineButton);

// Create the main content panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(demandList), BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        JPanel acceptedPanel = new JPanel(new BorderLayout());
        acceptedPanel.add(new JScrollPane(acceptedList), BorderLayout.CENTER);
        acceptedPanel.setBorder(BorderFactory.createTitledBorder("Accepted Demands"));

        JPanel declinedPanel = new JPanel(new BorderLayout());
        declinedPanel.add(new JScrollPane(declinedList), BorderLayout.CENTER);
        declinedPanel.setBorder(BorderFactory.createTitledBorder("Declined Demands"));


        // Set up the frame
        getContentPane().setLayout(new GridLayout(3, 3));
        getContentPane().add(mainPanel);
        getContentPane().add(acceptedPanel);
        getContentPane().add(declinedPanel);
        setTitle("Admin Interface");

        // Populate the demand list on startup
        updateDemandList();
        updateAcceptedAndDeclinedLists();
    }

    private void acceptDemand() {
        int selectedIndex = demandList.getSelectedIndex();
        if (selectedIndex != -1) {
            // Update the selected demand in the database
            updateDemandStatus(true);
            // Update the lists
            updateDemandList();
            updateAcceptedAndDeclinedLists();
        }
    }

    private void declineDemand() {
        int selectedIndex = demandList.getSelectedIndex();
        if (selectedIndex != -1) {
            // Update the selected demand in the database
            updateDemandStatus(false);
            // Update the lists
            updateDemandList();
            updateAcceptedAndDeclinedLists();
        }
    }

    private void updateDemandStatus(boolean accepted) {

//        String selectedElement = demandList.getSelectedValue();
//
//        String numericSubstring = selectedElement.substring(11);
//
        int selectedIndex = demandList.getSelectedIndex();

        System.out.println("accept : "+selectedIndex);

        if (selectedIndex != -1) {
            List<Demand> demands = fetchDemandsFromDatabase(true);
            Demand selectedDemand = demands.get(selectedIndex);

            try (Connection connection = DatabaseConnector.getConnection();
                 Statement statement = connection.createStatement()) {

                String updateQuery = "UPDATE Demande SET isValidated = " + accepted +
                        ", dateReponse = CURRENT_DATE WHERE id = " + selectedDemand.getId();

                statement.executeUpdate(updateQuery);

                if (accepted) {
                    updateDemandList();
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void updateAcceptedAndDeclinedLists() {
        List<Demand> acceptedDemands = fetchAcceptedDemandsFromDatabase();
        List<Demand> declinedDemands = fetchDeclinedDemandsFromDatabase();

        acceptedListModel.clear();
        declinedListModel.clear();

        if (acceptedDemands != null) {
            for (Demand demand : acceptedDemands) {
                if (demand.getDateReponse()!=null){
                    acceptedListModel.addElement("Demand ID: " + demand.getId()+ "    Student: "+ demand.getEtudiantNom()+"  Type demande : "+ demand.getTypeDemandeID());
                }
            }
        }

        if (declinedDemands != null) {
            for (Demand demand : declinedDemands) {
                if (demand.getDateReponse()!=null) {
                    declinedListModel.addElement("Demand ID: " + demand.getId()+ "    Student: "+ demand.getEtudiantNom()+"  Type demande : "+ demand.getTypeDemandeID());
                }
            }
        }
    }

    private List<Demand> fetchAcceptedDemandsFromDatabase() {
        return fetchDemandsFromDatabaseWithStatus(true);
    }

    private List<Demand> fetchDeclinedDemandsFromDatabase() {
        return fetchDemandsFromDatabaseWithStatus(false);
    }

    private List<Demand> fetchDemandsFromDatabaseWithStatus(boolean accepted) {
        List<Demand> demands = new ArrayList<>();

        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement()) {

            String status = accepted ? "1" : "0";
            String query = "SELECT Demande.*, Etudiant.nomEtudiant " +
                    "FROM Demande " +
                    "JOIN Etudiant ON Demande.etudiantID = Etudiant.id " +
                    "WHERE Demande.isValidated = " + status;
            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Demand demand = new Demand();

                demand.setId(resultSet.getInt("id"));
                demand.setTypeDemandeID(resultSet.getString("typeDemandeID"));
                demand.setEtudiantID(resultSet.getInt("etudiantID"));
                demand.setEtudiantNom(resultSet.getString("nomEtudiant")); // Use the alias from the SELECT clause
                demand.setDateSubmission(resultSet.getDate("dateSubmission"));
                demand.setDateReponse(resultSet.getDate("dateReponse"));
                demand.setDetails(resultSet.getString("details"));

                demands.add(demand);
            }
        }
        catch (SQLException e) {
            e.printStackTrace(); // Handle this exception properly in your application
        }

        return demands;
    }

    private void updateDemandList() {
        List<Demand> demands = fetchDemandsFromDatabase(false);
        demandListModel.clear();

        if (demands != null ) {
            for (Demand demand : demands) {
                if (demand.getDateReponse() == null){
                    demandListModel.addElement("Demand ID: " + demand.getId()+ "    Student: "+ demand.getEtudiantNom()+"  Type demande : "+ demand.getTypeDemandeID());
                }
            }
        }
    }

    private List<Demand> fetchDemandsFromDatabase( boolean isUpdating ) {
        List<Demand> demands = new ArrayList<>();

        try (Connection connection = DatabaseConnector.getConnection();
             Statement statement = connection.createStatement()) {

            String query;
            if (isUpdating){
                query = "SELECT * FROM Demande D where D.dateReponse IS NULL ";
            }else{
                query = "SELECT * FROM Demande";
            }

            ResultSet resultSet = statement.executeQuery(query);

            while (resultSet.next()) {
                Demand demand = new Demand();
                demand.setId(resultSet.getInt("id"));
                demand.setTypeDemandeID(resultSet.getString("typeDemandeID"));
                demand.setEtudiantID(resultSet.getInt("etudiantID"));
                demand.setDateSubmission(resultSet.getDate("dateSubmission"));
                demand.setDateReponse(resultSet.getDate("dateReponse"));
                demand.setDetails(resultSet.getString("details"));

                demands.add(demand);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Handle this exception properly in your application
        }

        return demands;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminInterface adminInterface = new AdminInterface();
            adminInterface.setSize(800, 600);
            adminInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            adminInterface.setVisible(true);
        });
    }
}
