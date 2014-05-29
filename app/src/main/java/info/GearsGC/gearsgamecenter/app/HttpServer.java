package info.GearsGC.gearsgamecenter.app;

import android.app.DownloadManager;
import android.content.res.AssetManager;

import org.apache.http.protocol.HTTP;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.WebServerPlugin;

/**
 * Created by Greg on 19/05/2014.
 */
public class HttpServer extends NanoHTTPD {
    /**
     * Common mime type for dynamic content: binary
     */
    public static final String MIME_DEFAULT_BINARY = "application/octet-stream";
    /**
     * Default Index file names.
     */
    public static final List<String> INDEX_FILE_NAMES = new ArrayList<String>() {{
        add("index.html");
        add("index.htm");
    }};
    /**
     * Hashtable mapping (String)FILENAME_EXTENSION -> (String)MIME_TYPE
     */
    private static final Map<String, String> MIME_TYPES = new HashMap<String, String>() {{
        put("css", "text/css");
        put("htm", "text/html");
        put("html", "text/html");
        put("xml", "text/xml");
        put("java", "text/x-java-source, text/java");
        put("md", "text/plain");
        put("txt", "text/plain");
        put("asc", "text/plain");
        put("gif", "image/gif");
        put("jpg", "image/jpeg");
        put("jpeg", "image/jpeg");
        put("png", "image/png");
        put("mp3", "audio/mpeg");
        put("m3u", "audio/mpeg-url");
        put("mp4", "video/mp4");
        put("ogv", "video/ogg");
        put("flv", "video/x-flv");
        put("mov", "video/quicktime");
        put("swf", "application/x-shockwave-flash");
        put("js", "application/javascript");
        put("pdf", "application/pdf");
        put("doc", "application/msword");
        put("ogg", "application/x-ogg");
        put("zip", "application/octet-stream");
        put("exe", "application/octet-stream");
        put("class", "application/octet-stream");
    }};

    private static Map<String, WebServerPlugin> mimeTypeHandlers = new HashMap<String, WebServerPlugin>();
    //private final List<File> rootDirs;
    //private final boolean quiet=false;
    private  AssetManager assetManager;
    public HttpServer(){

        super(8080);

    }
    public HttpServer(int port,AssetManager am){
        super(port);
        assetManager=am;


    }
    @Override public Response serve(IHTTPSession session) {
        Map<String, List<String>> decodedQueryParameters =
                decodeParameters(session.getQueryParameterString());

        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<head><title>Debug Server</title></head>");
        sb.append("<body>");
        sb.append("<h1>Debug Server</h1>");

        sb.append("<p><blockquote><b>URI</b> = ").append(
                String.valueOf(session.getUri())).append("<br />");

        sb.append("<b>Method</b> = ").append(
                String.valueOf(session.getMethod())).append("</blockquote></p>");

        sb.append("<h3>Headers</h3><p><blockquote>").
                append(toString(session.getHeaders())).append("</blockquote></p>");

        sb.append("<h3>Parms</h3><p><blockquote>").
                append(toString(session.getParms())).append("</blockquote></p>");

        sb.append("<h3>Parms (multi values?)</h3><p><blockquote>").
                append(toString(decodedQueryParameters)).append("</blockquote></p>");

        try {
            Map<String, String> files = new HashMap<String, String>();
            session.parseBody(files);
            sb.append("<h3>Files</h3><p><blockquote>").
                    append(toString(files)).append("</blockquote></p>");
        } catch (Exception e) {
            e.printStackTrace();
        }

        sb.append("</body>");
        sb.append("</html>");
        String mimeType=getMimeTypeForFile(session.getUri());
        InputStream data=null;
        try{
            data = assetManager.open(session.getUri().substring(1));
        }catch (IOException e){
            e.printStackTrace();
        }

        if (mimeType== MIME_DEFAULT_BINARY ){
            return new Response(sb.toString());
        }else {
            return new Response(Response.Status.OK,mimeType,data);
        }

        //
    }
    private String getMimeTypeForFile(String uri) {
        int dot = uri.lastIndexOf('.');
        String mime = null;
        if (dot >= 0) {
            mime = MIME_TYPES.get(uri.substring(dot + 1).toLowerCase());
        }
        return mime == null ? MIME_DEFAULT_BINARY : mime;
    }

    private String toString(Map<String, ? extends Object> map) {
        if (map.size() == 0) {
            return "";
        }
        return unsortedList(map);
    }

    private String unsortedList(Map<String, ? extends Object> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("<ul>");
        for (Map.Entry entry : map.entrySet()) {
            listItem(sb, entry);
        }
        sb.append("</ul>");
        return sb.toString();
    }

    private void listItem(StringBuilder sb, Map.Entry entry) {
        sb.append("<li><code><b>").append(entry.getKey()).
                append("</b> = ").append(entry.getValue()).append("</code></li>");
    }
}
