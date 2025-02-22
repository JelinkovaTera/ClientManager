package yuyu.clientmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import yuyu.clientmanager.gui.ClientManagerGUI;

import javax.swing.*;

@SpringBootApplication
public class ClientManagerApplication {
    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(ClientManagerApplication.class, args);
        SwingUtilities.invokeLater(() -> new ClientManagerGUI().launchGUI());
    }

}
