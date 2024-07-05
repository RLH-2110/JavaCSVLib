package roland.jmethod.amh;

import org.jetbrains.annotations.Nullable;
import roland.jmethod.exceptions.TypeException;

/**
 * an interface for extended {@link AccessMethodHolder AccessMethodHolders} that standardizes invoking the setter
 */
public interface ExtendedAMHInterface {

    // either an Object for an class setter, or a String for an string or primitive

    /**
     * invokes an "primary" setter saved in {@link AccessMethodHolder}. Note: Only class setters can handle (non-trivial) classes, and only String setters can handle primitive types (and string setters should be used for trivial classes)
     * @param argument a class object or a string
     * @throws NoSuchMethodException that setter does not exist
     * @throws NumberFormatException (when using a string setter) when a string conversion failed
     * @throws TypeException wrapper for errors, like when there was an exception inside the invoked setter.
     * the TypeException is there to "hide" these exception, since its assumed they are programmer errors rather than user errors, and so we might not want to handle them for the user.
     */
    public void invokeSetter(@Nullable Object argument) throws NoSuchMethodException, NumberFormatException, TypeException;

    /**
     * @return true if a (fitting) setter exist, false if none exists
     */
    public boolean isInvokable();
}
