package com.example.shopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.shopapp.Model.Book;
import com.example.shopapp.databinding.ActivityProductDetailBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class BookDetail extends AppCompatActivity {

    ActivityProductDetailBinding binding;
    private DatabaseReference mDatabaseRef;
    SharedPreferences sharedPref;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPref = getSharedPreferences("userInfo", MODE_PRIVATE);
        userId = sharedPref.getString("userId", "");

        Book book = getIntent().getParcelableExtra("bookInfo");

        Glide.with(this)
                .load(book.getImageURL())
                .fitCenter()
                .centerCrop()
                .into(binding.bookImageDetail);

        binding.bookNameDetail.setText(book.getName());
        binding.bookPriceDetail.setText(String.valueOf(book.getPrice() + " руб."));
        binding.bookDescDetail.setMovementMethod(new ScrollingMovementMethod());
        binding.bookDescDetail.setText(book.getDescription());

        binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users").child(userId).child("boughtBooks");
//                mDatabaseRef.setValue(book);
                String countRecords = String.valueOf(sharedPref.getAll().size()-2);
                Gson gson = new Gson();
                String bookJson = gson.toJson(book);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("bookJson" + countRecords, bookJson);
                editor.apply();
            }
        });

    }
}