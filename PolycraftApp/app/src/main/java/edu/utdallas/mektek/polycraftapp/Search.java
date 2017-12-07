package edu.utdallas.mektek.polycraftapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

/*
Search - the starting Activity
 */

public class Search extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "edu.utdallas.mektek.polycraftapp.MESSAGE";

    /**
     * onCreate
     * launched when the Search Activity is creating
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //setting up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    /**
     * sendMessage
     * takes the search string and uses it to start a new Intent
     * @param view
     */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

}
