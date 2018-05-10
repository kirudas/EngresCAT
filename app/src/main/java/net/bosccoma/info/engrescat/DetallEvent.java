package net.bosccoma.info.engrescat;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridLayout;

/**
 * Created by JordiC on 27/04/2018.
 */

public class DetallEvent extends AppCompatActivity {
    private String Name, ImageURL;
    private String prova = "https://analisi.transparenciacatalunya.cat/resource/ta2y-snj2.json?$select=descripcio,%20subt_tol,%20imatges,%20horari%20&$where=codi%20=20180315003";
    public DetallEvent(){

    }
    public DetallEvent(String name, String imageURL){
        Name = name;
        ImageURL = imageURL;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

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
