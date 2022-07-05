package com.example.todolist;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SideMenu extends VBox {
    private Button visualizeToday;
    private DatePicker chooseDate;
    private Label labeldatepicker, labeltags, labelurgency;
    private ComboBox tags, urgency;

    Elements center;
    public SideMenu(Elements center){

        this.center = center;
        //this.setAlignment(Pos.CENTER_LEFT);
        this.setAlignment(Pos.TOP_LEFT);
        this.refresh();

    }

    public void refresh(){
        this.getChildren().clear();
        this.setPadding(new Insets(15, 0, 0, 0));
        visualizeToday = new Button("Visualizza le cose da fare per oggi");
        visualizeToday.setOnAction(visualizeTodayTodos());
        //visualizeToday.setPadding(new Insets(15, 0, 0, 0));    //top right bottom left
        this.getChildren().add(visualizeToday);

        labeldatepicker = new Label("Visualizza le cose da fare per una data specifica");
        chooseDate = new DatePicker();
        chooseDate.setOnAction(visualizeTodoByDate());
        VBox date = new VBox();
        date.setPadding(new Insets(15, 0, 0, 0));
        date.getChildren().addAll(labeldatepicker,chooseDate);
        this.getChildren().add(date);

        labeltags = new Label("Visualizza le cose da fare in base al tag");
        tags = new ComboBox();
        tags.setOnAction(visualizeTodosByTag());

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
        tags.getSelectionModel().selectFirst();
        VBox mytags = new VBox();
        mytags.setPadding(new Insets(15, 0, 0, 0));
        mytags.getChildren().addAll(labeltags,tags);
        this.getChildren().add(mytags);

        labelurgency = new Label("Visualizza le cose da fare in base all'urgenza");
        urgency = new ComboBox();
        urgency.getItems().add("Molto urgente");
        urgency.getItems().add("Urgente");
        urgency.getItems().add("Poco urgente");
        urgency.getSelectionModel().selectFirst();
        urgency.setOnAction(visualizeTodosByUrgency());
        VBox myugency = new VBox();
        myugency.setPadding(new Insets(15, 0, 0, 0));
        myugency.getChildren().addAll(labelurgency,urgency);
        this.getChildren().add(myugency);
    }

    private EventHandler<ActionEvent> visualizeTodosByUrgency() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String selectedurgency = urgency.getValue().toString();
                center.removeAllElements();
                try {
                    BufferedReader bufferreader = new BufferedReader(new FileReader("todos.txt"));
                    String line;
                    while ((line = bufferreader.readLine()) != null) {
                        String[] info = line.split(";");
                        if(info[2].equals(selectedurgency)){
                            Element element = new Element(line,center.getShowDeleteButton(),center);
                            center.addElement(element);
                        }
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

    private EventHandler<ActionEvent> visualizeTodosByTag() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                String tag = tags.getValue().toString();
                center.removeAllElements();
                try {
                    BufferedReader bufferreader = new BufferedReader(new FileReader("todos.txt"));
                    String line;
                    while ((line = bufferreader.readLine()) != null) {
                        String[] info = line.split(";");
                        String[] infotags = info[1].split(",");
                        for(int i=0;i<infotags.length;i++) {
                            if (infotags[i].equals(tag)) {
                                Element element = new Element(line, center.getShowDeleteButton(),center);
                                center.addElement(element);
                            }
                        }
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

    private EventHandler<ActionEvent> visualizeTodoByDate() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                confirmVisualizeByDate(chooseDate.getValue().toString());
            }
        };
    }

    private EventHandler<ActionEvent> visualizeTodayTodos() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd");
                String todaysdate = dt1.format(new Date());
                confirmVisualizeByDate(todaysdate);
            }
        };
    }
    public void confirmVisualizeByDate(String date){
        center.removeAllElements();
        try {
            BufferedReader bufferreader = new BufferedReader(new FileReader("todos.txt"));
            String line;
            while ((line = bufferreader.readLine()) != null) {
                String[] info = line.split(";");
                if(info[0].equals(date)){
                    Element element = new Element(line,center.getShowDeleteButton(),center);
                    center.addElement(element);
                }
            }
            bufferreader.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
