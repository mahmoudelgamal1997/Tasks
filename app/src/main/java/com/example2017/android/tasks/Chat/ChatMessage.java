package com.example2017.android.tasks.Chat;

/**
 * Created by M7moud on 04-Feb-19.
 */
public class ChatMessage {
    String from ;
    String name;
    String ProfileImage;
    String message;
    String Time;

    public ChatMessage() {
    }

    public ChatMessage(String from, String name, String profileImage, String time, String message) {
        this.from = from;
        this.name = name;
        ProfileImage = profileImage;
        Time = time;
        this.message = message;
    }

    public String getfrom() {
        return from;
    }

    public String getName() {
        return name;
    }

    public String getProfileImage() {
        return ProfileImage;
    }

    public String getMessage() {
        return message;
    }

    public String getTime() {
        return Time;
    }
}
