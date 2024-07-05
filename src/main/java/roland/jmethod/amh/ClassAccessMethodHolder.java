package roland.jmethod.amh;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;
import roland.jmethod.exceptions.MethodHolderNotAttachedException;
import roland.jmethod.exceptions.TypeException;
import roland.jmethod.TypeUtil;
import roland.common.StaticLogger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;

public class ClassAccessMethodHolder extends AccessMethodHolder implements ExtendedAMHInterface{

    private Method classSetter;
    boolean hasOtherSetters;

    public ClassAccessMethodHolder(){
        super();
        classSetter = null;
    }

    public ClassAccessMethodHolder(AccessMethodHolder Super){
        super(Super);
        findClassSetter();
    }

    public void invokeSetter(@UnknownNullability Object argument) throws NoSuchMethodException, NumberFormatException, TypeException {
        if (classObj == null)
            throw new MethodHolderNotAttachedException();
        if (setters.isEmpty())
            throw new NoSuchMethodException(clazz.getName()+"has no setters!");

        if (classSetter == null)
            throw new NoSuchMethodException(clazz.getName()+"has no class setters!");

        tryInvokeClassSetter(argument);
    }

    /**
     * invokes an existing class setter and handles the errors
     * @param argument the argument you want to pass in the setter
     * @return returns true when the method was invoked
     */
    private boolean tryInvokeClassSetter(@UnknownNullability Object argument) throws TypeException{
        try {
                classSetter.invoke(classObj, argument);
                return true;
            }catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e){

                // build stack trace
                StringBuilder stackTrace = new StringBuilder();
                for (StackTraceElement stackTraceElement : e.getStackTrace())
                    stackTrace.append("\t").append(stackTraceElement.toString()).append("\n");

                StaticLogger.log(StringAccessMethodHolder.class, Level.SEVERE,
                        String.format("Error: Could not invoke real String setter%nError Message: %s%nCause %s%nTrace:%n %s",
                            e.getMessage(),e.getCause(),stackTrace));

                throw new TypeException(e);
            }
    }

    /**
     * Finds the first setter that's not trivial (not primitive, string, bigInteger, ...)
     * sets classSetter to the found method
     */
    private void findClassSetter(){
        if (setters.isEmpty()) {
            classSetter = null;
            return;
        }

        for (Method method : setters.get()){
            if (!TypeUtil.isTrivialType(method.getParameterTypes()[0])){
                classSetter = method;
                return;
            }
        }
        classSetter = null;
    }

    @Override
    public void setAll(@NotNull Class<?> clazz, @NotNull Field field, @NotNull Optional<Method> getter,@NotNull Optional<List<Method>> setters){
        super.setAll(clazz,field,getter,setters);
        findClassSetter();
    }

    /**
     * method to see if there are non-class setters
     * @return boolean of if there are non-class setters
     */
    public boolean hasOtherSetters(){
        return hasOtherSetters;
    }

    /**
     * if we have callable setters, then we return tur
     * @return boolean of it we have invokable setters
     */
    public boolean isInvokable(){
        return classSetter != null;
    }

}
