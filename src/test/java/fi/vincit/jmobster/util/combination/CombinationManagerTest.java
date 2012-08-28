package fi.vincit.jmobster.util.combination;

import org.junit.Test;

import java.lang.annotation.Annotation;

import static fi.vincit.jmobster.util.TestUtil.collectionFromObjects;
import static org.junit.Assert.*;

public class CombinationManagerTest {

    public static class ConcreteTestAnnotation implements Annotation {
        @Override
        public Class<? extends Annotation> annotationType() { return getClass(); }
    }
    
    public static class Type1 extends ConcreteTestAnnotation {}
    public static class Type2 extends ConcreteTestAnnotation {}
    public static class Type3 extends ConcreteTestAnnotation {}

    @Test
    public void testNoRequired() {
        CombinationManager cm = new CombinationManager( RequiredTypes.get());
        assertFalse(cm.matches( collectionFromObjects( new Type1() ) ) );
        assertFalse( cm.containsClass( Type1.class ) );
        assertEquals( null, cm.findClass( Type1.class ) );
    }

    @Test
    public void testOneRequired() {
        CombinationManager cm = new CombinationManager( RequiredTypes.get( Type1.class ));
        assertTrue(cm.matches( collectionFromObjects( new Type1() ) ) );
        assertTrue( cm.containsClass( Type1.class ) );
        assertEquals( Type1.class, cm.findClass( Type1.class ) );
    }

    @Test
    public void testTwoRequired() {
        CombinationManager cm = new CombinationManager(RequiredTypes.get(Type1.class, Type2.class));

        assertTrue(cm.matches( collectionFromObjects( new Type1(), new Type2() ) ) );
        assertTrue( cm.containsClass( Type1.class ) );
        assertEquals( Type1.class, cm.findClass( Type1.class ) );
        assertEquals( Type2.class, cm.findClass( Type2.class ) );
    }

    @Test
    public void testTwoRequiredOneOptional() {
        CombinationManager cm = new CombinationManager(
                RequiredTypes.get(Type1.class, Type2.class),
                OptionalTypes.get( Type3.class )
        );
        assertFalse( cm.matches( collectionFromObjects( new Type1(), new Type3() ) ) );

        assertTrue( cm.matches( collectionFromObjects( new Type1(), new Type2()) ) );
        assertTrue( cm.matches( collectionFromObjects( new Type1(), new Type2(), new Type3()) ) );
        assertTrue( cm.containsClass( Type1.class ) );
        assertTrue( cm.containsClass( Type3.class ) );
        assertEquals( Type1.class, cm.findClass( Type1.class ) );
        assertEquals( Type2.class, cm.findClass( Type2.class ) );
        assertEquals( Type3.class, cm.findClass( Type3.class ) );
    }

    @Test
    public void testNoClassFound() {
        CombinationManager cm = new CombinationManager( RequiredTypes.get(Type1.class), OptionalTypes.get(Type3.class));
        assertNull( cm.findClass( Type2.class ) );
        assertFalse( cm.containsClass( Type2.class ) );
    }

    @Test
    public void testEmptyCollections() {
        CombinationManager cm = new CombinationManager( RequiredTypes.get(), OptionalTypes.get() );
        assertFalse( cm.containsClass( Type2.class ) );
        assertNull( cm.findClass( Type2.class ) );
    }
}