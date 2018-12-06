package com.example2017.android.tasks.Admin;

/**
 * Created by M7moud on 04-Dec-18.
 */
public class CustomArrayList
{
    String Id,name,ProfileImage;

    public CustomArrayList() {
    }

    public CustomArrayList(String id, String name, String profileImage) {
        Id = id;
        this.name = name;
        ProfileImage = profileImage;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProfileImage(String profileImage) {
        ProfileImage = profileImage;
    }

    public String getId() {
        return Id;
    }

    public String getName() {
        return name;
    }

    public String getProfileImage() {
        return ProfileImage;
    }
}
