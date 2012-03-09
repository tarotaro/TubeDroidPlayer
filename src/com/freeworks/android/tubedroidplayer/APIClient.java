package com.freeworks.android.tubedroidplayer;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.zip.GZIPInputStream;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import org.apache.http.*;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.cookie.Cookie;

import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;


import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.util.Log;
import com.freeworks.android.tubedroidplayer.R;


public class APIClient {
    private Context mContext;
    private static APIClient sClient = null;
    private static String sUserAgent = null;
    
    public static APIClient getInstance(Context context) {
        if(sClient == null) {
            sClient = new APIClient(context.getApplicationContext());
        }
        return sClient;
    }

    public APIClient(Context context) {
        super();
        this.mContext = context;
    }

    public String getBaseUrl() {
        String base;
        if ("true".equals(mContext.getString(R.string.debug))) {
            base = mContext.getString(R.string.base_url_debug);
        }
        else {
            base = mContext.getString(R.string.base_url);
        }
        return base;
    }
    
    

    public DefaultHttpClient createHttpClient() {
        DefaultHttpClient httpClient = new DefaultHttpClient();
        if ("true".equals(mContext.getString(R.string.debug))) {
            httpClient.getCredentialsProvider().setCredentials(
                    new AuthScope(null, -1),
                    new UsernamePasswordCredentials("folkat", "mapin"));
        }

        httpClient.getParams().setParameter("http.protocol.expect-continue",
                false);
        httpClient.getParams().setParameter(
                CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        if(sUserAgent == null) {
            PackageManager pm = mContext.getPackageManager();
            String versionName = null;
            try {
                PackageInfo info = pm.getPackageInfo(mContext.getPackageName(), 0);
                versionName = info.versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }

        return httpClient;
    }

    public String buildQueryString(HashMap<String, String> params) {
        StringBuilder sb = new StringBuilder();
        int i = 0;
        Set<Map.Entry<String, String>> entrySet = params.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {

            i++;
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(URLEncoder.encode(entry.getValue()));
            if (i < entrySet.size())
                sb.append("&");
        }
        return sb.toString();
    }
    
    public String buildUrl(String path) {
        String url;
        if((path.indexOf("http://") == 0) || (path.indexOf("https://") == 0)) {
            url = path;
        }
        else {
            url = getBaseUrl() + path;
        }
        return url;
    }
    
    public String buildUrl(String path, HashMap<String, String> params) {
        String queryString = buildQueryString(params);
        return buildUrl(path + "?" + queryString);
    }

    public final HttpGet getUrl(String url) {
        HttpGet httpGet = new HttpGet(url);

       // httpGet.addHeader("Accept-Encoding", "gzip");

        return httpGet;
    }

    public final HttpPost postUrl(String url) {
        HttpPost httpPost = new HttpPost(url);

        return httpPost;
    }

    public HttpResponse GET(String url, HashMap<String, String> params)
            throws ClientProtocolException, IOException {
        String urlStr = buildUrl(url, params);
        return GET(urlStr);
    }

    public HttpResponse GET(String url) throws ClientProtocolException,
            IOException {
        String urlStr = buildUrl(url);
        HttpGet req = new HttpGet(urlStr);




        req.addHeader("Accept-Encoding", "gzip");
        DefaultHttpClient http = createHttpClient();
        Log.v("Loctouch", "GET " + urlStr);
       HttpResponse res = http.execute(req);
        return res;
    }

    public HttpResponse POST(String url, HashMap<String, String> params)
            throws ClientProtocolException, IOException {
        String urlStr = buildUrl(url);
        HttpPost req = new HttpPost(urlStr);

        req.addHeader("Accept-Encoding", "gzip");
        List<NameValuePair> content = new ArrayList<NameValuePair>(1);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            content.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }
        req.setEntity(new UrlEncodedFormEntity(content));
        DefaultHttpClient http = createHttpClient();
        Log.v("Loctouch", "POST " + urlStr);


        HttpResponse res = http.execute(req);

        return res;
    }





    public JSONObject getAPI(String path) throws ClientProtocolException,
            IOException, JSONException {
        String url = buildUrl(path);
        HttpResponse res = GET(url);
        Header contentEncoding = res.getFirstHeader("Content-Encoding");
        HttpEntity entity = res.getEntity();
        InputStream in = entity.getContent();
        if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
            in = new GZIPInputStream(in);
        }
        String json = readStream(in);
        Log.v("Loctouch", "JSON:" + json);
        return new JSONObject(json);

    }

    public JSONArray getArrayAPI(String path) throws ClientProtocolException,
            IOException, JSONException {
        String url = buildUrl(path);
        HttpResponse res = GET(url);
        Header contentEncoding = res.getFirstHeader("Content-Encoding");
        HttpEntity entity = res.getEntity();
        InputStream in = entity.getContent();
        if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
            in = new GZIPInputStream(in);
        }
        String json = readStream(in);
        Log.v("Loctouch", "JSON:" + json);
        return new JSONArray(json);

    }

    public JSONObject getAPI(String path, HashMap<String, String> params)
            throws ClientProtocolException, IOException, JSONException {
        String url = buildUrl(path);
        HttpResponse res = GET(url, params);
        Header contentEncoding = res.getFirstHeader("Content-Encoding");
        HttpEntity entity = res.getEntity();
        InputStream in = entity.getContent();
        if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
            in = new GZIPInputStream(in);
        }

        String json = readStream(in);
        Log.v("Loctouch", "JSON:" + json);
        return new JSONObject(json);
    }



    public JSONObject postAPI(String path, HashMap<String, String> params)
            throws ClientProtocolException, IOException, JSONException {
        String url = buildUrl(path);
        HttpResponse res = POST(url, params);
        Header contentEncoding = res.getFirstHeader("Content-Encoding");
        HttpEntity entity = res.getEntity();
        InputStream in = entity.getContent();
        if (contentEncoding != null && contentEncoding.getValue().equalsIgnoreCase("gzip")) {
            in = new GZIPInputStream(in);
        }
        String json = readStream(in);
        Log.v("Loctouch", "JSON:" + json);
        return new JSONObject(json);
    }
    

    
    private String readStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
    
    public static Bitmap getImage(String url) 
    {
        if (url == null)
            return null;
        
        byte[] byteArray = getByteArrayFromURL(url);
        if (byteArray == null)
            return null;
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }
    
    public static byte[] getByteArrayFromURL(String strUrl)
    {

        byte[] byteArray = new byte[1024];
        byte[] result = null;

        HttpURLConnection con = null;
        InputStream in = null;
        ByteArrayOutputStream out = null;
        int size = 0;
        try {
            URL url = new URL(strUrl);
            

            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            in = con.getInputStream();

            out = new ByteArrayOutputStream();
            while ((size = in.read(byteArray)) != -1) {
                out.write(byteArray, 0, size);
            }
            result = out.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            try {
                if (con != null)
                    con.disconnect();
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        return result;

    }
    
    public final String targetHost() {
		String host;
        if(mContext==null)
            return "";
		if ("true".equals(mContext.getString(R.string.debug))) {
			
			host = mContext.getString(R.string.base_url_debug);
		} else {
			host = mContext.getString(R.string.base_url_debug);
		}
		return host;
	}
    
    public final String authKey() {
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
		return pref.getString("authKey","");
	}

	public final String authKeySig() {
		
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
		return pref.getString("authKeySig","");
	}	
}
