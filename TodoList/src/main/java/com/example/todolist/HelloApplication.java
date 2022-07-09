package com.example.todolist;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HelloApplication extends Application
{
    Elements center;
    SideMenu sideMenu;
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

        MenuItem menuItem2 = new MenuItem("Rimuovi elemento");
        menuItem2.setOnAction(removeElement());
        menuItem2.setStyle("-fx-font-size: 20px");

        Menu mainmenu = new Menu(""); //create menu
        mainmenu.setGraphic(openView);    //set icon
        mainmenu.getItems().addAll(menuItem1,menuItem2);
        //mainmenu.setStyle("-fx-background-color: #fffdd0");

        MenuBar menuBar = new MenuBar();    //create menu bar
        menuBar.setMinHeight(40);
        menuBar.getMenus().add(mainmenu);
        //menuBar.setStyle("-fx-background-color: #fffdd0");

        VBox root = new VBox(menuBar);
        root.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        BorderPane grid = new BorderPane();
        root.getChildren().add(grid);

        Scene scene = new Scene(root, 800, 800);



        center = new Elements(false);
        grid.setCenter(center);

        sideMenu = new SideMenu(center,true);
        grid.setLeft(sideMenu);


        primaryStage.setTitle("TodoList");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * function called when the user press the remove element button in the main page menu
     * @return
     */
    private EventHandler<ActionEvent> removeElement() { // TODO: 03/07/2022 remove tag from tags.txt if the removed element was the last one with that tag 
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //VBox vbox = new VBox();
                BorderPane grid = new BorderPane();
                grid.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                Scene sceneRemoveTodo = new Scene(grid, 800,800);
                Stage stage = new Stage();
                stage.setScene(sceneRemoveTodo);

                Elements removeelement = new Elements(true);
                removeelement.getAllElements();
                SideMenu removesidemenu = new SideMenu(removeelement,false);

                grid.setLeft(removesidemenu);
                grid.setCenter(removeelement);
                
                stage.show();
            }
        };
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
                vbox.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
                Scene sceneAddTodo = new Scene(vbox, 800,800);
                Stage stage = new Stage();
                stage.setScene(sceneAddTodo);

                HBox hbox = new HBox();
                DatePicker todoDate = new DatePicker();
                Label labeltags = new Label("Tags:");
                TextField todoTags = new TextField();
                Label l = new Label("*");

                hbox.getChildren().addAll(todoDate,labeltags,todoTags,l);

                ComboBox urgency = new ComboBox();
                urgency.getItems().add("Vuoto");
                urgency.getItems().add("Molto urgente");
                urgency.getItems().add("Urgente");
                urgency.getItems().add("Poco urgente");
                urgency.getSelectionModel().selectFirst();

                TextArea todoText = new TextArea();
                todoText.setMaxHeight(200);
                todoText.setMaxWidth(400);

                Button confirm = new Button("Conferma");
                confirm.setOnAction(confirmAddTodo(todoDate,urgency,todoTags,todoText,stage));

                Label nb = new Label("* Inserisci i tag separati da una virgola (,)");

                error= new Label();
                vbox.getChildren().addAll(hbox,urgency,todoText,confirm,nb,error);

                stage.show();
            }
        };
    }

    /**
     * function called when the user want to add a todo
     * @param date datepicker from add todo stage
     * @param urgency combobox from add todo stage
     * @param tags textfield from add todo stage
     * @param todo textarea from add todo stage
     * @param stage stage to add a todo
     * @return
     */
    public EventHandler<ActionEvent> confirmAddTodo(DatePicker date, ComboBox urgency, TextField tags, TextArea todo, Stage stage) {

        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                if(date.getValue() == null || todo.getText() == null){
                    error.setText("Errore nell'inserimento della data o del testo"); //add window error instead
                }else{
                    //save element
                    try {
                        FileWriter myWriter = new FileWriter("todos.txt",true);
                        String line ="" + date.getValue().toString() + ";" + tags.getText() + ";"+ urgency.getValue() + ";" + todo.getText()+";"+false + "\r\n";
                        myWriter.write(line);
                        myWriter.close();
                        center.createElement(line);
                        if(tags.getText()!=""){ //update the list of all tags if there are someone new
                            String[] mytags = tags.getText().split(",");
                            //List<String> tagstoadd = new ArrayList<>();

                            myWriter = new FileWriter("tags.txt",true);
                            BufferedReader bufferreader = new BufferedReader(new FileReader("tags.txt"));
                            line="";

                            List<String> alltags = new ArrayList<>();
                            while ((line = bufferreader.readLine()) != null) {
                                alltags.add(line);
                            }
                            for(int i = 0; i<mytags.length;i++){
                                if(!alltags.contains(mytags[i]))
                                    //tagstoadd.add(mytags[i]);
                                    myWriter.write(mytags[i] + "\r\n");
                            }
                            bufferreader.close();
                        }
                        myWriter.close();
                    } catch (IOException ex) {
                        System.out.println(ex.toString());
                    }
                }
                sideMenu.refresh();
                SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
                String todaysdate = dt1.format(new Date());
                sideMenu.confirmVisualizeByDate(todaysdate);
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