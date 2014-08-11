package info.GearsGC.gearsgamecenter.app;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.gears.network.GCCommunicationServer;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import info.gearsgc.webserver.WebServer;


public class Main extends ActionBarActivity {

    //private HttpServer httpServer;
    private GCCommunicationServer webSocketServer;
    private WebServer webServer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try{

            File external = getExternalFilesDir(null);

            AppExternalFileManager fm=new AppExternalFileManager(external);
            AppAssetManager am = new AppAssetManager(getAssets());
            WebServerRouter router = new WebServerRouter();

            webServer=new WebServer(8080,fm,am);
            webServer.RegisterRouter(router);
            webSocketServer = new GCCommunicationServer(8081);
            webServer.start();
            webSocketServer.start();
            //Log.i("test",fm.getRootDirPath());
        }catch (IOException e){
            e.printStackTrace();
        }


    }
    @Override
    protected void onResume(){
        super.onResume();

        TextView textIpaddr = (TextView) findViewById(R.id.ipaddr);

        textIpaddr.setText("Please access! http://" + getLocalIpAddress() + ":" + 8080);
        //Log.e("Sdf", "asdf");

    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        try{
            webSocketServer.stop();
            webServer.stop();
        }catch (IOException e){
            e.printStackTrace();
        }catch (InterruptedException e){
            e.printStackTrace();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            getLocalIpAddress();
            return true;
        }
        getLocalIpAddress();
        return super.onOptionsItemSelected(item);
    }
    public String getLocalIpAddress() {


        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                if (intf.isLoopback()) {
                    continue;
                }
                if (intf.isVirtual()) {
                    continue;
                }
                if (!intf.isUp()) {
                    continue;
                }
                if (intf.isPointToPoint()) {
                    continue;
                }
                if (intf.getHardwareAddress() == null) {
                    continue;
                }


                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress.getAddress().length == 4)) {
                       // Log.i("IPs", inetAddress.getHostAddress());
                        return inetAddress.getHostAddress();

                    }
                }

            }
        } catch (SocketException ex) {
            Log.e("SocketException", ex.toString());
        }
        return null;
    }
}
