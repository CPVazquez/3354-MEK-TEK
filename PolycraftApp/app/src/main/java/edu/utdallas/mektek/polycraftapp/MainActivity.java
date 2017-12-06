package edu.utdallas.mektek.polycraftapp;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.TextView;

import net.xqhs.graphs.graph.Node;
import net.xqhs.graphs.graph.SimpleEdge;
import net.xqhs.graphs.graph.SimpleNode;

import java.sql.SQLException;


import edu.utdallas.mektek.polycraftapp.beans.*;
import edu.utdallas.mektek.polycraftapp.layout.*;
//
//import giwi.org.networkgraph.GraphSurfaceView;
//import giwi.org.networkgraph.beans.NetworkGraph;
//import giwi.org.networkgraph.beans.Point2D;
//import giwi.org.networkgraph.beans.Vertex;


public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_INFO = "edu.utdallas.mektek.polycraftapp.INFO";

    private GraphSurfaceView surface;
    private NetworkGraph processGraph;
    private GestureDetectorCompat mDetector;
    private DatabaseHandler dbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

       /* Item mockA = new Item("a", null, null, null, "element-a");
        Tree process = new Tree(mockA);
        Item elementA = new Item("a", new ArrayList<SuperNode>(), new ArrayList<SuperNode>(), null, "element-a");
        Item elementB = new Item("b", new ArrayList<SuperNode>(), new ArrayList<SuperNode>(), null, "element-b");
        Item water = new Item("water", new ArrayList<SuperNode>(), new ArrayList<SuperNode>(), null, "water");
        SuperNode recipe = new Recipe("distill", new ArrayList<SuperNode>(), new ArrayList<SuperNode>(), null, "recipie", null, null);
        recipe.getParents().add(elementA);
        recipe.getParents().add(elementB);
        recipe.getChildren().add(water);
        elementA.getChildren().add(recipe);
        elementB.getChildren().add(recipe);
        water.getParents().add(recipe);

        process.addNode(recipe);
*/
        Intent intent = getIntent();
        String message = intent.getStringExtra(Search.EXTRA_MESSAGE);

        this.dbh = DatabaseHandler.getInstance(this);
        Tree process = null;
        try {
            process = new GetTree().execute(message).get();
        }
        catch(Exception e){
           //Handle Exception
        }

        if(process != null){
            new DrawTree().execute(process);
        }else {
            TextView textView = (TextView) findViewById(R.id.textView8);
            textView.setText("You searched an invalid item");
        }
        Log.d("TREE", "Tree is drawn");
    }


    @Override
    public void onPause() {
        super.onPause();
        this.dbh.close();
    }

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

    private class GetSuperNode extends AsyncTask<Vertex, Void, SuperNode>{
        @Override
        protected SuperNode doInBackground(Vertex ... node){
            dbh.open();
            SuperNode ver = null;
            try{
                if(node[0].isRecipe()){
                    ver = dbh.getRecipeWithId(node[0].getId());
                }
                else{
                    ver = dbh.getItemWithId(node[0].getId());
                }
                Log.d("DEBUG", node[0].getId());
                dbh.close();
                return ver;
            }catch (SQLException ex){
                // Unhandled
            }
            return null;
        }
        @Override
        protected void onPostExecute(SuperNode ver){
            startIntent(ver);
        }
    }

    private class DrawTree extends AsyncTask<Tree, Object, Void> {
        @Override
        protected Void doInBackground(Tree... processTree) {
            drawTree(processTree[0]);
            return null;
        }
    }

    public boolean inRange(float xTest, float yTest, Point2D position, int range) {
        return xTest <= position.getX() + range && xTest >= position.getX() - range
                && yTest<= position.getY() + range && yTest >= position.getY() - range;
    }

    public void drawTree(Tree processTree){
        // Initialize Network Graph
        this.surface = (GraphSurfaceView) findViewById(R.id.mysurface);
        Dimension dim = this.surface.getDimension();
        processGraph = new NetworkGraph();

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
                //Unhandled
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
                    // Unhandled
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

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        this.mDetector.onTouchEvent(ev);
        Log.d("GRAPH", "graph is tapped");
        return super.onTouchEvent(ev);
    }

    // Pull up node DetailView
    public void startIntent(SuperNode ver){
        Intent intent;
        if(ver instanceof Recipe){
            intent = new Intent(this, RecipeDetail.class);
            Recipe casted = (Recipe) ver;
            intent.putExtra("Detail", casted);
            Log.d("Debug", "Recipe was tapped");
        }
        else{
            intent = new Intent(this, DetailView.class);
            Item casted = (Item) ver;
            intent.putExtra("Detail",casted);
            Log.d("Debug", "Item was tapped");
        }
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }




    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        private int tapSpacingThing = 40;
        @Override
        public boolean onDoubleTap(MotionEvent ev) {
            int tapSpacingThing = 40;
            float actionBarHeight  = 0;
            TypedValue tv = new TypedValue();
            if( getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)){
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
            }
            Log.d("Height", "action bar hight: " +actionBarHeight);
            float xTest = ev.getRawX();
            float yTest = ev.getRawY() - 220;
            Log.d("Gestures", "onDoubleTableEvent: x: " + xTest + " y: " + yTest);
            for(Vertex node : processGraph.getVertex()){
                Point2D position = node.getPosition();
                Log.d("Node", "x: " + position.getX() + " y: " + position.getY());
                if(inRange(xTest, yTest, position, tapSpacingThing)){
                    Log.d("Node", "yay!"); //Able to tap node, now launch Activity
                    new GetSuperNode().execute(node);
                    break;
                }
            }
            return true;
        }
    }

   /* private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
            invalidate();
            Log.d("Scale", "scale detected");
            return true;
        }
    }*/
}
