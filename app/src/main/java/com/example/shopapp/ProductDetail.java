package com.example.shopapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.shopapp.Model.Product;
import com.example.shopapp.databinding.ActivityProductDetailBinding;

public class ProductDetail extends AppCompatActivity {

    ActivityProductDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProductDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Product product = getIntent().getParcelableExtra("productInfo");

        Glide.with(this)
                .load(product.getImageURL())
                .fitCenter()
                .centerCrop()
                .into(binding.prodImage);

        binding.prodName.setText(product.getName());
        binding.prodPrice.setText( "$ " + String.valueOf(product.getPrice()));
    }
}