package com.example.shopapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
        bookCategoryList.add(new BookCategory(3, "Бестселлер"));
        bookCategoryList.add(new BookCategory(4, "Классика"));

        bookCategoryAdapter = new BookCategoryAdapter(this, bookCategoryList, new BookCategoryAdapter.OnBookCategoryClickListener() {
            @Override
            public void onClicked(BookCategory bookCategory) {

                books.clear();
                mDatabaseRef = FirebaseDatabase.getInstance().getReference("Books");
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            for(DataSnapshot childSnapshot : dataSnapshot.getChildren()){
                                Book book = childSnapshot.getValue(Book.class);
                                if (book.getStatus().equals(bookCategory.getName())){
                                    books.add(book);
                                }
                                else if(bookCategory.getName().equals("Новинки 2020")){
                                    if(book.getYear() == 2020){
                                        books.add(book);
                                    }
                                }
                                else if (bookCategory.getName().equals("Самые популярные")){
                                    books.add(book);
                                }
                            }
                        }

                        if(bookCategory.getName().equals("Самые популярные")){
                            Collections.sort(books, new Comparator<Book>() {
                                @Override
                                public int compare(Book o1, Book o2) {
                                    if( o1.getRating() < o2.getRating()){
                                        return 1;
                                    }
                                    else if( o1.getRating() > o2.getRating() ) {
                                        return -1;
                                    }
                                    else{
                                        return 0;
                                    }
                                }
                            });

                            books = books.stream().limit(10).collect(Collectors.toList());
                        }

                        bookAdapter.setBooksList(books);
                        bookAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });

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
        binding.tvCatAdventure.setTextSize(11);
        binding.tvCatDetectives.setTextColor( getResources().getColor(R.color.normal_cat));
        binding.tvCatDetectives.setTextSize(11);
        binding.tvCatFiction.setTextColor( getResources().getColor(R.color.normal_cat));
        binding.tvCatFiction.setTextSize(11);
        binding.tvCatNovels.setTextColor( getResources().getColor(R.color.normal_cat));
        binding.tvCatNovels.setTextSize(11);

        switch (cat){
            case "Adventure":
                binding.tvCatAdventure.setTextColor( getResources().getColor(R.color.selected_cat));
                binding.tvCatAdventure.setTextSize(12);
                break;
            case "Detectives":
                binding.tvCatDetectives.setTextColor( getResources().getColor(R.color.selected_cat));
                binding.tvCatDetectives.setTextSize(12);
                break;
            case "Fiction":
                binding.tvCatFiction.setTextColor( getResources().getColor(R.color.selected_cat));
                binding.tvCatFiction.setTextSize(12);
                break;
            case "Novels":
                binding.tvCatNovels.setTextColor( getResources().getColor(R.color.selected_cat));
                binding.tvCatNovels.setTextSize(12);
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
                bookAdapter.setBooksList(books);
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

}