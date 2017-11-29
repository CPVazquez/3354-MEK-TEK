package edu.utdallas.mektek.polycraftapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import net.xqhs.graphs.graph.Node;
import net.xqhs.graphs.graph.SimpleEdge;
import net.xqhs.graphs.graph.SimpleNode;

import java.util.ArrayList;

import giwi.org.networkgraph.GraphSurfaceView;
import giwi.org.networkgraph.beans.NetworkGraph;
import giwi.org.networkgraph.beans.Vertex;

public class MainActivity extends AppCompatActivity {

    private GraphSurfaceView surface;
    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        Item mockA = new Item("a", null, null, null, "element-a");
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

        drawTree(process);
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

   /* private class GetProcessTree extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            // This runs in a background thread
            for (int i = 0; i < 5; i++) {
                SystemClock.sleep(1000);

                // We use this method to communicate with the UI thread
                publishProgress(i + 1);
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            // UI cannot be modified from a background thread, so we do it here. This method is
            // executed on the UI thread
            textView.setText(String.valueOf(values[0]));
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // This is also executed on the UI thread when the background process finishes
            Toast.makeText(ThreadsActivity.this, "Done counting", Toast.LENGTH_SHORT)
                    .show();
        }
    }*/

    public void drawTree(Tree processTree){
        // Initialize Network Graph
        NetworkGraph processGraph = new NetworkGraph();
        // Set currentNode to target
        SuperNode currentNode = processTree.getTargetNode();
        // Set the drawable node to target and draw it
        Node graphPointer = new SimpleNode(currentNode.getId().toString());
        processGraph.getVertex().add(new Vertex(graphPointer, ContextCompat.getDrawable(this, R.drawable.icon)));

        // Start traversing through tree
        while(currentNode != null){
            SuperNode connectingNode = null;
            Node holder = null;
            /*for (SuperNode parent : currentNode.getParents()){
                Node nodeToDraw = new SimpleNode(parent.getId());
                processGraph.getVertex().add(new Vertex(nodeToDraw, ContextCompat.getDrawable(this,R.drawable.icon)));
                processGraph.addEdge(new SimpleEdge(nodeToDraw, ""));
            }*/
            // Draw each child on the graph
            for (SuperNode child : currentNode.getChildren()) {
                Node nodeToAdd = new SimpleNode(child.getId().toString());
                processGraph.getVertex().add(new Vertex(nodeToAdd, ContextCompat.getDrawable(this,R.drawable.icon)));
                // Check if we should draw the connecting edge to this child
                if(child.getParents().contains(currentNode)){
                    // Draw edge
                    processGraph.addEdge(new SimpleEdge(graphPointer, nodeToAdd, "12"));
                }
                // Check if we found the connectingNode
                if (!child.getChildren().isEmpty()) {
                    connectingNode = child; // We found the connecting node and this will become currentNode
                    holder = nodeToAdd; // Save the drawable of this node for later
                }
            }
            graphPointer = holder;
            currentNode = connectingNode;
        }

        this.surface = (GraphSurfaceView) findViewById(R.id.mysurface);
        surface.init(processGraph);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        this.mDetector.onTouchEvent(ev);
        Log.d("GRAPH", "graph is tapped");
        return super.onTouchEvent(ev);
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent ev) {
            float xTest = ev.getRawX();
            float yTest = ev.getRawY();
            Log.d("Gestures", "onDoubleTableEvent: " + ev.toString());
            return true;
        }
    }
}
