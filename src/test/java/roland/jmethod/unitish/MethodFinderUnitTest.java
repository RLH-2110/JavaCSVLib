package roland.jmethod.unitish;

import roland.commontestclasses.Address;
import roland.commontestclasses.Country;
import roland.commontestclasses.TestClass;
import roland.jmethod.amh.AccessMethodHolder;
import roland.jmethod.MethodFinder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;


class MethodFinderUnitTest {

    TestClass testClass = new TestClass();

    @Test
    void testTest(){ // test that tests work
        Assertions.assertEquals(1,1);
    }

    @Test
    void findMethodTest(){
        Optional<List<Method>> ret =
        MethodFinder.findMethodByNameOnly(testClass.getClass(),"getName");

        Assertions.assertTrue(ret.isPresent());
        Assertions.assertEquals(1,ret.get().size());
        Assertions.assertEquals("getName",ret.get().getFirst().getName());

        ret =
        MethodFinder.findMethodByNameOnly(testClass.getClass(),"setAge");

        Assertions.assertTrue(ret.isPresent());
        Assertions.assertEquals(2,ret.get().size());
        Assertions.assertEquals("setAge",ret.get().getFirst().getName());
        Assertions.assertEquals("setAge",ret.get().getLast().getName());

    }

    @Test
    void findMethodTest_notExist(){
        Optional<List<Method>> ret =
        MethodFinder.findMethodByNameOnly(testClass.getClass(),"LORUM IPSUM DIEZ NUTZ");

        Assertions.assertTrue(ret.isEmpty());

        ret =
        MethodFinder.findMethodByNameOnly(Address.class,"getAge");

         Assertions.assertTrue(ret.isEmpty());

    }

    @Test
    void findGetterAndSetterTest() throws Exception {
        AccessMethodHolder ret =
        MethodFinder.getSetterGetterMethods(testClass.getClass(),testClass.getClass().getDeclaredField("age"));

        ret.attachObject(testClass);

        Assertions.assertEquals(testClass,ret.getClassObj());
        Assertions.assertEquals(testClass.getClass().getDeclaredField("age"),ret.getField());


        Assertions.assertTrue(ret.getSetters().isPresent());
        Assertions.assertEquals(2,ret.getSetters().get().size());
        Assertions.assertEquals("setAge",ret.getSetters().get().getFirst().getName());
        Assertions.assertEquals("setAge",ret.getSetters().get().getLast().getName());

        Assertions.assertTrue(ret.getGetter().isPresent());
        Assertions.assertEquals("getAge",ret.getGetter().get().getName());
    }

     @Test
    void findGetterAndSetterTest_bool() throws Exception {
        AccessMethodHolder ret =
        MethodFinder.getSetterGetterMethods(TestClass.class,testClass.getClass().getDeclaredField("isTerran"));

        ret.attachObject(testClass);

        Assertions.assertEquals(testClass,ret.getClassObj());
        Assertions.assertEquals(testClass.getClass().getDeclaredField("isTerran"),ret.getField());


        Assertions.assertTrue(ret.getSetters().isPresent());
        Assertions.assertEquals(1,ret.getSetters().get().size());
        Assertions.assertEquals("setTerran",ret.getSetters().get().getFirst().getName());

        Assertions.assertTrue(ret.getGetter().isPresent());
        Assertions.assertEquals("isTerran",ret.getGetter().get().getName());


        ret =
        MethodFinder.getSetterGetterMethods(testClass.getClass(),testClass.getClass().getDeclaredField("diarrhea"));

        ret.attachObject(testClass);

        Assertions.assertEquals(testClass,ret.getClassObj());
        Assertions.assertEquals(testClass.getClass().getDeclaredField("diarrhea"),ret.getField());


        Assertions.assertTrue(ret.getSetters().isPresent());
        Assertions.assertEquals(1,ret.getSetters().get().size());
        Assertions.assertEquals("setDiarrhea",ret.getSetters().get().getFirst().getName());

        Assertions.assertTrue(ret.getGetter().isPresent());
        Assertions.assertEquals("getDiarrhea",ret.getGetter().get().getName());

    }

    @Test
    void findGetterAndSetterTest_Imposter() throws Exception {

        try{
            MethodFinder.getSetterGetterMethods(testClass.getClass(), Country.class.getDeclaredField("name"));
            Assertions.fail("Should have thrown an IllegalArgumentException");
        }catch (Exception e){
            Assertions.assertInstanceOf(IllegalArgumentException.class,e);
            Assertions.assertEquals("Field is from a different class!",e.getMessage());
        }

    }

    @Test
    void respectPrivateMethodsTest() throws NoSuchMethodException, NoSuchFieldException {
        Optional<List<Method>> ret =
        MethodFinder.findMethodByNameOnly(testClass.getClass(),"setTopSecret");

        TestClass.class.getDeclaredMethod("setTopSecret", String.class); // make sure it exist (will throw if it does not)
        Assertions.assertTrue(ret.isEmpty());


        AccessMethodHolder ret2 =
        MethodFinder.getSetterGetterMethods(testClass.getClass(),testClass.getClass().getDeclaredField("topSecret"));

        ret2.attachObject(testClass);

        Assertions.assertEquals(testClass,ret2.getClassObj());
        Assertions.assertEquals(testClass.getClass().getDeclaredField("topSecret"),ret2.getField());


        Assertions.assertTrue(ret2.getSetters().isEmpty());
        Assertions.assertTrue(ret2.getGetter().isEmpty());
    }

    @Test
    void rwExceptionTestTest() throws Exception {
        AccessMethodHolder amh = MethodFinder.getSetterGetterMethods(TestClass.class,testClass.getClass().getDeclaredField("name"));

        System.out.println("rwExceptionTestTest -> not attached test");
        Assertions.assertFalse(MethodFinder.rwExceptionTest(amh)); // not attached

        System.out.println("rwExceptionTestTest -> attached test");
        amh.attachObject(testClass);
        Assertions.assertTrue(MethodFinder.rwExceptionTest(amh));



        amh = MethodFinder.getSetterGetterMethods(testClass.getClass(),testClass.getClass().getDeclaredField("hash"));
        amh.attachObject(testClass);

        System.out.println("rwExceptionTestTest -> no setter test");
        Assertions.assertFalse(MethodFinder.rwExceptionTest(amh)); // no setter


        System.out.println("rwExceptionTestTest -> no getter test");
        amh = MethodFinder.getSetterGetterMethods(testClass.getClass(),testClass.getClass().getDeclaredField("writeOnly"));
        amh.attachObject(testClass);

        Assertions.assertFalse(MethodFinder.rwExceptionTest(amh)); // no getter
    }
}

