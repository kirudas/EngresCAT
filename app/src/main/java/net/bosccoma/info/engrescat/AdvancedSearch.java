package net.bosccoma.info.engrescat;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class AdvancedSearch extends AppCompatActivity implements View.OnClickListener {

private EditText DataInici, DataFi,paraulaClau;
private AutoCompleteTextView autoComplete;
private ArrayAdapter<String> adapter;
private AutoCompleteTextView categoria;
private CheckBox checkCategoria, checkClau, checkData;
private Button btnCercar;
private  String  inici,fi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advanced_search);
        DataInici = (EditText) findViewById(R.id.etDataInici);
        DataInici.setOnClickListener(this);

        DataFi = (EditText) findViewById(R.id.etDataFi);
        DataFi.setOnClickListener(this);

        String[] categories = getResources().getStringArray(R.array.categoria);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,categories);
        autoComplete = (AutoCompleteTextView) findViewById(R.id.autoCompleteCategoria);
        autoComplete.setAdapter(adapter);
        autoComplete.setThreshold(1);

        //Valors dels camps omplerts per l'usuari
        categoria = findViewById(R.id.autoCompleteCategoria);
        paraulaClau = findViewById(R.id.editTextClau);

        //Checkbox
        checkCategoria = findViewById(R.id.checkBoxCategoria);
        checkClau = findViewById(R.id.checkBoxClau);
        checkData = findViewById(R.id.checkBoxDates);

        btnCercar = findViewById(R.id.btn_cercar);
        btnCercar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(getBaseContext(), LlistaEventsActivity.class);
                 if (checkCategoria.isChecked()){
                     String cat = categoria.getText().toString();
                     if (cat.length() > 0)
                     intent.putExtra("cat",String.format("tags_categor_es LIKE \"%%s%\"",cat));
                 }
                 if (checkClau.isChecked()){
                     String paraula = paraulaClau.getText().toString();
                     if (paraula.length() > 0)
                     intent.putExtra("paraula",String.format("denominaci LIKE \"%%s%\"",paraula));
                 }

                 if(checkData.isChecked()) {

                     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    if (DataInici.getText().toString().length() > 0 && DataFi.getText().toString().length() > 0){
                     String any = DataInici.getText().toString().substring(6);
                     String dia = DataInici.getText().toString().substring(0,2);
                     String mes = DataInici.getText().toString().substring(3,5);

                     inici = any+'-'+mes+'-'+dia;

                     String anyF = DataFi.getText().toString().substring(6);
                     String diaF = DataFi.getText().toString().substring(0,2);
                     String mesF = DataFi.getText().toString().substring(3,5);

                     fi = anyF+'-'+mesF+'-'+diaF;

                     String consulta = String.format("data_inici>=\"%s\" AND data_inici<=\"%s\" AND data_fi>=\"%s\"", inici,fi, fi);
                     intent.putExtra("data", consulta);}
                 }
                 startActivity(intent);

             }
        });

        //categoria.getText().toString();


        checkCategoria.isChecked();
        checkClau.isChecked();
        checkData.isChecked();
    }




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

    private void showDatePickerDialog(final EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because january is zero
                final String selectedDate = twoDigits(day) + "-" + twoDigits(month+1) + "-" + year;
                editText.setText(selectedDate);
            }
        });
        newFragment.show(getFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {
        return (n<=9) ? ("0"+n) : String.valueOf(n);
    }


}
