/*An adapter used for displaying data for AllLeaguesActivity
* Contains image, name and type of the league*/
package com.example.sportmanager;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AllLeaguesAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;

    AllLeaguesAdapter(Context c) {
        inflater = LayoutInflater.from(c);
        this.context = c;
    }

    @Override
    public int getCount() {
        return League.getAllLeagues().size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.activity_allleagues, parent, false);

            //Gets the name of the league from the arrayList
            //Converts it to lowercase with no spaces
            String temp = ( League.getAllLeagues().get(position).getLeagueName());
            temp = temp.replace(" ", "");
            temp = temp.toLowerCase();

            //Sets the image for the league using the string created above
            ImageView iv = convertView.findViewById(R.id.imageView);
            Resources res = context.getResources();
            int resID = res.getIdentifier(temp, "drawable", context.getPackageName());
            iv.setImageResource(resID);

            //Sets the name for the league
            TextView tv = convertView.findViewById(R.id.textView);
            tv.setText(League.getAllLeagues().get(position).getLeagueName());

            //Sets the type of the league
            //Either PandR (Promotion and Relegation) or Franchise
            TextView tv2 = convertView.findViewById(R.id.textView2);
            tv2.setText(League.getAllLeagues().get(position).getLeagueType());
        }
        return convertView;
    }
}
