package info.GearsGC.gearsgamecenter.app;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import info.gearsgc.webserver.GcUrlRouter;

/**
 * Created by gregoryz on 2014-08-10.
 */
public class WebServerRouter implements GcUrlRouter {
    @Override
    public String RoutePath(String path) {

        List<String> apps=AssetAppList();
        for(String app : apps){
            if(path.endsWith(app+'/')){
                path+="index.html";

            }
        }
        return path;
    }

    @Override
    public List<String> AssetAppList() {
        List<String> apps= new ArrayList<String>();
        apps.add("whois");
        apps.add("fileManager");

        return apps;
    }
}
