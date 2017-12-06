package edu.utdallas.mektek.polycraftapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class DetailView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        Item item = (Item)intent.getSerializableExtra("Detail");

        TextView nameView = (TextView) findViewById(R.id.nameEntry);
        nameView.setText(item.getName());
        Log.d("DETAIL", "Setting name");

        TextView wikiView = (TextView) findViewById(R.id.wikiEntry);
        wikiView.setText(item.getUrl());

        String natural = "No";
        if(item.isNatural())
            natural = "Yes";

        TextView naturalView = (TextView) findViewById(R.id.naturalEntry);
        naturalView.setText(natural);

        ImageView icon = (ImageView) findViewById(R.id.imageView);
        String fileName = item.getImage().getName();
        String[] splitName = fileName.split("File:");
        String pngFile = splitName[1].toLowerCase();

        String pngPath = "images/" + pngFile;
        try{
            Drawable img = Drawable.createFromStream(getAssets().open(pngPath), null);
            icon.setImageDrawable(img);
        }
        catch(Exception e){
            // Unhandled
        }


        //new GetBitmap((ImageView) findViewById(R.id.imageView)).execute("https://minecraft.gamepedia.com/media/minecraft.gamepedia.com/c/c2/Potato.png");
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    /*private class GetBitmap extends AsyncTask<String, Void, Bitmap> {

        private Exception exception;
        private String link;
        private Bitmap image;
        private ImageView view;

        public GetBitmap(ImageView imgview){
            this.view = imgview;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                options.inJustDecodeBounds = false;
                InputStream in = new BufferedInputStream(new java.net.URL(urldisplay).openStream());
                mIcon11 = BitmapFactory.decodeStream(in);
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return mIcon11;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            this.view.setImageBitmap(result);
        }
    }*/
}