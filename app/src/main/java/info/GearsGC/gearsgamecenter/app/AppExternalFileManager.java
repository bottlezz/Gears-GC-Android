package info.GearsGC.gearsgamecenter.app;


import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import info.gearsgc.webserver.GcFileManager;

/**
 * Created by gregoryz on 2014-08-10.
 */
public class AppExternalFileManager implements GcFileManager {
    File externalRootDir;
    private String rootDirName="GcAppFiles";
    public AppExternalFileManager(File extRoot){
        externalRootDir = extRoot;
    }

    protected String getRootDirPath() throws IOException {

        File rootDir= new File(externalRootDir.getPath()+"/"+rootDirName);

        Log.e("ExternalFileManager", "path is:"+rootDir.getPath() );
        if(!rootDir.exists()){
            boolean createResult=rootDir.mkdir();
            if(!createResult){
                Log.e("ExternalFileManager","Fail Create dir");
                throw(new IOException());
            }

        }
        return rootDir.getPath();
    }
    @Override
    public void close() {


    }

    @Override
    public String getUploadPath() {
        return externalRootDir.getPath()+"/";
    }

    @Override
    public InputStream open(String fileName) throws IOException {
        String rootPath=getRootDirPath();
        InputStream is = null;

        try {
            is = new FileInputStream(rootPath+"/"+fileName);


        } catch (FileNotFoundException e) {

            throw(e);
        }
        System.out.println(fileName);
        return is;
        //return null;


    }

    @Override
    public InputStream open(String s, int i) throws IOException {
        return null;
    }

    @Override
    public boolean CreateDir(String subPath, String name) {
        String rootPath;
        try{
            rootPath=getRootDirPath();
        }catch (IOException e){
            return false;
        }

        String path=rootPath+subPath+name;

        File theDir = new File(path);
        System.out.println(path);
        boolean result = false;
        if(!theDir.exists()){

            try{
                theDir.mkdir();
                result = true;
            } catch(SecurityException e){
                //handle it
            }

            if(!result){
                Log.e("ExternalFileManager","Fail to create");
            }
        }else{
            Log.e("ExternalFileManager"," there exists a same name");
        }

        return result;
    }

    @Override
    public String CreateFile(ByteBuffer b, int offset, int len,String subPath, String name) {
        String rootPath;
        try{
            rootPath=getRootDirPath();
        }catch (IOException e){
            return "";
        }
        String filePath=rootPath+subPath+name;
        if (len > 0) {
            FileOutputStream fileOutputStream = null;
            try {

                ByteBuffer src = b.duplicate();
                fileOutputStream = new FileOutputStream(filePath);
                FileChannel dest = fileOutputStream.getChannel();
                src.position(offset).limit(offset + len);
                dest.write(src.slice());
                fileOutputStream.close();


            } catch (Exception e) { // Catch exception if any
                throw new Error(e); // we won't recover, so throw an error
            }
        }
        return filePath;
    }


    @Override
    public String GetDir(String filePath){
        String rootPath;
        try{
            rootPath=getRootDirPath();
        }catch (IOException e){
            return "";
        }
        String JSONFile = "{\"data\":[";
        String path=rootPath+filePath;
        File file = new File(path);
        for ( File fileEntry : file.listFiles()) {
            if (fileEntry.isDirectory()) {
                String format = "{\"filename\":\""+ fileEntry.getName() +"\",\"type\":"+"\"d\"},";
                JSONFile += format;
            } else {
                String format = "{\"filename\":\""+ fileEntry.getName() +"\",\"type\":"+"\"f\"},";
                JSONFile += format;
            }

        }
        if(file.list().length>0){
            JSONFile = JSONFile.substring(0, JSONFile.length()-1);
        }

        JSONFile += "]}";
        return JSONFile;
    }

    @Override
    public boolean DeleteFile(String subPath,String name){
        try{
            String path=getRootDirPath()+subPath+name;
            System.out.println(path);
            File index = new File(path);
            if(index.isFile()){
                index.delete();
            }else{
                String[] entries = index.list();
                for(String s: entries){
                    File currentFile = new File(index.getPath(),s);
                    currentFile.delete();
                }
                index.delete();
            }
        }catch(Exception e){
            Log.e("ExternalFileManager","failed delete");
            return false;
        }

        return true;
    }
}
