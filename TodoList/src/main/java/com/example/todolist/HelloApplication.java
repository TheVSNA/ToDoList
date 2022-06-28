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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            int cont=0;
            while ((line = bufferreader.readLine()) != null) {
                String date = line.substring(0,line.indexOf(';'));
                System.out.println(date.toString());
                if(date.equals(todaysdate)){
                    HBox todoelement = new HBox();
                    CheckBox checkBox = new CheckBox();
                    Label todo = new Label(line.substring(line.lastIndexOf(';')));
                    todoelement.getChildren().addAll(checkBox,todo);
                    center.getChildren().add(todoelement);
                    cont++;
                }
            }
            if(cont==0){
                Label nothing = new Label("Nessun elemento per questa giornata!");
                Button add = new Button("Aggiunti un elemento");
                add.setOnAction(addTodo());
                HBox nothingtodo = new HBox();
                nothingtodo.getChildren().addAll(nothing,add);
            }

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        grid.setCenter(center);

        VBox left = new VBox();

        Button today = new Button("Visualizza le cosa da fare per oggi");
        today.setOnAction(visualizeTodosByDate(dt1.format(new Date()).toString()));
        left.getChildren().add(today);

        DatePicker choosedate = new DatePicker();
        choosedate.setOnAction(visualizeTodosByDate(choosedate.getValue().toString()));   //verifica se funziona
        left.getChildren().add(choosedate);

        ComboBox tags = new ComboBox();
        tags.setOnAction(visualizeTodosByTag());    //verifica se funziona
        try {
            BufferedReader bufferreader = new BufferedReader(new FileReader("tags.txt"));
            String line;
            while((line = bufferreader.readLine()) !=null){
                tags.getItems().add(line);
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        left.getChildren().add(tags);

        //TODO aggiungi filtro per molto urgente, urgente, poco urgente
        grid.setLeft(left);


        primaryStage.setTitle("TodoList");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private EventHandler<ActionEvent> visualizeTodosByTag() {
        return new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

            }
        };
    }


    private EventHandler<ActionEvent> visualizeTodosByDate(String date) {
        return new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {

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
                Scene sceneAddTodo = new Scene(vbox, 800,800);
                Stage stage = new Stage();
                stage.setScene(sceneAddTodo);

                HBox hbox = new HBox();
                DatePicker todoDate = new DatePicker();
                Label labeltags = new Label("Tags:");
                TextField todoTags = new TextField();
                Label l = new Label("*");

                //TODO aggiungi possibilità di assegnare valore di urgenza al todo

                hbox.getChildren().addAll(todoDate,labeltags,todoTags,l);

                TextArea todoText = new TextArea();
                todoText.setMaxHeight(200);
                todoText.setMaxWidth(400);

                Button confirm = new Button("Conferma");
                confirm.setOnAction(confirmAddTodo(todoDate,todoTags,todoText,stage));

                Label nb = new Label("* Inserisci i tag separati da un ;");

                error= new Label();
                vbox.getChildren().addAll(hbox,todoText,confirm,nb,error);

                stage.show();
            }
        };
    }
    public EventHandler<ActionEvent> confirmAddTodo(DatePicker date, TextField tags, TextArea todo, Stage stage) {

        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                //System.out.println(""+date.getValue().toString()+" "+tags.getText()+" "+todo.getText());
                if(date.getValue() == null || todo.getText() == null){
                    error.setText("Errore nell'inserimento della data o del testo"); //add window error instead
                }else{
                    //save element
                    try {
                        FileWriter myWriter = new FileWriter("todos.txt",true);
                        myWriter.write("" + date.getValue().toString() + ";" + tags.getText() + ";" + todo.getText() + "\r\n");

                        if(tags.getText()!=""){ //update the list of all tags if there are someone new
                            String[] mytags = tags.getText().split(";");
                            //List<String> tagstoadd = new ArrayList<>();

                            myWriter = new FileWriter("tags.txt",true);
                            BufferedReader bufferreader = new BufferedReader(new FileReader("tags.txt"));
                            String line;

                            List<String> alltags = new ArrayList<>();
                            while ((line = bufferreader.readLine()) != null) {
                                alltags.add(line);
                            }
                            for(int i = 0; i<mytags.length;i++){
                                if(!alltags.contains(mytags[i]))
                                    //tagstoadd.add(mytags[i]);
                                    myWriter.write(mytags[i] + "\r\n");
                            }
                        }
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