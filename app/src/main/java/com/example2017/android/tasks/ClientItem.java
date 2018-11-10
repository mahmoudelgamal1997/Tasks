package com.example2017.android.tasks;

/**
 * Created by M7moud on 09-Nov-18.
 */
public class ClientItem  {

String name,id,state,taskDetails;

    public ClientItem() {
    }

    public ClientItem(String name, String id, String state, String taskDetails) {
        this.name = name;
        this.id = id;
        this.state = state;
        this.taskDetails = taskDetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTaskDetails() {
        return taskDetails;
    }

    public void setTaskDetails(String taskDetails) {
        this.taskDetails = taskDetails;
    }
}
