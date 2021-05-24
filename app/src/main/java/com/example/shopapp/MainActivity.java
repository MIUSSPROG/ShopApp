package com.example.shopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.shopapp.Adapter.BookAdapter;
import com.example.shopapp.Adapter.BookCategoryAdapter;
import com.example.shopapp.Model.Book;
import com.example.shopapp.Model.BookCategory;
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
    BookCategoryAdapter bookCategoryAdapter;
    BookAdapter bookAdapter;
    List<Book> books;
    SharedPreferences sharedPref;
    private DatabaseReference mDatabaseRef;
    String userName, userAvatarURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPref = getSharedPreferences("userInfo", MODE_PRIVATE);
        userName = sharedPref.getString("name", "");
        userAvatarURL = sharedPref.getString("imageURL", "");

        if(userName.isEmpty()){
            binding.tvWelcome.append(", гость!");
        }
        else{
            binding.tvWelcomeSignInUp.setText("Выйти");
            binding.tvWelcome.append(", " + userName + "!");
            Glide.with(this).load(userAvatarURL).into(binding.imgvUserLogo);
        }

        binding.tvWelcomeSignInUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userName.isEmpty()) {
                    startActivity(new Intent(MainActivity.this, SignIn.class));
                }
                else{
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("name","");
                    editor.putString("imageURL","");
                    editor.apply();

                    binding.tvWelcome.setText("Привет, гость!");
                    binding.imgvUserLogo.setImageDrawable(null);
                    binding.tvWelcomeSignInUp.setText("Войти");
                }
            }
        });

        List<BookCategory> bookCategoryList = new ArrayList<>();
        bookCategoryList.add(new BookCategory(1, "Новинки 2020"));
        bookCategoryList.add(new BookCategory(2, "Самые популярные"));
        bookCategoryList.add(new BookCategory(3, "Бестселлеры"));
        bookCategoryList.add(new BookCategory(4, "Открытие 2020"));
        bookCategoryList.add(new BookCategory(5, "Дебют"));

        bookCategoryAdapter = new BookCategoryAdapter(this, bookCategoryList);
        binding.catRecycler.setAdapter(bookCategoryAdapter);

        books = new ArrayList<>();
        bookAdapter = new BookAdapter(this);
        binding.prodItemRecycler.setAdapter(bookAdapter);

        displayProductsByCat("Detectives");

        binding.tvCatFiction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayProductsByCat("Fiction");
            }
        });

        binding.tvCatNovels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayProductsByCat("Novels");
            }
        });

        binding.tvCatDetectives.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayProductsByCat("Detectives");
            }
        });

        binding.tvCatAdventure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayProductsByCat("Adventure");
            }
        });

    }

    public void displayProductsByCat(String cat){

        binding.tvCatAdventure.setTextColor( getResources().getColor(R.color.normal_cat));
        binding.tvCatAdventure.setTextSize(14);
        binding.tvCatDetectives.setTextColor( getResources().getColor(R.color.normal_cat));
        binding.tvCatDetectives.setTextSize(14);
        binding.tvCatFiction.setTextColor( getResources().getColor(R.color.normal_cat));
        binding.tvCatFiction.setTextSize(14);
        binding.tvCatNovels.setTextColor( getResources().getColor(R.color.normal_cat));
        binding.tvCatNovels.setTextSize(14);

        switch (cat){
            case "Приключения":
                binding.tvCatAdventure.setTextColor( getResources().getColor(R.color.selected_cat));
                binding.tvCatAdventure.setTextSize(16);
                break;
            case "Детективы":
                binding.tvCatDetectives.setTextColor( getResources().getColor(R.color.selected_cat));
                binding.tvCatDetectives.setTextSize(16);
                break;
            case "Фантастика":
                binding.tvCatFiction.setTextColor( getResources().getColor(R.color.selected_cat));
                binding.tvCatFiction.setTextSize(16);
                break;
            case "Романы":
                binding.tvCatNovels.setTextColor( getResources().getColor(R.color.selected_cat));
                binding.tvCatNovels.setTextSize(16);
                break;
        }

        books.clear();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Books").child(cat);
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Book book = dataSnapshot.getValue(Book.class);
                    books.add(book);
                }
                bookAdapter.setProductsList(books);
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}