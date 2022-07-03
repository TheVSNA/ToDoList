package com.example.todolist;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HelloApplication extends Application
{
    VBox center;
    VBox left;
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
        BorderPane grid = new BorderPane();
        root.getChildren().add(grid);

        Scene scene = new Scene(root, 800, 800);

        center = new VBox();

        visualizeToday();

        grid.setCenter(center);

        left = new VBox();

        visualizeMenu();

        grid.setLeft(left);


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
                VBox vbox = new VBox();
                Scene sceneRemoveTodo = new Scene(vbox, 800,800);
                Stage stage = new Stage();
                stage.setScene(sceneRemoveTodo);

                VBox vboxinside = new VBox();


                MenuItem menuItem1 = new MenuItem("Filtra per data");   //create submenu
                menuItem1.setOnAction(filterByDate(vboxinside,stage));
                menuItem1.setStyle("-fx-font-size: 20px");   //-fx-background-color: #fffdd0

                MenuItem menuItem2 = new MenuItem("Filtra per urgenza");
                menuItem2.setOnAction(filterByUrgency(vboxinside,stage));
                menuItem2.setStyle("-fx-font-size: 20px");

                MenuItem menuItem3 = new MenuItem("Filtra per tag");
                menuItem3.setOnAction(filterByTag(vboxinside,stage));
                menuItem3.setStyle("-fx-font-size: 20px");
                
                Menu mainmenu = new Menu("Filtra"); //create menu
                mainmenu.setStyle("-fx-font-size: 20px");
                mainmenu.getItems().addAll(menuItem1,menuItem2,menuItem3);
                //mainmenu.setStyle("-fx-background-color: #fffdd0");

                MenuBar menuBar = new MenuBar();    //create menu bar
                menuBar.setMinHeight(40);
                menuBar.getMenus().add(mainmenu);
                
                vbox.getChildren().add(menuBar);
                vbox.getChildren().add(vboxinside);

                visualizeAllTodos(vboxinside,stage);
                
                stage.show();
            }
        };
    }

    /**
     * show a window to select a tag to filter all the todos
     * @param vbox VBox in which insert all the elements
     * @param stage Stage to close once the element is deleted
     * @return
     */
    private EventHandler<ActionEvent> filterByTag(VBox vbox, Stage stage) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                VBox tagvbox = new VBox();
                Scene sceneRemoveTodo = new Scene(tagvbox, 800,800);
                Stage tagstage = new Stage();
                tagstage.setScene(sceneRemoveTodo);

                HBox hBox = new HBox();
                Label l = new Label("Scegli il tag");
                ComboBox combo = new ComboBox();
                combo.setOnAction(confirmFilterByTag(vbox,stage,tagstage,combo));
                try {
                    BufferedReader bufferreader = new BufferedReader(new FileReader("tags.txt"));
                    String line;
                    while((line = bufferreader.readLine()) !=null){
                        combo.getItems().add(line);
                    }
                    bufferreader.close();
                } catch (Exception ex){
                    System.out.println(e.toString());
                }
                hBox.getChildren().addAll(l,combo);
                tagvbox.getChildren().add(hBox);
                tagstage.show();
            }
        };
    }

    /**
     * function called when the user select a tag in the filterByTag function
     * @param vbox
     * @param stage
     * @param tagstage
     * @param combo
     * @return
     */
    private EventHandler<ActionEvent> confirmFilterByTag(VBox vbox, Stage stage, Stage tagstage, ComboBox combo) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                tagstage.close();   //close the stage to select the tag
                vbox.getChildren().clear(); //remove all elements from the VBox so that new filtered items can be inserted
                String todotag = combo.getValue().toString();
                try {
                    BufferedReader bufferreader = new BufferedReader(new FileReader("todos.txt"));
                    String line;
                    int cont=0;
                    Label visualizetag = new Label("Todo per il tag "+todotag);
                    vbox.getChildren().add(visualizetag);
                    while ((line = bufferreader.readLine()) != null) {
                        String[] info = line.split(";");
                        String[] infotags = info[1].split(",");
                        for(int i=0;i<infotags.length;i++){
                            if(infotags[i].equals(todotag)){
                                HBox todoelement = new HBox();
                                CheckBox checkBox = new CheckBox();

                                String urgency = info[2];

                                Button delete = new Button("Elimina");
                                delete.setOnAction(confirmRemoveElement(line,stage));

                                Label todo = new Label(info[3]);
                                Label date = new Label(info[0]);

                                if(urgency.equals("Molto urgente")){
                                    todo.setStyle("-fx-text-fill:red");
                                    date.setStyle("-fx-text-fill:red");
                                }else if(urgency.equals("Urgente")){
                                    todo.setStyle("-fx-text-fill:orange");
                                    date.setStyle("-fx-text-fill:orange");
                                }else if(urgency.equals("Poco urgente")){
                                    todo.setStyle("-fx-text-fill:yellow");
                                    date.setStyle("-fx-text-fill:yellow");
                                }
                                todoelement.getChildren().addAll(checkBox,todo,date,delete);
                                vbox.getChildren().add(todoelement);
                                cont++;
                            }
                        }
                    }
                    if(cont==0){
                        Label nothing = new Label("Nessun elemento per questa giornata!");
                        Button add = new Button("Aggiunti un elemento");
                        add.setOnAction(addTodo());
                        HBox nothingtodo = new HBox();
                        nothingtodo.getChildren().addAll(nothing,add);
                        vbox.getChildren().add(nothingtodo);
                    }
                    bufferreader.close();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

    /**
     * show a window to select a urgency level to filter all the todos
     * @param vbox VBox in which insert all the elements
     * @param stage Stage to close once the element is deleted
     * @return
     */
    private EventHandler<ActionEvent> filterByUrgency(VBox vbox, Stage stage) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                VBox urgencybox = new VBox();
                Scene sceneRemoveTodo = new Scene(urgencybox, 800,800);
                Stage urgencystage = new Stage();
                urgencystage.setScene(sceneRemoveTodo);

                HBox hBox = new HBox();
                Label l = new Label("Scegli il tag");
                ComboBox combo = new ComboBox();
                combo.setOnAction(confirmFilterByUrgency(vbox,stage,urgencystage,combo));
                combo.getItems().add("Molto urgente");
                combo.getItems().add("Urgente");
                combo.getItems().add("Poco urgente");

                hBox.getChildren().addAll(l,combo);
                urgencybox.getChildren().add(hBox);
                urgencystage.show();
            }
        };
    }

    /**
     * function called when the user select a urgency level in the filterByUrgency function
     * @param vbox
     * @param stage
     * @param urgencystage
     * @param combo
     * @return
     */
    private EventHandler<ActionEvent> confirmFilterByUrgency(VBox vbox, Stage stage, Stage urgencystage, ComboBox combo) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                urgencystage.close();   //close the stage to select the tag
                vbox.getChildren().clear(); //remove all elements from the VBox so that new filtered items can be inserted
                String todourgency = combo.getValue().toString();
                try {
                    BufferedReader bufferreader = new BufferedReader(new FileReader("todos.txt"));
                    String line;
                    int cont=0;
                    Label visualizetag = new Label("Todo per l'urgenza "+todourgency);
                    vbox.getChildren().add(visualizetag);
                    while ((line = bufferreader.readLine()) != null) {
                        String[] info = line.split(";");
                        String urgency = info[2];
                        if(urgency.equals(todourgency)){
                            HBox todoelement = new HBox();
                            CheckBox checkBox = new CheckBox();

                            Button delete = new Button("Elimina");
                            delete.setOnAction(confirmRemoveElement(line,stage));

                            Label todo = new Label(info[3]);
                            Label date = new Label(info[0]);

                            if(urgency.equals("Molto urgente")){
                                todo.setStyle("-fx-text-fill:red");
                                date.setStyle("-fx-text-fill:red");
                            }else if(urgency.equals("Urgente")){
                                todo.setStyle("-fx-text-fill:orange");
                                date.setStyle("-fx-text-fill:orange");
                            }else if(urgency.equals("Poco urgente")){
                                todo.setStyle("-fx-text-fill:yellow");
                                date.setStyle("-fx-text-fill:yellow");
                            }
                            todoelement.getChildren().addAll(checkBox,todo,date,delete);
                            vbox.getChildren().add(todoelement);
                            cont++;
                        }
                    }
                    if(cont==0){
                        Label nothing = new Label("Nessun elemento per questa giornata!");
                        Button add = new Button("Aggiunti un elemento");
                        add.setOnAction(addTodo());
                        HBox nothingtodo = new HBox();
                        nothingtodo.getChildren().addAll(nothing,add);
                        vbox.getChildren().add(nothingtodo);
                    }
                    bufferreader.close();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

    /**
     * show a window to select a date to filter all the todos
     * @param vbox VBox in which insert all the elements
     * @param stage Stage to close once the element is deleted
     * @return
     */
    private EventHandler<ActionEvent> filterByDate(VBox vbox, Stage stage) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                VBox datebox = new VBox();
                Scene sceneRemoveTodo = new Scene(datebox, 800,800);
                Stage datestage = new Stage();
                datestage.setScene(sceneRemoveTodo);

                HBox hBox = new HBox();
                Label l = new Label("Scegli la data");
                DatePicker datePicker = new DatePicker();
                datePicker.setOnAction(confirmFilterByDate(vbox,stage,datestage,datePicker));

                hBox.getChildren().addAll(l,datePicker);
                datebox.getChildren().add(hBox);
                datestage.show();
            }
        };
    }

    /**
     * function called when the user select a date in the filterByDate function
     * @param vbox
     * @param stage
     * @param datestage
     * @param datePicker
     * @return
     */
    private EventHandler<ActionEvent> confirmFilterByDate(VBox vbox, Stage stage, Stage datestage, DatePicker datePicker) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                datestage.close();   //close the stage to select the tag
                vbox.getChildren().clear(); //remove all elements from the VBox so that new filtered items can be inserted
                String todourgency = datePicker.getValue().toString();
                try {
                    BufferedReader bufferreader = new BufferedReader(new FileReader("todos.txt"));
                    String line;
                    int cont=0;
                    Label visualizetag = new Label("Todo per l'urgenza "+todourgency);
                    vbox.getChildren().add(visualizetag);
                    while ((line = bufferreader.readLine()) != null) {
                        String[] info = line.split(";");
                        String urgency = info[2];
                        if(info[0].equals(todourgency)){
                            HBox todoelement = new HBox();
                            CheckBox checkBox = new CheckBox();

                            Button delete = new Button("Elimina");
                            delete.setOnAction(confirmRemoveElement(line,stage));

                            Label todo = new Label(info[3]);
                            Label date = new Label(info[0]);

                            if(urgency.equals("Molto urgente")){
                                todo.setStyle("-fx-text-fill:red");
                                date.setStyle("-fx-text-fill:red");
                            }else if(urgency.equals("Urgente")){
                                todo.setStyle("-fx-text-fill:orange");
                                date.setStyle("-fx-text-fill:orange");
                            }else if(urgency.equals("Poco urgente")){
                                todo.setStyle("-fx-text-fill:yellow");
                                date.setStyle("-fx-text-fill:yellow");
                            }
                            todoelement.getChildren().addAll(checkBox,todo,date,delete);
                            vbox.getChildren().add(todoelement);
                            cont++;
                        }
                    }
                    if(cont==0){
                        Label nothing = new Label("Nessun elemento per questa giornata!");
                        Button add = new Button("Aggiunti un elemento");
                        add.setOnAction(addTodo());
                        HBox nothingtodo = new HBox();
                        nothingtodo.getChildren().addAll(nothing,add);
                        vbox.getChildren().add(nothingtodo);
                    }
                    bufferreader.close();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

    /**
     * visualize all the todos with a button to remove a specific element
     * @param vbox VBox in which the elements are added
     * @param stage Stage to close once the element is deleted
     */
    private void visualizeAllTodos(VBox vbox,Stage stage){
        vbox.getChildren().clear();
        try {
            BufferedReader bufferreader = new BufferedReader(new FileReader("todos.txt"));
            String line;
            while ((line = bufferreader.readLine()) != null) {
                String[] info = line.split(";");
                HBox item = new HBox();
                item.getChildren().addAll(new Label(info[3]),new Label(info[0]));
                Button remove = new Button("Rimuovi");
                remove.setOnAction(confirmRemoveElement(line,stage));
                item.getChildren().add(remove);
                vbox.getChildren().add(item);
            }
            bufferreader.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * function called when the user press a remove button inside the visualizeAllTodos function
     * @param lineToRemove todo to remove
     * @param stage Stage to close
     * @return
     */
    private EventHandler<ActionEvent> confirmRemoveElement(String lineToRemove,Stage stage) {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                BufferedReader reader = null;
                BufferedWriter writer = null;
                try {
                    File inputFile = new File("todos.txt");
                    File tempFile = new File("temptodos.txt");
                    reader = new BufferedReader(new FileReader(inputFile));
                    writer = new BufferedWriter(new FileWriter(tempFile));

                    String currentLine;

                    while((currentLine = reader.readLine()) != null) {
                        // trim newline when comparing with lineToRemove
                        String trimmedLine = currentLine.trim();
                        if(!trimmedLine.equals(lineToRemove))
                            writer.write(currentLine + System.getProperty("line.separator"));
                    }
                    writer.close();
                    reader.close();
                    inputFile.delete();
                    tempFile.renameTo(inputFile);

                    visualizeToday();
                    stage.close();

                } catch (FileNotFoundException ex) {
                    throw new RuntimeException(ex);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            }
        };
    }


    /**
     * function to visualize left menu
     */
    private void visualizeMenu() {
        left.getChildren().clear();
        Button today = new Button("Visualizza le cosa da fare per oggi");
        today.setOnAction(visualizeTodayTodos());
        left.getChildren().add(today);

        DatePicker choosedate = new DatePicker();
        choosedate.setOnAction(visualizeTodosByDate(choosedate));   //verifica se funziona
        left.getChildren().add(choosedate);

        ComboBox tags = new ComboBox();
        tags.getItems().add("Visualizza per tag");
        tags.getSelectionModel().selectFirst();
        tags.setOnAction(visualizeTodosByTag(tags));
        try {
            BufferedReader bufferreader = new BufferedReader(new FileReader("tags.txt"));
            String line;
            while((line = bufferreader.readLine()) !=null){
                tags.getItems().add(line);
            }
            bufferreader.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
        } catch (IOException e) {
            System.out.println(e.toString());
        }
        left.getChildren().add(tags);

        ComboBox urgency = new ComboBox();
        urgency.getItems().add("Visualizza per livello di urgenza");
        urgency.getItems().add("Molto urgente");
        urgency.getItems().add("Urgente");
        urgency.getItems().add("Poco urgente");
        urgency.getSelectionModel().selectFirst();
        urgency.setOnAction(visualizeTodosByUrgency(urgency));

        left.getChildren().add(urgency);
    }

    /**
     * visualize all the todos filtered by urgency level
     * @param urgency level of urgency
     * @return
     */
    private EventHandler<ActionEvent> visualizeTodosByUrgency(ComboBox urgency) {
        return new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String todourgency = urgency.getValue().toString();
                center.getChildren().clear();
                Label visualizeUrgency = new Label("Visualizza todo per urgenza "+todourgency);
                center.getChildren().add(visualizeUrgency);


                try {
                    BufferedReader bufferreader = new BufferedReader(new FileReader("todos.txt"));
                    String line;
                    int cont=0;
                    while ((line = bufferreader.readLine()) != null) {
                        String[] info = line.split(";");
                        String urgency = info[2];
                        if(urgency.equals(todourgency)){
                            HBox todoelement = new HBox();
                            CheckBox checkBox = new CheckBox();
                            Label todo = new Label(info[3]);

                            if(urgency.equals("Molto urgente")){
                                todo.setStyle("-fx-text-fill:red");

                            }else if(urgency.equals("Urgente")){
                                todo.setStyle("-fx-text-fill:orange");

                            }else if(urgency.equals("Poco urgente")){
                                todo.setStyle("-fx-text-fill:yellow");

                            }

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
                        center.getChildren().add(nothingtodo);
                    }
                bufferreader.close();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
        };
    }

    /**
     * visualize today's todo when the button is pressed
     * @return
     */
    private EventHandler<ActionEvent> visualizeTodayTodos() {
        return new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                visualizeToday();
            }
        };
    }

    /**
     * visualize today's todo on page load
     */
    public void visualizeToday(){
        center.getChildren().clear();
        SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
        String todaysdate = dt1.format(new Date());
        try {
            BufferedReader bufferreader = new BufferedReader(new FileReader("todos.txt"));
            String line;
            int cont=0;
            Label today = new Label("Todo list per "+todaysdate);
            center.getChildren().add(today);
            while ((line = bufferreader.readLine()) != null) {
                String[] info = line.split(";");
                String urgency = info[2];
                if(info[0].equals(todaysdate)){
                    HBox todoelement = new HBox();
                    CheckBox checkBox = new CheckBox();
                    Label todo = new Label(line.substring(line.lastIndexOf(';')+1));

                    if(urgency.equals("Molto urgente")){
                        todo.setStyle("-fx-text-fill:red");

                    }else if(urgency.equals("Urgente")){
                        todo.setStyle("-fx-text-fill:orange");

                    }else if(urgency.equals("Poco urgente")){
                        todo.setStyle("-fx-text-fill:yellow");

                    }


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
                center.getChildren().add(nothingtodo);
            }
        bufferreader.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * visualize all todos with a specific tag
     * @param tags combobox inside left menu
     * @return
     */
    private EventHandler<ActionEvent> visualizeTodosByTag(ComboBox tags) {
        return new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                String todotag = tags.getValue().toString();
                center.getChildren().clear();
                try {
                    BufferedReader bufferreader = new BufferedReader(new FileReader("todos.txt"));
                    String line;
                    int cont=0;
                    Label visualizetag = new Label("Todo per il tag "+todotag);
                    center.getChildren().add(visualizetag);
                    while ((line = bufferreader.readLine()) != null) {
                        String[] info = line.split(";");
                        String[] infotags = info[1].split(",");
                        for(int i=0;i<infotags.length;i++){
                            if(infotags[i].equals(todotag)){
                                HBox todoelement = new HBox();
                                CheckBox checkBox = new CheckBox();

                                String urgency = info[2];


                                Label todo = new Label(info[3]);
                                Label date = new Label(info[0]);

                                if(urgency.equals("Molto urgente")){
                                    todo.setStyle("-fx-text-fill:red");
                                    date.setStyle("-fx-text-fill:red");
                                }else if(urgency.equals("Urgente")){
                                    todo.setStyle("-fx-text-fill:orange");
                                    date.setStyle("-fx-text-fill:orange");
                                }else if(urgency.equals("Poco urgente")){
                                    todo.setStyle("-fx-text-fill:yellow");
                                    date.setStyle("-fx-text-fill:yellow");
                                }
                                todoelement.getChildren().addAll(checkBox,todo,date);
                                center.getChildren().add(todoelement);
                                cont++;
                            }
                        }
                    }
                    if(cont==0){
                        Label nothing = new Label("Nessun elemento per questa giornata!");
                        Button add = new Button("Aggiunti un elemento");
                        add.setOnAction(addTodo());
                        HBox nothingtodo = new HBox();
                        nothingtodo.getChildren().addAll(nothing,add);
                        center.getChildren().add(nothingtodo);
                    }
                bufferreader.close();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        };
    }

    /**
     * visualize all the todos for a specific date
     * @param date datepicker inside left menu
     * @return
     */
    private EventHandler<ActionEvent> visualizeTodosByDate(DatePicker date) {   //TODO merge with visualizeToday
        return new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                center.getChildren().clear();
                String tododate = date.getValue().toString();
                try {
                    BufferedReader bufferreader = new BufferedReader(new FileReader("todos.txt"));
                    String line;
                    int cont=0;
                    Label today = new Label("Todo list per "+tododate);
                    center.getChildren().add(today);
                    while ((line = bufferreader.readLine()) != null) {
                        String[] info = line.split(";");
                        String urgency = info[2];
                        if(info[0].equals(tododate)){
                            HBox todoelement = new HBox();
                            CheckBox checkBox = new CheckBox();
                            Label todo = new Label(line.substring(line.lastIndexOf(';')+1));

                            if(urgency.equals("Molto urgente")){
                                todo.setStyle("-fx-text-fill:red");

                            }else if(urgency.equals("Urgente")){
                                todo.setStyle("-fx-text-fill:orange");

                            }else if(urgency.equals("Poco urgente")){
                                todo.setStyle("-fx-text-fill:yellow");

                            }


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
                        center.getChildren().add(nothingtodo);
                    }
                bufferreader.close();
                } catch (FileNotFoundException ex) {
                    ex.printStackTrace();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
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
                //System.out.println(""+date.getValue().toString()+" "+tags.getText()+" "+todo.getText());
                if(date.getValue() == null || todo.getText() == null){
                    error.setText("Errore nell'inserimento della data o del testo"); //add window error instead
                }else{
                    //save element
                    try {
                        FileWriter myWriter = new FileWriter("todos.txt",true);
                        myWriter.write("" + date.getValue().toString() + ";" + tags.getText() + ";"+ urgency.getValue() + ";" + todo.getText() + "\r\n");
                        myWriter.close();
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
                            bufferreader.close();
                        }
                        myWriter.close();
                    } catch (IOException ex) {
                        System.out.println(ex.toString());
                    }
                }
                visualizeToday();
                visualizeMenu();
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