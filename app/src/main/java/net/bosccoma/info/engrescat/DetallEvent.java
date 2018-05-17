package net.bosccoma.info.engrescat;

import android.Manifest;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.Locale;

/**
 * Created by JordiC on 27/04/2018.
 */

@RequiresApi(api = Build.VERSION_CODES.M)
public class DetallEvent extends AppCompatActivity {
    private String Name, ImageURL;

    FloatingActionButton  bt_location, bt_interest;

    private String titol,desc,imatges;
    private TextView txtTitol, txtDesc;
    private ImageView imgImatge;
    private String latitud, longitud;


    private String imageURL, name;
    private String prova = "https://analisi.transparenciacatalunya.cat/resource/ta2y-snj2.json?$select=descripcio,%20denominaci,%20imatges,%20horari,%20latitud,%20longitud%20&$where=codi%20=20180315003";
    FloatingActionButton bt_share;
    private JSONArray jsonArray;
    private TextView hola;
    private Uri gmmIntentUri;

    public DetallEvent(){
        }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        //Funcionalitat del botó SHARE
        bt_share=(FloatingActionButton) findViewById(R.id.btn_share);
        bt_share.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Intent.ACTION_SEND);
                myIntent.setType("text/plain");
                String shareBody = "Cos de l'event";
                String shareSub = "Titol de l'event";
                myIntent.putExtra(Intent.EXTRA_SUBJECT,shareBody);
                myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
                startActivity(Intent.createChooser(myIntent,"Compartir amb:"));  //Titol de l'activity
            }
        });

        if (VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shouldShowRequestPermissionRationale(Manifest.permission.INTERNET);
        }
        txtTitol = findViewById(R.id.id_detallevent_titol);
        txtDesc = findViewById(R.id.id_detallevent_descripcio);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, prova,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonArray = new JSONArray(response);
                            int cont = jsonArray.length();
                            desc = jsonArray.getJSONObject(0).getString("descripcio");
                            //horari = jsonArray.getJSONObject(0).getString("horari");
                            titol = jsonArray.getJSONObject(0).getString("denominaci");
                            latitud = jsonArray.getJSONObject(0).getString("latitud");
                            longitud = jsonArray.getJSONObject(0).getString("longitud");
                            txtTitol.setText(titol);
                            txtDesc.setText(desc);
                            bt_location.setEnabled(true);
                            gmmIntentUri=  Uri.parse(String.format("google.navigation:q=%s,%S", latitud,longitud));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DetallEvent.this, "That didn't work!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);




        //Funcionalitat del botó NAVIGATION
        bt_location=(FloatingActionButton) findViewById(R.id.btn_navigation);
        bt_location.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showMap();
            }
        });
        bt_location.setEnabled(false);
    }

    public void showMap() {
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    public DetallEvent(String name, String imageURL){
        Name = name;
        ImageURL = imageURL;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }


    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}
