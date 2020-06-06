package com.example.musicapp.Interfaces;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.musicapp.Entities.QuizObject;
import com.example.musicapp.Entities.Song;
import com.example.musicapp.Entities.User;
import com.example.musicapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Join extends AppCompatActivity {
    Button join ;
    EditText code ;
    User user ;
    RequestQueue queue;
    AlertDialog.Builder alertDialogBuilder   ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        join = findViewById(R.id.join);
        code = findViewById(R.id.codeText);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        alertDialogBuilder = new AlertDialog.Builder(this);
        queue = Volley.newRequestQueue(this);
        user = (User) bundle.getSerializable("USER");
        join.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Join(code.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public void Join(String code) throws JSONException {
        String url = "http://192.168.1.7:3000/Join";
        JSONObject rootObj = new JSONObject();
        rootObj.put("userid",user.getId());
        rootObj.put("code",code);
        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,rootObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        System.out.println(response.toString());

                        try {
                            if(response.getInt("success") == 1 ){
                                String code = response.getString("PartyCode");
                                String owner = response.getString("PartyOwner");
                                JSONArray array = response.getJSONArray("playlist");
                                ArrayList<Song> playlist = new ArrayList<Song>();
                                for (int i = 0 ; i<array.length() ; i++){
                                    JSONObject object = array.getJSONObject(i);
                                    String name = object.getString("name");
                                    String artist = object.getString("artist");
                                    JSONArray choices = object.getJSONArray("choices");
                                    ArrayList<String> Arraychoices = new ArrayList<String>();
                                    for(int x = 0 ; x<choices.length() ; x++){
                                        Arraychoices.add(choices.getString(x));
                                    }
                                    Song s = new Song (name, artist , Arraychoices);
                                    playlist.add(s);
                                }
                                QuizObject quiz = new QuizObject(code,owner,playlist);
                                System.out.println(quiz.getPlaylist().size());
                                quiz.setUser(user);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("quiz", quiz);
                                Intent intentt = new Intent(getApplicationContext(), Quiz.class);
                                intentt.putExtras(bundle);
                                startActivity(intentt);
                                finish();
                            }
                            else {
                                alertDialogBuilder.create();
                                alertDialogBuilder.setTitle("Alert");
                                alertDialogBuilder.setMessage(response.getString("Message"));
                                alertDialogBuilder.show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                alertDialogBuilder.create();
                alertDialogBuilder.setTitle("Alert");
                alertDialogBuilder.setMessage("there is a problem in server side try later");
                alertDialogBuilder.show();
            }
        });

        queue.add(request);
    }
}
