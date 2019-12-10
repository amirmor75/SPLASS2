package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Inventory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class InventoryTest {
    private  Inventory tester;

    @BeforeEach
    public void setUp(){
        tester = Inventory.getInstance();
        String gadgets[]={"knife", "gun"};
        tester.load(gadgets);
    }

    @Test
    public void testGetInstanceReturnsSameInstance(){
        Inventory temp=Inventory.getInstance();
        assertEquals(temp,tester);
    }

    @Test
    public void testGetInstanceOnlyOneExists(){
        Inventory temp=Inventory.getInstance();
        boolean isExists=tester.getItem("gun");
        assertEquals(false,temp.getItem("gun"));
        String gadgets[]={"sword"};
        tester.load(gadgets);
        assertEquals(true,temp.getItem("sword"));
    }

    @Test
    public void testLoad(){
        String gadgets[]={"RPG"};
        tester.load(gadgets);
        boolean isExists=tester.getItem("RPG");
        assertEquals(true,isExists);
    }

    @Test
    public void testGetItem(){
        boolean isExists=tester.getItem("gun");
        assertEquals(true,isExists);
        isExists=tester.getItem("Sword");
        assertEquals(false,isExists);
    }
}
