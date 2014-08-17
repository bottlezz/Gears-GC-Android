package info.GearsGC.gearsgamecenter.app;

/**
 * Created by luna on 2014-08-17.
 */
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.gears.network.GCCommunicationServer;

import info.gearsgc.webserver.WebServer;

public class MainActivity extends Activity {
    ListView userList;
    UserCustomAdapter userAdapter;
    ArrayList<Game> userArray = new ArrayList<Game>();

    private GCCommunicationServer webSocketServer;
    private WebServer webServer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         * add item in arraylist
         */
        userArray.add(new Game("WhoIs", "For 3+ people"));
        userArray.add(new Game("Mafia", "For 5+ people"));
        userArray.add(new Game("Dummy", "For fuuuun"));
        /**
         * set item into adapter
         */
        userAdapter = new UserCustomAdapter(MainActivity.this, R.layout.row,
                userArray);
        userList = (ListView) findViewById(R.id.listView);
        userList.setDividerHeight(7);
        userList.setItemsCanFocus(false);
        userList.setAdapter(userAdapter);
        /**
         * get on item click listener
         */
        userList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    final int position, long id) {
//                Log.i("List View Clicked", "**********");
//                Toast.makeText(MainActivity.this,
//                        "List View Clicked:" + position, Toast.LENGTH_LONG)
//                        .show();
            }
        });

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

        textIpaddr.setText("Please access! http://" + getLocalIpAddress() + ":" + 8080+"\n");
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

