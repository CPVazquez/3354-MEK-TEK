package edu.utdallas.mektek.polycraftapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Author: Anshu Rao
 * Version: 1.0
 * RecipeDetail - an Activity that displays Recipe details
 */
public class RecipeDetail extends AppCompatActivity {

    /**
     * onCreate
     * launches when RecipeDetail is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        //sets up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //retrieves the intent and retrieves the passed Recipe
        Intent intent = getIntent();
        Recipe selectedNode = (Recipe) intent.getSerializableExtra("Detail");

        //Displays Recipe inventory
        TextView nameView = (TextView) findViewById(R.id.recipeName);
        nameView.setText(selectedNode.getInventory());

        //Displays Recipe's parents and the required quantities
        TextView outputItems = (TextView) findViewById(R.id.outputItems);
        String outputBuilder = "";
        ArrayList<Integer> parentQuant = selectedNode.getParentQuantities();
        ArrayList<SuperNode> parents = selectedNode.getParents();
        for(int i=0; i < parentQuant.size(); i++){
            outputBuilder += parents.get(i) + " (" + parentQuant.get(i) + ") \n";
        }
        outputItems.setText(outputBuilder);

        //Displays Recipe's children and the required quantities
        TextView inputItems = (TextView) findViewById(R.id.inputItems);
        String inputBuilder = "";
        ArrayList<Integer> childQuant = selectedNode.getChildQuantities();
        ArrayList<SuperNode> children = selectedNode.getChildren();
        for(int i=0; i < childQuant.size(); i++){
            inputBuilder += children.get(i) + " (" + childQuant.get(i) + ") \n";
        }
        inputItems.setText(inputBuilder);
    }

    //supports backbutton navigation
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }
}
