package com.ArbitCode.ecomerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.ArbitCode.ecomerce.Model.Users;
import com.ArbitCode.ecomerce.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    private Button joinNowButton,loginButton;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        joinNowButton=(Button) findViewById(R.id.main_join_now_btn);
        loginButton=(Button) findViewById(R.id.main_login_btn);
        loadingBar= new ProgressDialog(this);


        Paper.init(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity .this,LoginActivity.class);
                startActivity(intent );
            }
        });

        joinNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        String UserPhoneKey=Paper.book().read(Prevalent.userPhonekey);
        String UserPasswordKey=Paper.book().read(Prevalent.userPassword);

        //Todo Remember Me Code
        if(UserPhoneKey!=""&& UserPasswordKey!=""){
            if(!TextUtils.isEmpty(UserPasswordKey) && !TextUtils.isEmpty(UserPhoneKey)){
                AllowAccess(UserPasswordKey,UserPhoneKey);


                loadingBar.setTitle("Already Logged in");
                loadingBar.setMessage("please wait...");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }

    }

    private void AllowAccess(final String password, final  String phone) {
        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String parentDbName="Users";
                if(snapshot.child(parentDbName).child(phone).exists()){

                    Users userData= snapshot.child(parentDbName).child(phone).getValue(Users.class);

                    if(userData.getPhone().equals(phone)){
                        if(userData.getPassword().equals(password)){
                            Toast.makeText(MainActivity.this, "Logged in Successfully!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent =new Intent(MainActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }
                        else{
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "ohh.. Login in again!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Login Again", Toast.LENGTH_SHORT).show();
                    }

                }
                else{
                    Toast.makeText(MainActivity.this, "Account does not exists! Please create Account", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                    Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}