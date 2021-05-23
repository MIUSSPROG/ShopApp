package com.example.shopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.shopapp.Adapter.ProductAdapter;
import com.example.shopapp.Adapter.ProductCategoryAdapter;
import com.example.shopapp.Model.Product;
import com.example.shopapp.Model.ProductCategory;
import com.example.shopapp.databinding.ActivityMainBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
//import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding binding;
    ProductCategoryAdapter productCategoryAdapter;
    ProductAdapter productAdapter;
    List<Product> products;
    SharedPreferences sharedPref;

//    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPref = getSharedPreferences("userInfo", MODE_PRIVATE);
        String userName = sharedPref.getString("name", "");
        String userAvatarURL = sharedPref.getString("imageURL", "");

        if(userName.isEmpty()){
            binding.tvWelcome.append(", гость!");
        }
        else{
            binding.tvWelcome.append(", " + userName + "!");
            Glide.with(this).load(userAvatarURL).into(binding.imgvUserLogo);
        }

        binding.tvWelcomeExtraInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignIn.class));
            }
        });

        List<ProductCategory> productCategoryList = new ArrayList<>();
        productCategoryList.add(new ProductCategory(1, "Trending"));
        productCategoryList.add(new ProductCategory(2, "Most Popular"));
        productCategoryList.add(new ProductCategory(3, "All Body Products"));
        productCategoryList.add(new ProductCategory(4, "Skin Care"));
        productCategoryList.add(new ProductCategory(5, "Hair Care"));
        productCategoryList.add(new ProductCategory(6, "Make Up"));
        productCategoryList.add(new ProductCategory(7, "Fragrance"));

        productCategoryAdapter = new ProductCategoryAdapter(this, productCategoryList);
        binding.catRecycler.setAdapter(productCategoryAdapter);

        products = new ArrayList<>();
        productAdapter = new ProductAdapter(this);
        binding.prodItemRecycler.setAdapter(productAdapter);

        displayProductsByCat("Hair");

        binding.tvCatHair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayProductsByCat("Hair");
            }
        });

        binding.tvCatFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayProductsByCat("Face");
            }
        });


    }

    public void displayProductsByCat(String cat){

        binding.tvCatFace.setTextColor( getResources().getColor(R.color.normal_cat));
        binding.tvCatFace.setTextSize(16);
        binding.tvCatHair.setTextColor( getResources().getColor(R.color.normal_cat));
        binding.tvCatHair.setTextSize(16);
        binding.tvCatBody.setTextColor( getResources().getColor(R.color.normal_cat));
        binding.tvCatBody.setTextSize(16);
        binding.tvCatSkin.setTextColor( getResources().getColor(R.color.normal_cat));
        binding.tvCatSkin.setTextSize(16);

        switch (cat){
            case "Hair":
                binding.tvCatHair.setTextColor( getResources().getColor(R.color.selected_cat));
                binding.tvCatHair.setTextSize(18);
                break;
            case "Body":
                binding.tvCatBody.setTextColor( getResources().getColor(R.color.selected_cat));
                binding.tvCatBody.setTextSize(18);
                break;
            case "Face":
                binding.tvCatFace.setTextColor( getResources().getColor(R.color.selected_cat));
                binding.tvCatFace.setTextSize(18);
                break;
            case "Skin":
                binding.tvCatSkin.setTextColor( getResources().getColor(R.color.selected_cat));
                binding.tvCatSkin.setTextSize(18);
                break;
        }

        products.clear();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference(cat);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Product product = dataSnapshot.getValue(Product.class);
                    products.add(product);
                }
                productAdapter.setProductsList(products);
                productAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}