package com.example.musicapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.musicapp.Entities.Score;
import com.example.musicapp.R;

import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    ArrayList<Score> scores = new ArrayList<Score>();
    Context context;
    LayoutInflater layoutInflater;
    public Adapter(ArrayList<Score> list, Context context) {
        this.scores = list;
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return scores.size();
    }

    @Override
    public Object getItem(int position) {
        return scores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = layoutInflater.inflate(R.layout.activity_i_score, null, true);
        TextView name = view.findViewById(R.id.name);
        TextView score = view.findViewById(R.id.score);
        TextView rank = view.findViewById(R.id.rank);
        Score s = (Score) getItem(position);
        name.setText(s.getName());
        score.setText(""+s.getScore());
        rank.setText(""+(position + 1) );
        return  view ;
    }
}
