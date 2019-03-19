
package com.example2017.android.tasks.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class R_values {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private Data data;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("time")
        @Expose
        private String time;
        @SerializedName("latitude")
        @Expose
        private String latitude;
        @SerializedName("longitude")
        @Expose
        private String longitude;
        @SerializedName("priority")
        @Expose
        private String priority;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("translations")
        @Expose
        private Translation translations;
        @SerializedName("notes")
        @Expose
        private String notes;

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public String getPriority() {
            return priority;
        }

        public void setPriority(String priority) {
            this.priority = priority;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }


        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }


        public Translation getTranslation() {
            return translations;
        }

        public void setTranslation(Translation translations) {
            this.translations = translations;
        }

        public static class Translation {

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

    }

    @Override
    public String toString() {
        return "Status Code: " + getStatus() + " Message: " +getMessage() + " Data: "+getData();
    }
}
