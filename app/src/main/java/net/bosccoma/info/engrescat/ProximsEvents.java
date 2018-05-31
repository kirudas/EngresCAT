package net.bosccoma.info.engrescat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.time.Instant.now;

/**
 * Classe que més endavant tenim pensat que carregui els pròxims events d'una manera més estructurada
 * i no fer-ho utilitzant els putExtra del intents per passar la clausula where de la consulta a la
 * api.
 */
public class ProximsEvents extends AppCompatActivity {
    private String prova = "https://analisi.transparenciacatalunya.cat/resource/ta2y-snj2.json?$select=codi%20&$where=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proxims_events);
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String data = dateFormat.format(date);
        TextView textView = findViewById(R.id.idData);
        textView.setText(data);
    }
}
