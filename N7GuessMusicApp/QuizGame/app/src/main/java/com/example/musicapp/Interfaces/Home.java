package com.example.musicapp.Interfaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.musicapp.Entities.User;
import com.example.musicapp.R;

public class Home extends AppCompatActivity {
    Button create , join ;
    User user ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        create = findViewById(R.id.createB);
        join = findViewById(R.id.joinB);
        Intent intent = this.getIntent();
        Bundle bundle = intent.getExtras();
        user = (User) bundle.getSerializable("USER");
        create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("USER", user);
                Intent intentt = new Intent(getApplicationContext(), Create.class);
                intentt.putExtras(bundle);
                startActivity(intentt);
                finish();
            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("USER", user);
                Intent intentt = new Intent(getApplicationContext(), Join.class);
                intentt.putExtras(bundle);
                startActivity(intentt);
                finish();
            }
        });
    }
}
