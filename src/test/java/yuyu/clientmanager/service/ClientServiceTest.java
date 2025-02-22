package yuyu.clientmanager.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import yuyu.clientmanager.database.ClientDatabase;
import yuyu.clientmanager.model.Client;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ClientServiceTest {
    @Mock
    private ClientDatabase mockClientDatabase;

    private ClientService clientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        clientService = new ClientService(mockClientDatabase);
    }

    @Test
    public void testGetAllClients() {
        Client client1 = new Client("7310054125", "Josef", "Novák");
        Client client2 = new Client("9812196042", "Karel", "Křížala");
        List<Client> mockClients = new ArrayList<>();
        mockClients.add(client1);
        mockClients.add(client2);

        when(mockClientDatabase.getClients()).thenReturn(mockClients);

        List<Client> result = clientService.getAllClients();

        assertEquals(2, result.size());
        verify(mockClientDatabase, times(1)).getClients();
    }

    @Test
    public void testFindClientsID() {
        Client searchCriteria = new Client("", "", "");
        searchCriteria.setDatabaseId(1);
        Client client1 = new Client("7310054125", "Josef", "Novák");
        client1.setDatabaseId(1);
        List<Client> expectedList = new ArrayList<>();
        expectedList.add(client1);
        ClientService spyClientService = spy(clientService);

        when(spyClientService.findClientById(searchCriteria)).thenReturn(client1);
        doNothing().when(spyClientService).fillAge(anyList());

        List<Client> resultList = spyClientService.findClients(searchCriteria);

        assertEquals(expectedList, resultList);
        verify(spyClientService, times(1)).findClientById(searchCriteria);
    }

    @Test
    public void testFindClientsBirthNumber() {
        Client searchCriteria = new Client("7310054125", "", "");
        Client client1 = new Client("7310054125", "Josef", "Novák");
        client1.setDatabaseId(1);
        List<Client> expectedList = new ArrayList<>();
        expectedList.add(client1);
        ClientService spyClientService = spy(clientService);

        when(spyClientService.findClientByBirthNumber(searchCriteria)).thenReturn(client1);
        doNothing().when(spyClientService).fillAge(anyList());

        List<Client> resultList = spyClientService.findClients(searchCriteria);

        assertEquals(expectedList, resultList);
        verify(spyClientService, times(1)).findClientByBirthNumber(searchCriteria);
    }

    @Test
    public void testFindClientsName() {
        Client searchCriteria = new Client("", "Josef", "");
        Client client1 = new Client("7310054125", "Josef", "Novák");
        client1.setDatabaseId(1);
        List<Client> expectedList = new ArrayList<>();
        expectedList.add(client1);
        ClientService spyClientService = spy(clientService);

        when(spyClientService.findClientsByNameAndSurname(searchCriteria)).thenReturn(expectedList);
        doNothing().when(spyClientService).fillAge(anyList());

        List<Client> resultList = spyClientService.findClients(searchCriteria);

        assertEquals(expectedList, resultList);
        verify(spyClientService, times(1)).findClientsByNameAndSurname(searchCriteria);
    }

    @Test
    public void testFindClientsSurname() {
        Client searchCriteria = new Client("", "", "Novák");
        Client client1 = new Client("7310054125", "Josef", "Novák");
        client1.setDatabaseId(1);
        List<Client> expectedList = new ArrayList<>();
        expectedList.add(client1);
        ClientService spyClientService = spy(clientService);

        when(spyClientService.findClientsByNameAndSurname(searchCriteria)).thenReturn(expectedList);
        doNothing().when(spyClientService).fillAge(anyList());

        List<Client> resultList = spyClientService.findClients(searchCriteria);

        assertEquals(expectedList, resultList);
        verify(spyClientService, times(1)).findClientsByNameAndSurname(searchCriteria);
    }

    @Test
    public void testFindClientsSurnameNotFound() {
        Client searchCriteria = new Client("", "", "Novák");
        Client client1 = new Client("7310054125", "Josef", "Novák");
        client1.setDatabaseId(1);
        List<Client> expectedList = new ArrayList<>();
        expectedList.add(client1);
        ClientService spyClientService = spy(clientService);

        when(spyClientService.findClientsByNameAndSurname(searchCriteria)).thenReturn(new ArrayList<>());
        doNothing().when(spyClientService).fillAge(anyList());

        List<Client> resultList = spyClientService.findClients(searchCriteria);

        assertTrue(resultList.isEmpty());
        verify(spyClientService, times(1)).findClientsByNameAndSurname(searchCriteria);
    }

}
