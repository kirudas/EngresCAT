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
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

/***
 * Classe que s'encarrega de mostrar la informació sencera d'un event, més altres funcionalitats
 * com la de compartir l'event o que a partir de la teva ubicació t'indiqui com anar a l'event
 */
public class DetallEvent extends AppCompatActivity {
    /***
     * Declaració dels atributs de la classe
     */
    // Variables que ens permeten guardar string i fer el codi més entenador
    private String codi, titol, desc, imatges, name, imageURL, latitud, longitud;
    // Botons que tenen associades funcionalitats. bt_interest segueix pendent de programar
    FloatingActionButton bt_location, bt_interest, bt_share;
    // Textos de que mostrem a la pantalla amb informació de l'event
    private TextView txtTitol, txtDesc, txtDataInici, txtDataFi;
    // Imatge de l'event
    private ImageView imgImatge;
    // Uri on indicarem la geolocalització de l'event
    private Uri gmmIntentUri;
    // Part inicial de la petició a la api, falta la clàusula where amb el codi encara
    private String request = "https://analisi.transparenciacatalunya.cat/resource/ta2y-snj2.json?$select=descripcio,%20denominaci,%20imatges,%20horari,%20latitud,%20longitud,%20data_inici,%20data_fi%20&$where=codi%20=";
    // Variable per recollir la resposta de la api
    private JSONArray jsonArray;

    /***
     * Constructor per crear un event on el codi l'obtindrem d'altres maneres
     * @param name nom o titol de l'event
     * @param imageURL url de la imatge de l'event
     */
    public DetallEvent(String name, String imageURL) {
        this.name = name;
        this.imageURL = imageURL;
    }

    /***
     * Constructor per defecte
     */
    public DetallEvent() {

    }

    /***
     * Constructor complet de l'event
     * @param codi identificador unic de l'event dins de l'api
     * @param name nom o titol de l'event
     * @param imageURL url de la imatge de l'event
     */
    public DetallEvent(String codi, String name, String imageURL) {
        this.name = name;
        this.codi = codi;
        this.imageURL = imageURL;
    }

    /***
     * Getter del codi
     * @return
     */
    public String getCodi() {
        return codi;
    }

    /***
     * Getter de la imageURL
     * @return
     */
    public String getImageURL() {
        return imageURL;
    }

    /***
     * Setter de la imageURL
     * @param imageURL
     */
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /***
     * Getter del name
     * @return
     */
    public String getName() {
        return name;
    }

    /***
     * Setter del name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /***
     * Mètode que obté les dades enviades d'altres Activities
     * @param savedInstanceState conté la informació
     * @return retorna el codi de l'event
     */
    private String getExtras(Bundle savedInstanceState) {
        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                newString = null;
            } else {
                newString = extras.getString("codi");
            }
        } else {
            newString = (String) savedInstanceState.getSerializable("codi");
        }
        return newString;
    }

    /***
     *  Mètode onCreate que s'executa al crear l'activity i carregar la pantalla
     * @param savedInstanceState conté informació enviada d'altres Activities
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//      Inicialitza l'Activity i s'associa amb la vista
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
//      Carreguem la barra superior personalitzan-la i activant el support per poder tenir menú
        Toolbar toolbar = (Toolbar) findViewById(R.id.event_toolbar);
        toolbar.bringToFront();
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
//      carraguem el codi de l'event i l'afegim a la petició
        codi = getExtras(savedInstanceState);
        request += codi;
//      Comprovem i/o demanem el permís d'internet
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            shouldShowRequestPermissionRationale(Manifest.permission.INTERNET);
        }
        //Funcionalitat del botó NAVIGATION
        bt_location = (FloatingActionButton) findViewById(R.id.btn_navigation);
        bt_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMap();
            }
        });
        //bt_location.setEnabled(false);
        //Funcionalitat del botó SHARE
        bt_share = (FloatingActionButton) findViewById(R.id.btn_share);
        //Carregar Dades de l'event
        txtTitol = findViewById(R.id.id_detallevent_titol);
        txtDesc = findViewById(R.id.id_detallevent_descripcio);
        imgImatge = findViewById(R.id.id_detallevent_imatge);
        txtDataInici = findViewById(R.id.id_detallevent_data_inici);
        txtDataFi = findViewById(R.id.id_detallevent_data_fi);
//      Creem la petició a la api
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, request,
                new Response.Listener<String>() {
//      Quan la petició sigui resposta executa el codi
                    @Override
                    public void onResponse(String response) {
                        try {
//      Carreguem les dades en una variable JSONArray i inicialitzem varibles locals per fer el codi més entenador
                            jsonArray = new JSONArray(response);
                            int cont = jsonArray.length();
                            desc = jsonArray.getJSONObject(0).getString("descripcio");
                            //horari = jsonArray.getJSONObject(0).getString("horari");
                            titol = jsonArray.getJSONObject(0).getString("denominaci");
                            if (jsonArray.getJSONObject(0).has("latitud"))
                                latitud = jsonArray.getJSONObject(0).getString("latitud");
                            if (jsonArray.getJSONObject(0).has("longitud"))
                                longitud = jsonArray.getJSONObject(0).getString("longitud");
                            txtDataInici.setText(formataData(jsonArray.getJSONObject(0).getString("data_inici")));
                            txtDataFi.setText(formataData(jsonArray.getJSONObject(0).getString("data_fi")));
                            String auxImatge = jsonArray.getJSONObject(0).getString("imatges");
                            if (auxImatge.contains(",")) {
                                auxImatge = auxImatge.substring(0, auxImatge.indexOf(','));
                            }
                            imatges = "https://agenda.cultura.gencat.cat" + auxImatge;
                            imatges = "http://www.lavanguardia.com/r/GODO/LV/p5/WebSite/2018/05/29/Recortada/img_jcanyissa_20180518-093415_imagenes_lv_terceros_ps_cartel2018-kUOB--992x558@LaVanguardia-Web.jpg";
//      Carreguem la informació de l'event a la pantalla
                            txtTitol.setText(titol);
                            txtDesc.setText(desc);
                            Picasso.with(getBaseContext()).load(imatges).into(imgImatge);
                            //Boto que prepara la navegació
                            bt_location.setEnabled(true);
                            gmmIntentUri = Uri.parse(String.format("google.navigation:q=%s,%S", latitud, longitud));
                            //Boto que comparteix l'esdeveniment
                            bt_share.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent myIntent = new Intent(Intent.ACTION_SEND);
                                    myIntent.setType("text/plain");
                                    String link = "https://agenda.cultura.gencat.cat/content/agenda/ca/article.html?article=" + codi;
                                    String shareBody = titol + "\n" + "\n" + link;
                                    myIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
//myIntent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
                                    startActivity(Intent.createChooser(myIntent, "Compartir amb:")); //Titol de l'activity
                                }
                            });
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

    //      Carreguem un menú d'opcions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_general, menu);
        return true;
    }

    //      Assignem que ha de fer el menú quan hi fan click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_opcions:
                Intent intent = new Intent(getBaseContext(), OpcionsMenu.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /***
     * Mètode que formata la data al format que estem acostumats
     * @param data string amb la data en el format de la api
     * @return la data en el format desitjat
     */
    private String formataData(String data) {
        StringBuilder sb = new StringBuilder();
        String[] split = data.substring(0, data.indexOf('T')).split("-");
        sb.append(split[2]);
        sb.append("/");
        sb.append(split[1]);
        sb.append("/");
        sb.append(split[0]);
        return sb.toString();
        //return data.substring(0,data.indexOf('T'));
    }

    /***
     * Mètode que carrega la navegació GoogleMaps amb les coordenades del destí on vols anar.
     */
    public void showMap() {
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

}
