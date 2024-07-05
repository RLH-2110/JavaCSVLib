package roland.jmethod.unitish.sma;

import roland.commontestclasses.Address;
import roland.commontestclasses.AddressCountry;
import roland.commontestclasses.Country;
import roland.commontestclasses.TestClass;
import roland.jmethod.amh.AccessMethodHolder;
import roland.jmethod.amh.ClassAccessMethodHolder;
import roland.jmethod.MethodFinder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ClassAccessMethodHolderUnitTest {
    TestClass testClass = new TestClass();
    AddressCountry addressCountry = new AddressCountry();

    @Test
    void testTest(){
        Assertions.assertEquals(1,1);
    }

    @Test
    void CAMHBasicTest() throws Exception {
        AccessMethodHolder testClassS =
                MethodFinder.getSetterGetterMethods(testClass.getClass(),
                    testClass.getClass().getDeclaredField("address")
                );

        AccessMethodHolder addressCountryS =
            MethodFinder.getSetterGetterMethods(addressCountry.getClass(),
                    addressCountry.getClass().getDeclaredField("country")
            );

        testClassS.attachObject(testClass);
        addressCountryS.attachObject(addressCountry);

        Address testClassV = new Address();
        Country addressCountryV = new Country();

        ClassAccessMethodHolder CAMH = new ClassAccessMethodHolder(testClassS);
        testClass.setAddress(null);
        CAMH.invokeSetter(testClassV);
        Assertions.assertEquals(testClassV,testClass.getAddress());

        CAMH = new ClassAccessMethodHolder(addressCountryS);
        addressCountry.setCountry(null);
        CAMH.invokeSetter(addressCountryV);
        Assertions.assertEquals(addressCountryV,addressCountry.getCountry());

    }





    @Test
    void SAMHExceptionsTest() throws Exception {
       Country country = new Country();

       // trying o invoke non-class field
        AccessMethodHolder code =
                MethodFinder.getSetterGetterMethods(country.getClass(),
                    country.getClass().getDeclaredField("code")
                );

        code.attachObject(country);

        ClassAccessMethodHolder CAMH = new ClassAccessMethodHolder(code);
        try {
            CAMH.invokeSetter("IT");
            Assertions.fail("Should have thrown an NoSuchMethodException");
        }catch (Exception e){
            Assertions.assertInstanceOf(NoSuchMethodException.class,e);
        }

        // trying to invoke private field
        AccessMethodHolder agentFor =
                MethodFinder.getSetterGetterMethods(testClass.getClass(),
                    testClass.getClass().getDeclaredField("agentFor")
                );

        agentFor.attachObject(testClass);

        CAMH = new ClassAccessMethodHolder(agentFor);
        try {
            CAMH.invokeSetter(new Country());
            Assertions.fail("Should have thrown an NoSuchMethodException");
        }catch (Exception e){
            Assertions.assertInstanceOf(NoSuchMethodException.class,e);
        }

    }

    @Test
    void CAMHNoneFoundTest() throws Exception {

        // trying to invoke a method that does not have setters
        AccessMethodHolder address =
                MethodFinder.getSetterGetterMethods(testClass.getClass(),
                    testClass.getClass().getDeclaredField("secretAddress")
                );

        address.attachObject(testClass);

        ClassAccessMethodHolder CAMH = new ClassAccessMethodHolder(address);
        try {
            CAMH.invokeSetter(new Address());
            Assertions.fail("Should have thrown an NoSuchMethodException");
        }catch (Exception e){
            Assertions.assertInstanceOf(NoSuchMethodException.class,e);
        }

    }


    @Test
    void camhEquals() throws NoSuchFieldException {
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
        ClassAccessMethodHolder a = new ClassAccessMethodHolder(at);
        ClassAccessMethodHolder b = new ClassAccessMethodHolder(bt);


        Assertions.assertEquals(a,b);

        b.attachObject(new TestClass());
        Assertions.assertNotEquals(a,b);

        bt = MethodFinder.getSetterGetterMethods(TestClass.class,
                    testClass.getClass().getDeclaredField("age")
            );
        bt.attachObject(testClass);
        b = new ClassAccessMethodHolder(bt);

        Assertions.assertNotEquals(a,b);
    }
}
