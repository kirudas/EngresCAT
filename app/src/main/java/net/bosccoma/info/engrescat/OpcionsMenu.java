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

public class OpcionsMenu extends AppCompatActivity {
    GridLayout mainGrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opcions_menu);
        mainGrid = (GridLayout) findViewById(R.id.mainGrid);

        //Set Event
        setSingleEvent(mainGrid);
        //setToggleEvent(mainGrid);
    }
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

    private void setSingleEvent(GridLayout mainGrid) {
        //Loop all child item of Main Grid
        for (int i = 0; i < mainGrid.getChildCount(); i++) {
            //You can see , all child item is CardView , so we just cast object to CardView
            CardView cardView = (CardView) mainGrid.getChildAt(i);
            final int finalI = i;
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    switch(finalI){
                        case 0:
                            Intent intent = new Intent(getBaseContext(),LlistaEventsActivity.class);
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                            String data = dateFormat.format(new Date());
                            Calendar c = Calendar.getInstance();
                            c.add(Calendar.DAY_OF_MONTH, 5);
                            String datafin = dateFormat.format(c.getTime());
                            String consulta = String.format("data_inici>=\"%s\" AND data_inici<=\"%s\" AND data_fi>=\"%s\" AND data_fi<=\"%s\" ",data,datafin,data,datafin);
                            intent.putExtra("data",consulta);
                            startActivity(intent);
                            break;

                        case 1:
                            Intent intent1 = new Intent(OpcionsMenu.this,AdvancedSearch.class);
                            //intent1.putExtra("info","This is activity from card item index  "+finalI);
                            startActivity(intent1);
                            break;

                        case 2:
                            Intent intent2 = new Intent(OpcionsMenu.this,LoginActivity.class);
                            //intent2.putExtra("info","This is activity from card item index  "+finalI);
                            startActivity(intent2);
                            break;

                        case 3:
                            Intent intent3 = new Intent(OpcionsMenu.this,Profile.class);
                        //intent3.putExtra("info","This is activity from card item index  "+finalI);
                            startActivity(intent3);
                            break;

                    }


                }
            });
        }
    }
}
