package roland.csvlib;

import roland.csvlib.exception.DelimeterNotSupportedException;
import roland.jmethod.FieldAnnotationFinder;
import roland.jmethod.MethodFinder;
import roland.jmethod.TypeUtil;
import roland.jmethod.amh.AccessMethodHolder;
import roland.jmethod.amh.ClassAccessMethodHolder;
import roland.jmethod.amh.FieldPathHolder;
import roland.jmethod.amh.StringAccessMethodHolder;
import roland.jmethod.exceptions.TypeException;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static java.nio.file.Files.readAllBytes;

public class CSVConverter {

    private char delimiter = ';';
    private final Logger logger;
    private String lineSeparator = System.lineSeparator();





    public CSVConverter(){
        logger = Logger.getLogger(CSVConverter.class.getName());
    }
    public CSVConverter(char delimiter){
        this();
        setDelimiter(delimiter);
    }




    public <T> void saveAsCSV(Class<T> clazz, List<T> list, File file) throws IOException {
         String CSV = saveAsCSV(clazz,list);

        try( FileWriter fw = new FileWriter(file)) // auto closes resource
        {
            fw.write(CSV);
        }
    }

    public <T> String saveAsCSV(Class<T> clazz, List<T> list) {

        String CSV = "";

        List<AccessMethodHolder> fields = FieldAnnotationFinder.getFieldAnnotationFieldMethods(clazz,CSVValue.class);



        // generate header
        for (int i = 0; i < fields.size(); i++) {

                if (MethodFinder.rwExceptionTest(fields.get(i))){
                    // exception test passed, save it in the header

                    if (fields.get(i).isTrivialGetter())
                        CSV += fields.get(i).getField().getName() + delimiter;
                    else {
                        // ITS A CLASS! we need to add the sub elements!

                        List<FieldPathHolder> fieldPathHolders = TypeUtil.convertIntoPathHolder(fields.get(i), fields.get(i).getField().getName(), 10,true);

                        for (FieldPathHolder fieldPathHolder : fieldPathHolders) {
                            CSV += fieldPathHolder.getFieldPath() + delimiter;
                        }

                    }

                }else {  // exception test failed, remove Holders with errors

                    logger.log(Level.WARNING, String.format(
                            "Field %s from %s can not be added to the CSV, because it failed the exception tests!%n"
                                    + "Try seeing if your getters and setters are pubic, and your class is not abstract!",
                            fields.get(i).getField().getName(),
                            clazz.getName()
                    ));

                    fields.remove(i);
                    --i;

                }
        }





        CSV += lineSeparator;

        for (T element : list){ // for all entities

            // for all fields
            for (AccessMethodHolder field : fields){
                field.attachObject(element);

                if (field.isTrivialGetter())
                    // call the getter and add the result to CSV
                    try{ CSV += field.invokeGetter().toString() + delimiter; }catch (NoSuchMethodException e){} // error can never happen, because it would have been triggered earlier
                else{
                    // the field returns a class!

                    // gets all "sub elements" for the class field
                    List<FieldPathHolder> fieldPathHolders = TypeUtil.convertIntoPathHolder(field,field.getField().getName(),10,false);

                    for (FieldPathHolder fieldPathHolder : fieldPathHolders){

                        try{ CSV += fieldPathHolder.invokeGetter().toString() + delimiter; }

                        catch (NoSuchMethodException e) // error can never happen
                        {throw new TypeException(e);}


                    }
                }
            }

            CSV += lineSeparator;
        }


        return CSV;
    }










    public <T> List<T> loadFromCSV(Class<T> clazz, File file) throws IOException {

        String csv;

        try
        {
            csv = new String(readAllBytes(Paths.get(file.getAbsolutePath()))); // read the entire file into the string

        } catch (FileNotFoundException e) {

           logger.log(Level.WARNING, "File not found: " + file.getAbsolutePath());
           return new ArrayList<>();
        }

        return loadFromCSV(clazz,csv);
    }

    public <T> List<T> loadFromCSV(Class<T> clazz, String CSV){

        String[] lines = Util.SplitByNewline(CSV);
        List<T> objects = new ArrayList<>();
        List<Optional<AccessMethodHolder>> methods = getMethodsFromHeader(lines[0],clazz);

        // for every line
        for (int lineIndex = 1; lineIndex < lines.length; lineIndex++) { // index 0 is the header, so we start at 1
            try {


                String[] columns = lines[lineIndex].split(Pattern.quote("" + delimiter)); // split by delimiter
                objects.add((T)TypeUtil.getNewObject(clazz));

                // for every field in a line
                for (int fieldIndex = 0; fieldIndex < columns.length; fieldIndex++) {

                    if (methods.get(fieldIndex).isEmpty()) // skip if field is bad
                        continue;


                    // TODO make work with class
                    StringAccessMethodHolder samh = new StringAccessMethodHolder(methods.get(fieldIndex).get());
                    samh.attachObject(objects.get(lineIndex-1)); // -1 because the data lines start at 1 and objects start at 0

                    samh.invokeSetter(columns[fieldIndex]);
                }

            }catch (NoSuchMethodException e){
                logger.log(Level.WARNING, "Can not read line: " + lines[lineIndex]);
            }
        }
        return objects;
    }






    private List<Optional<AccessMethodHolder>> getMethodsFromHeader(String header,Class<?> clazz){
       List<Optional<AccessMethodHolder>> methods = new ArrayList<>();

        String[] columns = header.split(Pattern.quote(""+delimiter)); // split by delimiter

        // for every entry in the header
        for (String column : columns){
            try {

                Field field = clazz.getDeclaredField(column); // get the field in the class
                AccessMethodHolder AMH = MethodFinder.getSetterGetterMethods(clazz,field); // get the methods

                // check if getter and setter are usable
                if (!MethodFinder.rwExceptionTest(AMH))
                    throw new NoSuchMethodException();

                // they are usable

                methods.add(Optional.of(AMH));
            }catch (NoSuchFieldException | NoSuchMethodException e){
                methods.add(Optional.empty());

                logger.log(Level.WARNING,String.format(
                        "field %s from %s could not be loaded! exception: %s",
                        column,
                        clazz.getName(),
                        e.getMessage()
                        )
                );
            }
        }

       return methods;
    }




    public void setDelimiter(char delimiter) throws DelimeterNotSupportedException {
        if (delimiter == '.' || delimiter == ' ')
            throw new DelimeterNotSupportedException();

        this.delimiter = delimiter;
    }




    public String getLineSeparator() {
        return lineSeparator;
    }

    public void setLineSeparator(String lineSeparator) {
        this.lineSeparator = lineSeparator;
    }




}
