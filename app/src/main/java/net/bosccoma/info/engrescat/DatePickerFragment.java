package net.bosccoma.info.engrescat;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by JordiC on 24/05/2018.
 */

/***
 * Classe que crea un Fragment d'un calendari per escullir una data.
 */
public class DatePickerFragment extends DialogFragment {
    // Listener de quan la data a estat escollida
    private DatePickerDialog.OnDateSetListener listener;

    /***
     * Mètode que crea un nou Fragment DatePickerFragment
     * @param listener Listener de quan la data a estat escollida
     * @return
     */
    public static DatePickerFragment newInstance(DatePickerDialog.OnDateSetListener listener) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setListener(listener);
        return fragment;
    }

    /***
     * Setter del listener
     * @param listener OnDateSetListener que vols assignar al DatePicker
     */
    public void setListener(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    /***
     * Mètode que s'invoca a la creació del fragment i obre el Dialeg amb un Calendari
     * @param savedInstanceState conté la informació de l'activity que crea el DatePiclerFragment
     * @return retorna la data seleccionada
     */
    @Override
    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }
}
