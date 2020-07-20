package main;

import controller.Controller;
import gui.paymentMenu.PaymentMenuForCustomer;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

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

        stage.setScene(new PaymentMenuForCustomer(null, stage, controller, null).getScene());

//
//        GMenu mainMenu = new MainMenuG( null, stage, controller);
//        GMenu initialMenu = new FirstSupervisorMenu(null, stage, controller);
//        stage.setScene((controller.getIsFirstSupervisorCreated() ? mainMenu : initialMenu).getScene());
        stage.show();
    }

}
