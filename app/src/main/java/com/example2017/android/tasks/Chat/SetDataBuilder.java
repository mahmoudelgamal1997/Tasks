package com.example2017.android.tasks.Chat;

/**
 * Created by M7moud on 18-Feb-19.
 */
public class SetDataBuilder  {

    public  String UserName,Time,SenderId,Message;

    public SetDataBuilder(Builder build) {
        build.UserName = UserName;
        build.Time = Time;
        build.SenderId = SenderId;
        build.Message=Message;
    }



    public static class Builder{
        public  String UserName,Time,SenderId,Message;


        public Builder UserName(String userName){
            this.UserName=userName;

            return this;
        }

        public Builder Time(String time){
            this.Time=time;

            return this;
        }

        public Builder SenderId(String senderId){
            this.SenderId=senderId;
            return this;
        }

        public Builder Message(String Message){
            this.Message=Message;

            return this;
        }


        public SetDataBuilder build(){
            return new SetDataBuilder(this);
        }
    }
}
