package yuyu.clientmanager.database;

import org.springframework.stereotype.Repository;
import yuyu.clientmanager.model.Client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Repository
public class ClientDatabase {

    private final Map<Integer, Client> clientsById;
    private final Map<String, Client> clientsByBirthNumber;
    private final List<Client> clients;
    private static int lastID;
    private static final Logger logger = Logger.getLogger(ClientDatabase.class.getName());

    public ClientDatabase() {
        clientsById = new HashMap<>();
        clientsByBirthNumber = new HashMap<>();
        clients = new ArrayList<>();
        lastID = 0;
    }

    public List<Client> getClients() {
        logger.info("Returning all clients.");
        return clients;
    }

    public boolean addClient(Client client) {
        if (birthNumberAlreadyInDatabase(client.getBirthNumber())) {
            logger.info("Birth number is already database.");
            return false;
        }
        client.setDatabaseId(++lastID);
        clientsById.put(client.getDatabaseId(), client);
        clientsByBirthNumber.put(client.getBirthNumber(), client);
        clients.add(client);
        logger.info("Client added to database: " + client);
        return true;
    }

    public boolean removeClientById(int databaseId) {
        Client client = clientsById.get(databaseId);
        if (client == null) {
            return false;
        }

        clientsById.remove(databaseId);
        clientsByBirthNumber.remove(client.getBirthNumber());
        clients.remove(client);
        logger.info("Client removed from database: " + client);

        return true;
    }

    public boolean removeClientByBirthNumber(String birthNumber) {
        Client client = clientsByBirthNumber.get(birthNumber);
        if (client == null) {
            return false;
        }

        clientsByBirthNumber.remove(birthNumber);
        clients.remove(client);
        clientsById.remove(client.getDatabaseId());
        logger.info("Client removed from database: " + client);

        return true;
    }

    public Client findClientWithId(int databaseId) {
        Client client = clientsById.get(databaseId);
        if (client == null) {
            logger.info("Client not found with id: " + databaseId);
        } else {
            logger.info("Client found with id: " + databaseId);
        }

        return client;
    }

    public Client findClientWithBirthNumber(String birthNumber) {
        Client client = clientsByBirthNumber.get(birthNumber);
        if (client == null) {
            logger.info("Client not found with birth number: " + birthNumber);
        } else {
            logger.info("Client found with birth number: " + birthNumber);
        }

        return client;

    }

    private boolean birthNumberAlreadyInDatabase(String birthNumber) {
        return clientsByBirthNumber.containsKey(birthNumber);
    }
}
