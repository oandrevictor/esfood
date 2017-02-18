package com.example.unifood.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class University {
    private String id;
    private String name;
    private List<String> allCampus;

    public University() {
        allCampus = new ArrayList<String>();
    }

    public University(String name) {
        this();
        this.name = name;

    }

    public boolean addCampus(String campus){

        if (campus!= null) {
            return allCampus.add(campus);
        }
        return false;
    }

    public String getId(){return id;}

    public void setId(String id1){this.id =id1;}

    public String getName(){return name;}

    public void setName(String name1){
        this.name = name1;
    }

    public List<String> getAllCampus(){return allCampus;}

    public  void setAllCampus(List<String> allCampus1){
        allCampus=allCampus1;
    }

}
