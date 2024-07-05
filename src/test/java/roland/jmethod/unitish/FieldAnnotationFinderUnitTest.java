package roland.jmethod.unitish;
import roland.commontestclasses.TestClass;
import roland.jmethod.amh.AccessMethodHolder;
import roland.jmethod.FieldAnnotationFinder;
import roland.jmethod.MethodFinder;
import roland.jmethod.testclasses.testAnnotation;
import roland.jmethod.testclasses.TestClassAnnotation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class FieldAnnotationFinderUnitTest {

    TestClassAnnotation testClass = new TestClassAnnotation();

    @Test
    void testTest(){ // test that tests work
        Assertions.assertEquals(1,1);
    }

    @Test
    void findAnnotations() throws NoSuchFieldException {

        initTest(); // init values we will compare against

        // do the function, we compare later
        List<AccessMethodHolder> ret =
        FieldAnnotationFinder.getFieldAnnotationFieldMethods(TestClass.class,
                testAnnotation.class);

        // it returns a list, assert for every element
        for (AccessMethodHolder holder : ret) {

            holder.attachObject(testClass);

            // same class?
            Assertions.assertEquals(TestClassAnnotation.class, holder.getClassObj());

            // one of the fields we expect, and its only in the list once?
            Assertions.assertTrue(expectedFields.contains(new TestField(holder.getField())));
            expectedFields.remove(new TestField(holder.getField())); // we dont want to find the same field twice

            // has the right getter?
            Assertions.assertEquals(
                    expectedGetters.get(holder.getField().getName())
                    ,holder.getGetter()
            );

            // has the right setters?
            Assertions.assertEquals(
                    expectedSetters.get(holder.getField().getName())
                    ,holder.getSetters()
            );

        }
    }


    List<TestField> expectedFields = new ArrayList<>();

    Map<String ,Optional<Method>> expectedGetters = new HashMap<>();
    Map<String,Optional<List<Method>>> expectedSetters = new HashMap<>();

    /**
     * Initializes the expected data
     */
    void initTest() throws NoSuchFieldException {

        // note: Field has no good .equals method and is final, so I need a wrapper
        TestField[] fields =  {
                new TestField(TestClassAnnotation.class.getDeclaredField("name")),
                new TestField(TestClassAnnotation.class.getDeclaredField("age")),
                new TestField(TestClassAnnotation.class.getDeclaredField("birthYear")),
                new TestField(TestClassAnnotation.class.getDeclaredField("address")),
                new TestField(TestClassAnnotation.class.getDeclaredField("hash")),
                new TestField(TestClassAnnotation.class.getDeclaredField("yourMom")),
                new TestField(TestClassAnnotation.class.getDeclaredField("neighbors")),
        };

        expectedFields.clear();
        for (TestField field : fields)  // add all fields
            expectedFields.add(field);

        expectedGetters.clear();
        for (TestField field : fields) // add all getters
            expectedGetters.put(field.field.getName(),
                    MethodFinder.getSetterGetterMethods(testClass.getClass(),field.field).getGetter());

        expectedSetters.clear();
        for (TestField field : fields) // add all setters
            expectedSetters.put(field.field.getName(),
                    MethodFinder.getSetterGetterMethods(testClass.getClass(),field.field).getSetters());

    }

}

/**
 * wrapper for Field, so we have an usable equals method
 */
class TestField{
    public Field field;

    public TestField(Field field) {
        this.field = field;
    }

    @Override
    public boolean equals(Object otherRaw){

        if (!(otherRaw instanceof TestField))
            return false;

        TestField other = (TestField) otherRaw;

        if (other.field.getDeclaringClass() != field.getDeclaringClass())
            return false;

        if (!other.field.getName().equals(field.getName()))
            return false;

        return true;

    }

}
