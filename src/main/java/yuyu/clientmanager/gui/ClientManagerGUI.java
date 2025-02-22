package yuyu.clientmanager.gui;

import org.springframework.stereotype.Component;
import yuyu.clientmanager.database.ClientDatabase;
import yuyu.clientmanager.model.Client;
import yuyu.clientmanager.service.ClientService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Component
public class ClientManagerGUI {
    private final ClientService clientService = new ClientService(new ClientDatabase());
    private final Logger logger = Logger.getLogger(ClientManagerGUI.class.getName());

    public void launchGUI() {
        JFrame frame = createFrame();
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton addClientButton = new JButton("Add Client");
        JButton findClientButton = new JButton("Find Client");
        JButton deleteClientButton = new JButton("Delete Client");
        JButton showAllButton = new JButton("Show All");

        panel.add(addClientButton);
        panel.add(findClientButton);
        panel.add(deleteClientButton);
        panel.add(showAllButton);

        JTable clientTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(clientTable);
        frame.add(scrollPane, BorderLayout.CENTER);

        addClientButton.addActionListener(e -> {
            logger.info("Add Client button clicked.");
            addClient();
            showResults(clientTable, clientService.getAllClients());
        });

        findClientButton.addActionListener(e -> {
            logger.info("Find Client button clicked.");
            findClient(clientTable);
        });

        deleteClientButton.addActionListener(e -> {
            logger.info("Delete Client button clicked.");
            deleteClient();
            showResults(clientTable, clientService.getAllClients());
        });

        showAllButton.addActionListener(e -> {
            logger.info("Show All button clicked.");
            showResults(clientTable, clientService.getAllClients());
        });

        frame.add(panel, BorderLayout.NORTH);
        frame.setVisible(true);
    }

    private void addClient() {
        JTextField birthNumberField = new JTextField(10);
        JTextField nameField = new JTextField(10);
        JTextField surnameField = new JTextField(10);

        JPanel inputPanel = createInputPanel(birthNumberField, nameField, surnameField);
        int option = JOptionPane.showConfirmDialog(null, inputPanel, "Add Client", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String birthNumber = birthNumberField.getText().trim();
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();

            if (birthNumber.isEmpty() || name.isEmpty() || surname.isEmpty()) {
                logger.info("All fields are empty.");
                JOptionPane.showMessageDialog(null, "All fields are mandatory.");
                return;
            }

            if (isInvalidBirthNumber(birthNumber)) {
                logger.info("Invalid birth number.");
                JOptionPane.showMessageDialog(null, "Invalid birth number format.");
                return;
            }
            String trimmedBirthNumber = trimBirthNumber(birthNumber);

            Client newClient = new Client(trimmedBirthNumber, name, surname);
            logger.info("AddClient called with input: " + newClient);
            boolean wasAdded = clientService.addClient(newClient);

            if (wasAdded) {
                logger.info("Client added successfully.");
                JOptionPane.showMessageDialog(null, "Client added successfully.");
            } else {
                logger.info("Client was already in the database.");
                JOptionPane.showMessageDialog(null, "This client is already in database.");
            }
        }
    }

    private void findClient(JTable clientTable) {
        JTextField birthNumberField = new JTextField(10);
        JTextField nameField = new JTextField(10);
        JTextField surnameField = new JTextField(10);

        JPanel inputPanel = createInputPanel(birthNumberField, nameField, surnameField);
        int option = JOptionPane.showConfirmDialog(null, inputPanel, "Find Client", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String birthNumber = birthNumberField.getText().trim();
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();

            if (birthNumber.isEmpty() && name.isEmpty() && surname.isEmpty()) {
                logger.info("All fields are empty.");
                JOptionPane.showMessageDialog(null, "At least one field must be filled.");
                return;
            }
            if (!birthNumber.isEmpty() && isInvalidBirthNumber(birthNumber)) {
                logger.info("Invalid birth number.");
                JOptionPane.showMessageDialog(null, "Invalid birth number format.");
                return;
            }
            String trimmedBirthNumber = trimBirthNumber(birthNumber);

            Client searchCriteria = new Client(trimmedBirthNumber, name, surname);
            logger.info("FindClient called with input: " + searchCriteria);
            List<Client> foundClients = clientService.findClients(searchCriteria);

            if (foundClients.isEmpty()) {
                logger.info("No clients found.");
                JOptionPane.showMessageDialog(null, "No clients found.");
            } else {
                logger.info("Returning " + foundClients.size() + " clients.");
                showResults(clientTable, foundClients);
            }
        }
    }

    private void deleteClient() {
        JTextField birthNumberField = new JTextField(10);
        JTextField idField = new JTextField(10);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));

        inputPanel.add(new JLabel("Enter Birth Number to Delete (YYMMDDXXXX or YYMMDD/XXXX):"));
        inputPanel.add(birthNumberField);
        inputPanel.add(new JLabel("OR"));
        inputPanel.add(new JLabel("Enter Client ID to Delete:"));
        inputPanel.add(idField);

        int option = JOptionPane.showConfirmDialog(null, inputPanel, "Delete Client", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String birthNumber = birthNumberField.getText().trim();
            String clientIdString = idField.getText().trim();

            if (!birthNumber.isEmpty() && !clientIdString.isEmpty()) {
                logger.info("Both birth number and client ID were filled. Only one can be entered.");
                JOptionPane.showMessageDialog(null, "Please enter only one criteria: either birth number or client ID.");
                return;
            }
            if (birthNumber.isEmpty() && clientIdString.isEmpty()) {
                logger.info("All fields are empty.");
                JOptionPane.showMessageDialog(null, "Please enter either a birth number or a client ID.");
                return;
            }

            if (!clientIdString.isEmpty()) {
                deleteClientById(clientIdString);
            } else {
                deleteClientByBirthNumber(birthNumber);
            }
        }
    }

    private void deleteClientById(String id) {
        int clientId = Integer.parseInt(id);
        logger.info("DeleteClient called with client ID: " + clientId);
        boolean wasRemoved = clientService.removeClientById(clientId);
        if (!wasRemoved) {
            logger.info("Client with ID " + clientId + " was not found.");
            JOptionPane.showMessageDialog(null, "Client with ID " + clientId + " was not found.");
        } else {
            logger.info("Client deleted successfully.");
            JOptionPane.showMessageDialog(null, "Client deleted successfully.");
        }
    }

    private void deleteClientByBirthNumber(String birthNumber) {
        if (isInvalidBirthNumber(birthNumber)) {
            JOptionPane.showMessageDialog(null, "Invalid birth number format.");
            return;
        }
        String trimmedBirthNumber = trimBirthNumber(birthNumber);
        logger.info("DeleteClient called with birth number: " + trimmedBirthNumber);
        boolean wasRemoved = clientService.removeClientByBirthNumber(trimmedBirthNumber);
        if (!wasRemoved) {
            logger.info("Client with birthNumber " + birthNumber + " was not found.");
            JOptionPane.showMessageDialog(null, "Client with birthNumber " + birthNumber + " was not found.");
        } else {
            logger.info("Client deleted successfully.");
            JOptionPane.showMessageDialog(null, "Client deleted successfully.");
        }
    }

    private static JFrame createFrame() {
        JFrame frame = new JFrame("Client Database");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null); // Center the frame
        return frame;
    }

    private static JPanel createInputPanel(JTextField birthNumberField, JTextField nameField, JTextField surnameField) {
        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Birth Number (YYMMDDXXXX or YYMMDD/XXXX):"));
        inputPanel.add(birthNumberField);
        inputPanel.add(new JLabel("Name:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Surname:"));
        inputPanel.add(surnameField);

        return inputPanel;
    }

    public static boolean isInvalidBirthNumber(String birthNumber) {
        String regex = "^\\d{6}[/-]?\\d{4}$";
        Pattern pattern = Pattern.compile(regex);
        if (!pattern.matcher(birthNumber).matches()) {
            return true;
        }
        try {
            LocalDate date = ClientService.birthNumberToLocalDate(birthNumber);
            return false;
        } catch (DateTimeException e) {
            return true;
        }
    }

    private String trimBirthNumber(String birthNumber) {
        if (birthNumber.length() == 11) {
            birthNumber = birthNumber.substring(0, 6) + birthNumber.substring(7,11);
        }
        return birthNumber;
    }

    private void showResults(JTable clientTable, List<Client> foundClients) {
        String[] columnNames = {"ID", "Birth Number", "Name", "Surname", "Age"};
        String[][] data = new String[foundClients.size()][5];

        for (int i = 0; i < foundClients.size(); i++) {
            Client client = foundClients.get(i);
            data[i][0] = String.valueOf(client.getDatabaseId());
            data[i][1] = client.getBirthNumber();
            data[i][2] = client.getName();
            data[i][3] = client.getSurname();
            data[i][4] = String.valueOf(client.getAge());
        }

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        clientTable.setModel(model);
    }
}
