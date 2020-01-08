/*An adapter used for displaying data for LeagueActivity
 * Contains Manager's money; teams' names, points, and worth; button that allows
 * manager to buy a team*/
package com.example.sportmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LeagueAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private int pos;
    private League selectedLeague;
    private Button button;
    private Context context;

    private final String TOAST_MESSAGE = "Not enough money";

    LeagueAdapter(Context c, int pos){
        this.context = c;
        this.inflater = LayoutInflater.from(c);
        this.pos = pos;

        selectedLeague = League.getAllLeagues().get(pos);
    }

    @Override
    public int getCount() { return League.getAllLeagues().get(pos).getTeams().size();}

    @Override
    public Object getItem(int position) {return position;}

    @Override
    public long getItemId(int position) {return  position;}

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.activity_league, parent, false);

        //Text with Manager's money. It's attached to the 0th cell in the grid
        TextView tv = convertView.findViewById(R.id.managerMoney);
        if(position == 0) tv.setText("$: " + Manager.getMoney());

        //Team's name
        TextView tv2 = convertView.findViewById(R.id.name);
        tv2.setText(selectedLeague.getTeamTable().get(position).getTeamName());

        //Team's points
        TextView tv3 = convertView.findViewById(R.id.points);
        tv3.setText("points:\n" + selectedLeague.getTeamTable().get(position).getPoints());

        //Team's worth
        TextView tv4 = convertView.findViewById(R.id.worth);
        tv4.setText("    $" + selectedLeague.getTeamTable().get(position).getMoney());

        //Button that allows Manager to buy a team
        button = convertView.findViewById(R.id.buy);
        for (Team t : Manager.getManagerTeams()) {
            if (t.getTeamID() == selectedLeague.getTeamTable().get(position).getTeamID()) button.setEnabled(false);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cost = selectedLeague.getTeamTable().get(position).getMoney();
                int mMoney = Manager.getMoney();
                if (cost < 0) cost = 0; //If team's worth is less than 0, the cost of that team is 0
                if(cost <= mMoney) {
                    Manager.addTeam(selectedLeague.getTeamTable().get(position));
                    button.setEnabled(false);
                    Manager.subtractMoney(cost);
                    notifyDataSetChanged();
                }
                if(cost > mMoney) Toast.makeText(context.getApplicationContext(), TOAST_MESSAGE,
                        Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
