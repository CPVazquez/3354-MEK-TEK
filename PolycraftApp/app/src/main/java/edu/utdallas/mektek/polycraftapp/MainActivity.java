package edu.utdallas.mektek.polycraftapp;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import net.xqhs.graphs.graph.Node;
import net.xqhs.graphs.graph.SimpleEdge;
import net.xqhs.graphs.graph.SimpleNode;

import java.sql.SQLException;
import java.util.ArrayList;

import giwi.org.networkgraph.GraphSurfaceView;
import giwi.org.networkgraph.beans.NetworkGraph;
import giwi.org.networkgraph.beans.Point2D;
import giwi.org.networkgraph.beans.Vertex;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_INFO = "edu.utdallas.mektek.polycraftapp.INFO";

    private GraphSurfaceView surface;
    private NetworkGraph processGraph;
    private GestureDetectorCompat mDetector;

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

        DatabaseHandler dbh= new DatabaseHandler("test.db",7);
        Tree process = null;
        try {
            process = dbh.getProcessTree("Cartridge (Ethane)");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        new DrawTree().execute(process);
        Log.d("TREE", "Tree is drawn");
    }

        // Get the Intent that started this activity and extract the string
        /* Intent intent = getIntent();
        String message = intent.getStringExtra(Search.EXTRA_MESSAGE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
*/
    private class DrawTree extends AsyncTask<Tree, Object, Void> {
        @Override
        protected Void doInBackground(Tree... processTree) {
            drawTree(processTree[0]);
            return null;
        }
    }

    public void drawTree(Tree processTree){
        // Initialize Network Graph
        processGraph = new NetworkGraph();
        // Set currentNode to target
        //SuperNode currentNode = processTree.getTargetNode();
        // Set the drawable node to target and draw it

        // Set a pointer to the current Recipe and a graphPointer will be the last recipe drawn
        SuperNode currentRecipe = processTree.getTargetNode().getChildren().get(0);
        SuperNode oldRecipe = null;
        Node oldDrawnRecipe = null;

        while(currentRecipe != null){

            // Draw the recipe
            Node drawnRecipe = new SimpleNode(currentRecipe.getId());
            processGraph.getVertex().add(new Vertex(drawnRecipe, ContextCompat.getDrawable(this,R.drawable.icon)));

            // Draw parents of recipe
            for(SuperNode parent : currentRecipe.getParents()) {
                Node nodeToAdd = new SimpleNode(parent.getId());
                processGraph.getVertex().add(new Vertex(nodeToAdd, ContextCompat.getDrawable(this, R.drawable.icon)));
                processGraph.addEdge(new SimpleEdge(nodeToAdd, drawnRecipe, "1"));
                // Check if the parent of the recipe is where we make the connection to the old recipe
                if(oldRecipe != null){
                    if(parent.getId().equals(oldRecipe.getChildren().get(0))){
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
                Node childToDraw = new SimpleNode(child.getId());
                processGraph.getVertex().add(new Vertex(childToDraw, ContextCompat.getDrawable(this, R.drawable.icon)));
                processGraph.addEdge(new SimpleEdge(drawnRecipe, childToDraw, "3" ));
                currentRecipe = null;
            }
        }
        // Start traversing through tree
        /*while(currentNode != null){
            SuperNode connectingNode = null;
            Node holder = null;
            // Draw each child on the graph
            for (SuperNode child : currentNode.getChildren()) {
                Node nodeToAdd = new SimpleNode(child.getId());
                processGraph.getVertex().add(new Vertex(nodeToAdd, ContextCompat.getDrawable(this,R.drawable.icon)));
                processGraph.addEdge(new SimpleEdge(nodeToAdd, graphPointer, "11"));
                // Check if we should draw the connecting edge to this child
                //if(child.getParents().contains(currentNode)){
                    // Draw edge
                    //processGraph.addEdge(new SimpleEdge(nodeToAdd, graphPointer, "12"));
                //}
                // Check if we found the connectingNode
                if (!child.getChildren().isEmpty()) {
                    connectingNode = child; // We found the connecting node and this will become currentNode
                    holder = nodeToAdd; // Save the drawable of this node for later
                }
            }
            graphPointer = holder;
            currentNode = connectingNode;
        }*/

        this.surface = (GraphSurfaceView) findViewById(R.id.mysurface);
        surface.init(processGraph);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        this.mDetector.onTouchEvent(ev);
        Log.d("GRAPH", "graph is tapped");
        return super.onTouchEvent(ev);
    }

    // Pull up node detail
    public void startIntent(String info){
        Intent intent = new Intent(this, detail.class);
        intent.putExtra(EXTRA_INFO, info);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp(){
        finish();
        return true;
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent ev) {
            float xTest = ev.getRawX();
            float yTest = ev.getRawY() - 280;
            //Log.d("Gestures", "onDoubleTableEvent: x: " + xTest + " y: " + yTest);
            for(Vertex node : processGraph.getVertex()){
                Point2D position = node.getPosition();
                Log.d("Node", "x: " + position.getX() + " y: " + position.getY());
                if(xTest <= position.getX() + 20 && xTest >= position.getX() - 20  && yTest<= position.getY() + 20 && yTest >= position.getY() - 20 ){
                    Log.d("Node", "yay!"); //Able to tap node, now launch Activity
                    // Call to database
                    String info = "https://minecraft.gamepedia.com/media/minecraft.gamepedia.com/4/43/Nether_Wart.png";
                    startIntent(info);
                    break;
                }
            }
            return true;
        }
    }
}
