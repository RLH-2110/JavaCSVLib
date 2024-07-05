package roland.jmethod.amh;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import roland.jmethod.TypeUtil;
import roland.jmethod.exceptions.MethodHolderNotAttachedException;
import roland.jmethod.exceptions.TypeException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * A class for storeing field data and its getter and setters.
 * it also shows if the getters are trivial and lets one invoke the methods in attached objects
 * is the super class of {@link StringAccessMethodHolder} and {@link ClassAccessMethodHolder}
 * @see #attachObject
 */
public class AccessMethodHolder
{
    protected Class<?> clazz;
    protected Field field;
    protected Optional<Method> getter;
    protected Optional<List<Method>> setters;
    protected Object classObj = null;
    protected boolean trivialSetter;

    public void setAll(@NotNull Class<?> clazz,@NotNull Field field,@NotNull Optional<Method> getter,@NotNull Optional<List<Method>> setters){
        this.clazz = clazz;
        this.field = field;
        this.getter = getter;
        this.setters = setters;

        // we dont have a setter, so say true, because false is the edge case
        if (setters.isEmpty())
            trivialSetter = true; //

        // determine if a setter can be set via string
        else{
            trivialSetter = false;

            for (Method m : setters.get()){ // for every setter

                if (TypeUtil.isTrivialType(m.getParameterTypes()[0])){ // parameter is trivial?
                    trivialSetter = true;
                    break;
                }


            }
        }


    }

    public AccessMethodHolder() {
        setAll(null, null, Optional.empty(), Optional.empty());
    }

    public AccessMethodHolder(@NotNull AccessMethodHolder other) {
        setAll(other.clazz,other.field,other.getter,other.setters);
        this.classObj = other.classObj;
    }

    public AccessMethodHolder(@NotNull Class<?> clazz, @NotNull Field field, @NotNull Optional<Method> getter, @NotNull Optional<List<Method>> setters) {
        setAll(clazz, field, getter, setters);
    }

    /**
     * attaches an object, which allows us to invoke the getter,
     * and with the extended classes, the setter!
     * @param classObj the object we want to attach.
     * @throws IllegalArgumentException, when the Object is not an instance of the class the field and its methods are from.
     */
    public void attachObject(@Nullable Object classObj){

        // allow us to set it to null
        if (classObj == null){
            this.classObj = null;
            return;
        }

        // allow us to set it ot an object of the right class
        if (!classObj.getClass().equals(clazz))
            throw new IllegalArgumentException("Object is of the wrong type! got: "
                    + classObj.getClass().getName()+" expected: "+ clazz.getName());
        this.classObj = classObj;
    }

    /**
     * @return true if there is a getter
     */
    public boolean hasGetter(){
        return getter.isPresent();
    }

    /**
     * invokes the getter of an attached object (see {@link #attachObject(Object)}).
     * @return whatever the getter returns
     * @throws NoSuchMethodException if the getter does not exist
     * @throws TypeException if anything happens during the invocation, like an InvocationTargetException or IllegalAccessException
     */
    public @Nullable Object invokeGetter() throws NoSuchMethodException, TypeException {
        if (classObj == null)
            throw new MethodHolderNotAttachedException();
        if (getter.isEmpty())
            throw new NoSuchMethodException();
        if (getter.get().getParameterCount() > 0)
            throw new NoSuchMethodException();

        try{
            return getter.get().invoke(classObj);
        }catch (InvocationTargetException | IllegalAccessException e){
            throw new TypeException(e);
        }
    }

    /**
     * gets the attached object, or null, if there is none
     * @return null or attached object
     */
    public @Nullable Object getClassObj() {
        return classObj;
    }

    /**
     * gets the class of the field
     * @return class of the field
     */
    public @NotNull Class<?> getClazz() {
        return clazz;
    }


    /**
     * @return true for any type where the value can easily be extracted or set
     * Meaning: any primitive, Primitive wrapper, String, StringBuilder, BigInteger or BigDecimal
     */
    public boolean isTrivialGetter(){
        if (getter.isEmpty())
            return true;
        return TypeUtil.isTrivialType(getter.get().getReturnType());
    }

    /**
     * @return true for any type where the value can easily be extracted or set
     * Meaning: any primitive, Primitive wrapper, String, StringBuilder, BigInteger or BigDecimal
     */
    public boolean isTrivialSetter(){
        return trivialSetter;
    }

    public @NotNull Field getField() {
        return field;
    }

    public @NotNull Optional<Method> getGetter() {
        return getter;
    }

    public @NotNull Optional<List<Method>> getSetters() {
        return setters;
    }


    @Override
    public boolean equals(@Nullable Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        AccessMethodHolder that = (AccessMethodHolder) object;
        return Objects.equals(clazz, that.clazz) && Objects.equals(field, that.field) && Objects.equals(classObj, that.classObj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clazz, field, classObj);
    }
}