package roland.jmethod;

import org.jetbrains.annotations.NotNull;
import roland.jmethod.amh.AccessMethodHolder;
import roland.jmethod.exceptions.TypeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

/**
 * "static" final class with a method to find annotations on class fields
 */
public final class FieldAnnotationFinder { // "static" class

    private FieldAnnotationFinder() {}

    /**
     * Finds all the fields with the given annotation in a class and gives the getter and setters
     * @param clazz the class we want to find the annotations in.
     * @param annotation the class of the annotation
     * @return a list of {@link AccessMethodHolder AccessMethodHolders} which contains the getter, the setters as well as the field name
     */
    public static @NotNull List<AccessMethodHolder> getFieldAnnotationFieldMethods(@NotNull Class<?> clazz, @NotNull Class<? extends Annotation> annotation) {

        List<AccessMethodHolder> fieldMethods = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            // see if the annotation is present.
            for (Annotation presentAnnotation : field.getDeclaredAnnotations()) {
                if (presentAnnotation.annotationType().equals(annotation)) {
                    // get the getters and setters of the annotated field
                    fieldMethods.add(MethodFinder.getSetterGetterMethods(clazz, field));
                    break; // if we found it, we dont need to search the others
                }
            }
        }

        return fieldMethods;
    }


}

