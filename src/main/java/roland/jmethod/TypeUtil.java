package roland.jmethod;

import java.lang.reflect.*;

import org.jetbrains.annotations.NotNull;
import roland.jmethod.amh.AccessMethodHolder;
import roland.jmethod.amh.FieldPathHolder;
import roland.jmethod.amh.StringAccessMethodHolder;
import roland.jmethod.exceptions.MethodException;
import roland.jmethod.exceptions.SearchDepthException;
import roland.jmethod.exceptions.TypeException;
import roland.common.StaticLogger;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Utility Class that provides static methods
 */
public final class TypeUtil { // "static" class
    private TypeUtil(){}

    private static final Map<Class<?>,Class<?>> primitiveToWrapperMap = new HashMap<>();

    /**
     * checks if type is primitive or a wrapper of a primitive type
     * ignores void
     * @param type the type to check
     * @return true if primitive or wrapper, false is not primitive or void
     */
    public static boolean isPrimitiveOrWrapper(@NotNull Class<?> type){

        if (type.isPrimitive() && type != Void.TYPE)
                return true;

        if ( // if wrapper
                type == Boolean.class  ||
                type == Byte.class  ||
                type == Character.class  ||
                type == Short.class  ||
                type == Integer.class  ||
                type == Long.class  ||
                type == Float.class  ||
                type == Double.class
        )
            return true;

        return false;
    }


    /**
     * gets the wrapper for a primitive type. (example argument of type int returns Integer)
     * @param type the type we want to find the wrapper of
     * @return the wrapper of the type, or the argument, if the argument was a wrapper already
     */
    public static Class<?> getWrapperClass(@NotNull Class<?> type){
        if (!isPrimitiveOrWrapper(type))
            throw new IllegalArgumentException("getWrapperClass, parameter must be a primitive or wrapper of a primitive!");
        return primitiveToWrapperMap.get(type);
    }


    /**
     * This method converts a String to any given primitive wrapper (for example String to Integer)
     * this version of the method forces exception handling, if you want to ignore exceptions see {@link #convertStringToWrapper}
     * @param primitive the type we want to convert into (primitive or primitive wrapper)
     * @param str the string we want to convert
     * @return the wrapper type that was specified, with the converted string value
     * @param <T> this is the type specified in the "primitive" parameter
     */
    public static <T> T convertStringToWrapper_NoIgnore(Class<?> primitive, String str)  throws TypeException, NumberFormatException, MethodException{
        return convertStringToWrapper(primitive, str);
    }

    /**
     * This method converts a String to any given primitive wrapper (for example String to Integer)
     * this version of the method hides the exception, if you want to force exception handling see {@link #convertStringToWrapper_NoIgnore}
     * @param primitive the type we want to convert into (primitive or primitive wrapper)
     * @param str the string we want to convert
     * @return the type that was specified, with the converted string value
     * @param <T> this is the type specified in the "primitive" parameter
     */
    // TODO test this with null
    public static <T> T convertStringToWrapper(@NotNull Class<?> primitive, @NotNull String str) {
        if (!isPrimitiveOrWrapper(primitive))
            throw new IllegalArgumentException();

        Class<?> wrapperClass = getWrapperClass(primitive); // make type a wrapper, if it is not one already

        try{

            if (wrapperClass == Character.class) // if it's a char
                return (T) (Character)str.charAt(0);
            else                                 // if it's a number or boolean
                return (T) wrapperClass.getMethod("valueOf", String.class) // invoke the valueOf method of the wrapper
                        .invoke(wrapperClass, str);

        }catch(NoSuchMethodException | IllegalAccessException e){

            // build stack trace
            StringBuilder stackTrace = new StringBuilder();
            for (StackTraceElement stackTraceElement : e.getStackTrace())
                stackTrace.append("\t").append(stackTraceElement.toString()).append("\n");

            StaticLogger.log(StringAccessMethodHolder.class, Level.WARNING,
                    String.format("Error: Could not convert string to type%nError Message: %s%nCause %s%nTrace:%n %s",
                            e.getMessage(),e.getCause(),stackTrace));

            throw new TypeException(e);


        }catch (InvocationTargetException e){ // wraps exceptions that happened in the method
            Throwable cause = e.getCause();

            if (cause instanceof NumberFormatException)
                throw (NumberFormatException) cause;   // unwrap exception

            // I think we never get here
            throw new MethodException(cause);  // its another exception, just re-wrap it.
        }

    }

    /**
     * @param type the class of the type
     * @return true for any type where the value can easily be extracted or set
     * Meaning: any primitive, Primitive wrapper, String, StringBuilder, BigInteger or BigDecimal
     */
    public static boolean isTrivialType(@NotNull Class<?> type){
        if (isPrimitiveOrWrapper(type))
            return true;

        if (
                type == String.class ||
                type == StringBuilder.class ||
                type == BigInteger.class ||
                type == BigDecimal.class
        )   return true;

        return false;
    }

    static {
        primitiveToWrapperMap.put(Boolean.TYPE, Boolean.class);
        primitiveToWrapperMap.put(Byte.TYPE, Byte.class);
        primitiveToWrapperMap.put(Character.TYPE, Character.class);
        primitiveToWrapperMap.put(Double.TYPE, Double.class);
        primitiveToWrapperMap.put(Float.TYPE, Float.class);
        primitiveToWrapperMap.put(Integer.TYPE, Integer.class);
        primitiveToWrapperMap.put(Long.TYPE, Long.class);
        primitiveToWrapperMap.put(Short.TYPE, Short.class);

        primitiveToWrapperMap.put(Boolean.class, Boolean.class);
        primitiveToWrapperMap.put(Byte.class, Byte.class);
        primitiveToWrapperMap.put(Character.class, Character.class);
        primitiveToWrapperMap.put(Double.class, Double.class);
        primitiveToWrapperMap.put(Float.class, Float.class);
        primitiveToWrapperMap.put(Integer.class, Integer.class);
        primitiveToWrapperMap.put(Long.class, Long.class);
        primitiveToWrapperMap.put(Short.class, Short.class);
    }

    /**
     * creates a new object from a class (uses the empty/default constructor!)
     * @param clazz the class we want to create an object for
     * @return the created object
     * @throws NoSuchMethodException did not find the constructor
     * @throws TypeException something went wrong, you can see what happened in the cause
     */
    public static @NotNull Object getNewObject(@NotNull Class<?> clazz) throws NoSuchMethodException, TypeException{ // TODO write test
        try {
            return clazz.getDeclaredConstructor().newInstance();
        }catch (InvocationTargetException | InstantiationException | IllegalAccessException e){
            throw new TypeException(e);
        }

    }


    /**
     * gets path and the getters for class parameters in classes
     * @param accessMethodHolder the access method holder of the thing with a class return type
     * @param currentPath the current path. for example, a test class has a Country field which has a name field. the path would be Country.name.
     *                    furthermore if the Country had an Person field with name, the path from the test class would be Country.Person.name
     * @param maxDepth how deep we should search / how deep the recursion is allowed to go.
     * @return a list of {@link FieldPathHolder FieldPathHolders}
     * @throws SearchDepthException when the search depth was exceeded
     * @see AccessMethodHolder
     */
    public static @NotNull List<FieldPathHolder> convertIntoPathHolder(@NotNull AccessMethodHolder accessMethodHolder, @NotNull String currentPath, int maxDepth, boolean IgnoreNullErrors) throws SearchDepthException{
        if (maxDepth < 0)
            throw new SearchDepthException();

        List<FieldPathHolder> pathHolders = MethodFinder.findAllRWFieldMethods(
                accessMethodHolder.getGetter().get().getReturnType(),
                currentPath
        );

        // for all pathHolders (except the new ones we might add in the loop)
        for (int i = 0;i < pathHolders.size();i++){

            try {
                pathHolders.get(i).attachObject(
                        accessMethodHolder.invokeGetter()
                );
            }catch (NoSuchMethodException | NullPointerException e){

                // only if we want to handle null errors
                if (e instanceof NumberFormatException && !IgnoreNullErrors) {
                    StaticLogger.log(TypeUtil.class, Level.WARNING, "Getters/Setters for " + pathHolders.get(i).getFieldName() + " can not be used with " + accessMethodHolder.getGetter().get().getReturnType());

                    // remove if it does not work and try the next one
                    pathHolders.remove(i);
                    i--;
                    continue;
                }
            }

            if (pathHolders.get(i).isTrivialGetter())
                continue;

            // class in calss in class ...
            // call recursive and add the new ones, we WONT iterate over them.
            pathHolders.addAll(
                    convertIntoPathHolder(
                            pathHolders.get(i).getAccessMethodHolder(),
                            currentPath+"."+ pathHolders.get(i).getFieldName(),
                            maxDepth-1,
                            IgnoreNullErrors
                    )
            );

        }

        return pathHolders;
    }


}
