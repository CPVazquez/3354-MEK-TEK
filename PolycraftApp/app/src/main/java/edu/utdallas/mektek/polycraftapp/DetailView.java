package edu.utdallas.mektek.polycraftapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Author: Anshu Rao
 * Version: 1.0
 * DetailView - an Activity that displays Item details
 */

public class DetailView extends AppCompatActivity {


    /**
     * onCreate
     * launches immediately when DetailView is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //sets up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //receives intent and retrieves sent Item
        Intent intent = getIntent();
        Item item = (Item)intent.getSerializableExtra("Detail");

        //Displays Item name
        TextView nameView = (TextView) findViewById(R.id.nameEntry);
        nameView.setText(item.getName());
        Log.d("DETAIL", "Setting name");

        //Display's Item's wiki page
        TextView wikiView = (TextView) findViewById(R.id.wikiEntry);
        wikiView.setText(item.getUrl());

        //Determines whether the Item is naturally occurring
        String natural = "No";
        if(item.isNatural())
            natural = "Yes";

        //Displays whether the Item is naturally occurring
        TextView naturalView = (TextView) findViewById(R.id.naturalEntry);
        naturalView.setText(natural);

        //Displays Item image
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
            // Unhandled - Sad.
        }
    }

    //enables backbutton navigation
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }


}
