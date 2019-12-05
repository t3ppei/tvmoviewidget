package com.example.movieandtvwidget.favorite;

import android.os.Parcel;
import android.os.Parcelable;

public class FavoriteItem implements Parcelable {
    private int id;
    private String name;
    private String popular;
    private String description;
    private String photo;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPopular() {
        return popular;
    }

    public void setPopular(String popular) {
        this.popular = popular;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.popular);
        dest.writeString(this.description);
        dest.writeString(this.photo);
    }

    public FavoriteItem() {

    }

    public FavoriteItem(int id, String name, String popular, String description, String photo) {
        this.id = id;
        this.name = name;
        this.popular = popular;
        this.description = description;
        this.photo = photo;

    }

    private FavoriteItem(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.popular = in.readString();
        this.description = in.readString();
        this.photo = in.readString();
    }

    public static final Parcelable.Creator<FavoriteItem> CREATOR = new Parcelable.Creator<FavoriteItem>() {
        @Override
        public FavoriteItem createFromParcel(Parcel source) {
            return new FavoriteItem(source);
        }

        @Override
        public FavoriteItem[] newArray(int size) {
            return new FavoriteItem[size];
        }
    };

}
