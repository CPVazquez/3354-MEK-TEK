package edu.utdallas.mektek.polycraftapp;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by trix1 on 12/6/2017.
 */
@RunWith(MockitoJUnitRunner.class)
public class ItemCreationTest {

    @Mock
    Context mMockContext;

    private DatabaseHandler dbh;
    @Before
    public void setup(){
        this.dbh = DatabaseHandler.getInstance(mMockContext);
    }
    @Test
    public void getItemWithId(String id, Item output) throws DatabaseHandler.ItemNotFoundException {
    
    }

}