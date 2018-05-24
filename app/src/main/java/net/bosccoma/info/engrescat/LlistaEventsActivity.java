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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class LlistaEventsActivity extends AppCompatActivity {
    private FeatureCoverFlow coverFlow;
    private EventAdapter eventAdapter;
    private List<DetallEvent> detallEventList = new ArrayList<>();
    private TextSwitcher mTitle;
    private JSONArray jsonArray;
    private String v_ini_url = "https://analisi.transparenciacatalunya.cat/resource/ta2y-snj2.json?$select=codi, denominaci, imatges ";
    private String v_fi_url = "&$group=codi, denominaci, imatges ";
    private boolean isCharged = false;
    private int posicio = 0;
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
        coverFlow = (FeatureCoverFlow) findViewById(R.id.coverFlow);
        coverFlow.setAdapter(eventAdapter);
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
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, v_ini_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            jsonArray = new JSONArray(response);
                            int cont = jsonArray.length();
                            String nom;
                            String imatge;
                            String codi;
                            if (cont > 0)
                                detallEventList.remove(0);
                            for (int i = 0; i < cont; i++){
                                codi = jsonArray.getJSONObject(i).getString("codi");
                                nom = jsonArray.getJSONObject(i).getString("denominaci");
                                imatge = jsonArray.getJSONObject(i).getString("imatges");
                                if (imatge.contains(","))
                                    imatge = imatge.substring(0,imatge.indexOf(','));
                                detallEventList.add(new DetallEvent(codi, nom,"https://agenda.cultura.gencat.cat"+imatge));
                            }
                            isCharged = true;
                            eventAdapter = new EventAdapter(detallEventList, getBaseContext());
                            coverFlow.setAdapter(eventAdapter);
                            int pausa = 0;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
        mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCharged){
                    Intent intent = new Intent(getBaseContext(), DetallEvent.class);
                    intent.putExtra("codi",detallEventList.get(posicio).getCodi());
                    startActivity(intent);
                }
            }
        });
    }
    private String getExtras(Bundle savedInstanceState){
        String newString;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                newString= null;
            } else {
                newString= extras.getString("data");
            }
        } else {
            newString= (String) savedInstanceState.getSerializable("data");
        }
        return newString;
    }
    private void initData() {
        detallEventList.add(new DetallEvent("Exposició Japonesa","http://www.agendaolot.cat/wp-content/uploads/Expo_Cer%C3%A0mica_Japonesa.jpg"));
    }
}
