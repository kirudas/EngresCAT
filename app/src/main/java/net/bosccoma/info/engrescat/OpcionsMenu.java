package net.bosccoma.info.engrescat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/***
 * Classe que crea la pantalla amb les opcions que té l'app fins el moment
 */
public class OpcionsMenu extends AppCompatActivity {
    //Taula on hi van cada una de les opcions
    GridLayout mainGrid;

    /***
     * Mètode que s'executa quan creem OpcionsMenu i hi associem la vista
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Inicialitza l'Activity i s'associa amb la vista
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcions_menu);
//      Associar el grid de la pantalla a una variable per treballar sobre ella
        mainGrid = (GridLayout) findViewById(R.id.mainGrid);

        //Set Event
        // execució del mètode
        setSingleEvent(mainGrid);
        //setToggleEvent(mainGrid);
    }

    /***
     * Mètode que no estem utilizant però faria que cada vegada que clickis sobre un cardview del grid canvies de color Tronja
     * @param mainGrid
     */
    private void setToggleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            final CardView cardView = (CardView) mainGrid.getChildAt(i);
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (cardView.getCardBackgroundColor().getDefaultColor() == -1) {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FF6F00"));
                        Toast.makeText(OpcionsMenu.this, "State : True", Toast.LENGTH_SHORT).show();

                    } else {
                        //Change background color
                        cardView.setCardBackgroundColor(Color.parseColor("#FFFFFF"));
                        Toast.makeText(OpcionsMenu.this, "State : False", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    /***
     * Mètode en el que assignem que farà cada un dels cardview del mainGrid
     * @param mainGrid
     */
    private void setSingleEvent(GridLayout mainGrid) {
        //Per tots els fills del grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //Assignem un listener segons quin fill sigui del grid
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    switch (finalI) {
                        //Primer CardView carregarà la llista d'events amb els proxims events
                        case 0:
                            Intent intent = new Intent(getBaseContext(), LlistaEventsActivity.class);
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String data = dateFormat.format(new Date());
                            Calendar c = Calendar.getInstance();
                            c.add(Calendar.DAY_OF_MONTH, 5);
                            String datafin = dateFormat.format(c.getTime());
                            String consulta = String.format("data_inici>=\"%s\" AND data_inici<=\"%s\" AND data_fi>=\"%s\" AND data_fi<=\"%s\" ", data, datafin, data, datafin);
                            intent.putExtra("data", consulta);
                            startActivity(intent);
                            break;
                        //Segon CardView obrirà la pantalla de Cerca Avançada
                        case 1:
                            Intent intent1 = new Intent(OpcionsMenu.this, AdvancedSearch.class);
                            startActivity(intent1);
                            break;
                        //Tercer CardView tornarà a la pantalla inicial desfent-se de la pila d'Activities
                        case 2:
                            Intent intent2 = new Intent(OpcionsMenu.this, LoginActivity.class);
                            intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent2);
                            break;
                        //Quart CardView obrirà la pantalla on hi ha perfil
                        case 3:
                            Intent intent3 = new Intent(OpcionsMenu.this, Profile.class);
                            startActivity(intent3);
                            break;
                    }
                }
            });
        }
    }
}
