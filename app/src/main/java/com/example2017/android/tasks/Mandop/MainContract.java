package com.example2017.android.tasks.Mandop;

import android.app.Activity;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by M7moud on 17-Dec-18.
 */
public class MainContract {

    public interface MainModel{
    }

    public interface IView{


        void onItemClick(Activity v, LatLng start,LatLng end);
    }
}
