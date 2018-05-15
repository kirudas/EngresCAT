package net.bosccoma.info.engrescat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llista_events);
        v_ini_url += getExtras(savedInstanceState);
        initData();
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
                            for (int i = 0; i < cont; i++){
                                nom = jsonArray.getJSONObject(i).getString("denominaci");
                                imatge = jsonArray.getJSONObject(i).getString("imatges");
                                if (imatge.contains(","))
                                    imatge = imatge.substring(0,imatge.indexOf(','));
                                detallEventList.add(new DetallEvent(nom,"https://agenda.cultura.gencat.cat"+imatge));
                            }
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
        mTitle = (TextSwitcher)findViewById(R.id.title2);
        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(getBaseContext());
                TextView txt = (TextView) inflater.inflate(R.layout.layout_title,null);
                return  txt;
            }
        });

        Animation in = AnimationUtils.loadAnimation(this,R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(this,R.anim.slide_out_bottom);
        mTitle.setInAnimation(in);
        mTitle.setOutAnimation(out);


        //
        eventAdapter = new EventAdapter(detallEventList, this);
        coverFlow = (FeatureCoverFlow) findViewById(R.id.coverFlow);
        coverFlow.setAdapter(eventAdapter);

        coverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle.setText(detallEventList.get(position).getName());
            }

            @Override
            public void onScrolling() {

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
        detallEventList.add(new DetallEvent("Emprimavera't","http://www.agendaolot.cat/wp-content/uploads/emprimaverataco1.jpg"));
        detallEventList.add(new DetallEvent("Tots Dansen","http://www.agendaolot.cat/wp-content/uploads/Tots-Dansen_1.jpg"));
        detallEventList.add(new DetallEvent("Exposició Japonesa","http://www.agendaolot.cat/wp-content/uploads/Expo_Cer%C3%A0mica_Japonesa.jpg"));
        detallEventList.add(new DetallEvent("Emprimavera't","http://www.agendaolot.cat/wp-content/uploads/emprimaverataco1.jpg"));
        detallEventList.add(new DetallEvent("Tots Dansen","http://www.agendaolot.cat/wp-content/uploads/Tots-Dansen_1.jpg"));
    }
}
