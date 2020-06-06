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
import com.example.musicapp.Entities.User;
import com.example.musicapp.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    Button submit;
    EditText username, password;
    RequestQueue queue;
    AlertDialog.Builder alertDialogBuilder ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        submit = findViewById(R.id.loginB);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        queue = Volley.newRequestQueue(this);
        alertDialogBuilder = new AlertDialog.Builder(this);
        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    String usernameV = username.getText().toString();
                    String passwordV =  password.getText().toString();
                    if(usernameV.equals("") || passwordV.equals("")){
                        alertDialogBuilder.create();
                        alertDialogBuilder.setTitle("Alert");
                        alertDialogBuilder.setMessage("Entre the username and password");
                        alertDialogBuilder.show();
                    }
                    else {
                        Login(usernameV,passwordV);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

  void Login(String Username , String Password) throws JSONException {
      String url = "http://192.168.1.7:3000/Connexion";
      JSONObject rootObj = new JSONObject();
      rootObj.put("username",Username);
      rootObj.put("password",Password);
      JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,rootObj,
              new Response.Listener<JSONObject>() {
                  @Override
                  public void onResponse(JSONObject response) {

                      try {
                          if(response.getInt("success") == 1 ){
                              Bundle bundle = new Bundle();
                              User user = new User(response.getInt("userid"),response.getString("name"));
                              Intent intent = new Intent(getApplicationContext(), Home.class);
                              bundle.putSerializable("USER", user);
                              intent.putExtras(bundle);
                              startActivity(intent);
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

          }
      });

      queue.add(request);

  }
}
