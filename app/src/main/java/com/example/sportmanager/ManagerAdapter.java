/*An adapter used for displaying data for ManagerActivity
 * Contains Manager's money; names of teams that the manager owns;
 * button that allows manager to invest(increase worth) in a team;
 * button that allows manager to sell a team*/
package com.example.sportmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ManagerAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private final String TOAST_MESSAGE = "Not enough money";
    private Context context;

    ManagerAdapter(Context c) {
        inflater = LayoutInflater.from(c);
        context = c;
    }

    @Override
    public int getCount() {return Manager.getManagerTeams().size();}

    @Override
    public Object getItem(int position) {return position;}

    @Override
    public long getItemId(int position) {return  position;}

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if(convertView == null) {
            convertView = inflater.inflate(R.layout.activity_manager, parent, false);

            holder = new ViewHolder();

            holder.tv = convertView.findViewById(R.id.mManagerMoney);
            holder.tv2 = convertView.findViewById(R.id.mName);
            holder.tv3 = convertView.findViewById(R.id.mWorth);
            holder.button = convertView.findViewById(R.id.mInvest);
            holder.button2 = convertView.findViewById(R.id.mSell);

            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        //Text with Manager's money. It's attached to the 0th cell in the grid
        if(position == 0) holder.tv.setText("$: " + Manager.getMoney());

        //Team's name
        holder.tv2.setText(Manager.getManagerTeams().get(position).getTeamName());

        //Team's worth
        holder.tv3.setText("worth:\n" + Manager.getManagerTeams().get(position).getMoney());

        //Invest button. Increases team's worth by 100
        //Removes 100 from manager's money
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Manager.getMoney() >= 100) {
                    Manager.getManagerTeams().get(position).increaseWorth();
                    Manager.subtractMoney(100);
                    notifyDataSetChanged();
                }
                else Toast.makeText(context.getApplicationContext(), TOAST_MESSAGE,
                        Toast.LENGTH_SHORT).show();
            }
        });

        //Sell button. It removes a team from manager's teams
        //Adds the team's worth to manager's money, if team's worth if greater than 0
        holder.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Team t = Manager.getManagerTeams().get(position);
                if(t.getMoney() > 0) Manager.addMoney(t.getMoney());
                Manager.removeTeam(t);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    //Reduces the number of findViewByID calls
    static class ViewHolder {
        TextView tv;
        TextView tv2;
        TextView tv3;
        Button button;
        Button button2;
    }
}
