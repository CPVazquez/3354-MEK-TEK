package edu.utdallas.mektek.polycraftapp;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * Created by dnarayanan on 12/6/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class SQLinjectionTest {

    @Mock
    Context mContext;

    DatabaseHandler dbh;

    @Before
    public void setUp() throws Exception {
        //Context context =
        dbh = DatabaseHandler.getInstance(mContext);
    }

    @Test
    public void getProcessTreeTest1() throws Exception {
        try {
            dbh.getProcessTree(";DROP TABLE *;'");
        }catch(Exception e){
            assertTrue("This is a Error", e instanceof DatabaseHandler.ItemNotFoundException);
        }
    }

}