package yuyu.clientmanager.service;

import org.springframework.stereotype.Service;
import yuyu.clientmanager.database.ClientDatabase;
import yuyu.clientmanager.model.Client;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private final ClientDatabase clientDatabase;
    private final BirthNumberConverter converter;
    private static final Logger logger = Logger.getLogger(ClientService.class.getName());

    public ClientService(ClientDatabase clientDatabase, BirthNumberConverter birthNumberConverter) {
        this.clientDatabase = clientDatabase;
        this.converter = birthNumberConverter;
    }

    public List<Client> getAllClients() {
        logger.info("Calling getClients.");
        List<Client> clientList = clientDatabase.getClients();
        fillAge(clientList);
        return clientList;
    }

    public boolean addClient(Client client) {
        logger.info("Calling addClient.");
        return clientDatabase.addClient(client);
    }

    public boolean removeClientById(int clientId) {
        logger.info("Calling removeClientById.");
        return clientDatabase.removeClientById(clientId);
    }

    public boolean removeClientByBirthNumber(String birthNumber) {
        logger.info("Calling removeClientByBirthNumber.");
        return clientDatabase.removeClientByBirthNumber(birthNumber);
    }

    public List<Client> findClients(Client searchCriteria) {
        List<Client> foundClients = new ArrayList<>();

        if (searchCriteria.getDatabaseId() != 0) {
            logger.info("Finding clients by clientId: " + searchCriteria.getDatabaseId());
            foundClients.add(findClientById(searchCriteria));
        } else if (!searchCriteria.getBirthNumber().isEmpty()) {
            logger.info("Finding clients by birth number: " + searchCriteria.getBirthNumber());
            foundClients.add(findClientByBirthNumber(searchCriteria));
        } else {
            logger.info("Finding clients by name: " + searchCriteria.getName() + " and surname: " + searchCriteria.getSurname());
            foundClients.addAll(findClientsByNameAndSurname(searchCriteria));
        }

        fillAge(foundClients);
        return foundClients;
    }

    Client findClientById(Client searchCriteria) {
        Client clientFoundById = clientDatabase.findClientWithId(searchCriteria.getDatabaseId());
        if (clientFoundById != null &&
                birthNumberMatchesIfFilled(clientFoundById.getBirthNumber(), searchCriteria.getBirthNumber()) &&
                nameAndSurnameMatchIfFilled(clientFoundById, searchCriteria)) {
            return clientFoundById;
        }
        return null;
    }

    Client findClientByBirthNumber(Client searchCriteria) {
        Client clientFoundByBirthNumber = clientDatabase.findClientWithBirthNumber(searchCriteria.getBirthNumber());
        if (clientFoundByBirthNumber != null &&
                nameAndSurnameMatchIfFilled(clientFoundByBirthNumber, searchCriteria)) {
            return clientFoundByBirthNumber;
        }
        return null;
    }

    List<Client> findClientsByNameAndSurname(Client searchCriteria) {
        return clientDatabase.getClients().stream()
                .filter(client -> nameAndSurnameMatchIfFilled(client, searchCriteria))
                .collect(Collectors.toList());
    }

    private boolean birthNumberMatchesIfFilled(String birthNumber, String searchBirthNumber) {
        boolean birthNumberMatches = !searchBirthNumber.isEmpty() || searchBirthNumber.equals(birthNumber);
        logger.info("Checking if birth number matches: " + birthNumberMatches);
        return birthNumberMatches;
    }

    private boolean nameAndSurnameMatchIfFilled(Client client, Client searchCriteria) {
        boolean nameMatches = searchCriteria.getName().isEmpty() || searchCriteria.getName().equals(client.getName());
        boolean surnameMatches = searchCriteria.getSurname().isEmpty() || searchCriteria.getSurname().equals(client.getSurname());
        logger.info("Checking if name matches: " + nameMatches + " and surname matches: " + surnameMatches);

        return nameMatches && surnameMatches;
    }

    void fillAge(List<Client> clientList) {
        logger.info("Filling age.");
        clientList.forEach(client -> client.setAge(calculateAge(client.getBirthNumber())));
    }

    int calculateAge(String birthNumber) {
        LocalDate birthDate = converter.birthNumberToLocalDate(birthNumber);
        long age = ChronoUnit.YEARS.between(birthDate, LocalDate.now());
        return (int) age;
    }
}
