package edu.utdallas.mektek.polycraftapp;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import net.xqhs.graphs.graph.Edge;

import java.sql.SQLException;

import edu.utdallas.mektek.polycraftapp.beans.ArcUtils;
import edu.utdallas.mektek.polycraftapp.beans.Dimension;
import edu.utdallas.mektek.polycraftapp.beans.NetworkGraph;
import edu.utdallas.mektek.polycraftapp.beans.Point2D;
import edu.utdallas.mektek.polycraftapp.beans.RandomLocationTransformer;
import edu.utdallas.mektek.polycraftapp.beans.Vertex;
import edu.utdallas.mektek.polycraftapp.layout.FRLayout;

public class GraphSurfaceView extends SurfaceView {

    private ScaleGestureDetector mScaleDetector;
    private GestureDetectorCompat detector;
    private DatabaseHandler dbh;
    private TypedArray attributes;
    private NetworkGraph mNetworkGraph;

    //scaling constants and class variables.
    private float mScaleFactor = 1.0f;
    private final static float mMinFactor = 0.5f;
    private final static float mMaxFactor = 5.0f;

    //Panning variables
    private float positionX = 0;
    private float positionY = 0;

    //Painters
    Paint edgePainter;
    Paint whitePaint;
    Paint vertexPainter;

    //GraphNode Constants
    private final float nodeCircleRadius = 100; //FLOAT VALUE.
    private final float bitmapRadius = 150; //BitmapRadius

    /**
     * Instantiates a new Graph surface view. from SurfaceView
     *
     * @param context the context
     */
    public GraphSurfaceView(Context context) {
        super(context);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        detector = new GestureDetectorCompat(context, new MyGestureListener());
    }

    /**
     * Instantiates a new Graph surface view.
     *
     * @param context the context
     * @param attrs   the attrs
     */
    public GraphSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        attributes = getContext().obtainStyledAttributes(attrs, R.styleable.GraphSurfaceView);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        detector = new GestureDetectorCompat(context, new MyGestureListener());
    }

    /**
     * Instantiates a new Graph surface view.
     *
     * @param context  the context
     * @param attrs    the attrs
     * @param defStyle the def style
     */
    public GraphSurfaceView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        attributes = getContext().obtainStyledAttributes(attrs, R.styleable.GraphSurfaceView);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        detector = new GestureDetectorCompat(context, new MyGestureListener());
    }


    /**
     * Initialization function called from MainActivity . Instantiates the class variables above
     * and sets up the fonts, texts and colors of objects to be drawn on the canvas. The SurfaceView
     * is also created and callbacks are found here.
     * Additionally, the {@link DatabaseHandler} singleton instance is stored here.
     *
     * @param graph the graph object that needs to be drawn on the canvas
     */
    public void init(final NetworkGraph graph) {
        mNetworkGraph = graph;
        RandomLocationTransformer.iterator = 0;
        this.dbh = DatabaseHandler.getInstance(getContext());

        //Define all the Paint Object Styles
        this.edgePainter = new Paint();
        edgePainter.setAntiAlias(true);
        edgePainter.setTextAlign(Paint.Align.CENTER);
        edgePainter.setTextSize(20f);
        edgePainter.setColor(attributes.getColor(R.styleable.GraphSurfaceView_defaultColor, mNetworkGraph.getDefaultColor()));

        this.whitePaint = new Paint();
        whitePaint.setColor(attributes.getColor(R.styleable.GraphSurfaceView_nodeBgColor, mNetworkGraph.getNodeBgColor()));
        whitePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        whitePaint.setStrokeWidth(2f);
        whitePaint.setShadowLayer(
                5,
                0,
                0,
                attributes.getColor(
                        R.styleable.GraphSurfaceView_defaultColor,
                        mNetworkGraph.getDefaultColor()
                ));

        this.vertexPainter = new Paint();
        vertexPainter.setStyle(Paint.Style.FILL);
        vertexPainter.setTextAlign(Paint.Align.CENTER);
        vertexPainter.setTextSize(50f);
        vertexPainter.setStrokeWidth(0f);
        vertexPainter.setColor(attributes.getColor(R.styleable.GraphSurfaceView_nodeColor, mNetworkGraph.getNodeColor()));

        //Position and Format SurfaceView
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);

        //Define the SurfaceCallback so that it can safely be created and destroyed during state changes
        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                holder.getSurface().release();
            }
        });
        postInvalidate(); //call postInvalidate instead of invalidate() because this is not the UI thread
    }

    /**
     * This function takes an input bitmap (i.e. png) image, and crops it to fit inside a given
     * circular radius. This is used to generate the node images drawn on the graph view.
      * @param bmp input bitmap image to be cropped/resized
     * @param radius the radius of the circle we need to scale the image to fit inside
     * @return the cropped/scaled image.
     */
    private Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
        Bitmap sbmp;
        if (bmp.getWidth() != radius || bmp.getHeight() != radius) {
            sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
        } else {
            sbmp = bmp;
        }
        Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(
                sbmp.getWidth() / 2 + 0.7f, sbmp.getHeight() / 2 + 0.7f, sbmp.getWidth() / 2 + 0.1f, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(sbmp, rect, rect, paint);
        return output;
    }

    /**
     * Detects an OnTouchEvent and properly handles this call, passing it to the appropriate
     * handlers when necessary. The Zoom and Pan Gesture listeners are properly tipped off at the beginning
     *
     * To prevent issues with onTouchEvent blocking the callback stack, we pass true to:
     * {@link android.view.ViewParent#requestDisallowInterceptTouchEvent(boolean)} so that we allow
     * our gestureDetectors below [mScaleDetector and detector] to listen for multi-touch zoom and
     * pan gestures, respectviely.
     *
     * @param ev the Touch Event.
     * @return True to prevent "blocking" of the callback stack. False to block/ignore subsequent
     * input temporarily.
     */

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        mScaleDetector.onTouchEvent(ev);
        boolean result = detector.onTouchEvent(ev);
        if(!result){
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                result = true;
            }
            if(ev.getAction() == MotionEvent.ACTION_DOWN) {
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    result = true;
                }
            }
        }
        return result;

    }

    /**
     * Overrides {@link android.view.View#dispatchDraw(Canvas)} to write objects to a {@link Canvas}
     * object that is displayed on the View.
     *
     * This method returns null (does not run) if the mNetworkGraph passed to the {@link #init(NetworkGraph)}
     * function is NULL or contains no vertices.
     *
     * This method first scales and transforms the canvas, using the global parameters. Then, this
     * method iterates through all the nodes in the mNetworkGraph, peforming the following operations:
     *      1) Drawing Edges of Nodes
     *      2) Drawing Arcs to connect these "edges"
     *      3) Drawing the Vertices of the nodes (including text and image)
     *
     *
     * @param canvas Canvas to write to.
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        if(mNetworkGraph == null)
            return;
        else if(mNetworkGraph.getVertex().isEmpty()){
            return;
        }
        super.dispatchDraw(canvas);
        //modify canvas: scale+pan
        canvas.save();
        canvas.scale(mScaleFactor,mScaleFactor);
        canvas.translate(positionX,positionY);

        //local param that adjusts how much the node label overlaps the node image.
        final float nodeTextOverlapPercent = 0.2f;

        FRLayout layout = new FRLayout(mNetworkGraph, new Dimension(getWidth(), getHeight()));

        //Draw all edges between nodes in the graph
        for (Edge edge : mNetworkGraph.getEdges()) {
            Point2D p1 = new Point2D();
            p1.setLocation(0,0);
            Point2D p2 = new Point2D();
            p2.setLocation(0,0);
            for(Vertex node : mNetworkGraph.getVertex()){
                if(edge.getFrom() == node.getNode()){
                    if(node.getPosition() == null)
                        p1 = layout.transform(edge.getFrom());
                    else
                        p1 = node.getPosition();
                }
                if(edge.getTo() == node.getNode()){
                    if(node.getPosition() == null)
                        p2 = layout.transform(edge.getTo());
                    else
                        p2 = node.getPosition();

                }
            }
            edgePainter.setStrokeWidth(Float.valueOf(edge.getLabel()) + 1f);
            edgePainter.setColor(attributes.getColor(R.styleable.GraphSurfaceView_edgeColor, mNetworkGraph.getEdgeColor()));
            Paint curve = new Paint();
            curve.setAntiAlias(true);
            curve.setStyle(Paint.Style.STROKE);
            curve.setStrokeWidth(2);
            curve.setColor(attributes.getColor(R.styleable.GraphSurfaceView_edgeColor, mNetworkGraph.getEdgeColor()));
            PointF e1 = new PointF((float) p1.getX(), (float) p1.getY());
            PointF e2 = new PointF((float) p2.getX(), (float) p2.getY());
            ArcUtils.drawArc(e1, e2, 36f, canvas, curve, edgePainter, whitePaint, Integer.parseInt(edge.getLabel()));
        }

        for (Vertex node : mNetworkGraph.getVertex()) {
            Point2D position = new Point2D();
            position.setLocation(0,0);
            if(node.getPosition() == null) {
                position = layout.transform(node.getNode());
                node.setPosition(position);
            }else{
                position = node.getPosition();
            }
            canvas.drawCircle((float) position.getX(), (float) position.getY(), nodeCircleRadius, whitePaint);
            if (node.getIcon() != null) {
                Bitmap b = ((BitmapDrawable) node.getIcon()).getBitmap();
                Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
                Bitmap roundBitmap = getCroppedBitmap(bitmap, (int) bitmapRadius);
                canvas.drawBitmap(roundBitmap,
                        (float) position.getX() - (bitmapRadius/2f),
                        (float) position.getY() - (bitmapRadius/2f),
                        null);
            }

            //Get Text Size and properly size the rectangle around the object
            Rect mBounds = new Rect();
            vertexPainter.getTextBounds(node.getNode().getLabel(),0, node.getNode().getLabel().length(),mBounds);
            float mTextWidth = vertexPainter.measureText(node.getNode().getLabel());
            float mTextHeight = mBounds.height();
            Log.i("RECT", node.getNode().getLabel() + " " + mTextHeight + " " + mTextWidth);

            canvas.drawRect(
                    (float) position.getX() - mTextWidth/2f,
                    (float) position.getY() + (1f - nodeTextOverlapPercent)*nodeCircleRadius + mTextHeight*1.25f,
                    (float) position.getX() + mTextWidth/2f,
                    (float) position.getY() + (1f - nodeTextOverlapPercent)*nodeCircleRadius,
                    whitePaint);

            canvas.drawText(node.getNode().getLabel(), (float) position.getX(),
                    (float) position.getY() + (1f - nodeTextOverlapPercent)*nodeCircleRadius + mTextHeight, vertexPainter);
        }

        //display on canvas.
        canvas.restore();
    }

    /**
     * Private ScaleListener that listens for the user to pinch or expand two fingers on the screen.
     * This class overrides the onScale() function to store how much the user adjusts the screen in
     * local variables
     */
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        /**
         * If onScale is triggered by the UI, store the amount of "scaling" in mScaleFactor.
         * Ensure that it is within the limits defined in mMaxFactor and mMinFactor above. This
         * ensures the view stays visible and is not unweildy.
         *
         * @param detector the detector that detected the event.
         * @return True to prevent further calls to the listeners from being blocked.
         */
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(mMinFactor, Math.min(mScaleFactor, mMaxFactor));
            postInvalidate();
            return true;
        }
    }

    /**
     * Listener to handle Pan events
     */
    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        /**
         * onDown must return TRUE to allow onScroll() to activate
         * @param e motionevent triggered by a user touching and then dragging the screen
         * @return True to allow onScroll() to activate.
         */
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        /**
         * Activated when the user "scrolls" on the screen. While this provides a lot of useful
         * information, allowing for detection of exactly where on the screen the user is dragging,
         * we do not have a lot of location-specific scrolling that needs detecting. Thus, we ignore
         * all but the scroll distances measured. The scroll distances are additive
         * (you can scroll one way, then another, so the total change should be summed).
         *
         * Our sign convention uses "-=" because the screen should move along with the user's finger
         *
         * @param e1 initial position of the drag event (location and acceleration of finger)
         * @param e2 final position of the drag event (location and accel. of finger)
         * @param distanceX change in the "X" dimension (screen width, when viewed vertically)
         * @param distanceY change in the "Y" dimension (screen height, when viewed vertically)
         * @return True to prevent blocking of additional listeners
         */
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

            positionX -= distanceX;
            positionY -= distanceY;

            postInvalidate(); //updates the canvas
            return true;
        }

        /**
         * onDoubleTap listens for a user to DoubleTap the screen at a specific position. If the
         * user double taps a node on the screen (within a reasonable amount of accuracy),
         * the node detail for that item is launched in a new activity.
         *
         * By design, ev measures the tap position relative to the screen pixels: (0,0) on top left,
         * (width, height) on bottom right. However, the canvas location may not always match this
         * absolute measurement due to panning and zooming the view.
         *
         * According to Android API, Scaling the view will cause a multiplicative effect on the
         * measured position (and thus, we must divide to negate the effect). Panning is additive.
         * The sign convention used below is based on user interface need - users are accustomed to
         * having the screen move in the direction of their finger.
         *
         * @param ev Is a {@link android.view.MotionEvent} triggered at the location where the user
         *           double taps (specifically, their second tap)
         * @return True to continue listening for additional input (i.e. a drag or pinch event)
         *           False to block additional listeners.
         */
        @Override
        public boolean onDoubleTap(MotionEvent ev){
            int sensitivityRange = (int)nodeCircleRadius; //equal to drawn node size
            float actionBarHeight  = 0;
            TypedValue tv = new TypedValue();

            //Get the ActionBarHeight for the Device to compensate tap location by that amount.
            if( getContext().getApplicationContext().getTheme().resolveAttribute(
                    android.R.attr.actionBarSize, tv,true)) {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(
                        tv.data, getResources().getDisplayMetrics());
            }
            //Correction for positioning
            float xTap = (ev.getRawX()/mScaleFactor + -1f*positionX);
            float yTap = (ev.getRawY()/mScaleFactor + -1f*positionY - actionBarHeight);
            for(Vertex node : mNetworkGraph.getVertex()){
                Point2D position = node.getPosition();
                if(position.inRange(xTap, yTap, sensitivityRange, mScaleFactor)){
                    new GetNodeDetails().execute(node);
                    break;
                }
            }
            return true;
        }
    }

    /**
     * GetNodeDetails is a backgroundTask that queries the database when passed a given node and
     * returns either an {@link Item} or {@link Recipe} object containing their information.
     * After query, the onPostExecute() is called and starts the intent. An ASyncTask is used because
     * calls to SQLite databases could block the UI thread and cause performance issues.
     */
    private class GetNodeDetails extends AsyncTask<Vertex, Void, SuperNode> {
        @Override
        protected SuperNode doInBackground(Vertex ... node){
            dbh.open();
            SuperNode ver;
            try{
                if(node[0].isRecipe()){
                    ver = dbh.getRecipeWithId(node[0].getId());
                }
                else{
                    ver = dbh.getItemWithId(node[0].getId());
                }
                return ver;
            }catch (SQLException ex){
                //ignore problems and do nothing.
            }finally{
                dbh.close(); //this prevents memory leaks -> ignore the warnings above.
            }
            return null;
        }
        @Override
        protected void onPostExecute(SuperNode ver){
            startIntent(ver);
        }
    }

    /**
     * Given a SuperNode object returned from the GetNodeDetails().execute(Node) function above,
     * this casts the object to its appropriate type and starts either the DetailView or RecipeDetail
     * Activity.
     *
     * @param ver the SuperNode object returned by the ASyncTask GetNodeDetails.
     */
    public void startIntent(SuperNode ver){
        Intent intent;
        if(ver instanceof Recipe){
            intent = new Intent(getContext(), RecipeDetail.class);
            Recipe casted = (Recipe) ver;
            intent.putExtra("Detail", casted);
        }
        else{
            intent = new Intent(getContext(), DetailView.class);
            Item casted = (Item) ver;
            intent.putExtra("Detail",casted);

        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //flag needed to launch an activity from non-UI thread

        getContext().getApplicationContext().startActivity(intent); //launch the intent.
    }

}


