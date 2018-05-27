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
import java.util.Date;

/**
 * A login screen that offers login via email/password.
 */

public class LoginActivity extends AppCompatActivity {

    static final int CAMERA_APP_CODE = 100;

    ImageButton btnFerFoto;
    ImageView imgPerfil;
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
                //EditText nom = (EditText) findViewById(R.id.txt_nom);
                //EditText cognoms = (EditText) findViewById(R.id.txt_cognoms);
                //setContentView(R.layout.activity_event);
                Intent intent = new Intent(getBaseContext(),AdvancedSearch.class);
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String data = dateFormat.format(new Date());
                String consulta = String.format("&$where=data_inici=\"%s\" AND data_fi>=\"%s\"",data,data);
                intent.putExtra("data",consulta);
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

    /**
     * Obre el menu
     *
     * @param nom     nom de l'usuari.
     * @param congoms cognoms de l'usuari.
     */

}
