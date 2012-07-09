package fi.vincit.jmobster.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CombinationManagerTest {

    @Test
    public void testOneRequired() {
        CombinationManager cm = new CombinationManager( RequiredTypes.get( String.class ));
        //assertTrue(cm.test(String.class));
        Assert.assertTrue( cm.containsClass( String.class ) );
        assertEquals(String.class, cm.findClass(String.class));
    }

    @Test
    public void testTwoRequired() {
        CombinationManager cm = new CombinationManager(RequiredTypes.get(String.class, Integer.class));
        //assertFalse( cm.test( String.class ) );

        //assertTrue( cm.test( String.class, Integer.class ) );
        Assert.assertTrue( cm.containsClass( String.class ) );
        assertEquals(String.class, cm.findClass(String.class));
        assertEquals(Integer.class, cm.findClass(Integer.class));
    }

    @Test
    public void testTwoRequiredOneOptional() {
        CombinationManager cm = new CombinationManager(
                RequiredTypes.get(String.class, Integer.class),
                OptionalTypes.get( Float.class )
        );
        //assertFalse( cm.matches( toList(String.class, Float.class) ) );

        //assertTrue( cm.matches( String.class, Integer.class ) );
        //assertTrue( cm.matches( String.class, Integer.class, Float.class ) );
        Assert.assertTrue( cm.containsClass( String.class ) );
        Assert.assertTrue( cm.containsClass( Float.class ) );
        assertEquals(String.class, cm.findClass(String.class));
        assertEquals(Integer.class, cm.findClass(Integer.class));
        assertEquals(Float.class, cm.findClass(Float.class));
    }

    private static List<Class> toList(Class...classes) {
        List<Class> list = new ArrayList<Class>(classes.length);
        for( Class c : classes ) {
            list.add(c);
        }
        return list;
    }
}