/*
 * Created on Jul 19, 2005
 *
 * Copyright (c) 2005, the JUNG Project and the Regents of the University 
 * of California
 * All rights reserved.
 *
 * This software is open-source under the BSD license; see either
 * "license.txt" or
 * http://jung.sourceforge.net/license.txt for a description.
 */
package giwi.org.networkgraph.beans;


import android.util.Log;

import net.xqhs.graphs.graph.SimpleNode;

import org.apache.commons.collections4.Transformer;
import org.w3c.dom.Node;

import java.util.Date;
import java.util.Random;

/**
 * Transforms the input type into a random location within
 * the bounds of the Dimension property.
 * This is used as the backing Transformer for the LazyMap
 * for many Layouts,
 * and provides a random location for unmapped vertices
 * the first time they are accessed.
 *
 * @param <V> the type parameter
 * @author Tom Nelson
 */
public class RandomLocationTransformer<V> implements Transformer<V, Point2D> {

    /**
     * The D.
     */
    private Dimension d;

    private static double previousWidth;
    private static double previousHeight;
    private static double iterator = 0;

    /**
     * The Random.
     */
    private Random random;

    /**
     * Creates an instance with the specified size which uses the current time
     * as the random seed.
     *
     * @param d the d
     */
    public RandomLocationTransformer(Dimension d) {
        this(d, new Date().getTime());
    }

    /**
     * Creates an instance with the specified dimension and random seed.
     *
     * @param d    the d
     * @param seed the seed
     */
    private RandomLocationTransformer(final Dimension d, long seed) {
        this.d = d;
        this.random = new Random(seed);
        this.previousWidth = random.nextDouble() * d.width;
        this.previousHeight = random.nextDouble() * d.height;
    }

    /**
     * Transform point 2 d.
     *
     * @param v the v
     * @return the point 2 d
     */
    public Point2D transform(V v) {

        Log.d("DIMENSIONS?", "width: " + d.width + " height: " + d.height);
        if(v instanceof Node){
            Log.d("TRANSFORM", "V is instance of Node");
        } else {
            Log.d("FAILED_TRANSFORM", v.getClass().toString());
            //SimpleNode sn = (SimpleNode) v;
            //sn.getLabel();
        }
        iterator += 350;
        return new Point2D(previousWidth, previousHeight + iterator);
        //return new Point2D(random.nextDouble() * d.width, random.nextDouble() * d.height);
    }
}
