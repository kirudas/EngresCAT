package net.bosccoma.info.engrescat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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

public class LlistaEventsActivity extends AppCompatActivity {
    public static final String TAG = "Proxims";
    private FeatureCoverFlow coverFlow;
    private EventAdapter eventAdapter;
    private List<DetallEvent> detallEventList = new ArrayList<>();
    private TextSwitcher mTitle;
    private JSONArray jsonArray;
    private String v_ini_url = "https://analisi.transparenciacatalunya.cat/resource/ta2y-snj2.json?$select=codi,%20denominaci,%20imatges%20";
    private String v_fi_url = "&$order=data_inici,%20data_fi,%20codi%20ASC";//"&$group=codi, denominaci, imatges ";
    private boolean isCharged = false;
    private int posicio = 0;
    RequestQueue queue;
    StringRequest stringRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llista_events);
        initData();
        v_ini_url += getExtras(savedInstanceState);
        v_ini_url += v_fi_url;


        Animation in = AnimationUtils.loadAnimation(this,R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(this,R.anim.slide_out_bottom);
        mTitle = (TextSwitcher)findViewById(R.id.title2);
        mTitle.setInAnimation(in);
        mTitle.setOutAnimation(out);
        eventAdapter = new EventAdapter(detallEventList, getBaseContext());
        eventAdapter.notifyDataSetChanged();
        coverFlow = (FeatureCoverFlow) findViewById(R.id.coverFlow);
        coverFlow.setAdapter(eventAdapter);
        coverFlow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if (isCharged) {
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
        queue = Volley.newRequestQueue(this);
        stringRequest = new StringRequest(Request.Method.GET, v_ini_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonArray = new JSONArray(response);
                            int cont = jsonArray.length();
                            String nom;
                            String imatge;
                            String codi;
                            if (cont > 0){
                                //detallEventList.remove(0);
                                detallEventList.clear();
                                coverFlow.clearCache();
                                coverFlow.refreshDrawableState();
                            }
                            for (int i = 0; i < cont; i++){
                                codi = jsonArray.getJSONObject(i).getString("codi").trim();
                                nom = jsonArray.getJSONObject(i).getString("denominaci").trim();
                                imatge = jsonArray.getJSONObject(i).getString("imatges").trim();
                                if (imatge.contains(",/content")){
                                    imatge = imatge.substring(0,imatge.indexOf(','));
                                }
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
                        isCharged = true;
                        eventAdapter = new EventAdapter(detallEventList, getBaseContext());
                        coverFlow.setAdapter(eventAdapter);
                        int pausa = 0;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LlistaEventsActivity.this, "That didn't work!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);

        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(getBaseContext());
                TextView txt = (TextView) inflater.inflate(R.layout.layout_title,null);
                return  txt;
            }
        });

    }

    @Override
    protected void onStop () {
        super.onStop();
        if (queue != null) {
            queue.cancelAll(TAG);
        }
    }
    private String getExtras(Bundle savedInstanceState){
        String newString;
        StringBuilder sb = new StringBuilder();
        ArrayList<String> condicions = new ArrayList<>();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras != null) {
                for (String key:extras.keySet()) {
                    condicions.add(extras.getString(key));
                }
                //condicions.add(extras.getString("cat"));
                //condicions.add(extras.getString("paraula"));
                //condicions.add(extras.getString("data"));
            }
        } else {
            condicions.add((String)savedInstanceState.getSerializable("cat"));
            condicions.add((String) savedInstanceState.getSerializable("paraula"));
            condicions.add((String) savedInstanceState.getSerializable("data"));
            //newString= (String) savedInstanceState.getSerializable("data");
        }
        boolean primer = true;
        for (String condicio:condicions){
            if (condicio != null){
                if (primer){
                    primer = false;
                    sb.append("&$where=");
                }else{
                    sb.append("AND%20");
                }
                sb.append(condicio);
                sb.append(" ");
            }
        }
        return sb.toString();
    }
    private void initData() {
        detallEventList.add(new DetallEvent("Exposició Japonesa","http://www.agendaolot.cat/wp-content/uploads/Expo_Cer%C3%A0mica_Japonesa.jpg"));
    }

}
