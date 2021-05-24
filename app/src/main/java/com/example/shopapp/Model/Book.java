package com.example.shopapp.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    String author;
    String description;
    String name;
    Float price;
    Integer year;
    String imageURL;

    public Book() {
    }

    public Book(String author, String description, String name, Float price, Integer year, String imageURL) {
        this.author = author;
        this.description = description;
        this.name = name;
        this.price = price;
        this.year = year;
        this.imageURL = imageURL;
    }

    protected Book(Parcel in) {
        author = in.readString();
        description = in.readString();
        name = in.readString();
        if (in.readByte() == 0) {
            price = null;
        } else {
            price = in.readFloat();
        }
        if (in.readByte() == 0) {
            year = null;
        } else {
            year = in.readInt();
        }
        imageURL = in.readString();
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(description);
        dest.writeString(name);
        if (price == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeFloat(price);
        }
        if (year == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(year);
        }
        dest.writeString(imageURL);
    }
}
