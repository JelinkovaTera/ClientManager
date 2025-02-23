package yuyu.clientmanager;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import yuyu.clientmanager.gui.ClientManagerGUI;

import javax.swing.*;

@SpringBootApplication
public class ClientManagerApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(ClientManagerApplication.class)
                .headless(false)
                .web(WebApplicationType.NONE)
                .run(args);
        SwingUtilities.invokeLater(() -> context.getBean(ClientManagerGUI.class).launchGUI());
    }
}
