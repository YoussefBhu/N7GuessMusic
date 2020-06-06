package com.example.musicapp.Interfaces;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.musicapp.Adapter.Adapter;
import com.example.musicapp.Entities.Score;
import com.example.musicapp.Entities.User;
import com.example.musicapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Scores extends AppCompatActivity {
    ListView list ;
    Button refresh , home ;
    String code ;
    User user ;
    RequestQueue queue ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);
        Intent intent = this.getIntent();
        final Bundle bundle = intent.getExtras();
        list = findViewById(R.id.list);
        code = bundle.getString("code");
        user = (User) bundle.getSerializable("USER");
        queue = Volley.newRequestQueue(this);
        refresh = findViewById(R.id.refresh);
        home = findViewById(R.id.home);
        try {
            getScores(code);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        home.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                Intent intent = new Intent(getApplicationContext(), Home.class);
                bundle.putSerializable("USER", user);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();


            }
        });
        refresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Scores.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();


            }
        });

    }

    public void CInterface(ArrayList<Score> scores ){
        Adapter adapter = new Adapter(scores,this);
        list.setAdapter(adapter);
    }

    public void getScores(String code) throws JSONException {
        ArrayList<Score> scoresR ;
        String url = "http://192.168.1.7:3000/GetScores";
        JSONObject rootObj = new JSONObject();
        rootObj.put("code",code);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,rootObj,
                new Response.Listener<JSONObject>() {

                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ArrayList<Score> scoresl = new ArrayList<Score>();
                            JSONArray array = response.getJSONArray("scores");
                           for( int i = 0 ; i<array.length() ; i++){
                               System.out.println(i);
                               Score s = new Score(array.getJSONObject(i).getString("name"),array.getJSONObject(i).getInt("score"));
                               scoresl.add(s);
                           }
                            Collections.sort(scoresl, new Comparator<Score>() {
                                @Override
                                public int compare(Score o1, Score o2) {
                                    return  o2.getScore() - o1.getScore() ;
                                }

                            });
                            CInterface(scoresl);


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {

            }
        });

        queue.add(request);
    }
}
