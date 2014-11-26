package com.example.mladen.helloandroidactivitystates;

import android.graphics.Path;

import java.util.List;

/**
 * Created by Mladen on 23.11.2014..
 */
public class MyPath {
    private List<MyPathMethod> mMyPathMethodList;

    public MyPath(List<MyPathMethod> mMyPathMethodList) {
        this.mMyPathMethodList = mMyPathMethodList;
    }

    public Path createGeneralPath() {
        Path polyline = new Path();

        int ind = 0;
        MyPathMethod tmpPathMethod = mMyPathMethodList.get(ind);
        tmpPathMethod.drawWithMyPathMethod(polyline);

        for (ind = 1; ind < mMyPathMethodList.size(); ind++) {
            tmpPathMethod = mMyPathMethodList.get(ind);
            tmpPathMethod.drawWithMyPathMethod(polyline);
        }

        return polyline;
    }

    @Override
    public String toString() {
        StringBuilder strBuild = new StringBuilder();


        for(MyPathMethod item:mMyPathMethodList){
            strBuild.append(item.toString()).append("\n");
        }

        return strBuild.toString();
    }
}
