package roland.jmethod.amh;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import roland.jmethod.exceptions.ClassSetterCantParseStringException;
import roland.jmethod.exceptions.MethodException;
import roland.jmethod.exceptions.MethodHolderNotAttachedException;
import roland.jmethod.exceptions.TypeException;
import roland.jmethod.TypeUtil;
import roland.common.StaticLogger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

/**
 * an extended AccessMethodHolder that lets you call a setter with a string parameter
 * even if there is no string setter, but an int setter for example
 */
public class StringAccessMethodHolder extends AccessMethodHolder implements ExtendedAMHInterface{

    private Method stringSetter;
    private Method alternativeSetter;
    private boolean onlyClassSetter;

    public StringAccessMethodHolder(){
        super();
        stringSetter = null;
        alternativeSetter = null;
    }

    public StringAccessMethodHolder(AccessMethodHolder Super){
        super(Super);
        findStringSetter();
    }

    @Override
    public void setAll(Class<?> clazz, Field field, Optional<Method> getter, Optional<List<Method>> setters){
        super.setAll(clazz,field,getter,setters);
        findStringSetter();
    }

    /**
     * invokes the saved setter
     * @param stringArgument the string we want to set. MUST BE A STRING!
     * @throws NoSuchMethodException there might not be a setter. see isInvokable()
     * @throws NumberFormatException conversion might fail
     */
    public void invokeSetter(@Nullable Object stringArgument) throws NoSuchMethodException, NumberFormatException, TypeException {
        if (classObj == null)
            throw new MethodHolderNotAttachedException();
        if (setters.isEmpty())
            throw new NoSuchMethodException(clazz.getName()+"has no setters!");

        // if its not a string
        if (!(stringArgument instanceof String) && stringArgument != null) {

            if (TypeUtil.isTrivialType(stringArgument.getClass()))
                stringArgument = stringArgument.toString(); // then make it a string
            else
                throw new IllegalArgumentException("Input must be a trivial type!"); // unless its not trivial
        }

        String argument = (String) stringArgument;

        if (tryInvokeRealStringSetter(argument))
            return; // we managed to call an existing string setter

        // there is no string setter, or we failed in calling it

        if (stringArgument == null){ // string can not be null in the alternative setter!
            if (alternativeSetter.getParameterTypes()[0].equals(char.class) || alternativeSetter.getParameterTypes()[0].equals(Character.class))
                stringArgument = new String(" ");
            else
              stringArgument = new String("0");
        }

        // try the alternative setter
        tryInvokeAlternativeSetter(argument);
    }

    /**
     * invokes an existing String setter and handles the errors
     * @param argument the argument you want to pass in the setter
     * @return returns true when the method was invoked
     */
    private boolean tryInvokeRealStringSetter(@NotNull String argument){

        if (stringSetter != null){
            try {
                stringSetter.invoke(classObj, argument);
                return true;
            }catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException e){

                // build stack trace
                StringBuilder stackTrace = new StringBuilder();
                for (StackTraceElement stackTraceElement : e.getStackTrace())
                    stackTrace.append("\t").append(stackTraceElement.toString()).append("\n");

                StaticLogger.log(StringAccessMethodHolder.class, Level.SEVERE,
                        String.format("Error: Could not invoke real String setter%nError Message: %s%nCause %s%nTrace:%n %s",
                            e.getMessage(),e.getCause(),stackTrace));

                return false; // we can try again with another setter, if applicable
            }
        }
        return false;
    }



    /**
     * if no string setter is present, then this function is called when we  to invoke the invokeStringSetter method
     * @param argument the string we want to set the object to
     * @throws NoSuchMethodException there might not be a setter. you can call isOnlyClassSetter() to get this info beforehand
     * @throws NumberFormatException if the number is too big for the type, or someone tries to convert abc to int
     */
     private void tryInvokeAlternativeSetter(@NotNull String argument) throws NoSuchMethodException, NumberFormatException, TypeException {

        if (alternativeSetter != null){
            try {

                if (alternativeSetter.getParameterTypes()[0].equals(StringBuilder.class)) {
                    alternativeSetter.invoke(classObj, new StringBuilder(argument));
                    return;
                }

                if (TypeUtil.isPrimitiveOrWrapper(alternativeSetter.getParameterTypes()[0])){
                    alternativeSetter.invoke(
                            classObj,
                            (Object) TypeUtil.convertStringToWrapper_NoIgnore(alternativeSetter.getParameterTypes()[0], argument)
                    );
                   return;
                }

                 if (alternativeSetter.getParameterTypes()[0].equals(BigInteger.class)) {
                    alternativeSetter.invoke(classObj, new BigInteger(argument));
                    return;
                 }

                 if (alternativeSetter.getParameterTypes()[0].equals(BigDecimal.class)) {
                    alternativeSetter.invoke(classObj, new BigDecimal(argument));
                    return;
                 }

                // must be a class setter
                throw new ClassSetterCantParseStringException();

            }catch (NumberFormatException e){
                throw e;
            }catch (IllegalArgumentException | InvocationTargetException | IllegalAccessException | TypeException |
                    MethodException e){

                // build stack trace
                StringBuilder stackTrace = new StringBuilder();
                for (StackTraceElement stackTraceElement : e.getStackTrace())
                    stackTrace.append("\t").append(stackTraceElement.toString()).append("\n");

                StaticLogger.log(StringAccessMethodHolder.class, Level.SEVERE,
                        String.format("Error: Could not invoke alternative setter%nError Message: %s%nCause %s%nTrace:%n %s",
                            e.getMessage(),e.getCause(),stackTrace));

                throw new TypeException(e);
            }
        }
        throw new NoSuchMethodException("No setter for "+ clazz.getName());
    }



    /**
     * finds any string setter,
     * if none is found in the class, then find a StringBuilder setter
     * if none is found, then find a primitive setter.
     */
    private void findStringSetter(){
        onlyClassSetter = false; // reset variable

        if(setters.isEmpty())
            return;

        Method stringBuilderSetter = null;
        Method primitiveSetter = null;
        Method trivialSetter = null;

        // for every method
        for (Method setter : setters.get())
        {
            // only consider it, if it has exactly 1 parameter
            Class<?>[] parameters = setter.getParameterTypes();
            if (parameters.length != 1)
                continue;

            // check for setters
            if (parameters[0] == String.class){
                stringSetter = setter;
                continue;
            }

            if (parameters[0] == StringBuilder.class){
                stringBuilderSetter = setter;
                continue;
            }

            if (TypeUtil.isPrimitiveOrWrapper(parameters[0])) {
                primitiveSetter = setter;
                continue;
            }

            if (parameters[0] == BigInteger.class){
                trivialSetter = setter;
                continue;
            }

            if (parameters[0] == BigDecimal.class){
                trivialSetter = setter;
                continue;
            }
            onlyClassSetter = true; // it can only be a class setter at this point
        }

        // set the alternativeSetter to the stringBuilderSetter,
        // if non exist, set it to the primitiveSetter
        // if non exist, set it to  trivialSetter
        this.alternativeSetter = trivialSetter;
        if (primitiveSetter != null)
            this.alternativeSetter = primitiveSetter;
        if (stringBuilderSetter != null)
            this.alternativeSetter = stringBuilderSetter;
    }


    /**
     * method to see if there is only a class setter.
     * @return boolean of if there is only a class setter
     */
    public boolean isOnlyClassSetter(){
        return onlyClassSetter;
    }

    /**
     * if we have callable setters, then we return tur
     * @return boolean of it we have invokable setters
     */
    public boolean isInvokable(){
        return stringSetter != null || alternativeSetter != null;
    }

}
