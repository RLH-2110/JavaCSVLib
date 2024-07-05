package roland.jmethod.amh;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * an "extension" of the {@link AccessMethodHolder} class, but rather than extending,
 * it stores the {@link AccessMethodHolder} within itself.
 *
 * This class is intended to hold the path to a field
 * like when you have a person that has an address class
 * we want a format like address.street in that case
 * and if the address has a country class, it would be
 * address.country.name
 */
public class FieldPathHolder {

    protected AccessMethodHolder accessMethodHolder;
    protected String fieldPath;

    /**
     * this is an unused empty constructor, it might be useful if you want to extend the class.
     */
    public FieldPathHolder() {}

    /**
     * @param accessMethodHolder an {@link AccessMethodHolder} that this class quasi extends without extending it
     * @param fieldPath
     * when you have a person that has an address class
     * we want a format like address.street in that case
     * and if the address has a country class, it would be
     * address.country.name
     * @see AccessMethodHolder
     */
    public FieldPathHolder(@NotNull AccessMethodHolder accessMethodHolder, @NotNull String fieldPath) {
        this.accessMethodHolder = accessMethodHolder;
        this.fieldPath = fieldPath;
    }

    /**
     * provides either a {@link StringAccessMethodHolder} or a {@link ClassAccessMethodHolder}, but can be treated as an {@link AccessMethodHolder}
     * @return the specialized class based on the setter type. can be StringAccessMethodHolder or ClassAccessMethodHolder.
     */
    public AccessMethodHolder getAccessMethodHolder() {
        if (accessMethodHolder.isTrivialSetter())
            return new StringAccessMethodHolder(accessMethodHolder);
        else
            return new ClassAccessMethodHolder(accessMethodHolder);
    }


    public @NotNull String getFieldPath() {
        return fieldPath;
    }

    /**
     * invokes the getter of an attached object, wraps {@link AccessMethodHolder#invokeGetter()}
     * @return whatever the getter returns
     * @throws NoSuchMethodException if the getter does not exist
     */
    public @Nullable Object invokeGetter() throws NoSuchMethodException {
        return accessMethodHolder.invokeGetter();
    }

    /**
     * wraps {@link AccessMethodHolder#isTrivialGetter()}
     * @return true if the return type of the getter is trivial
     */
    public boolean isTrivialGetter(){
        return accessMethodHolder.isTrivialGetter();
    }

    /**
     * gets the field name of the field that's attached to the attached {@link AccessMethodHolder}
     * @return the name field name
     */
    public @NotNull String getFieldName(){
        return accessMethodHolder.field.getName();
    }

    /**
     * wraps {@link AccessMethodHolder#attachObject(Object)}.
     * attached an object to the attached {@link AccessMethodHolder}
     * @param object the object to attach.
     * @throws IllegalArgumentException, when the Object is not an instance of the class the field and its methods are from.
     */
    public void attachObject(@Nullable Object object){
        accessMethodHolder.attachObject(object);
    }


}
