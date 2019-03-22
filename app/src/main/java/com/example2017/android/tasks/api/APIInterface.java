package com.example2017.android.tasks.api;

import com.example2017.android.tasks.models.R_values;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by M7moud on 08-Mar-19.
 */
public interface APIInterface {

    public static final String server_url = "http://maham.softex-it.com/";

    @FormUrlEncoded
    @POST("api/v1/task/create")
    Call<Reports> SendReqToGetFeedBack(@Field("userId") String userId, @Field("latitude") String latitude, @Field("longitude") String longitude, @Field("priority") String priority, @Field("notes") String notes,@Field("time") String time, @Field("teamType") String teamType, @Field("timeFrom") String timeFrom, @Field("timeTo") String timeTo,@Field("attachments") File[] attachments);


    @GET("api/v1/user/UserNewForTestMahmoud/tasks")
    Call<Tasks> getTasks();

}
