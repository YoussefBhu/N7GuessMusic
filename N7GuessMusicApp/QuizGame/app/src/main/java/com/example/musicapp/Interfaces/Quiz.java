package com.example.musicapp.Interfaces;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Quiz extends AppCompatActivity {
    TextView name , code ;
    ImageButton play , replay ;
    Button choice1  , choice2 , choice3 ;
    QuizObject quiz ;
    Song s ;
    MediaPlayer  mediaPlayer ;
    AlertDialog.Builder alertDialogBuilder ;
    boolean pressed = false;
    RequestQueue queue ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);
        alertDialogBuilder = new AlertDialog.Builder(this);
        name = findViewById(R.id.name);
        code = findViewById(R.id.code);
        play = findViewById(R.id.play);
        replay = findViewById(R.id.replay);
        choice1 = findViewById(R.id.choice1);
        choice2 = findViewById(R.id.choice2);
        choice3 = findViewById(R.id.choice3);
        queue = Volley.newRequestQueue(this);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        quiz = (QuizObject) bundle.getSerializable("quiz");
        s = (Song) quiz.getPlaylist().get(quiz.getCurrent()) ;
        String url = "http://192.168.1.7/Music/"+s.getName()+".mp3";
        name.append(quiz.getUser().getName());
        code.append(quiz.getCode());
        choice1.setText(s.getChoices().get(0));
        choice2.setText(s.getChoices().get(1));
        choice3.setText(s.getChoices().get(2));
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            mediaPlayer.setDataSource(url);

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        play.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                    play.setImageDrawable(getResources().getDrawable(R.drawable.pause));

                }
                else{
                    mediaPlayer.pause();
                    play.setImageDrawable(getResources().getDrawable(R.drawable.play));
                }

            }
        });
        replay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mediaPlayer.stop();
                mediaPlayer.prepareAsync();
                play.setImageDrawable(getResources().getDrawable(R.drawable.play));
                mediaPlayer.start();
            }
        });
        choice1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkAnswer(choice1.getText().toString(),s.getName())){
                    quiz.setScore(quiz.getScore() + 50);
                }
                mediaPlayer.stop();
                try {
                    goNext();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        choice2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkAnswer(choice2.getText().toString(),s.getName())){
                    quiz.setScore(quiz.getScore() + 50);
                }
                mediaPlayer.stop();
                try {
                    goNext();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        choice3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkAnswer(choice3.getText().toString(),s.getName())){
                    quiz.setScore(quiz.getScore() + 50);
                }
                mediaPlayer.stop();
                try {
                    goNext();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


    }


    public void  goNext() throws JSONException {
        if((quiz.getCurrent() + 1) == quiz.getPlaylist().size()){
       /*     System.out.println(quiz.getScore());
            alertDialogBuilder.create();
            alertDialogBuilder.setTitle("Alert");
            alertDialogBuilder.setMessage("Your final score"+quiz.getScore());
            alertDialogBuilder.show(); */
            SendScore();
        }
        else {
            quiz.setCurrent(quiz.getCurrent() + 1);
            Bundle bundle = new Bundle();
            Intent intent = new Intent(getApplicationContext(), Quiz.class);
            bundle.putSerializable("quiz", quiz);
            intent.putExtras(bundle);
            startActivity(intent);
            finish();
        }

    }
    public Boolean checkAnswer(String A , String r){
        if(A.equals(r)){
            return true ;
        }
        else{
            return false ;
        }
    }

    public void SendScore() throws JSONException {
        String url = "http://192.168.1.7:3000/Score";
        JSONObject rootObj = new JSONObject();
        rootObj.put("userid",quiz.getUser().getId());
        rootObj.put("code",quiz.getCode());
        rootObj.put("score",quiz.getScore());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,rootObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getInt("success") == 1 ){
                                Bundle bundle = new Bundle();
                                User user = quiz.getUser();
                                String code = quiz.getCode();
                                Intent intent = new Intent(getApplicationContext(), Scores.class);
                                bundle.putSerializable("USER", user);
                                bundle.putString("code", code);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                            }
                            else {
                                alertDialogBuilder.create();
                                alertDialogBuilder.setTitle("Alert");
                                alertDialogBuilder.setMessage(response.getString("Message"));
                                alertDialogBuilder.show();
                                alertDialogBuilder.setNeutralButton( "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Bundle bundle = new Bundle();
                                                User user = quiz.getUser();
                                                String code = quiz.getCode();
                                                Intent intent = new Intent(getApplicationContext(), Scores.class);
                                                bundle.putSerializable("USER", user);
                                                bundle.putSerializable("code", code);
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
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
                alertDialogBuilder.setMessage("server error");
                alertDialogBuilder.show();
            }
        });

        queue.add(request);

    }
}
