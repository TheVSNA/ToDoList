package com.example.todolist;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

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


        MenuItem menuItem1 = new MenuItem("Aggiungi elemento");   //create submenu
        menuItem1.setOnAction(addTodo());
        menuItem1.setStyle("-fx-font-size: 20px");   //-fx-background-color: #fffdd0

        Menu mainmenu = new Menu(""); //create menu
        mainmenu.setGraphic(openView);    //set icon
        mainmenu.getItems().add(menuItem1);
        //mainmenu.setStyle("-fx-background-color: #fffdd0");

        MenuBar menuBar = new MenuBar();    //create menu bar
        menuBar.setMinHeight(40);
        menuBar.getMenus().add(mainmenu);
        //menuBar.setStyle("-fx-background-color: #fffdd0");

        VBox root = new VBox(menuBar);
        BorderPane grid = new BorderPane();
        root.getChildren().add(grid);

        Scene scene = new Scene(root, 800, 800);



        VBox center = new VBox();
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
        String todaysdate = dt1.format(new Date()).toString();  //TODO LET USER CHOOSE DATE
        try {
            BufferedReader bufferreader = new BufferedReader(new FileReader("todos.txt"));
            String line;
            while ((line = bufferreader.readLine()) != null) {
                String date = line.substring(0,line.indexOf(';'));
                System.out.println(date.toString());
                if(date.equals(todaysdate)){
                    HBox todoelement = new HBox();
                    CheckBox checkBox = new CheckBox();
                    Label todo = new Label(line.substring(line.lastIndexOf(';')));
                    todoelement.getChildren().addAll(checkBox,todo);
                    center.getChildren().add(todoelement);
                }
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        grid.setCenter(center);

        VBox left = new VBox(); //TODO IMPLEMENT
        Label test = new Label("test");
        left.getChildren().add(test);

        grid.setLeft(left);


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
                confirm.setOnAction(confirmAddTodo(todoDate,todoTags,todoText,stage));

                error= new Label();
                vbox.getChildren().addAll(hbox,todoText,confirm,error);

                stage.show();
            }
        };
    }
    public EventHandler<ActionEvent> confirmAddTodo(DatePicker date, TextField tags, TextArea todo, Stage stage) {

        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                System.out.println(""+date.getValue().toString()+" "+tags.getText()+" "+todo.getText());
                if(date == null || todo == null){
                    error.setText("Errore nell'inserimento della data o del testo"); //add window error instead
                }else{
                    //save element
                    try {
                        FileWriter myWriter = new FileWriter("todos.txt",true);
                        myWriter.write(""+date.getValue().toString()+ ";"+tags.getText()+";"+todo.getText()+"\r\n");
                        myWriter.close();

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                stage.close();
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