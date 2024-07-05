package roland.jmethod;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import roland.jmethod.amh.AccessMethodHolder;
import roland.jmethod.amh.ClassAccessMethodHolder;
import roland.jmethod.amh.FieldPathHolder;
import roland.jmethod.amh.StringAccessMethodHolder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Helper class to help with finding certain methods, like getters and setters
 */
public final class MethodFinder {

    private MethodFinder() {} // we dont need a constructor

    /**
     * takes a field name and a class, and returns the getter and setter of the field
     *
     * @param clazz the class we want to get the methods from
     * @param field the field we want to get the setter and getter from
     * @return an AccessMethodHolder that contains the getter and setters as well as the class and field
     * @see AccessMethodHolder
     */
    public static @NotNull AccessMethodHolder getSetterGetterMethods(@NotNull Class<?> clazz, @NotNull Field field){

        String fieldName = field.getName();

        boolean booleanEdgeCase = false; // if your name your bool isSomething, then you dont name your getter isIsSomething


        // filter out invalid arguments
        if (clazz == null) throw new NullPointerException("clazz is null");
        if (fieldName == null) throw new NullPointerException("fieldName is null");
        if (fieldName.isEmpty()) throw new IllegalArgumentException("fieldName is empty");

        // catch that the class from the field is not the same as the clazz
        try{ if (!(clazz.getDeclaredField(fieldName).equals(field))) throw new NoSuchFieldException();
        } catch (NoSuchFieldException e) {
            throw new IllegalArgumentException("Field is from a different class!");
        }

        Optional<Method> getter;
        Optional<List<Method>> setters;
        String methodName;

        // getter logic
        {
            // construct method name by making the first letter uppercase and prepending get
            methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

            // get getter, and convert Optional<List<Method>> to Optional<Method>
            if (findMethodByNameOnly(clazz, methodName).isPresent())
                getter = Optional.of(findMethodByNameOnly(clazz, methodName).get().getFirst());
            else
                getter = Optional.empty();

            // logic for boolean getters
            if(getter.isEmpty()){ // if we dont have normal getters

                // booleans have "is getters" instead of normal getters
                methodName = "is" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);

               // get getter, and convert Optional<List<Method>> to Optional<Method>
                if (findMethodByNameOnly(clazz, methodName).isPresent())
                    getter = Optional.of(findMethodByNameOnly(clazz, methodName).get().getFirst());




                if(getter.isEmpty()){ // double edge case!
                    // "is" is already in the fieldName!
                    methodName = fieldName;
                    if (findMethodByNameOnly(clazz, methodName).isPresent()){
                        getter = Optional.of(findMethodByNameOnly(clazz, methodName).get().getFirst());
                        booleanEdgeCase = true;
                    }
                }


            }
        }

        // setter logic

        methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        setters = findMethodByNameOnly(clazz,methodName);

        if (setters.isEmpty() && booleanEdgeCase){
            methodName = "set" + fieldName.substring(2); // remove the "is" and prefix "set"
            setters = findMethodByNameOnly(clazz,methodName);
        }

        return new AccessMethodHolder(clazz,field,getter,setters);
    }



    /**
     * finds the PUBLIC method by only using the method name
     * @param clazz the class to find the methods in
     * @param methodName the name of the method we want to find
     * @return an optional with a list of methods with a matching name
     */
    public static @NotNull Optional<List<Method>> findMethodByNameOnly(@NotNull Class<?> clazz, @NotNull String methodName){
        List<Method> methods = new ArrayList<>();

        for (Method method : clazz.getMethods()){
            if (method.getName().equals(methodName))
                methods.add(method);
        }

        if (methods.isEmpty())
            return Optional.empty();

        return Optional.of(methods);
    }


    /**
     * Finds all fields where both getter and setter are usable without error
     * @param clazz the class we want to find the fields in
     * @param path the path we will prefix
     * @return a list of {@link FieldPathHolder FieldPathHolders} with the field and the methodsolder
     */
    public static @NotNull List<FieldPathHolder> findAllRWFieldMethods(@NotNull Class<?> clazz, @NotNull String path){ // TODO write test
        List<FieldPathHolder> methods = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()){
            AccessMethodHolder amh = getSetterGetterMethods(clazz, field);

            if (rwExceptionTest(amh)) // if setters and getters are usable
                methods.add(new FieldPathHolder(amh,path+"."+field.getName()));
        }

        return methods;
    }


    /**
     * will test if an AccessMethodHolder can use both the setter and the getter
     * (moved from CSV package to here, because we need it here)
     * @param methodHolder the method holder we want to test
     * @return true if we can call getter and setter without error
     * @see AccessMethodHolder
     */
    public static boolean rwExceptionTest(@Nullable AccessMethodHolder methodHolder) {
        try {
            // will trigger exception if there is no getter or setter


            // create dummy and assign a methodHolder to itself, to check for exceptions
            Object dummy = TypeUtil.getNewObject(methodHolder.getClazz());

            methodHolder.attachObject(dummy);

            if (methodHolder.isTrivialSetter()) {
                StringAccessMethodHolder test = new StringAccessMethodHolder(methodHolder);
                test.invokeSetter(test.invokeGetter()); // trigger exception if methods are missing
            } else {
                ClassAccessMethodHolder test = new ClassAccessMethodHolder(methodHolder);
                test.invokeSetter(test.invokeGetter()); // trigger exception if methods are missing
            }
        } catch (Exception e) {
            return false; // exception happened
        }
        return true; // no exception happened
    }
}
