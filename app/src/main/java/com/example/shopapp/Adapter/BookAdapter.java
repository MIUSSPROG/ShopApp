package com.example.shopapp.Adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shopapp.Model.Book;
import com.example.shopapp.BookDetail;
import com.example.shopapp.R;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ProductViewHolder> {

    Context context;
    List<Book> productsList;

    public BookAdapter(Context context) {
        this.context = context;
    }

    public void setProductsList(List<Book> productsList) {
        this.productsList = productsList;
    }

    @NonNull
    @Override
    public BookAdapter.ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.products_row_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookAdapter.ProductViewHolder holder, int position) {

        Book book = productsList.get(position);

        Glide.with(context)
                .load(book.getImageURL())
                .fitCenter()
                .centerCrop()
                .into(holder.bookImage);
        holder.bookName.setText(book.getName());
        holder.bookAuthor.setText(book.getAuthor());
        holder.bookYear.setText(String.valueOf(book.getYear()));
        holder.bookPrice.setText(String.valueOf(book.getPrice()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context, BookDetail.class);
                i.putExtra("bookInfo", book);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(holder.bookImage, "image");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation((Activity) context, pairs);
                context.startActivity(i, activityOptions.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        if(productsList != null) {
            return productsList.size();
        }
        else{
            return 0;
        }
    }

    public static final class ProductViewHolder extends RecyclerView.ViewHolder{

        ImageView bookImage;
        TextView bookAuthor, bookName, bookDesc, bookYear, bookPrice;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            bookImage = itemView.findViewById(R.id.book_image);
            bookAuthor = itemView.findViewById(R.id.book_author);
            bookPrice = itemView.findViewById(R.id.book_price);
            bookYear = itemView.findViewById(R.id.book_year);
            bookName = itemView.findViewById(R.id.book_name);
        }
    }
}
