package edu.utdallas.mektek.polycraftapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class RecipeDetail extends AppCompatActivity {

    private Recipe selectedNode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        this.selectedNode = (Recipe) intent.getSerializableExtra("Detail");
        TextView nameView = (TextView) findViewById(R.id.recipeName);
        nameView.setText(selectedNode.getInventory());

        TextView outputItems = (TextView) findViewById(R.id.outputItems);
        String outputBuilder = "";
        ArrayList<Integer> parentQuant = selectedNode.getParentQuantities();
        ArrayList<SuperNode> parents = selectedNode.getParents();
        for(int i=0; i < parentQuant.size(); i++){
            outputBuilder += parents.get(i) + " (" + parentQuant.get(i) + ") \n";
        }
        outputItems.setText(outputBuilder);

        TextView inputItems = (TextView) findViewById(R.id.inputItems);
        String inputBuilder = "";
        ArrayList<Integer> childQuant = selectedNode.getChildQuantities();
        ArrayList<SuperNode> children = selectedNode.getChildren();
        for(int i=0; i < childQuant.size(); i++){
            outputBuilder += children.get(i) + " (" + childQuant.get(i) + ") \n";
        }
        inputItems.setText(inputBuilder);
    }
}
