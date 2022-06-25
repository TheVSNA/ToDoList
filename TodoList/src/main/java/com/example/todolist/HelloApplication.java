package com.example.todolist;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author blj0011
 */
public class HelloApplication extends Application
{
    Label label;
    @Override
    public void start(Stage primaryStage)
    {
        label = new Label("spas");

        Button b = new Button("test");
        b.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                label.setText("Accepted");
            }
        });

        Image openIcon = new Image(getClass().getResourceAsStream("media/menu_icon.png"));  //get menu icon
        ImageView openView = new ImageView(openIcon);
        openView.setFitWidth(40);
        openView.setFitHeight(40);


        MenuItem test = new MenuItem("test");   //create submenu
        test.setOnAction(test());

        Menu miFile = new Menu(""); //create menu
        miFile.setGraphic(openView);    //set icon
        miFile.getItems().add(test);

        MenuBar menuBar = new MenuBar();    //create menu bar
        menuBar.setMinHeight(40);
        menuBar.getMenus().add(miFile);

        VBox root = new VBox(menuBar);
        root.getChildren().add(label);
        root.getChildren().add(b);

        Scene scene = new Scene(root, 300, 250);

        primaryStage.setTitle("TodoList");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public EventHandler<ActionEvent> test(){
        return new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                label.setText("spos");
            }
        };
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        launch(args);
    }

}