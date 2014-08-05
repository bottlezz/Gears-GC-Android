package info.GearsGC.gearsgamecenter.app;

import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

import info.gearsgc.webserver.GcAssetManager;
/**
 * Created by gregoryz on 2014-08-03.
 */
public class AppAssetManager implements GcAssetManager {
    private AssetManager assetManager;
    public AppAssetManager(){

    }
    public AppAssetManager(String path){
        localFolder=path;
    }
    public AppAssetManager(AssetManager am){
        assetManager=am;
    }
    private String localFolder;

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    @Override
    public InputStream open(String fileName) throws IOException {
        return assetManager.open(fileName);

    }

    @Override
    public InputStream open(String fileName, int accessMode) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
}
