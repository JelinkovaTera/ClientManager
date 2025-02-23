package yuyu.clientmanager;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import yuyu.clientmanager.gui.ClientManagerGUI;

import javax.swing.*;

@SpringBootApplication
public class ClientManagerApplication {
    private final ClientManagerGUI gui;

    ClientManagerApplication(ClientManagerGUI gui) {
        this.gui = gui;
    }

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        SpringApplication.run(ClientManagerApplication.class, args);
    }

    @PostConstruct
    public void init() {
        SwingUtilities.invokeLater(gui::launchGUI);
    }
}
