package com.example.todolist;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * a simple class that extends VBox to represent a list of todo elements
 */
public class Elements extends VBox {
    private ArrayList<Element> elements;
    private boolean show_delete_button;

    public Elements(boolean show_delete_button){
        elements = new ArrayList<>();
        this.setAlignment(Pos.TOP_CENTER);
        this.show_delete_button = show_delete_button;
    }

    public void setShowDeleteButton(boolean b){
        show_delete_button=b;
    }
    public boolean getShowDeleteButton(){
        return show_delete_button;
    }

    public void refresh(){
        this.getChildren().clear();
        this.getChildren().addAll(elements);
    }

    /**
     * add 1 element to the list
     * @param e
     */
    public void addElement(Element e){
        elements.add(e);
        refresh();
    }

    /**
     * create a new element from a string
     * @param line String taken from a file that represent a element
     */
    public void createElement(String line){
        elements.add(new Element(line,show_delete_button,this));
        refresh();
    }

    /**
     * add a list of elements to the list
     * @param e
     */
    public void addElements(ArrayList<Element> e){
        elements.addAll(e);
        refresh();
    }

    /**
     * return all elements
     * @return
     */
    public ArrayList<Element> getElements() {
        return elements;
    }

    /**
     * delete a specific element if present
     * @param e
     */
    public void deleteElement(Element e){
        elements.remove(e);
        refresh();
    }

    /**
     * clear the list of elements
     */
    public void removeAllElements(){
        elements = new ArrayList<>();
        refresh();
    }

    public void getAllElements(){
        try {
            BufferedReader bufferreader = new BufferedReader(new FileReader("todos.txt"));
            String line;
            while((line = bufferreader.readLine()) !=null){
                elements.add(new Element(line,show_delete_button,this));
            }
            bufferreader.close();
        } catch (Exception ex){
            System.out.println(ex.toString());
        }
        refresh();
    }

    public void showError(String s) {
        Label l = new Label(s);
        this.getChildren().clear();
        this.getChildren().add(l);
    }
}
