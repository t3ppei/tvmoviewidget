package com.example.movieandtvwidget.tvshow;

import android.os.Parcel;
import android.os.Parcelable;

class TvShow implements Parcelable {
    private int id;
    private String name;
    private String popular;
    private String description;
    private String photo;

    public TvShow(){

    }

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

    protected TvShow(Parcel in) {
        id = in.readInt();
        name = in.readString();
        popular = in.readString();
        description = in.readString();
        photo = in.readString();
    }

    public static final Parcelable.Creator<TvShow> CREATOR = new Creator<TvShow>() {
        @Override
        public TvShow createFromParcel(Parcel in) {
            return new TvShow(in);
        }

        @Override
        public TvShow[] newArray(int size) {
            return new TvShow[size];
        }
    };
}
