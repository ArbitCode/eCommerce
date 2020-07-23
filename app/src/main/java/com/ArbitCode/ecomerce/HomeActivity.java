package com.ArbitCode.ecomerce;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity {
    private Button LogoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        LogoutButton = (Button) findViewById(R.id.home_logout_btn);

        LogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Paper.book().destroy();
                Toast.makeText(HomeActivity.this, "Logged out", Toast.LENGTH_SHORT).show();
                Intent intent= new Intent(HomeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });



    }
}