package net.bosccoma.info.engrescat;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by JordiC on 27/04/2018.
 */

public class EventAdapter extends BaseAdapter{
private List<Event> eventList;
private Context mContext;

    public EventAdapter(List<Event> eventList, Context mContext) {
        this.eventList = eventList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return eventList.size();
    }

    @Override
    public Object getItem(int i) {
        return eventList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = view;
        if (rowView == null){
            rowView = LayoutInflater.from(mContext).inflate(R.layout.layout_item,null);
            TextView name = (TextView)rowView.findViewById(R.id.label);
            ImageView imageView = (ImageView)rowView.findViewById(R.id.image);

            //Definir el contingut - Set Data
            Picasso.with(mContext).load(eventList.get(i).getImageURL()).into(imageView);
            name.setText(eventList.get(i).getName());
        }
        return rowView;
    }
}
