package gui.cartMenu;

import cart.Cart;
import cart.ProductInCart;
import gui.GMenu;
import gui.loginMenu.LoginGMenu;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.skin.LabeledSkinBase;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import product.Product;

import java.io.File;
import java.util.ArrayList;

import static javafx.scene.control.ContentDisplay.CENTER;

public class CartGMenu extends GMenu {

    public CartGMenu(String menuName, GMenu parentMenu, Stage stage) {
        super(menuName, parentMenu, stage);
    }

    @Override
    protected Scene createScene() {
        GridPane backgroundLayout = new GridPane();
        GridPane mainPane = new GridPane();
        AnchorPane mainAnchorPain = new AnchorPane();
        mainAnchorPain.setPrefHeight(513.0);
        mainAnchorPain.setPrefWidth(657.0);
        TableView tableView = new TableView();
        tableView.setPrefHeight(272.0);
        tableView.setPrefWidth(450.0);
        tableView.setLayoutX(97.0);
        tableView.setLayoutY(121.0);


        mainAnchorPain.getChildren().add(tableView);
        Label label = new Label();
        label.setPrefHeight(21.0);
        label.setPrefWidth(450.0);
        label.setLayoutX(97.0);
        label.setStyle("-fx-background-color: #9cbfe3;");
        label.setContentDisplay(CENTER);
        label.setLayoutY(100.0);
        label.setText("Products On Your Shopping Cart");
        label.setAlignment(Pos.CENTER);

        mainAnchorPain.getChildren().add(label);
        Button clearCart = new Button();
        clearCart.setPrefHeight(26.0);

        clearCart.setPrefWidth(109.0);
        clearCart.setLayoutX(139.0);
        clearCart.setLayoutY(432.0);
        clearCart.setText("Clear Cart");
        clearCart.setMnemonicParsing(false);
        clearCart.getStylesheets().add(new File("src/main/resources/css/Style.css").toURI().toString());
        clearCart.getStyleClass().add("button");

        mainAnchorPain.getChildren().add(clearCart);
        Button placeOrder = new Button();
        placeOrder.setPrefHeight(26.0);
        placeOrder.setPrefWidth(110.0);
        placeOrder.setLayoutX(419.0);
        placeOrder.setLayoutY(432.0);
        placeOrder.setText("Place Order");
        placeOrder.setMnemonicParsing(false);
        placeOrder.getStylesheets().add(new File("src/main/resources/css/Style.css").toURI().toString());
        placeOrder.getStyleClass().add("button");

        placeOrder.setOnMouseClicked(e->{
            if(controller.getAccountController().hasSomeOneLoggedIn()){
                stage.setScene(new PurchaseMenuG("Purchase Menu", this, stage).getScene());
            }
            else {
                stage.setScene(new LoginGMenu("Login Menu", this, stage).getScene());
            }

        });

        mainAnchorPain.getChildren().add(placeOrder);
        Button updateCart = new Button();
        updateCart.setPrefHeight(26.0);

        updateCart.setPrefWidth(110.0);
        updateCart.setLayoutX(273.0);
        updateCart.setLayoutY(432.0);
        updateCart.setText("Update Cart");
        updateCart.setMnemonicParsing(false);
        updateCart.getStylesheets().add(new File("src/main/resources/css/Style.css").toURI().toString());
        updateCart.getStyleClass().add("button");

        mainAnchorPain.getChildren().add(updateCart);

        backgroundLayout.getChildren().add(mainAnchorPain);

        ArrayList<ProductInCart> productInCarts = controller.getAccountController().controlViewCart().getProductsIn();
        for (ProductInCart productInCart : productInCarts) {
           mainAnchorPain.getChildren().add(createHBox(productInCart));
            System.out.println("hi");
        }
        Scene scene = new Scene(backgroundLayout);
        return scene;
    }

    private static GridPane createHBox(ProductInCart productInCart){
        GridPane gridPane = new GridPane();
        Product product = productInCart.getProduct();
        Label IdLabel = new Label(product.getProductId());
        Label nameLabel = new Label(product.getName());
        Label priceLabel = new Label(String.valueOf(product.getPrice(productInCart.getSupplier())));
//        Label CountLabel = new Label()

        Button increment = new Button("+");
        Button decrement = new Button("-");

        int column = 0;
        gridPane.add(IdLabel , 1, column );
        column ++;
        gridPane.add(nameLabel, 1, column);
        column ++;
        gridPane.add(priceLabel, 1, column);
        column ++;
        gridPane.add(increment, 0, column);
        column ++;
        gridPane.add(decrement, 2, column);

        return gridPane;
    }

}
