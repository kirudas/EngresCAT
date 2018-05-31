package net.bosccoma.info.engrescat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/***
 * Primera Activity de la Aplicació s'encarrega de fer el login dels usuaris o almenys era la nostre
 * intenció inicial
 */
public class LoginActivity extends AppCompatActivity {
    //Codi utilizat per obrir i tornar de la camera d'Android
    static final int CAMERA_APP_CODE = 100;
    //Botó per fer-se fotos i mostrar-les
    ImageButton btnFerFoto;
    //Imatge de perfil
    ImageView imgPerfil;
    //Botó per loginejar-te
    Button btnEntrar;


    /**
     * Realitza la inicialització de tots els fragments.
     *
     * @param savedInstanceState estat guardat de l'activitat.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        inicialitzarGui();
    }


    /**
     * Inicialitza els objectes de la GUI.
     */
    private void inicialitzarGui() {
        btnFerFoto = (ImageButton) findViewById(R.id.btn_foto);
        btnFerFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ferUnaFoto(v);
            }
        });
        imgPerfil = (ImageView) findViewById(R.id.img_perfil);
        btnEntrar = (Button) findViewById(R.id.btn_entrar);
        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//      Listener que obrira la llista d'events amb els pròxims events
                Intent intent = new Intent(getBaseContext(), LlistaEventsActivity.class);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String data = dateFormat.format(new Date());
                Calendar c = Calendar.getInstance();
                c.add(Calendar.DAY_OF_MONTH, 5);
                String datafin = dateFormat.format(c.getTime());
                String consulta = String.format("data_inici>=\"%s\" AND data_inici<=\"%s\" AND data_fi>=\"%s\" AND data_fi<=\"%s\" ", data, datafin, data, datafin);
                intent.putExtra("data", consulta);
                startActivity(intent);

            }
        });
    }

    /**
     * Obre l'aplicacio de la camera per fer una foto.
     *
     * @param view Vista actual.
     */
    public void ferUnaFoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, CAMERA_APP_CODE);
    }

    /**
     * En acabar l'execucio de l'aicacio de la camera, guarda la imatge.
     *
     * @param requestCode codi demanat.
     * @param resultCode  codi de resultat.
     * @param data        intent.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == requestCode && resultCode == RESULT_OK) {
            Bitmap foto = (Bitmap) data.getExtras().get("data");
            imgPerfil.setImageBitmap(foto);
            imgPerfil.setVisibility(View.VISIBLE);
        }
    }
}
