package edu.utdallas.mektek.polycraftapp;

/**
 * A class to test the getFileNamePath function in MainActivity
 * Author: Chandranshu Rao
 * Date: 12/6/17
 * Version: 1.0
 */

import org.junit.*;

import java.io.File;

import static junit.framework.Assert.assertEquals;

public class FileNamePathTest {

    @Test
    public void testOne(){
        Item item = new Item("qg", "Drum (Crude Oil)",
                new File("https://polycraft.utdallas.edu/index.php?title=File:Vessel_drum.png"),
                "https://polycraft.utdallas.edu/index.php?title=Drum_(Crude_Oil)", 1);
        assertEquals("Drum (Crude Oil) image", "vessel_drum.png", item.getFileNamePath());
    }

    @Test
    public void testTwo(){
        Item item = new Item("g8", "Vial (Gas Oil)",
                new File("https://polycraft.utdallas.edu/index.php?title=File:Vessel_vial.png"),
                "https://polycraft.utdallas.edu/index.php?title=Vial_(Gas_Oil)", 0);
        assertEquals("Vial (Gas Oil) image", "vessel_vial.png", item.getFileNamePath());
    }

    @Test
    public void testThree(){
        Item item = new Item("g", "Shale",
                new File("https://minecraft.gamepedia.com/File:Shale.png"),
                "https://polycraft.utdallas.edu/index.php?title=Shale", 1);
        assertEquals("Shale image", "shale.png", item.getFileNamePath());
    }

    @Test
    public void testFour(){
        Item item = new Item("3m", "Bucket (Crude Oil)",
                new File("https://minecraft.gamepedia.com/File:Bucket.png"),
                "https://polycraft.utdallas.edu/index.php?title=Bucket_(Crude_Oil)", 1);
        assertEquals("Bucket (Crude Oil) image", "bucket.png", item.getFileNamePath());
    }
}
