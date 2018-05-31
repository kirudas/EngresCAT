package net.bosccoma.info.engrescat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

/**
 * Created by JordiC on 27/04/2018.
 * Adapter que s'encarregarà de mostrar les dades de cadascun dels events de la llista d'events
 */

public class EventAdapter extends BaseAdapter {
    //Llista d'events
    private List<DetallEvent> detallEventList;
    //Contexta de l'activity
    private Context mContext;

    /**
     * Constructor de la classe EventAdapter
     *
     * @param detallEventList indica la llista d'events
     * @param mContext        indica el contexta
     */
    public EventAdapter(List<DetallEvent> detallEventList, Context mContext) {
        this.detallEventList = detallEventList;
        this.mContext = mContext;
    }

    /**
     * Mètode que retorna el numero d'elements de la llista
     *
     * @return el numero d'events de la llista
     */
    @Override
    public int getCount() {
        return detallEventList.size();
    }

    /**
     * Mètode per obtenir un Objecte concret de la llista
     *
     * @param i index o posició de l'event que es vol
     * @return l'event de la posició i en la llista
     */
    @Override
    public Object getItem(int i) {
        return detallEventList.get(i);
    }

    /**
     * Mètode que retorna l'identificador de l'objecte desitjat de la llista
     *
     * @param i index o posició de l'event que es vol
     * @return l'identificador numeric de l'event que es vol
     */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /**
     * Mètode que s'encarrega de carregar les imatges i textos a les vistes
     *
     * @param i         index o posició de la vista que es vol
     * @param view      Vista que es carrega o tmb pot ser null i si és així la crearem nosaltres
     * @param viewGroup Pare de la vista, en el nostre cas es un RelativeLayout
     * @return
     */
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (getCount() > 0) {
            if (rowView == null) {
                rowView = LayoutInflater.from(mContext).inflate(R.layout.layout_item, null);
                TextView name = (TextView) rowView.findViewById(R.id.label);
                ImageView imageView = (ImageView) rowView.findViewById(R.id.image);

                //Definir el contingut - Set Data
                Picasso.with(mContext).load(detallEventList.get(i).getImageURL()).into(imageView);
                name.setText(detallEventList.get(i).getName());

            }
        }
        return rowView;
    }

}
