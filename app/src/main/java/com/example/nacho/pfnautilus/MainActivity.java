package com.example.nacho.pfnautilus;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    TextView nota;
    double longitud;
    double latitud;
    Location location;
    LocationManager locationManager;


    Weather weather;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imgcambiot = (ImageView) findViewById(R.id.imageView);
        mTemp = (TextView) findViewById(R.id.tvTempActu);
        mHoraActual = (TextView) findViewById(R.id.tvhoraActual);
        mciudad = (TextView) findViewById(R.id.tvCiudad);
        mtempMin = (TextView) findViewById(R.id.tvTMin);
        mtempMax = (TextView) findViewById(R.id.tvtMax);
        nota=(TextView)findViewById(R.id.tvNota);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else{
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();

            Location location = locationManager.getLastKnownLocation(locationManager
                    .getBestProvider(criteria, false));
            latitud = location.getLatitude();
            longitud = location.getLongitude();

        }

        /*
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            } else {
                latitud= location.getLatitude();
                longitud=location.getLongitude();
            }
        }
        //******
       /* if ( !locationManager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            Toast.makeText(this,"el GPS esta desactivado",Toast.LENGTH_SHORT).show();
        }
        //****
        */

        //Weather gWeather =new Weather();
        //llama al metodo  para que se ejecute en el metodo principal


        compruebaConexion(MainActivity.this);
        apiLluvia(null);

        //


    }
    public void apiLluvia (View v){


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

                            weather=new Weather();//objeto de la clase wheather para meter los datos
                            //DENTRO DEL MAIN
                            weather.setTemp(main.getDouble("temp"));//metemos dentro de settemo los datos del objeto
                            weather.setHumidity(main.getDouble("humidity"));
                            weather.setTempMax(main.getDouble("temp_max"));
                            weather.setTempMin(main.getDouble("temp_min"));

                            weather.setNubosidad(clouds.getDouble("all"));
                            weather.setSalidaSol(sSol.getLong("sunrise"));
                            weather.setPuestaSol(sSol.getLong("sunset"));

                            mTemp.setText("Ahora\n"+String.format("%.1f",weather.toCelsius(weather.getTemp()))+"ºC");
                            mtempMin.setText("T-Min\n"+String.format("%.1f",weather.toCelsius(weather.getTempMin()))+"ºC");
                            mtempMax.setText("T-Max\n"+String.format("%.1f",weather.toCelsius(weather.getTempMax()))+"ºC");

                            cambioImagen();
                            horaActual();






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
        //añade solicitud cola de //respuesta
        llamada.add(stringRequest);
    }



    public void cambioImagen(){//Continuar en casa

        if(weather.getNubosidad()>=80 && weather.getHumidity()>=80){

            imgcambiot.setImageResource(R.drawable.bajolluvia);
            nota.setText("Según como lo veo, si quieres el arco iris,\n  tienes que aguantar la lluvia.\n-Dolly Parton-");
            Toast.makeText(this,"Frio y Lluvia nos llega",Toast.LENGTH_LONG).show();

        }

        else{
            imgcambiot.setImageResource(R.drawable.solvida);
            nota.setText("Cada día el sol ilumina un mundo nuevo.\n-Paulo Coelho-");

            Toast.makeText(this,"Sonría, hoy es día de salir a caminar",Toast.LENGTH_LONG).show();

        }


    }

    public void horaActual(){
        Calendar calendario=new GregorianCalendar();
        int hora;
        String minutos;
        String segundos;
        hora=(calendario.get(Calendar.HOUR_OF_DAY));
        if(hora<=12) {
            String horaActualAM = "Última actualización\n" +"A las" + hora+" horas" + " am";
            mHoraActual.setText(horaActualAM);

        }else{
            String horaActualPM = "Última actualización\n"+"A las " + hora +" horas" + " pm";
            mHoraActual.setText(horaActualPM);


        }





    }

    public  boolean compruebaConexion(Context context)
    {
        boolean connected = false;
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Recupera todas las redes (tanto móviles como wifi)
        NetworkInfo[] redes = connec.getAllNetworkInfo();

        for (int i = 0; i < redes.length; i++) {
            // Si alguna red tiene conexión, se devuelve true
            if (redes[i].getState() == NetworkInfo.State.CONNECTED) {
                connected = true;
            }
        }
        if (!connected){
            Toast.makeText(this,"No tiene conexión a internet",Toast.LENGTH_SHORT).show();
            Toast.makeText(MainActivity.this,"Puede que su aplicación estalle",Toast.LENGTH_LONG).show();
            finish();
        }
        return connected;
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