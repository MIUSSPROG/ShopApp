package com.example.shopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.shopapp.Model.Upload;
import com.example.shopapp.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignIn extends AppCompatActivity {

    ActivitySignInBinding binding;
    private DatabaseReference mDatabaseRef;
    FirebaseAuth auth;
    SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();

        binding.btnGoToSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        });

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.etEmail.getText().toString();
                String pass = binding.etPass.getText().toString();

                if(!email.isEmpty() && !pass.isEmpty()){
                    auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(SignIn.this, "Вход успешный!", Toast.LENGTH_SHORT).show();

                                mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users").child(auth.getUid());
                                mDatabaseRef.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (!task.isSuccessful()) {
                                            Toast.makeText(SignIn.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            DataSnapshot snapshot = task.getResult();
                                            Upload upload = snapshot.getValue(Upload.class);
                                            sharedPref = getSharedPreferences("userInfo", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putString("name", upload.getName());
                                            editor.putString("imageURL", upload.getUserAvatarUrl());
                                            editor.apply();

                                            startActivity(new Intent(SignIn.this, MainActivity.class));
                                        }
                                    }
                                });

                            }
                            else{
                                Toast.makeText(SignIn.this, "Неверные данные", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}