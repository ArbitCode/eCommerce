package com.ArbitCode.ecomerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private Button createAccountButton;
    private EditText InputName,  InputPhoneNumber, InputPassword, InputEmail;
    private CountryCodePicker ccp;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccountButton = (Button) findViewById(R.id.register_btn);
        InputName=(EditText) findViewById(R.id.register_name_input);
        InputPhoneNumber =(EditText) findViewById(R.id.register_phone_number_input);
        InputPassword = (EditText) findViewById(R.id.register_password_input);
        ccp=(CountryCodePicker) findViewById(R.id.register_ccp);
        InputEmail=(EditText) findViewById(R.id.register_email_input);
        loadingBar= new ProgressDialog(this);


        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateAccount();
            }
        });
    }
    private void CreateAccount(){
        String phone=InputPhoneNumber.getText().toString();
        String name=InputName.getText().toString();
        String password=InputPassword.getText().toString();
        String countryCode=ccp.getSelectedCountryCode();
        String email=InputEmail.getText().toString();


         if(TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(this, "Please Enter valid Email address", Toast.LENGTH_SHORT).show();
        }

        else if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Write your name", Toast.LENGTH_SHORT).show();
        }
        else if(phone.length()<10){
            Toast.makeText(this, "Please Write your valid number", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("please wait! We are checking the credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateAccount(name,phone,email,password,countryCode);
         }
    }

    private void ValidateAccount(final String name, final String phone, final String email, final String password, final String countryCode) {

            final DatabaseReference RootRef;
            RootRef= FirebaseDatabase.getInstance().getReference();

            RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(!(snapshot.child("Users").child(phone).exists())){
                        // create Account
                        HashMap<String,Object> userDataMap = new HashMap<>();
                         userDataMap.put("phone",phone);
                        userDataMap.put("email",email);
                        userDataMap.put("name",name);
                        userDataMap.put("countryCode",countryCode);
                        userDataMap.put("password",password);

                        RootRef.child("Users").child(phone).updateChildren(userDataMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                         if(task.isSuccessful()){
                                             Toast.makeText(RegisterActivity.this, "Congrats! Your account has been created", Toast.LENGTH_SHORT).show();
                                             loadingBar.dismiss();

                                             Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                             startActivity(intent);
                                         }
                                         else{
                                             loadingBar.dismiss();
                                             Toast.makeText(RegisterActivity.this, "Network Error: Please Try Again", Toast.LENGTH_SHORT).show();
                                         }

                                    }
                                });

                    }
                    else{
                        Toast.makeText(RegisterActivity.this, "This mobile number already exists", Toast.LENGTH_SHORT).show();
                        loadingBar.dismiss();
                        Intent intent =new Intent(RegisterActivity.this,MainActivity.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    }
}