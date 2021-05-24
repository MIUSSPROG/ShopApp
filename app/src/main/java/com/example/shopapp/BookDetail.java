package com.example.shopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;

import com.bumptech.glide.Glide;
import com.example.shopapp.Model.Book;
import com.example.shopapp.databinding.ActivityProductDetailBinding;

public class BookDetail extends AppCompatActivity {

    ActivityProductDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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

    }
}