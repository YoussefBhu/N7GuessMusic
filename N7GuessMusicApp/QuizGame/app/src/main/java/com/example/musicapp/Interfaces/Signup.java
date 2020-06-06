package com.example.musicapp.Interfaces;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.musicapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Signup extends AppCompatActivity {
    Button signup ;
    EditText username , password , name ;
    RequestQueue queue;
    AlertDialog.Builder alertDialogBuilder ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signup = findViewById(R.id.signupB);
        username = findViewById(R.id.usernamet);
        password = findViewById(R.id.passwordt);
        name = findViewById(R.id.namet);
        signup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String usernameV = username.getText().toString();
                String passwordV =  password.getText().toString();
                String nameV = name.getText().toString();
                if(usernameV.equals("") || passwordV.equals("") || nameV.equals("")){
                    alertDialogBuilder.create();
                    alertDialogBuilder.setTitle("Alert");
                    alertDialogBuilder.setMessage("Entre all the infos needed please");
                    alertDialogBuilder.show();
                }
                else {
                    try {
                        Signup(usernameV,passwordV,nameV);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        }

    public void Signup(String Username , String Password , String name) throws JSONException {
        String url = "http://192.168.1.7:3000/Registre";
        JSONObject Obj = new JSONObject();
        Obj.put("username",Username);
        Obj.put("password",Password);
        Obj.put("name",name);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,Obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            if(response.getInt("success") == 1 ){
                                alertDialogBuilder.create();
                                alertDialogBuilder.setTitle("Alert");
                                alertDialogBuilder.setMessage(response.getString("Message"));
                                alertDialogBuilder.setNeutralButton( "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                                Intent intent = new Intent(getApplicationContext(), Login.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        });
                                alertDialogBuilder.show();

                            }
                            else {
                                alertDialogBuilder.create();
                                alertDialogBuilder.setTitle("Alert");
                                alertDialogBuilder.setMessage(response.getString("Message"));
                                alertDialogBuilder.setNeutralButton( "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
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

            }
        });

        queue.add(request);

    }

    }

