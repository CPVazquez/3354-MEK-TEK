package edu.utdallas.mektek.polycraftapp;

/**
 * A class to test the inRange function in MainActivity
 * Author: Carla Vazquez
 * Date: 12/5/17
 * Version: 1.0
 */
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import org.junit.Before;

import giwi.org.networkgraph.beans.Point2D;

public class RangeFunctionTest {
    MainActivity tester;

    @Before
    public void setup(){
        tester = new MainActivity();
    }

    @Test
    public void test1(){
        Point2D point = new Point2D();
        point.setLocation(30,50);
        assertEquals("Point 25, 10 is within rage", true, tester.inRange(25, 10, point, 40));
    }

    @Test
    public void test2(){
        Point2D point = new Point2D();
        point.setLocation(90,90);
        assertEquals("Point 25, 10 is within rage", false, tester.inRange(25, 10, point, 40));
    }

    @Test
    public void test3(){
        Point2D point = new Point2D();
        point.setLocation(1400,90);
        assertEquals("Point 1360, 50 is within rage", true, tester.inRange(1360, 50, point, 40));
    }

    @Test
    public void test4(){
        Point2D point = new Point2D();
        point.setLocation(1400,90);
        assertEquals("Point 1340, 50 is within rage", false, tester.inRange(1340, 50, point, 40));
    }

}
