package net.bosccoma.info.engrescat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

public class LlistaEventsActivity extends AppCompatActivity {
    private FeatureCoverFlow coverFlow;
    private EventAdapter eventAdapter;
    private List<DetallEvent> detallEventList = new ArrayList<>();
    private TextSwitcher mTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_llista_events);
        initData();
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
    private void initData() {

        detallEventList.add(new DetallEvent("Exposició Japonesa","http://www.agendaolot.cat/wp-content/uploads/Expo_Cer%C3%A0mica_Japonesa.jpg"));
        detallEventList.add(new DetallEvent("Emprimavera't","http://www.agendaolot.cat/wp-content/uploads/emprimaverataco1.jpg"));
        detallEventList.add(new DetallEvent("Tots Dansen","http://www.agendaolot.cat/wp-content/uploads/Tots-Dansen_1.jpg"));
        detallEventList.add(new DetallEvent("Exposició Japonesa","http://www.agendaolot.cat/wp-content/uploads/Expo_Cer%C3%A0mica_Japonesa.jpg"));
        detallEventList.add(new DetallEvent("Emprimavera't","http://www.agendaolot.cat/wp-content/uploads/emprimaverataco1.jpg"));
        detallEventList.add(new DetallEvent("Tots Dansen","http://www.agendaolot.cat/wp-content/uploads/Tots-Dansen_1.jpg"));
    }
}
