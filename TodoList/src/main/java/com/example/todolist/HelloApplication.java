package com.example.todolist;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;

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
        Image openIcon = new Image(getClass().getResourceAsStream("media/menu_icon.png"));  //get menu icon
        ImageView openView = new ImageView(openIcon);
        openView.setFitWidth(40);
        openView.setFitHeight(40);


        MenuItem test = new MenuItem("Aggiungi elemento");   //create submenu
        test.setOnAction(addTodo());
        test.setStyle("-fx-font-size: 20px");   //-fx-background-color: #fffdd0

        Menu mainmenu = new Menu(""); //create menu
        mainmenu.setGraphic(openView);    //set icon
        mainmenu.getItems().add(test);
        //mainmenu.setStyle("-fx-background-color: #fffdd0");

        MenuBar menuBar = new MenuBar();    //create menu bar
        menuBar.setMinHeight(40);
        menuBar.getMenus().add(mainmenu);
        //menuBar.setStyle("-fx-background-color: #fffdd0");

        VBox root = new VBox(menuBar);
        GridPane grid = new GridPane();
        root.getChildren().add(grid);

        Scene scene = new Scene(root, 800, 800);

        primaryStage.setTitle("TodoList");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * create event handler to add a new item to a todolist
     * @return
     */
    Label error;
    public EventHandler<ActionEvent> addTodo(){
        return new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                VBox vbox = new VBox();
                Scene sceneAddTodo = new Scene(vbox, 800,800);
                Stage stage = new Stage();
                stage.setScene(sceneAddTodo);




                HBox hbox = new HBox();
                DatePicker todoDate = new DatePicker();
                Label labeltags = new Label("Tags:");
                TextField todoTags = new TextField();
                hbox.getChildren().addAll(todoDate,labeltags,todoTags);

                TextArea todoText = new TextArea();
                todoText.setMaxHeight(200);
                todoText.setMaxWidth(400);

                Button confirm = new Button("Conferma");
                confirm.setOnAction(confirmAddTodo(todoDate.getValue(),todoTags.getText(),todoText.getText()));

                error= new Label();
                vbox.getChildren().addAll(hbox,todoText,confirm,error);

                stage.show();
            }
        };
    }
    public EventHandler<ActionEvent> confirmAddTodo(LocalDate date, String tags, String todo) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if(date==null || todo ==null){
                    error.setText("Errore nell'inserimento della data o del testo"); //add window error instead
                }else{
                    //save element
                }
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