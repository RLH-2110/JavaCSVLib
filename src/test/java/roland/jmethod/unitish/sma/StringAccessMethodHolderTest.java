package roland.jmethod.unitish.sma;

import roland.commontestclasses.TestClass;
import roland.jmethod.amh.AccessMethodHolder;
import roland.jmethod.MethodFinder;
import roland.jmethod.amh.StringAccessMethodHolder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class StringAccessMethodHolderTest {

    TestClass testClass = new TestClass();

    @Test
    void testTest(){ // test that tests work
        Assertions.assertEquals(1,1);
    }

    @Test
    void SAMHBasicTest() throws Exception {
        AccessMethodHolder stringAndWByteS =
                MethodFinder.getSetterGetterMethods(testClass.getClass(),
                    testClass.getClass().getDeclaredField("age")
                );

        AccessMethodHolder stringS =
            MethodFinder.getSetterGetterMethods(testClass.getClass(),
                    testClass.getClass().getDeclaredField("name")
            );

        stringAndWByteS.attachObject(testClass);
        stringS.attachObject(testClass);

        StringAccessMethodHolder SAMH = new StringAccessMethodHolder(stringS);
        testClass.setName("");
        SAMH.invokeSetter("Queen of England");
        Assertions.assertEquals("Queen of England",testClass.getName());

        SAMH = new StringAccessMethodHolder(stringAndWByteS);
        testClass.setAge("0");
        SAMH.invokeSetter("127");
        Assertions.assertEquals(127,testClass.getAge());

    }

    @Test
    void SAMHPrimitiveTest() throws Exception {
        AccessMethodHolder rawTypeS =
                MethodFinder.getSetterGetterMethods(testClass.getClass(),
                    testClass.getClass().getDeclaredField("fav_number")
                );

        AccessMethodHolder wrapTypeS =
            MethodFinder.getSetterGetterMethods(testClass.getClass(),
                    testClass.getClass().getDeclaredField("least_fav_number")
            );

        AccessMethodHolder boolS =
            MethodFinder.getSetterGetterMethods(testClass.getClass(),
                    testClass.getClass().getDeclaredField("isTerran")
            );

        rawTypeS.attachObject(testClass);
        wrapTypeS.attachObject(testClass);
        boolS.attachObject(testClass);

        StringAccessMethodHolder SAMH = new StringAccessMethodHolder(rawTypeS);
        Assertions.assertFalse(SAMH.isOnlyClassSetter());
        testClass.setFav_number(0);
        SAMH.invokeSetter("69");
        Assertions.assertEquals(69,testClass.getFav_number());

        SAMH = new StringAccessMethodHolder(wrapTypeS);
        Assertions.assertFalse(SAMH.isOnlyClassSetter());
        testClass.setLeast_fav_number(0);
        SAMH.invokeSetter("451");
        Assertions.assertEquals(451,testClass.getLeast_fav_number());

        SAMH = new StringAccessMethodHolder(boolS);
        Assertions.assertFalse(SAMH.isOnlyClassSetter());
        testClass.setTerran(true);
        SAMH.invokeSetter("false");
        Assertions.assertFalse(testClass.isTerran());

    }

    @Test
    void SAMHCharAndStringBuildTest() throws Exception {
        AccessMethodHolder charS =
                MethodFinder.getSetterGetterMethods(testClass.getClass(),
                    testClass.getClass().getDeclaredField("favLetter")
                );

        AccessMethodHolder stringBuildS =
            MethodFinder.getSetterGetterMethods(testClass.getClass(),
                    testClass.getClass().getDeclaredField("titles")
            );

        charS.attachObject(testClass);
        stringBuildS.attachObject(testClass);

        StringAccessMethodHolder SAMH = new StringAccessMethodHolder(charS);
        Assertions.assertFalse(SAMH.isOnlyClassSetter());
        testClass.setFavLetter(' ');
        SAMH.invokeSetter("X");
        Assertions.assertEquals('X',testClass.getFavLetter());

        SAMH = new StringAccessMethodHolder(stringBuildS);
        Assertions.assertFalse(SAMH.isOnlyClassSetter());
        testClass.setTitles(new StringBuilder());
        SAMH.invokeSetter("Supreme Leader");
        Assertions.assertEquals("Supreme Leader",testClass.getTitles().toString());

    }




    @Test
    void SAMHExceptionsTest() throws Exception {
        AccessMethodHolder fav_numberS =
                MethodFinder.getSetterGetterMethods(testClass.getClass(),
                    testClass.getClass().getDeclaredField("fav_number")
                );

        fav_numberS.attachObject(testClass);

        StringAccessMethodHolder SAMH = new StringAccessMethodHolder(fav_numberS);
        try {
            SAMH.invokeSetter("X");
            Assertions.fail("Should have thrown an NumberFormatException");
        }catch (Exception e){
            Assertions.assertInstanceOf(NumberFormatException.class,e);
        }

        try {
            SAMH.invokeSetter(String.valueOf(Long.MAX_VALUE));
            Assertions.fail("Should have thrown an NumberFormatException");
        }catch (Exception e){
            Assertions.assertInstanceOf(NumberFormatException.class,e);
        }

        AccessMethodHolder topSecret =
                MethodFinder.getSetterGetterMethods(testClass.getClass(),
                    testClass.getClass().getDeclaredField("topSecret")
                );

        topSecret.attachObject(testClass);

        SAMH = new StringAccessMethodHolder(topSecret);
        try {
            SAMH.invokeSetter("Geheim");
            Assertions.fail("Should have thrown an NoSuchMethodException");
        }catch (Exception e){
            Assertions.assertInstanceOf(NoSuchMethodException.class,e);
        }

    }

    @Test
    void SAMHClassTest() throws Exception {
         AccessMethodHolder address =
                MethodFinder.getSetterGetterMethods(testClass.getClass(),
                    testClass.getClass().getDeclaredField("address")
                );

         address.attachObject(testClass);

        StringAccessMethodHolder SAMH = new StringAccessMethodHolder(address);
        Assertions.assertTrue(SAMH.isOnlyClassSetter());

        try {
            SAMH.invokeSetter("42");
            Assertions.fail("Should have thrown an NoSuchMethodException");
        }catch (Exception e){
            Assertions.assertInstanceOf(NoSuchMethodException.class,e);
        }

    }

    @Test
    void SAMHNoneFoundTest() throws Exception {
        // trying to invoke a method that does not have setters
        AccessMethodHolder readOnly =
                MethodFinder.getSetterGetterMethods(testClass.getClass(),
                    testClass.getClass().getDeclaredField("readOnly")
                );

        readOnly.attachObject(testClass);

        StringAccessMethodHolder SAMH = new StringAccessMethodHolder(readOnly);
        try {
            SAMH.invokeSetter("false");
            Assertions.fail("Should have thrown an NoSuchMethodException");
        }catch (Exception e){
            Assertions.assertInstanceOf(NoSuchMethodException.class,e);
        }

    }

 @Test
    void samhEquals() throws NoSuchFieldException {
        AccessMethodHolder at =
                MethodFinder.getSetterGetterMethods(TestClass.class,
                        TestClass.class.getDeclaredField("name")
                );

        AccessMethodHolder bt =
            MethodFinder.getSetterGetterMethods(TestClass.class,
                    testClass.getClass().getDeclaredField("name")
            );

        at.attachObject(testClass);
        bt.attachObject(testClass);
        StringAccessMethodHolder a = new StringAccessMethodHolder(at);
        StringAccessMethodHolder b = new StringAccessMethodHolder(bt);


        Assertions.assertEquals(a,b);

        b.attachObject(new TestClass());
        Assertions.assertNotEquals(a,b);

        bt = MethodFinder.getSetterGetterMethods(TestClass.class,
                    testClass.getClass().getDeclaredField("age")
            );
        bt.attachObject(testClass);
        b = new StringAccessMethodHolder(bt);

        Assertions.assertNotEquals(a,b);
    }

}
