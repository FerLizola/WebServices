package ittepic.edu.mx.webservices;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    Button btn ;
    TextView res;
    WServices http;
    //String GET_TODO = "http://api.openweathermap.org/data/2.5/weather?q=Tepic,mx&APPID=89c111dec8ae148f2627b8dd81d5279d";
    String json_string;
    private String GET_TODO = "http://api.openweathermap.org/data/2.5/weather?q=Tepic,mx&APPID=ff814fca3c2eaa16496254d64d6d0396";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        res=(TextView) findViewById(R.id.txt_Res);
        btn=(Button)findViewById(R.id.btn_obten);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                http = new WServices();
                http.execute(GET_TODO, "1");
            }
        });
    }

    private void mensaje(String t, String s) {
        AlertDialog.Builder alerta = new AlertDialog.Builder(this);
        alerta.setTitle(t).setMessage(s).show();
    }


    public class WServices extends AsyncTask<String, Void, String> {
        URL url;

        @Override
        protected String doInBackground(String... params) {
            String cadena = "";
            if (params[1] == "1") {
                try {
                    url = new URL(GET_TODO);
                    HttpURLConnection connection = null; // Abrir conexion
                    connection = (HttpURLConnection) url.openConnection();
                    int respuesta = 0;
                    respuesta = connection.getResponseCode();
                    InputStream inputStream = null;
                    inputStream = connection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder stringBuilder = new StringBuilder();

                    if (respuesta == HttpURLConnection.HTTP_OK) {
                        while ((json_string = bufferedReader.readLine()) != null) {
                            stringBuilder.append(json_string + "\n");
                        }
                        bufferedReader.close();
                        inputStream.close();
                        connection.disconnect();
                        String temporal = stringBuilder.toString();
                        JSONObject jsonObj = new JSONObject(temporal);
                        //mensaje("clima",jsonObj.toString());
                        JSONArray clima = jsonObj.getJSONArray("weather");
                        JSONObject uno = clima.getJSONObject(0);
                        JSONObject main = jsonObj.getJSONObject("main");
                        JSONObject wind = jsonObj.getJSONObject("wind");
                        JSONObject clouds = jsonObj.getJSONObject("clouds");
                        JSONObject sys = jsonObj.getJSONObject("sys");

                        cadena += "CLIMA" + "\n" + "ID: " + uno.getString("id") + " MAIN: " + uno.getString("main") + " \nDESCRIPCION: " +
                                uno.getString("description") +
                                "\n" + " BASE: " + jsonObj.getString("base") +
                                "\n" + "MAIN" +
                                "\n" + "TEMPERATURA: " + main.getString("temp") + " \nPRESIÓN: " + main.getString("pressure") + " \nHUMEDAD: " +
                                main.getString("humidity") + " \nTEMP_MIN: " + main.getString("temp_min") + " \nTEMP_MAX: " + main.getString("temp_max") +
                                /*"\n" + "VISIBILITY: " + jsonObj.getString("visibility") +
                                "\n" + "WIND" +
                                "\n" + "SPEED: " + wind.optInt("speed") + " DEG: " + wind.getString("deg") +
                                "\n" + "CLOUDS" +
                                "\n" + "ALL: " + clouds.optInt("all") +
                                "\n" + "DT: " + jsonObj.getString("dt") +
                                "\n" + "SYS" +
                                "\n" + " ID: " + sys.optInt("id") + " MESSAGE: " + sys.optInt("message") +*/
                                "\n COUNTRY: " + sys.getString("country") + " SUNRISE: " + sys.optInt("sunrise") + " SUNSET: " + sys.getString("sunset") +
                                "\n" + "ID: " + jsonObj.getString("id") +
                                "\n" + "NAME: " + jsonObj.getString("name") +
                                "\n" + "COD: " + jsonObj.getString("cod");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            return cadena;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            res.setText(s);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }
    }
}