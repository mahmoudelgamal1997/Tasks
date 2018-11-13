package com.example2017.android.tasks;

/**
 * Created by M7moud on 09-Nov-18.
 */
public class ClientItem  {

String name,id,state,taskDetails,ProfileImage;

    public ClientItem() {
    }


    public ClientItem(String name, String profileImage, String taskDetails, String state, String id) {
        this.name = name;
        ProfileImage = profileImage;
        this.taskDetails = taskDetails;
        this.state = state;
        this.id = id;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
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
