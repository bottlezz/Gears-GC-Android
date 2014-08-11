package info.GearsGC.gearsgamecenter.app;

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
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        final String formatedIpAddress = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        textIpaddr.setText("Please access! http://" + formatedIpAddress + ":" + 8080);
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
