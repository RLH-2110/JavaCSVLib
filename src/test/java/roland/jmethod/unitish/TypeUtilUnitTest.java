package roland.jmethod.unitish;

import roland.jmethod.amh.AccessMethodHolder;
import roland.jmethod.TypeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;


class TypeUtilUnitTest {

    @Test
    void testTest(){ // test that tests work
        Assertions.assertEquals(1,1);
    }

    @Test
    void testWrappers(){
        Assertions.assertTrue(TypeUtil.isPrimitiveOrWrapper(Byte.class));
        Assertions.assertTrue(TypeUtil.isPrimitiveOrWrapper(Character.class));
        Assertions.assertTrue(TypeUtil.isPrimitiveOrWrapper(Short.class));
        Assertions.assertTrue(TypeUtil.isPrimitiveOrWrapper(Integer.class));
        Assertions.assertTrue(TypeUtil.isPrimitiveOrWrapper(Long.class));
        Assertions.assertTrue(TypeUtil.isPrimitiveOrWrapper(Float.class));
        Assertions.assertTrue(TypeUtil.isPrimitiveOrWrapper(Double.class));
    }

    @Test
    void testPrimitive(){
        Assertions.assertTrue(TypeUtil.isPrimitiveOrWrapper(Byte.TYPE));
        Assertions.assertTrue(TypeUtil.isPrimitiveOrWrapper(Character.TYPE));
        Assertions.assertTrue(TypeUtil.isPrimitiveOrWrapper(Short.TYPE));
        Assertions.assertTrue(TypeUtil.isPrimitiveOrWrapper(Integer.TYPE));
        Assertions.assertTrue(TypeUtil.isPrimitiveOrWrapper(Long.TYPE));
        Assertions.assertTrue(TypeUtil.isPrimitiveOrWrapper(Float.TYPE));
        Assertions.assertTrue(TypeUtil.isPrimitiveOrWrapper(Double.TYPE));
    }

    @Test
    void testVoid(){
        Assertions.assertFalse(TypeUtil.isPrimitiveOrWrapper(Void.class));
        Assertions.assertFalse(TypeUtil.isPrimitiveOrWrapper(Void.TYPE));
    }

    @Test
    void testTypeTypes_Primitives_Wrappers(){
        TypeTestDataPositive testDataObject = new TypeTestDataPositive();

        for (Field field : testDataObject.getClass().getDeclaredFields()){
            Assertions.assertTrue(TypeUtil.isPrimitiveOrWrapper(field.getType()));
        }

        for (Method method : testDataObject.getClass().getDeclaredMethods()){
            Assertions.assertFalse(TypeUtil.isPrimitiveOrWrapper(method.getReturnType()));
        }
    }

    @Test
    void testTypeTypes_Classes(){
        TypeTestDataNegative testDataObject = new TypeTestDataNegative();

        for (Field field : testDataObject.getClass().getDeclaredFields()){
            Assertions.assertFalse(TypeUtil.isPrimitiveOrWrapper(field.getType()));
        }

    }
    @Test
    void getWrapperTest(){

        // wrapper returns wrapper
        Assertions.assertEquals(Byte.class, TypeUtil.getWrapperClass(Byte.class));
        Assertions.assertEquals(Character.class, TypeUtil.getWrapperClass(Character.class));
        Assertions.assertEquals(Short.class, TypeUtil.getWrapperClass(Short.class));
        Assertions.assertEquals(Integer.class, TypeUtil.getWrapperClass(Integer.class));
        Assertions.assertEquals(Long.class, TypeUtil.getWrapperClass(Long.class));
        Assertions.assertEquals(Float.class, TypeUtil.getWrapperClass(Float.class));
        Assertions.assertEquals(Double.class, TypeUtil.getWrapperClass(Double.class));
        Assertions.assertEquals(Boolean.class, TypeUtil.getWrapperClass(Boolean.class));

        // primitive returns primitive
        Assertions.assertEquals(Byte.class, TypeUtil.getWrapperClass(Byte.TYPE));
        Assertions.assertEquals(Character.class, TypeUtil.getWrapperClass(Character.TYPE));
        Assertions.assertEquals(Short.class, TypeUtil.getWrapperClass(Short.TYPE));
        Assertions.assertEquals(Integer.class, TypeUtil.getWrapperClass(Integer.TYPE));
        Assertions.assertEquals(Long.class, TypeUtil.getWrapperClass(Long.TYPE));
        Assertions.assertEquals(Float.class, TypeUtil.getWrapperClass(Float.TYPE));
        Assertions.assertEquals(Double.class, TypeUtil.getWrapperClass(Double.TYPE));
        Assertions.assertEquals(Boolean.class, TypeUtil.getWrapperClass(Boolean.TYPE));

        //neither primitive or wrapper
        try{
            TypeUtil.getWrapperClass(String.class);
            Assertions.fail("should have thrown an IllegalArgumentException!");
        }catch(Exception e){
            Assertions.assertInstanceOf(IllegalArgumentException.class, e);
        }

    }

    @Test
    void convertStringToWrapperTest(){
        Assertions.assertEquals(Byte.valueOf("123"), TypeUtil.convertStringToWrapper(Byte.TYPE,"123"));
        Assertions.assertEquals(Character.valueOf('?'), TypeUtil.convertStringToWrapper(Character.TYPE,"?"));
        Assertions.assertEquals(Short.valueOf("123"), TypeUtil.convertStringToWrapper(Short.TYPE,"123"));
        Assertions.assertEquals(Integer.valueOf("123"), TypeUtil.convertStringToWrapper(Integer.TYPE,"123"));
        Assertions.assertEquals(Long.valueOf("123"), TypeUtil.convertStringToWrapper(Long.TYPE,"123"));
        Assertions.assertEquals(Float.valueOf("123"), TypeUtil.convertStringToWrapper(Float.TYPE,"123"));
        Assertions.assertEquals(Double.valueOf("123"), TypeUtil.convertStringToWrapper(Double.TYPE,"123"));
        Assertions.assertEquals(Boolean.valueOf("123"), TypeUtil.convertStringToWrapper(Boolean.TYPE,"123"));


        Assertions.assertEquals(Byte.valueOf("123"), TypeUtil.convertStringToWrapper(Byte.class,"123"));
        Assertions.assertEquals(Character.valueOf('?'), TypeUtil.convertStringToWrapper(Character.class,"?"));
        Assertions.assertEquals(Short.valueOf("123"), TypeUtil.convertStringToWrapper(Short.class,"123"));
        Assertions.assertEquals(Integer.valueOf("123"), TypeUtil.convertStringToWrapper(Integer.class,"123"));
        Assertions.assertEquals(Long.valueOf("123"), TypeUtil.convertStringToWrapper(Long.class,"123"));
        Assertions.assertEquals(Float.valueOf("123"), TypeUtil.convertStringToWrapper(Float.class,"123"));
        Assertions.assertEquals(Double.valueOf("123"), TypeUtil.convertStringToWrapper(Double.class,"123"));
        Assertions.assertEquals(Boolean.valueOf("123"), TypeUtil.convertStringToWrapper(Boolean.class,"123"));

        try{
            TypeUtil.convertStringToWrapper(String.class,"123");
            Assertions.fail("should have thrown an IllegalArgumentException!");
        }catch(Exception e){
            Assertions.assertInstanceOf(IllegalArgumentException.class, e);
        }

        try{
            TypeUtil.convertStringToWrapper(Byte.class,"99999");
            Assertions.fail("should have thrown an NumberFormatException!");
        }catch(Exception e){
            Assertions.assertInstanceOf(NumberFormatException.class, e);
        }

    }

    @Test
    void trivialTest(){
        Assertions.assertTrue(TypeUtil.isTrivialType(int.class));
        Assertions.assertTrue(TypeUtil.isTrivialType(Integer.class));
        Assertions.assertTrue(TypeUtil.isTrivialType(String.class));
        Assertions.assertTrue(TypeUtil.isTrivialType(StringBuilder.class));
        Assertions.assertTrue(TypeUtil.isTrivialType(BigDecimal.class));
        Assertions.assertTrue(TypeUtil.isTrivialType(BigInteger.class));

        Assertions.assertFalse(TypeUtil.isTrivialType(Void.class));
        Assertions.assertFalse(TypeUtil.isTrivialType(TypeUtil.class));
    }

}


class TypeTestDataPositive{
    public static byte byteRaw;
    public static char charRaw;
    public static short shortRaw;
    public static int intRaw;
    public static long longRaw;
    public static float floatRaw;
    public static double doubleRaw;

    public static void voidRaw(){}

    public static Byte byteWrap;
    public static Character charWrap;
    public static Short shortWrap;
    public static Integer intWrap;
    public static Long longWrap;
    public static Float floatWrap;
    public static Double doubleWrap;

    public static Void voidWarp(){return null;}


}


class TypeTestDataNegative{
    public static String str;
    public static TypeTestDataPositive testDataObject;
    public static Object object;
    public static AccessMethodHolder accessMethodHolder;
}


