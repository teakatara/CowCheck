package com.example.syouk.Agricowture;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DroneThread implements Runnable {

    @Override
    public void run() {
        Log.d("thread","in");

        HttpURLConnection con;
        URL url;
        String urlSt;
        StringBuilder result = new StringBuilder();
        Constant.dronethreadOK = false;

        try{
            while (true){
                if(Constant.droneOK) {
                    Log.d("droneOK", "true");
                    Log.d("dronethread CowNum",""+Constant.CowNum);
                    urlSt = "https://cowcheck.herokuapp.com/flightdrone/"+String.valueOf(Constant.CowNum);//←constantクラスを通じてURLを変える　Constant.CowNum
                    Log.d("urlSt",""+urlSt);
                    url = new URL(urlSt);
                    con = (HttpURLConnection) url.openConnection();
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(Constant.CONNECTION_TIME_OUT);
                    con.setReadTimeout(Constant.READ_TIME_OUT);
                    con.setInstanceFollowRedirects(false);
                    con.setDoInput(true);
                    Log.d("URLConnection", "Connecting");
                    con.connect();
                    int statusCode = con.getResponseCode();
                    Log.d("statusCode", "" + statusCode);
                    if (statusCode == HttpURLConnection.HTTP_OK) {
                        Log.d("DroneHTTPURLConnection", "Success");
                        //ここから続き書く。

                        final InputStream in = con.getInputStream();
                        Log.d("in", "" + in);
                        final InputStreamReader inReader = new InputStreamReader(in, StandardCharsets.UTF_8);
                        Log.d("inReader", "" + inReader);
                        final BufferedReader bufferedReader = new BufferedReader(inReader);
                        Log.d("bufferedReader", "" + bufferedReader);
                        String line;
                        line = bufferedReader.readLine();
                        Log.d("while", "true");
                        Log.d("line", "" + line);
                        result.append(line);
                        Log.d("result", "" + result);
                        bufferedReader.close();
                        inReader.close();
                        in.close();

                        //ドローンの状態をサーバーから取得
                        Log.d("response", "" + line);
                    }

                    Constant.droneOK = false;
                    break;
                } else if(Constant.droneWhileEscape){
                    Log.d("droneWhileEscape","true");
                    Constant.droneWhileEscape = false;
                    break;
                } else {
                    Thread.sleep(5000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Constant.droneOK = false;
            Constant.droneWhileEscape = false;
        } catch (ProtocolException e) {
            e.printStackTrace();
            Constant.droneOK = false;
            Constant.droneWhileEscape = false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Constant.droneOK = false;
            Constant.droneWhileEscape = false;
        } catch (IOException e) {
            e.printStackTrace();
            Constant.droneOK = false;
            Constant.droneWhileEscape = false;
        }
        Constant.dronethreadOK = true;
    }
}