package com.example.shopapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.example.shopapp.Model.Upload;
import com.example.shopapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class SignUp extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 101;
    ActivitySignUpBinding binding;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    FirebaseAuth auth;
    Uri chosenImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.imgvChooseUserLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });

        auth = FirebaseAuth.getInstance();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        mStorageRef = FirebaseStorage.getInstance().getReference("userAvatar");
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.etEmail.getText().toString();
                String password = binding.etPass.getText().toString();
                String username = binding.etName.getText().toString();
                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            StorageReference fileReference = mStorageRef.child(userid + "." + getFileExtension(chosenImageUri));
                            UploadTask uploadTask = fileReference.putFile(chosenImageUri);

                            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                                @Override
                                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                    if(!task.isSuccessful()){
                                        throw task.getException();
                                    }

                                    return fileReference.getDownloadUrl();
                                }
                            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Upload upload = new Upload(username, task.getResult().toString(), email);
                                    mDatabaseRef.child(userid).setValue(upload);
                                    Toast.makeText(SignUp.this, "Данные успешно сохранены!", Toast.LENGTH_SHORT).show();
                                }
                            });

                            startActivity(new Intent(SignUp.this, SignIn.class));
                        }
                        else{
                            Toast.makeText(SignUp.this, "Вы не можете зарегистрироваться с такими Email и Паролем", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case PICK_IMAGE_REQUEST:
                if (resultCode == RESULT_OK && data != null && data.getData() != null){
                   chosenImageUri = data.getData();
                   binding.imgvChooseUserLogo.setImageURI(chosenImageUri);
                   binding.imgvUserAvatarCameraRight.setVisibility(View.INVISIBLE);
                }
        }
    }
}