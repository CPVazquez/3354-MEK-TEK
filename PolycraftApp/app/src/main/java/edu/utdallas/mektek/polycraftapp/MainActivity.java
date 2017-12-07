package edu.utdallas.mektek.polycraftapp;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import net.xqhs.graphs.graph.Node;
import net.xqhs.graphs.graph.SimpleEdge;
import net.xqhs.graphs.graph.SimpleNode;
import java.sql.SQLException;
import edu.utdallas.mektek.polycraftapp.beans.*;

/**
 * MainActivity - the Activity where the graph is drawn
 */

public class MainActivity extends AppCompatActivity {
    private DatabaseHandler dbh;

    /**
     * onCreate
     * launched when MainActivity is created
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //receive intent and receive the passed string
        Intent intent = getIntent();
        String message = intent.getStringExtra(Search.EXTRA_MESSAGE);

        //create dbh instance for DatabaseHandler singleton
        this.dbh = DatabaseHandler.getInstance(this);
        Tree process = null;

        //create Tree
        try {
            process = new GetTree().execute(message).get();
        }
        catch(Exception e){
           Log.d("Exception", e.toString());
        }

        //draw graphical tree
        if(process != null){
            new DrawTree().execute(process);
        }else {
            TextView textView = (TextView) findViewById(R.id.textView8);
            textView.setText("You searched an invalid item");
        }
        Log.d("TREE", "Tree is drawn");
    }

    //makes sure the database closes on pause
    @Override
    public void onPause() {
        super.onPause();
        this.dbh.close();
    }

    /**
     * GetTree
     * all accesses to the database should be in an AsyncTask
     * this class creates the graph of SuperNodes and returns a Tree
     */
    private class GetTree extends AsyncTask<String, Object, Tree>{
        @Override
        protected Tree doInBackground(String... query){
            dbh.open();
            Tree process = null;
            try {
                process = dbh.getProcessTree(query[0]);
                //TODO print error message if process is null at this point
                System.out.println(process);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dbh.close();
            return process;
        }
    }

    /**
     * DrawTree
     * a AsyncTask wrapper class for the drawTree function
     */
    private class DrawTree extends AsyncTask<Tree, Object, Void> {
        @Override
        protected Void doInBackground(Tree... processTree) {
            drawTree(processTree[0]);
            return null;
        }
    }

    /**
     * drawTree
     * creates the vertices and edges for the tree to be drawn
     * @param processTree - a Tree object built by database handler
     */
    public void drawTree(Tree processTree){
        // Initialize Network Graph
        GraphSurfaceView surface = (GraphSurfaceView) findViewById(R.id.mysurface);
        Dimension dim;// = this.surface.getDimension();
        NetworkGraph processGraph = new NetworkGraph();

        // Set a pointer to the current Recipe and a graphPointer will be the last recipe drawn
        SuperNode target=processTree.getTargetNode();
        SuperNode currentRecipe;
        if(target.getChildren().size()>0) {
            currentRecipe = target.getChildren().get(0);
        }else {
            TextView textView = (TextView) findViewById(R.id.textView8);
            textView.setText("You searched a base item");
            currentRecipe=null;
        }

        SuperNode oldRecipe = null;
        Node oldDrawnRecipe = null;

        while(currentRecipe != null){
            dim=new Dimension(1000,1000);

            // Draw the recipe
            Node drawnRecipe = new SimpleNode(currentRecipe.getId());
            String[] png = currentRecipe.getImage().getName().split("File:");
            try{
                processGraph.getVertex().add(new Vertex(drawnRecipe, Drawable.createFromStream(getAssets().open("images/distillation.png"), null), currentRecipe.getId(),processTree.getPosition(dim,drawnRecipe), true));
            }
            catch(Exception e){
                Log.d("Exception", e.toString());
            }

            // Draw parents of recipe
            for(SuperNode parent : currentRecipe.getParents()) {
                Node nodeToAdd = new SimpleNode(parent.getName());
                String[] png2 = parent.getImage().getName().split("File:");

                try{
                    processGraph.getVertex().add(
                            new Vertex(nodeToAdd, Drawable.createFromStream(getAssets().open("images/" + png2[1].toLowerCase()), null),
                                    parent.getId(), processTree.getPosition(dim,nodeToAdd)));
                }
                catch(Exception e){
                    Log.d("Exception", e.toString());
                }
                processGraph.addEdge(new SimpleEdge(nodeToAdd, drawnRecipe, "1"));
                // Check if the parent of the recipe is where we make the connection to the old recipe
                if(oldRecipe != null){
                    if(parent.getId().equals(oldRecipe.getChildren().get(0).getId())){
                        processGraph.addEdge(new SimpleEdge(nodeToAdd, oldDrawnRecipe, "2"));
                    }
                }
            }
            oldDrawnRecipe = drawnRecipe;
            oldRecipe = currentRecipe;
            if(!currentRecipe.getChildren().get(0).getChildren().isEmpty()){
                currentRecipe = currentRecipe.getChildren().get(0).getChildren().get(0);
            }
            else{
                // Draw child!!!
                SuperNode child = currentRecipe.getChildren().get(0);
                Node childToDraw = new SimpleNode(child.getName());
                try{
                    String[] pngArr = child.getImage().getName().split("File:");
                    processGraph.getVertex().add(new Vertex(childToDraw, Drawable.createFromStream(getAssets().open("images/" + pngArr[1].toLowerCase()), null), child.getId(), processTree.getPosition(dim,childToDraw)));
                }catch (Exception e){

                }

                processGraph.addEdge(new SimpleEdge(drawnRecipe, childToDraw, "3" ));
                currentRecipe = null;
            }
        }

        surface.init(processGraph);
    }


    //supports backbutton navigation
    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

}
