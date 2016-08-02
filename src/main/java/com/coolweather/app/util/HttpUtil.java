package com.coolweather.app.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/8/2 0002.
 */
public class HttpUtil {
    public static void sendHttpRequest(final String adress,final HttpCallbackListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection=null;
                try {
                    URL url=new URL(adress);
                    connection= (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(8000);
                    connection.setRequestMethod("GET");
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader=new BufferedReader(new InputStreamReader(in));
                    StringBuilder response=new StringBuilder();
                    String line;
                    while ((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    if (listener!=null){
                        /**
                         * 成功回调
                         */
                        listener.onFinish(response.toString());
                    }
                }catch (Exception e){
                   if (listener!=null){
                       listener.onErroe(e);
                   }
                }finally {
                    if (connection!=null){
                        connection.disconnect();
                    }
                }
            }
        }).start();
    }
}
