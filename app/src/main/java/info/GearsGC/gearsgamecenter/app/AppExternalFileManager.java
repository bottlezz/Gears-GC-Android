package info.GearsGC.gearsgamecenter.app;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import info.gearsgc.webserver.GcFileManager;

/**
 * Created by gregoryz on 2014-08-10.
 */
public class AppExternalFileManager implements GcFileManager {
    @Override
    public void close() {

    }

    @Override
    public String getUploadPath() {
        return null;
    }

    @Override
    public InputStream open(String s) throws IOException {
        return null;
    }

    @Override
    public InputStream open(String s, int i) throws IOException {
        return null;
    }

    @Override
    public boolean CreateDir(String s, String s2) {
        return false;
    }

    @Override
    public String CreateFile(ByteBuffer byteBuffer, int i, int i2, String s, String s2) {
        return null;
    }

    @Override
    public String GetDir(String s) {
        return null;
    }

    @Override
    public boolean DeleteFile(String s, String s2) {
        return false;
    }
}
