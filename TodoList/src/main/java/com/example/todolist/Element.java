package com.example.todolist;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

/**
 * a simple class that extends HBox to represent one element of a todolist
 */
public class Element extends HBox {
    private CheckBox checkBox;
    private Label todotext,date;
    private Button delete;
    private String urgency;
    private String info[];
    private ArrayList<String> tags;

    public Element(String line, boolean show_delete_button){
        info=line.split(";");
        urgency = info[2];  // TODO: 03/07/2022 change urgency system to ints
        checkBox = new CheckBox();
        checkBox.setPadding(new Insets(10, 10, 0, 0));    //top right bottom left

        todotext=new Label(info[3]);
        todotext.setPadding(new Insets(10,10,0,0));
        date = new Label(info[0]);
        date.setPadding(new Insets(10,10,0,0));

        if(urgency.equals("Molto urgente")){
            todotext.setStyle("-fx-text-fill:red");
            date.setStyle("-fx-text-fill:red");
        }else if(urgency.equals("Urgente")){
            todotext.setStyle("-fx-text-fill:orange");
            date.setStyle("-fx-text-fill:orange");
        }else if(urgency.equals("Poco urgente")){
            todotext.setStyle("-fx-text-fill:yellow");
            date.setStyle("-fx-text-fill:yellow");
        }
        tags= new ArrayList<>();
        Collections.addAll(tags,info[1].split(","));
        this.getChildren().addAll(checkBox,todotext,date);

        if(show_delete_button){
            delete = new Button("Rimuovi");
            delete.setOnAction(deleteElement());
            delete.setPadding(new Insets(10,10,0,0));
            this.getChildren().add(delete);
        }

        this.setAlignment(Pos.CENTER);
    }

    /**
     * getters and setters
     */
    public String getDate() {
        return date.getText();
    }
    public void setDate(String newdate){
        date.setText(newdate);
    }

    public String getTodoText() {
        return todotext.getText();
    }
    public void setTodotext(String newtext){
        todotext.setText(newtext);
    }

    public String getUrgency() {
        return urgency;
    }
    public void setUrgency(String newurgency){
        urgency = newurgency;
    }

    public ArrayList<String> getTags(){
        return tags;
    }

    /**
     * add 1 tag to the list of tags
     * @param newtag
     */
    public void addTag(String newtag){
        tags.add(newtag);
    }

    /**
     * remove 1 tag from the list if present
     * @param tag
     */
    public void removeTag(String tag){
        tags.remove(tag);
    }

    /**
     * verify if the tag list contains a specific tag
     * @param tag
     * @return
     */
    public boolean containTag(String tag){
        return tags.contains(tag);
    }

    @Override
    public String toString(){
        return "Text: "+todotext+" Date: "+date + " Urgency: " + urgency + " Tags: "+tags.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null)
            return false;
        if(!(obj instanceof Element))
            return false;
        return ((Element) obj).toString().equals(this.toString());
    }

    /**
     * function to execute if the remove button is pressed
     * this function delete the entry relative to this element from the file
     * @return
     */
    private EventHandler<ActionEvent> deleteElement() { // TODO: 03/07/2022 remove tag from tags.txt if the removed element was the last one with that tag
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
                        String lineToRemove = getDate()+";"+getTags().toString()+";"+getUrgency()+";"+getTodoText();
                        if(!trimmedLine.equals(lineToRemove))
                            writer.write(currentLine + System.getProperty("line.separator"));
                    }
                    writer.close();
                    reader.close();
                    inputFile.delete();
                    tempFile.renameTo(inputFile);

                } catch (FileNotFoundException ex) {
                    System.out.println(ex);
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
        };
    }
}
