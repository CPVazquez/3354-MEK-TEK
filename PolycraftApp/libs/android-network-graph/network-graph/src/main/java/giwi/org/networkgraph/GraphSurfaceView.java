package giwi.org.networkgraph;

import android.content.Intent;
import android.view.SurfaceView;


import net.xqhs.graphs.graph.Edge;

import android.content.Context;
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
import android.support.annotation.NonNull;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Scroller;

import giwi.org.networkgraph.beans.ArcUtils;
import giwi.org.networkgraph.beans.Dimension;
import giwi.org.networkgraph.beans.NetworkGraph;
import giwi.org.networkgraph.beans.Point2D;
import giwi.org.networkgraph.beans.Vertex;
import giwi.org.networkgraph.layout.FRLayout;

/**
 * The type NetworkGraph surface view.
 */
public class GraphSurfaceView extends SurfaceView {

    private Context mainContext;

    private ScaleGestureDetector mScaleDetector;

    private GestureDetectorCompat detector;

    private final float RADIUS = 50; //FLOAT VALUE.

    private Scroller mScroller;

    private TypedArray attributes;

    private float mScaleFactor = 1.0f;
    private static float mMinFactor = 0.5f;
    private static float mMaxFactor = 5.0f;
    private NetworkGraph mNetworkGraph;

    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;

    private float positionX = 0;
    private float positionY = 0;

    //Painters
    Paint paint;
    Paint whitePaint;

    /**
     * Instantiates a new NetworkGraph surface view.
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
     * Init.
     *
     * @param graph the graph
     */
    public void init(final NetworkGraph graph) {
        mNetworkGraph = graph;

        this.paint = new Paint();
        this.whitePaint = new Paint();
        paint.setAntiAlias(true);
        whitePaint.setColor(attributes.getColor(R.styleable.GraphSurfaceView_nodeBgColor, mNetworkGraph.getNodeBgColor()));
        whitePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        whitePaint.setStrokeWidth(2f);
        whitePaint.setShadowLayer(5, 0, 0, attributes.getColor(R.styleable.GraphSurfaceView_defaultColor, mNetworkGraph
                .getDefaultColor()));


        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        //mScroller = new Scroller(getContext());
        getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                Log.d("Surface", "Surface Created Called");
               // mCanvas = holder.lockCanvas(null);
               // mCanvas.drawARGB(0, 225, 225, 255);
                //drawGraph(mCanvas, mNetworkGraph); //TODO: Handle G[0,0],[] for graph.
               // holder.unlockCanvasAndPost(mCanvas);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                // TODO Auto-generated method stub
                Log.d("Surface", "Surface Changed Called");

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                // TODO Auto-generated method stub

            }
        });
        invalidate();
       // postInvalidate();
    }

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
     * On touch event.
     *
     * @param ev the ev
     * @return the boolean
     */

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent ev) {
        mScaleDetector.onTouchEvent(ev);
        //super.onTouchEvent(ev);
        boolean result = detector.onTouchEvent(ev);
        if(!result){
            if (ev.getAction() == MotionEvent.ACTION_UP) {
                //stopScrolling();
                result = true;
            }
            if(ev.getAction() == MotionEvent.ACTION_DOWN) {
                if (getParent() != null) {
                    getParent().requestDisallowInterceptTouchEvent(true);
                    result = true;
                }
            }
        }
        return result; //just commented out
      //  mScaleDetector.onTouchEvent(ev);
       // postInvalidate();
      //  Log.d("GSV", "Screen was touched");
        //return super.onTouchEvent(ev); this lets double tap work

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Log.i("ONDRAW", "onDraw is called");
        canvas.save();
        canvas.scale(mScaleFactor,mScaleFactor);
        canvas.translate(positionX,positionY);

        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(20f);
        paint.setColor(attributes.getColor(R.styleable.GraphSurfaceView_defaultColor, mNetworkGraph.getDefaultColor()));
        FRLayout layout = new FRLayout(mNetworkGraph, new Dimension(getWidth(), getHeight()));

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
            paint.setStrokeWidth(Float.valueOf(edge.getLabel()) + 1f);
            paint.setColor(attributes.getColor(R.styleable.GraphSurfaceView_edgeColor, mNetworkGraph.getEdgeColor()));
            Paint curve = new Paint();
            curve.setAntiAlias(true);
            curve.setStyle(Paint.Style.STROKE);
            curve.setStrokeWidth(2);
            curve.setColor(attributes.getColor(R.styleable.GraphSurfaceView_edgeColor, mNetworkGraph.getEdgeColor()));
            PointF e1 = new PointF((float) p1.getX(), (float) p1.getY());
            PointF e2 = new PointF((float) p2.getX(), (float) p2.getY());
            ArcUtils.drawArc(e1, e2, 36f, canvas, curve, paint, whitePaint, Integer.parseInt(edge.getLabel()));
        }
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(30f);
        paint.setStrokeWidth(0f);
        paint.setColor(attributes.getColor(R.styleable.GraphSurfaceView_nodeColor, mNetworkGraph.getNodeColor()));
        for (Vertex node : mNetworkGraph.getVertex()) {
            Point2D position = new Point2D();
            position.setLocation(0,0);
            if(node.getPosition() == null) {
                position = layout.transform(node.getNode());
                node.setPosition(position);
            }else{
                position = node.getPosition();
            }
            canvas.drawCircle((float) position.getX(), (float) position.getY(), RADIUS, whitePaint);
            if (node.getIcon() != null) {
                Bitmap b = ((BitmapDrawable) node.getIcon()).getBitmap();
                Bitmap bitmap = b.copy(Bitmap.Config.ARGB_8888, true);
                Bitmap roundBitmap = getCroppedBitmap(bitmap, 75);
                canvas.drawBitmap(roundBitmap,
                        (float) position.getX() - (38f), (float) position.getY() - (38f), null);
            }

            //Get Text Size and properly size the rectangle around the object
            Rect mBounds = new Rect();
            paint.getTextBounds(node.getNode().getLabel(),0, node.getNode().getLabel().length(),mBounds);
            float mTextWidth = paint.measureText(node.getNode().getLabel());
            float mTextHeight = mBounds.height();
            Log.i("RECT", node.getNode().getLabel() + " " + mTextHeight + " " + mTextWidth);

            canvas.drawRect(
                    (float) position.getX() - mTextWidth/2f,
                    (float) position.getY() + RADIUS + mTextHeight/2f,
                    (float) position.getX() + mTextWidth/2f,
                    (float) position.getY() + mTextHeight/2f,
                    whitePaint);

//            canvas.drawRect(
//                    (float) position.getX() - 20,
//                    (float) position.getY() + 50,
//                    (float) position.getX() + 20, (float) position.getY() + 10, whitePaint);
            canvas.drawText(node.getNode().getLabel(), (float) position.getX(),
                    (float) position.getY() + RADIUS, paint);
        }

        canvas.restore();


    }

    /**
     * Gets scale factor.
     *
     * @return the scale factor
     */
    public float getScaleFactor() {
        return mScaleFactor;
    }

    /**
     * The type Scale listener.
     */
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        /**
         * On scale.
         *
         * @param detector the detector
         * @return the boolean
         */
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor *= detector.getScaleFactor();
            mScaleFactor = Math.max(mMinFactor, Math.min(mScaleFactor, mMaxFactor));
            invalidate();
            return true;
        }
    }

    private class MyGestureListener extends GestureDetector.SimpleOnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.i("SCROLL", "Screen is scrolled by X & Y : " + distanceX + " " + distanceY);
            //mSurfaceHolder = getHolder();
            //mCanvas = mSurfaceHolder.lockCanvas(null);
            positionX += distanceX;
            positionY += distanceY;

            invalidate();

            //getHolder().
           // drawGraph(mCanvas,mNetworkGraph);
            //mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            //mCanvas.
            //postInvalidate();
            return true;
            //return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onDoubleTap(MotionEvent ev){
            Log.d("GSV", "Screen was double tapped");
            //Intent intent = new Intent(mainContext, edu.utdallas.mektek.polycraftapp.RecipeDetail.class);
            return super.onDoubleTap(ev);
        }
    }

}
