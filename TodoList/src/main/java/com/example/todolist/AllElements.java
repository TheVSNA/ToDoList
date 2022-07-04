package com.example.todolist;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AllElements {
    private ArrayList<Element> elements;

    public  AllElements(){
        elements = new ArrayList<>();
        refresh();
    }
    public void addElement(Element e){
        elements.add(e);
    }
    public void deleteElement(Element e){
        elements.remove(e);
    }
    public ArrayList<Element> getAllELement(){
        return elements;
    }

    public ArrayList<Element> filterByDate(String date){
        ArrayList<Element> filteredElements = new ArrayList<>();
        for(int i=0;i<elements.size();i++){
            if(elements.get(i).getDate().equals(date)){
                filteredElements.add(elements.get(i));
            }
        }
        return filteredElements;
    }
    public ArrayList<Element> filterByTag(String tag){
        ArrayList<Element> filteredElements = new ArrayList<>();
        for(int i=0;i<elements.size();i++){
            if(elements.get(i).containTag(tag)){
                filteredElements.add(elements.get(i));
            }
        }
        return filteredElements;
    }

    public ArrayList<Element> filterByUrgency(String urgency){
        ArrayList<Element> filteredElements = new ArrayList<>();
        for(int i=0;i<elements.size();i++){
            if(elements.get(i).getUrgency().equals(urgency)){
                filteredElements.add(elements.get(i));
            }
        }
        return filteredElements;
    }

    public void refresh(){
        elements = new ArrayList<>();
        try {
            BufferedReader bufferreader = new BufferedReader(new FileReader("todos.txt"));
            String line;
            while((line = bufferreader.readLine())!=null){
                addElement(new Element(line,false));
            }
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
