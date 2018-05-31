package net.bosccoma.info.engrescat;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

/**
 * Classe de LlistaEvents s'encarrega de enviar la petició a la Api, rebre les dades, parsejar-les i
 * carregar-les correctament en el CoverFlow.
 */
public class LlistaEventsActivity extends AppCompatActivity {
    //String que conté el tag o nom de la queue o cua.
    public static final String TAG = "Proxims";
    //Element que és una llista amb animacions i transicions ja incorporades
    private FeatureCoverFlow coverFlow;
    //Adaptador per la llista
    private EventAdapter eventAdapter;
    //Llista que guardarà els events
    private List<DetallEvent> detallEventList = new ArrayList<>();
    //Text que mostrem a la part inferior de la pantalla per indicar el nom de l'event actual
    private TextSwitcher mTitle;
    //Variable encarregada de recollir la resposta de la api
    private JSONArray jsonArray;
    //String que conté el començament de la sentncia http enviada a la api
    private String v_ini_url = "https://analisi.transparenciacatalunya.cat/resource/ta2y-snj2.json?$select=codi,%20denominaci,%20imatges%20";
    //String que conté la part final de la sentència http enviada a la api
    private String v_fi_url = "&$order=data_inici,%20data_fi,%20codi%20ASC";//"&$group=codi, denominaci, imatges ";
    //private boolean isCharged = false;
    //Variable que guarda la posició actual del coverflow i es va actualizant mentre fas scroll
    private int posicio = 0;
    //Variable que conté la cua que utilitzarem per enviar peticions a la api
    RequestQueue queue;
    //Variable que contindrà la petició a la api, en el nostre cas mètode get i l'url
    StringRequest stringRequest;

    /**
     * Mètode onCreate que crearà la pantalla amb el CoverFlow, toolbar, etc...
     *
     * @param savedInstanceState l'utilizem per carregar informació entre pantalles
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//      Inicialitza l'Activity i s'associa amb la vista
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llista_events);
//      Carreguem la barra superior personalitzan-la i activant el support per poder tenir menú
        Toolbar toolbar = (Toolbar) findViewById(R.id.llista_toolbar);
        toolbar.bringToFront();
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
//      Inicialitzem la llista d'events amb events hardcoded per evitar crashs en cas de que falli la conexió a internet
        initData();
//      Creem la consulta url de la api
        v_ini_url += getExtras(savedInstanceState);
        v_ini_url += v_fi_url;

//      Inicialitzem els elements de la pantalla
        Animation in = AnimationUtils.loadAnimation(this, R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.slide_out_bottom);
        mTitle = (TextSwitcher) findViewById(R.id.title2);
        mTitle.setInAnimation(in);
        mTitle.setOutAnimation(out);
        eventAdapter = new EventAdapter(detallEventList, getBaseContext());
        eventAdapter.notifyDataSetChanged();
        coverFlow = (FeatureCoverFlow) findViewById(R.id.coverFlow);
        coverFlow.setAdapter(eventAdapter);
        coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (true) {//isCharged) {
                    Intent intent = new Intent(getBaseContext(), DetallEvent.class);
                    intent.putExtra("codi", detallEventList.get(posicio).getCodi());
                    startActivity(intent);
                }
            }
        });
        coverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle.setText(detallEventList.get(position).getName());
                posicio = position;
            }

            @Override
            public void onScrolling() {

            }
        });
//      Creem la petició a la api
        /*queue = Volley.newRequestQueue(this);
        stringRequest = new StringRequest(Request.Method.GET, v_ini_url,
                new Response.Listener<String>() {
//      Quan la petició sigui resposta executa el codi
                    @Override
                    public void onResponse(String response) {
                        try {
//      Carreguem les dades en una variable JSONArray i inicialitzem varibles locals per fer el codi més entenador
                            jsonArray = new JSONArray(response);
                            int cont = jsonArray.length();
                            String nom;
                            String imatge;
                            String codi;
//      Comprovem que hem rebut events i si els hem rebut netejem els events hardcoded de la llista
                            if (cont > 0){
                                //detallEventList.remove(0);
                                detallEventList.clear();
                                coverFlow.clearCache();
                                coverFlow.refreshDrawableState();
                            }
//      Parsejem cada objecte Json de l'array en un event de la llista
                            for (int i = 0; i < cont; i++){
                                codi = jsonArray.getJSONObject(i).getString("codi").trim();
                                nom = jsonArray.getJSONObject(i).getString("denominaci").trim();
                                imatge = jsonArray.getJSONObject(i).getString("imatges").trim();
//      De moment només agafem la primera imatge de l'event, més endavant intentarem millorar-ho
                                if (imatge.contains(",/content")){
                                    imatge = imatge.substring(0,imatge.indexOf(','));
                                }
//      La api té events repetits, dóna la opció de fer group by però están bastant limitats per tant
//      hem decidit implementar-ho nosaltres al codi per tal de personalizar-ho i intentar agafar l'event
//      amb les dades més correctes. Encara cal millorar més aquesta última part.
                                boolean repetit = false;
                                int pos = 0;
                                int canv = 0;
                                for (DetallEvent item:detallEventList) {
                                    if (item.getCodi().equals(codi)||item.getName().equals(nom)||item.getImageURL().equals(imatge)){
                                        repetit = true;
                                    }
                                    pos++;
                                }
                                if (repetit){
                                    if (detallEventList.get(canv).getImageURL() == null){
                                        detallEventList.get(canv).setImageURL(imatge);
                                    }
                                }else{
                                    detallEventList.add(new DetallEvent(codi, nom,"https://agenda.cultura.gencat.cat"+imatge));
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //isCharged = true;
//      Un cop carregats tots els events de la petició realitzada hem de actualizar la pantalla
                        eventAdapter = new EventAdapter(detallEventList, getBaseContext());
                        coverFlow.setAdapter(eventAdapter);
                        coverFlow.releaseAllMemoryResources();
                        mTitle.setText(detallEventList.get(position).getName());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LlistaEventsActivity.this, "That didn't work!", Toast.LENGTH_SHORT).show();
            }
        });
//      Afegim la petició a la api a la cua que internament ja carreguera la tasca asyncrona
        queue.add(stringRequest);*/
//      Carreguem el títol de la part inferior de la pantalla
        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(getBaseContext());
                TextView txt = (TextView) inflater.inflate(R.layout.layout_title, null);
                return txt;
            }
        });

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

    //      Quan la aplicació s'atura hem d'eliminar la cua, ho fem mitjançant el TAG que li hem donat quan l'hem creat
    @Override
    protected void onStop() {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }

    /***
     * Mètode que obté les dades enviades d'altres Activities
     * @param savedInstanceState conté la informació
     * @return la clàusula where de la petició a la api
     */
    private String getExtras(Bundle savedInstanceState) {
//        Obté les dades i les afageix a un Arraylist
        String newString;
        StringBuilder sb = new StringBuilder();
        ArrayList<String> condicions = new ArrayList<>();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                for (String key : extras.keySet()) {
                    condicions.add(extras.getString(key));
                }
            }
        } else {
            condicions.add((String) savedInstanceState.getSerializable("cat"));
            condicions.add((String) savedInstanceState.getSerializable("paraula"));
            condicions.add((String) savedInstanceState.getSerializable("data"));
            //newString= (String) savedInstanceState.getSerializable("data");
        }
//      A partir de l'ArrayList construeix la clàusula where
        boolean primer = true;
        for (String condicio : condicions) {
            if (condicio != null) {
                if (primer) {
                    primer = false;
                    sb.append("&$where=");
                } else {
                    sb.append("AND%20");
                }
                sb.append(condicio);
                sb.append("%20");
            }
        }
        return sb.toString();
    }

    /***
     * Mètode que inicializa events hardcoded
     */
    private void initData() {
        detallEventList.add(new DetallEvent("20180523012", "Primavera Sound", "http://www.lavanguardia.com/r/GODO/LV/p5/WebSite/2018/05/29/Recortada/img_jcanyissa_20180518-093415_imagenes_lv_terceros_ps_cartel2018-kUOB--992x558@LaVanguardia-Web.jpg"));
        detallEventList.add(new DetallEvent("20180409040", "FIMAG, Festival de Magia", "http://redcostabrava.com/wp-content/uploads/2018/05/Cartell-FIMAG18.jpg"));
        detallEventList.add(new DetallEvent("20180518021", "Fira Medieval-Festa de la Sal", "http://www.vilarsrurals.com/media/cache/header_desktop/content/files/ofertas/Agenda/Banner-Fira-Medieval.jpg"));
        detallEventList.add(new DetallEvent("20180410017", "VIII Festival Minipop", "http://www.sulu.es/wp-content/uploads/2018/04/cartel-minipop.jpg"));
        detallEventList.add(new DetallEvent("20180517007", "XIII Festival Inund'Art", "http://www.inundart.org/wp-content/uploads/2018/05/CARTELL_3_sense-logos-e1525858619125.jpg"));
    }


}
