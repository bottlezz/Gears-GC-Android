package info.GearsGC.gearsgamecenter.app;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.gearsgc.webserver.GcUrlRouter;

/**
 * Created by gregoryz on 2014-08-10.
 */
public class WebServerRouter implements GcUrlRouter {
    WebServerRouter(){
        applist = new ArrayList<String>();
        validPath = "/";
    }
    String validPath;
    private ArrayList<String> applist;
    @Override
    public String RoutePath(String path) {
        Log.i("URLpath",path);
        if(!path.contains(validPath)){
            path="/";
        }
        List<String> apps=AssetAppList();

        for(String app : apps){
            if(path.endsWith(app+'/')){
                path+="index.html";

            }
        }

        return path;
    }
    public void OnlyAllowPath(String dir){
        validPath = "/"+dir;
    }

    @Override
    public List<String> AssetAppList() {
        List<String> apps= new ArrayList<String>();
        apps.add("whois");
        apps.add("fileManager");

        return apps;
    }
}
