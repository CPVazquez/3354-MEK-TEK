package giwi.org.networkgraph.beans;


import android.util.Log;

import org.apache.commons.collections4.Transformer;

import java.util.Date;
import java.util.Random;

/**
 * Created by trix1 on 12/5/2017.
 */

public class sequentialTransformer<V> implements Transformer<V,Point2D > {
    private Dimension d;
    private Random random;


    public sequentialTransformer(final Dimension d) {
        this.d = d;
    }
    @Override
    public Point2D transform(V input) {
        return new Point2D(500,500);
    }
}
