package net.bosccoma.info.engrescat;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by JordiC on 27/04/2018.
 */

public class DetallEvent extends AppCompatActivity {
    private String Name, ImageURL;
    FloatingActionButton bt_share;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
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
    }
    public DetallEvent(String name, String imageURL){
        Name = name;
        ImageURL = imageURL;
    }

    public DetallEvent() {
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
