package net.bosccoma.info.engrescat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * Classe que et permet crear un filtre personalizat per buscar els events. Després t'envia a la
 * Llista d'events on veus els events que compleixen el teu filtre.
 */
public class AdvancedSearch extends AppCompatActivity implements View.OnClickListener {
    // Inputs del formulari
    private EditText DataInici, DataFi, paraulaClau;
    // camp autocompletat
    private AutoCompleteTextView autoComplete;
    //adaptador del camp autocompletat ja que internament té una llista
    private ArrayAdapter<String> adapter;
    // camp autocompletat
    private AutoCompleteTextView categoria;
    // Checkbox per dir si vols aplicar cada un dels filtres
    private CheckBox checkCategoria, checkClau, checkData;
    // Butó que t'enviarà a llistaEvents juntament amb la peticiò amb el filtres que t'has personalizat
    private Button btnCercar;
    //String que contindrà dataInici i dataFi selecionat pero els guardem internament per agilitzar el seu tractament.
    private String inici, fi;

    /***
     * Mètode cridat a la creació de le pantalla
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
//      Inicialitza l'Activity i s'associa amb la vista
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_search);
//      Carreguem la barra superior personalitzan-la i activant el support per poder tenir menú
        Toolbar toolbar = (Toolbar) findViewById(R.id.advanced_toolbar);
        toolbar.bringToFront();
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        setSupportActionBar(toolbar);
//      Accedim als filtres de dates de la vista i associem els listeners
        DataInici = (EditText) findViewById(R.id.etDataInici);
        DataInici.setOnClickListener(this);
        DataFi = (EditText) findViewById(R.id.etDataFi);
        DataFi.setOnClickListener(this);
//      Carraguem les posibles categories d'un event en una variable
        String[] categories = getResources().getStringArray(R.array.categoria);
//      Creem un arrayAdapter amb aquestes categories
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, categories);
//      Configurem el filtre de buscar categories que s'autocompleta perquè tingui els camps amb els cuals
//      farà els autocompletats
        autoComplete = (AutoCompleteTextView) findViewById(R.id.autoCompleteCategoria);
        autoComplete.setAdapter(adapter);
        autoComplete.setThreshold(1);

//      Accedim als altres elements de la pantalla
        categoria = findViewById(R.id.autoCompleteCategoria);
        paraulaClau = findViewById(R.id.editTextClau);
        //Checkbox
        checkCategoria = findViewById(R.id.checkBoxCategoria);
        checkClau = findViewById(R.id.checkBoxClau);
        checkData = findViewById(R.id.checkBoxDates);
        //Button
        btnCercar = findViewById(R.id.btn_cercar);
//      Creem el listener del botó cercar.
        btnCercar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//      Creem un nou intent per anar a la LlistaEvents
                Intent intent = new Intent(getBaseContext(), LlistaEventsActivity.class);
//      Aplica els filtres segons si els checkbox estan checked o no i també comprova que no siguin camps buits
//      en cas de complir per cadascun es fa un putextra amb la seva part del filtre
                if (checkCategoria.isChecked()) {
                    String cat = categoria.getText().toString();
                    if (cat.length() > 0)
                        intent.putExtra("cat", String.format("tags_categor_es%%20LIKE%%20%%22%%25%s%%25%%22", cat));
                }
                if (checkClau.isChecked()) {
                    String paraula = paraulaClau.getText().toString();
                    if (paraula.length() > 0)
                        intent.putExtra("paraula", String.format("denominaci%%20LIKE%%20%%22%%25%s%%25%%22", paraula));
                }

                if (checkData.isChecked()) {

                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    if (DataInici.getText().toString().length() > 0 && DataFi.getText().toString().length() > 0) {
                        String any = DataInici.getText().toString().substring(6);
                        String dia = DataInici.getText().toString().substring(0, 2);
                        String mes = DataInici.getText().toString().substring(3, 5);

                        inici = any + '-' + mes + '-' + dia;

                        String anyF = DataFi.getText().toString().substring(6);
                        String diaF = DataFi.getText().toString().substring(0, 2);
                        String mesF = DataFi.getText().toString().substring(3, 5);

                        fi = anyF + '-' + mesF + '-' + diaF;

                        String consulta = String.format("data_inici>=\"%s\"%%20AND%%20data_inici<=\"%s\"%%20AND%%20data_fi>=\"%s\"", inici, fi, fi);
                        intent.putExtra("data", consulta);
                    }
                }
//      S'obre la Llista d'events
                startActivity(intent);

            }
        });

        //categoria.getText().toString();


        checkCategoria.isChecked();
        checkClau.isChecked();
        checkData.isChecked();
    }

    //      Carreguem un menú d'opcions
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_general, menu);
        return true;
    }

    //      Assignem que ha de fer el menú quan hi fan click
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.menu_item_opcions:
                Intent intent = new Intent(getBaseContext(), OpcionsMenu.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /***
     * Listener que s'encarrega de distingir quin filtre has clickat i d'obrir el DatePicker per
     * escollir una data de forma fàcil
     * @param view
     */
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.etDataInici:
                showDatePickerDialog(DataInici);
                break;

            case R.id.etDataFi:
                showDatePickerDialog(DataFi);
                break;
        }
    }

    /***
     * Mètode que obre el DatePicker
     * @param editText
     */
    private void showDatePickerDialog(final EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = twoDigits(day) + "-" + twoDigits(month + 1) + "-" + year;
                editText.setText(selectedDate);
            }
        });
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {
        return (n <= 9) ? ("0" + n) : String.valueOf(n);
    }


}
