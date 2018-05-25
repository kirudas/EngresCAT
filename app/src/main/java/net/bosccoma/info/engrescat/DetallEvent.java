package net.bosccoma.info.engrescat;

import android.Manifest;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by JordiC on 27/04/2018.
 */

public class DetallEvent extends AppCompatActivity {
    private String codi;

    public String getCodi() {
        return codi;
    }

    private String Name, ImageURL;

    FloatingActionButton  bt_location, bt_interest;

    private String titol,desc,imatges;
    private TextView txtTitol, txtDesc,txtDataInici,txtDataFi;
    private ImageView imgImatge;
    private String latitud, longitud;
    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    private Uri gmmIntentUri;
    private String imageURL, name;
    private String request = "https://analisi.transparenciacatalunya.cat/resource/ta2y-snj2.json?$select=descripcio,%20denominaci,%20imatges,%20horari,%20latitud,%20longitud,%20data_inici,%20data_fi%20&$where=codi%20=";
    FloatingActionButton bt_share;
    private JSONArray jsonArray;
    public DetallEvent(String name, String imageURL){
        this.name = name;
        this.imageURL = imageURL;
    }
    public DetallEvent(){

    }
    public DetallEvent(String codi, String name, String imageURL){
        this.name = name;
        this.codi = codi;
        this.imageURL = imageURL;
    }
    private String getExtras(Bundle savedInstanceState){
        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("codi");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("codi");
        }
        return newString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        //Funcionalitat del botó SHARE
        codi = getExtras(savedInstanceState);
        request += codi;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shouldShowRequestPermissionRationale(Manifest.permission.INTERNET);
        }
        //Funcionalitat del botó NAVIGATION
        bt_location=(FloatingActionButton) findViewById(R.id.btn_navigation);
        bt_location.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                showMap();
            }
        });
        bt_location.setEnabled(false);
        //Carregar Dades de l'event
        txtTitol = findViewById(R.id.id_detallevent_titol);
        txtDesc = findViewById(R.id.id_detallevent_descripcio);
        imgImatge = findViewById(R.id.id_detallevent_imatge);
        txtDataInici = findViewById(R.id.id_detallevent_data_inici);
        txtDataFi = findViewById(R.id.id_detallevent_data_fi);
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, request,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonArray = new JSONArray(response);
                            int cont = jsonArray.length();
                            desc = jsonArray.getJSONObject(0).getString("descripcio");
                            //horari = jsonArray.getJSONObject(0).getString("horari");
                            titol = jsonArray.getJSONObject(0).getString("denominaci");
                            if(jsonArray.getJSONObject(0).has("latitud"))
                                latitud = jsonArray.getJSONObject(0).getString("latitud");
                            if(jsonArray.getJSONObject(0).has("longitud"))
                                longitud = jsonArray.getJSONObject(0).getString("longitud");
                            txtDataInici.setText(formataData(jsonArray.getJSONObject(0).getString("data_inici")));
                            txtDataFi.setText(formataData(jsonArray.getJSONObject(0).getString("data_fi")));
                            String auxImatge = jsonArray.getJSONObject(0).getString("imatges");
                            if (auxImatge.contains(",")) {
                                auxImatge = auxImatge.substring(0, auxImatge.indexOf(','));
                            }
                            imatges = "https://agenda.cultura.gencat.cat"+auxImatge;
                            txtTitol.setText(titol);
                            txtDesc.setText(desc);
                            Picasso.with(getBaseContext()).load(imatges).into(imgImatge);
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
    }

    private String formataData(String data) {
        return data.substring(0,data.indexOf('T'));
    }

    public void showMap() {
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

}
