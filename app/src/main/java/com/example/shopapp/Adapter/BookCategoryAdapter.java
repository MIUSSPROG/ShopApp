package com.example.shopapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shopapp.Model.BookCategory;
import com.example.shopapp.R;

import java.util.List;

public class BookCategoryAdapter extends RecyclerView.Adapter<BookCategoryAdapter.ProductViewHolder>{

    Context context;
    List<BookCategory> bookCategoryList;
    OnBookCategoryClickListener onBookCategoryClickListener;

    public interface OnBookCategoryClickListener {
        void onClicked(BookCategory bookCategory);
    }

    public BookCategoryAdapter(Context context, List<BookCategory> bookCategoryList, OnBookCategoryClickListener onBookCategoryClickListener) {
        this.context = context;
        this.bookCategoryList = bookCategoryList;
        this.onBookCategoryClickListener = onBookCategoryClickListener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.category_row_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.catagoryName.setText(bookCategoryList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBookCategoryClickListener.onClicked(bookCategoryList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(bookCategoryList != null){
            return bookCategoryList.size();
        }
        else{
            return 0;
        }
    }

    public static final class ProductViewHolder extends RecyclerView.ViewHolder{
        TextView catagoryName;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            catagoryName = itemView.findViewById(R.id.cat_name);

        }
    }
}
