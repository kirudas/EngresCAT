package net.bosccoma.info.engrescat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Joan on 08/05/2018.
 */

/***
 * Classe que de moment només carrega un Perfil estàtic però en un futur tenim pensat fer-lo funcional
 * i emplenar-lo amb les dades de l'usuari.
 */
public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }


}
