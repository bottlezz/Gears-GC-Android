package info.GearsGC.gearsgamecenter.app;


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
 * Created by gregoryz on 2014-08-03.
 */
public class AppInternalFileManager implements GcFileManager {
    public AppInternalFileManager(){

    }
    public AppInternalFileManager(String path){
        localFolder=path;
    }
    private String localFolder;

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }

    @Override
    public InputStream open(String fileName) throws IOException {
        // TODO Auto-generated method stub
        InputStream is = null;

        try {
            is = new FileInputStream(localFolder+"/"+fileName);


        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(fileName);
        return is;

    }

    @Override
    public InputStream open(String fileName, int accessMode) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public String getUploadPath() {
        // TODO Auto-generated method stub
        return localFolder+"/";
    }
    @Override
    public boolean CreateDir(String subPath,String name) {
        // TODO Auto-generated method stub
        String path=localFolder+subPath+name;

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
                System.out.println("Fail to create");
            }
        }else{
            System.out.println("Sorry, there exists a same name");
        }

        return result;
    }

    public String GetDir(String filePath){
        String JSONFile = "{\"data\":[";
        String path=localFolder+filePath;
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

    public boolean DeleteFile(String subPath,String name){
        try{
            String path=localFolder+subPath+name;
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
            System.out.println("faile delete");
            return false;
        }

        return true;
    }
    @Override
    public String CreateFile(ByteBuffer b, int offset, int len,String subPath, String name) {
        String filePath=localFolder+subPath+name;
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
}
