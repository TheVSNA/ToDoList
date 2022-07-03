package com.example.todolist;

import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

import java.util.ArrayList;

/**
 * a simple class that extends VBox to represent a list of todo elements
 */
public class Elements extends VBox {
    private ArrayList<Element> elements;

    public Elements(){
        elements = new ArrayList<>();
        this.setAlignment(Pos.CENTER);
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
     * @param show_delete_button boolean set to true if you want to add the remove button
     */
    public void createElement(String line, boolean show_delete_button){
        elements.add(new Element(line,show_delete_button));
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


}
