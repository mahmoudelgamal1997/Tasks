
package com.example2017.android.tasks.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Translation {

    @SerializedName("locale")
    @Expose
    private String locale;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("task_id")
    @Expose
    private Integer taskId;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
