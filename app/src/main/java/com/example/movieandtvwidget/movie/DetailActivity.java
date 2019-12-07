package com.example.movieandtvwidget.movie;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.movieandtvwidget.R;
import com.example.movieandtvwidget.db.FavoriteHelper;
import com.example.movieandtvwidget.favorite.FavoriteItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import static android.provider.BaseColumns._ID;
import static com.example.movieandtvwidget.db.DatabaseContract.MovieFavoriteColumns.CONTENT_URI;
import static com.example.movieandtvwidget.db.DatabaseContract.MovieFavoriteColumns.DESCRIPTION;
import static com.example.movieandtvwidget.db.DatabaseContract.MovieFavoriteColumns.NAME;
import static com.example.movieandtvwidget.db.DatabaseContract.MovieFavoriteColumns.PHOTO;
import static com.example.movieandtvwidget.db.DatabaseContract.MovieFavoriteColumns.POPULAR;

public class DetailActivity extends AppCompatActivity {
    private FavoriteItem favoriteItem;
    private FavoriteHelper favoriteHelper;
    private Uri uriWithId;
    private FloatingActionButton fabAdd;
    private int position;

    public static final String EXTRA_FAVORITE = "extra_favorite";
    public static final String EXTRA_POSITION = "extra_position";
    public static final int REQUEST_ADD = 100;
    public static final int RESULT_ADD = 101;
    public static final int RESULT_DELETE = 301;
    private final int ALERT_DIALOG_DELETE = 20;
    public static final int REQUEST_UPDATE = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

//        favoriteHelper = FavoriteHelper.getInstance(getApplicationContext());
//        favoriteHelper.open();

        ImageView imgFoto = findViewById(R.id.img_item_photo);
        TextView tvJudul = findViewById(R.id.tv_item_judul);
        TextView tvYear = findViewById(R.id.tv_item_year);
        TextView tvDeskripsi = findViewById(R.id.tv_item_deskripsi);
        TextView tvId = findViewById(R.id.tv_item_id);

        favoriteItem = getIntent().getParcelableExtra(EXTRA_FAVORITE);
        if (favoriteItem != null) {

            tvJudul.setText(favoriteItem.getName());
            tvYear.setText(favoriteItem.getPopular());
            tvDeskripsi.setText(favoriteItem.getDescription());
            tvId.setText(Integer.toString(favoriteItem.getId()));
            Glide.with(this)
                    .load(favoriteItem.getPhoto())
                    .into(imgFoto);

            fabAdd = findViewById(R.id.btn_fav);
            fabAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showAlertDialog(ALERT_DIALOG_DELETE);
                }
            });

        } else {

            favoriteItem = new FavoriteItem();
            final Movie detail = getIntent().getParcelableExtra("KEY_EXTRA");
            if (detail != null) {
                tvJudul.setText(detail.getName());
                tvYear.setText(detail.getPopular());
                tvDeskripsi.setText(detail.getDescription());
                tvId.setText(Integer.toString(detail.getId()));
                Glide.with(this)
                        .load(detail.getPhoto())
                        .into(imgFoto);

                fabAdd = findViewById(R.id.btn_fav);
                fabAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int id = detail.getId();
                        String judul = detail.getName();
                        String popular = detail.getPopular();
                        String deskripsi = detail.getDescription();
                        String foto = detail.getPhoto();

                        favoriteItem.setId(id);
                        favoriteItem.setName(judul);
                        favoriteItem.setPopular(popular);
                        favoriteItem.setDescription(deskripsi);
                        favoriteItem.setPhoto(foto);

                        Intent intent = new Intent();
                        intent.putExtra(EXTRA_FAVORITE, favoriteItem);
                        intent.putExtra(EXTRA_POSITION, position);

                        ContentValues values = new ContentValues();
                        values.put(_ID, id);
                        values.put(NAME, judul);
                        values.put(POPULAR, popular);
                        values.put(DESCRIPTION, deskripsi);
                        values.put(PHOTO, foto);


//                        long result = favoriteHelper.insert(values);
//                        if (result > 0) {
//                            favoriteItem.setId((int) result);
//                            setResult(RESULT_ADD, intent);
//                            Toast.makeText(DetailActivity.this, getString(R.string.add_success), Toast.LENGTH_LONG).show();
//                            finish();
//                        } else {
//                            Toast.makeText(DetailActivity.this, getString(R.string.add_failed), Toast.LENGTH_SHORT).show();
//                        }
                        getContentResolver().insert(CONTENT_URI, values);
                        Toast.makeText(DetailActivity.this, getString(R.string.add_success), Toast.LENGTH_LONG).show();
                            finish();

                    }
                });
            }
        }



    }

    private void showAlertDialog(int type) {
        String dialogTitle, dialogMessage;

        dialogMessage = getString(R.string.del_fav_sure);
        dialogTitle = getString(R.string.del_fav);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle(dialogTitle);
        alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.btn_ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        long result = favoriteHelper.deleteById(String.valueOf(favoriteItem.getId()));
//                        if (result > 0) {
//                            Intent intent = new Intent();
//                            intent.putExtra(EXTRA_POSITION, position);
//                            setResult(RESULT_DELETE, intent);
//                            finish();
//                        } else {
//                            Toast.makeText(DetailActivity.this, getString(R.string.del_fav_failed) + " " + getString(R.string.title_movies), Toast.LENGTH_SHORT).show();
//                        }
                        uriWithId = Uri.parse(CONTENT_URI + "/" + favoriteItem.getId());
                        getContentResolver().delete(uriWithId, null, null);
                        Toast.makeText(DetailActivity.this, getString(R.string.del_fav_success) + " " + getString(R.string.title_movies), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.btn_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
