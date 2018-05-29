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
 */

public class EventAdapter extends BaseAdapter{
    private List<DetallEvent> detallEventList;
    private Context mContext;

    public EventAdapter(List<DetallEvent> detallEventList, Context mContext) {
        this.detallEventList = detallEventList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return detallEventList.size();
    }

    @Override
    public Object getItem(int i) {
        return detallEventList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (getCount() > 0){
            if (rowView == null){
                rowView = LayoutInflater.from(mContext).inflate(R.layout.layout_item,null);
                TextView name = (TextView)rowView.findViewById(R.id.label);
                ImageView imageView = (ImageView)rowView.findViewById(R.id.image);

                //Definir el contingut - Set Data
                Picasso.with(mContext).load(detallEventList.get(i).getImageURL()).into(imageView);
                name.setText(detallEventList.get(i).getName());

            }}
        return rowView;
    }

}
