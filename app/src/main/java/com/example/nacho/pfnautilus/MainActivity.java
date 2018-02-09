package com.example.nacho.pfnautilus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    TextView mHorasSol;
    ImageView imgcambiot;
    TextView mTemp;
    TextView mHoras;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHorasSol=(TextView) findViewById(R.id.tvHorasSol);
        imgcambiot=(ImageView) findViewById(R.id.imageView);
        mTemp=(TextView) findViewById(R.id.tvTemp);
        mHoras=(TextView) findViewById(R.id.tvHRestantes);
        Weather gWeather =new Weather();
        mTemp.setText(Double.toString(gWeather.getTemp()));//Lo he conseguido!!!
        //llama al metodo  para que se ejecute en el metodo principal
        apiLluvia(null);



    }
    public void apiLluvia (View v){
        //instanciamos la respuesta queue
        RequestQueue llamada = Volley.newRequestQueue(this);
        String url="http://api.openweathermap.org/data/2.5/weather?lat=35&lon=139&appid=1a3a9ef7c45a8d64f5f26a847eaa2734";

        //solicitud de llamada de respuesta de URL
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject json = new JSONObject(response);// json van todos los contenidos de la api
                            JSONObject main=  json.getJSONObject("main");//objeto principal del json{dentro hay más}
                            JSONObject clouds=json.getJSONObject("clouds");
                            JSONObject sSol=json.getJSONObject("sys");


                            Weather wheather =new Weather();//objeto de la clase wheather para meter los datos
                            //DENTRO DEL MAIN
                            wheather.setTemp(main.getDouble("temp"));//metemos dentro de settemo los datos del objeto
                            wheather.setHumidity(main.getDouble("humidity"));
                            wheather.setTempMax(main.getDouble("temp_max"));
                            wheather.setTempMin(main.getDouble("temp_min"));

                            wheather.setNubosidad(clouds.getDouble("clouds.all"));
                            wheather.setSalidaSol(sSol.getLong("sys.sunrise"));
                            wheather.setPuestaSol(sSol.getLong("sys.sunset"));






                            //tienes que poner que TIPO de objeto va a recoger si no dará error

                        } catch (JSONException jsone) {
                            jsone.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mHorasSol.setText(error.getMessage());

            }
        });
    //añade solicitud cola de respuesta
        llamada.add(stringRequest);
    }

    public void cambioImagen(){
        Weather wheather=new  Weather();
        if(wheather.getNubosidad()>80 && wheather.getHumidity()>80){
        //imgcambiot.setImageBitmap();
        }
        else{
          //  imgcambiot.setImageBitmap();
        }

    }

     public void horasSolRestante(){

     }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
