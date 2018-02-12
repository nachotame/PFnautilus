package com.example.nacho.pfnautilus;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    TextView mHorasSol;
    ImageView imgcambiot;
    TextView mTemp;
    TextView mHoraActual;
    TextView mciudad;
    TextView mtempMin;
    TextView mtempMax;
    ListView mDatosJson;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imgcambiot=(ImageView) findViewById(R.id.imageView);
        mTemp=(TextView) findViewById(R.id.tvTempActu);
        mHoraActual=(TextView) findViewById(R.id.tvhoraActual);
        mciudad=(TextView)findViewById(R.id.tvCiudad);
        mtempMin=(TextView)findViewById(R.id.tvTMin);
        mtempMax=(TextView)findViewById(R.id.tvtMax);

        //Weather gWeather =new Weather();
        //llama al metodo  para que se ejecute en el metodo principal
        apiLluvia(null);
        horaActual();
        cambioImagen();



    }
    public void apiLluvia (View v){

        String latitud="-0.39";
        String longitud="39.48";
        //instanciamos la respuesta queue
        RequestQueue llamada = Volley.newRequestQueue(this);
        String url="http://api.openweathermap.org/data/2.5/weather?lat="+latitud+"&lon="+longitud+"&appid=1a3a9ef7c45a8d64f5f26a847eaa2734";


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


                            mciudad.setText(""+json.get("name"));//Iujuuu!!!//string Metido

                            Weather wheather =new Weather();//objeto de la clase wheather para meter los datos
                            //DENTRO DEL MAIN
                            wheather.setTemp(main.getDouble("temp"));//metemos dentro de settemo los datos del objeto
                            wheather.setHumidity(main.getDouble("humidity"));
                            wheather.setTempMax(main.getDouble("temp_max"));
                            wheather.setTempMin(main.getDouble("temp_min"));

                            wheather.setNubosidad(clouds.getDouble("all"));
                            wheather.setSalidaSol(sSol.getLong("sunrise"));
                            wheather.setPuestaSol(sSol.getLong("sunset"));

                            mTemp.setText(String.format("%.1f","Tº Actual"+wheather.getTemp()));
                            mtempMin.setText(String.format("%.1f","Tº Min"+wheather.toCelsius(wheather.getTempMin())));
                            mtempMax.setText(String.format("%.1f","Tº Max"+wheather.toCelsius(wheather.getTempMax())));







                            //tienes que poner que TIPO de objeto va a recoger si no dará error

                        } catch (JSONException jsone) {
                            jsone.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mciudad.setText(error.getMessage());

            }
        });
    //añade solicitud cola de respuesta
        llamada.add(stringRequest);
    }

    public void cambioImagen(){//Continuar en casa
        Weather wheather=new  Weather();
        if(wheather.getNubosidad()>80 && wheather.getHumidity()>80){
        //imgcambiot.setImageBitmap();
            //imgcambiot.setImageResource(R.drawable.icono_sol);
        }
        else{
          //  imgcambiot.setImageBitmap();
        }

    }

     public void horaActual(){
         Calendar calendario=Calendar.getInstance();
         Calendar calendarioG=new GregorianCalendar();
         long hora,minutos,segundos;
         hora=calendario.get(Calendar.HOUR_OF_DAY);
         minutos=calendario.get(Calendar.MINUTE);
         segundos=calendario.get(Calendar.SECOND);
         String horaActual=hora+":"+minutos;
         mHoraActual.setText(horaActual);

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
