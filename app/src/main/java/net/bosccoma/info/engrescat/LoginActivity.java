package net.bosccoma.info.engrescat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toolbar;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */

public class LoginActivity extends AppCompatActivity {

    static final int CAMERA_APP_CODE = 100;

    private FeatureCoverFlow coverFlow;
    private EventAdapter eventAdapter;
    private List<Event> eventList = new ArrayList<>();
    private TextSwitcher mTitle;

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

        initData();
        mTitle = (TextSwitcher)findViewById(R.id.title2);
        mTitle.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                LayoutInflater inflater = LayoutInflater.from(LoginActivity.this);
                TextView txt = (TextView) inflater.inflate(R.layout.layout_title,null);
                return  txt;
            }
        });

        Animation in = AnimationUtils.loadAnimation(this,R.anim.slide_in_top);
        Animation out = AnimationUtils.loadAnimation(this,R.anim.slide_out_bottom);
        mTitle.setInAnimation(in);
        mTitle.setOutAnimation(out);


        //
        eventAdapter = new EventAdapter(eventList, this);
        coverFlow = (FeatureCoverFlow) findViewById(R.id.coverFlow);
        coverFlow.setAdapter(eventAdapter);

        coverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                mTitle.setText(eventList.get(position).getName());
            }

            @Override
            public void onScrolling() {

            }
        });

    }

    private void initData() {

        eventList.add(new Event("Exposició Japonesa","http://www.agendaolot.cat/wp-content/uploads/Expo_Cer%C3%A0mica_Japonesa.jpg"));
        eventList.add(new Event("Emprimavera't","http://www.agendaolot.cat/wp-content/uploads/emprimaverataco1.jpg"));
        eventList.add(new Event("Tots Dansen","http://www.agendaolot.cat/wp-content/uploads/Tots-Dansen_1.jpg"));
        eventList.add(new Event("Exposició Japonesa","http://www.agendaolot.cat/wp-content/uploads/Expo_Cer%C3%A0mica_Japonesa.jpg"));
        eventList.add(new Event("Emprimavera't","http://www.agendaolot.cat/wp-content/uploads/emprimaverataco1.jpg"));
        eventList.add(new Event("Tots Dansen","http://www.agendaolot.cat/wp-content/uploads/Tots-Dansen_1.jpg"));
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
                EditText nom = (EditText) findViewById(R.id.txt_nom);
                EditText cognoms = (EditText) findViewById(R.id.txt_cognoms);
                setContentView(R.layout.activity_event);

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
