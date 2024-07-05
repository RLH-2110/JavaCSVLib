package roland.jmethod.unitish.sma;

import roland.commontestclasses.Address;
import roland.commontestclasses.TestClass;
import roland.jmethod.amh.AccessMethodHolder;
import roland.jmethod.MethodFinder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import roland.jmethod.exceptions.MethodHolderNotAttachedException;

public class AccessMethodHolderUnitTest {

    TestClass testClass = new TestClass();

    @Test
    void testTest(){ // test that tests work
        Assertions.assertEquals(1,1);
    }

    @Test
    void AMHGetterBasic() throws Exception {
        AccessMethodHolder byteG =
                MethodFinder.getSetterGetterMethods(TestClass.class,
                        TestClass.class.getDeclaredField("age")
                );

        AccessMethodHolder stringG =
            MethodFinder.getSetterGetterMethods(TestClass.class,
                    testClass.getClass().getDeclaredField("name")
            );

        byteG.attachObject(testClass);
        stringG.attachObject(testClass);

        testClass.setName("Marcel Davis");
        Assertions.assertEquals("Marcel Davis",stringG.invokeGetter());

        testClass.setAge((byte) -15);
        Assertions.assertEquals((byte) -15,byteG.invokeGetter());

    }


    @Test
    void AMHGetterClass() throws Exception {
        AccessMethodHolder addressG =
                MethodFinder.getSetterGetterMethods(testClass.getClass(),
                    testClass.getClass().getDeclaredField("address")
                );

        addressG.attachObject(testClass);

        Address foo = new Address();
        foo.setCity("bar");

       testClass.setAddress(foo);
       Assertions.assertEquals(foo,addressG.invokeGetter());
    }




    @Test
    void AMHExceptionsTest() throws Exception {
        AccessMethodHolder topSecret =
                MethodFinder.getSetterGetterMethods(testClass.getClass(),
                        testClass.getClass().getDeclaredField("topSecret")
                );

        AccessMethodHolder notAttached =
                MethodFinder.getSetterGetterMethods(testClass.getClass(),
                        testClass.getClass().getDeclaredField("topSecret")
                );

        topSecret.attachObject(testClass);

        try {
            topSecret.invokeGetter();
            Assertions.fail("Should have thrown an NoSuchMethodException");
        } catch (Exception e) {
            Assertions.assertInstanceOf(NoSuchMethodException.class, e);
        }


         try {
            notAttached.invokeGetter();
            Assertions.fail("Should have thrown an MethodHolderNotAttachedException");
        } catch (Exception e) {
            Assertions.assertInstanceOf(MethodHolderNotAttachedException.class, e);
        }
    }

    @Test
    void attachWrongTest() throws Exception {
        AccessMethodHolder byteG =
                MethodFinder.getSetterGetterMethods(testClass.getClass(),
                    testClass.getClass().getDeclaredField("age")
                );

        Address other = new Address();;

        try{
            byteG.attachObject(other);
            Assertions.fail("Should have thrown an IllegalArgumentException");
        }catch (Exception e){
            Assertions.assertInstanceOf(IllegalArgumentException.class, e);
        }


    }

    @Test
    void amhEquals() throws NoSuchFieldException {
        AccessMethodHolder a =
                MethodFinder.getSetterGetterMethods(TestClass.class,
                        TestClass.class.getDeclaredField("name")
                );

        AccessMethodHolder b =
            MethodFinder.getSetterGetterMethods(TestClass.class,
                    testClass.getClass().getDeclaredField("name")
            );

        a.attachObject(testClass);
        b.attachObject(testClass);

        Assertions.assertEquals(a,b);

        b.attachObject(new TestClass());
        Assertions.assertNotEquals(a,b);

        b = MethodFinder.getSetterGetterMethods(TestClass.class,
                    testClass.getClass().getDeclaredField("age")
            );
        b.attachObject(testClass);

        Assertions.assertNotEquals(a,b);
    }
}
