package main;

import controller.Controller;
import gui.GMenu;
import gui.loginMenu.FirstSupervisorMenu;
import gui.mainMenu.MainMenuG;
import gui.paymentMenu.PaymentMenuForCustomer;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args)  {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Image logoImage = null;
        try {
            logoImage = new Image(new FileInputStream("./src/main/resources/header/Logo.png"));
        } catch (FileNotFoundException e) {
            System.out.println("Icon not found");
        }
        stage.setTitle("Team 34 Online retail store");
        stage.getIcons().add(logoImage);

        Controller controller = new Controller();

        stage.setOnCloseRequest(e -> {
            e.consume();
            try {
                controller.disconnect();
            } catch (IOException ioException) {
                System.out.println("Error disconnecting server, never mind");
            }
            System.exit(0);
        });

        stage.setScene(new PaymentMenuForCustomer(null, stage, controller, 1000).getScene());
        stage.show();
//        GMenu mainMenu = new MainMenuG( null, stage, controller);
//        GMenu initialMenu = new FirstSupervisorMenu(null, stage, controller);
//        stage.setScene((controller.getIsFirstSupervisorCreated() ? mainMenu : initialMenu).getScene());
//        stage.show();
    }

}
