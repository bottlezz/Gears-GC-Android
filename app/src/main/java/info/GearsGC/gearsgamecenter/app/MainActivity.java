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
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.ClipboardManager;
import android.content.ClipData;

import org.gears.network.GCCommunicationServer;

import info.gearsgc.webserver.WebServer;

public class MainActivity extends Activity {
    ListView userList;
    UserCustomAdapter userAdapter;
    ArrayList<Game> userArray = new ArrayList<Game>();

    private GCCommunicationServer webSocketServer;
    private WebServer webServer;
    private WebServerRouter webRouter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        File external = getExternalFilesDir(null);

        AppExternalFileManager fm=new AppExternalFileManager(external);
        AppAssetManager am = new AppAssetManager(getAssets());
        webRouter = new WebServerRouter();
        /**
         * add item in arraylist
         */

        userArray.add(new Game("whois", "For 3+ people"));
        userArray.add(new Game("fileManager", "for upload and remove files"));
        userArray.addAll(fm.getGameList());
        //userArray.add(new Game("WhoIs", "For 3+ people"));
        //userArray.add(new Game("Mafia", "For 5+ people"));
        //userArray.add(new Game("Dummy", "For fuuuun"));
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
                try{
                    Game selected=userArray.get(position);
                    TextView ip = (TextView)findViewById(R.id.ipaddr);
                    ip.setText( getLocalIpAddress() + ":" + "8080"+"/"+selected.getName()+"/index.html");
                    Log.i("nameINfo",selected.getName());
                    webServer.stop();

                    //if(webSocketServer.)
                    //webServer.stop();
                    if (webSocketServer!=null)webSocketServer.stop();
                    webSocketServer = new GCCommunicationServer(8081);
                    webRouter.OnlyAllowPath(userArray.get(position).getName());

                    webServer.RegisterRouter(webRouter);
                    webServer.start();
                    webSocketServer.start();

                }catch (IOException e){
                    Log.e("error","failed restart server");
                }catch (Exception e){
                    Log.e("error","failed Restart Socket server");
                }



            }
        });

        ImageView copy = (ImageView) findViewById(R.id.copy);
        copy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager _clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                ClipData _clip;
                TextView ip = (TextView)findViewById(R.id.ipaddr);
                String text = ip.getText().toString();
                _clip = ClipData.newPlainText("ip", text);
                _clipboard.setPrimaryClip(_clip);

                Toast.makeText(MainActivity.this,
                        "Ip Address copied to clipboard.", Toast.LENGTH_LONG)
                        .show();
            }
        });

        //try{


            webServer=new WebServer(8080,fm,am);
            //webServer.RegisterRouter(webRouter);
            //webSocketServer = new GCCommunicationServer(8081);
            //webServer.start();
            //webSocketServer.start();
            //Log.i("test",fm.getRootDirPath());
        //}catch (IOException e){
          //  e.printStackTrace();
       // }

    }

    @Override
    protected void onResume(){
        super.onResume();

        TextView textIpaddr = (TextView) findViewById(R.id.ipaddr);
        TextView textIpinfo = (TextView) findViewById(R.id.ipinfo);

        textIpinfo.setText("No game server is running.");
        textIpaddr.setText("" + getLocalIpAddress() + ":" + 8080);
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

