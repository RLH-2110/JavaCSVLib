package roland.csvlib;

import org.junit.jupiter.api.Disabled;
import roland.commontestclasses.Country;
import roland.csvlib.testclasses.SimpleData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import roland.csvlib.testclasses.SuperClass;
import roland.csvlib.testclasses.BasicData;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class CSVConverterUnitTest {

    CSVConverter csvConverter = new CSVConverter();



    @Test
    void testTest(){
        Assertions.assertEquals(1,1);
    }

    @Test
    void basicWritingTest() throws Exception {
        BasicData basicData = new BasicData(15,9,"Johan");

        ArrayList<BasicData> basicDataList = new ArrayList<>();
        basicDataList.add(basicData);


        System.out.println(csvConverter.saveAsCSV(BasicData.class,basicDataList));

        Assertions.assertEquals(basicCSV,
                csvConverter.saveAsCSV(BasicData.class,basicDataList)
        );

    }
    @Test
    void basicReadingTest(){

        List<BasicData> basicDataList = csvConverter.loadFromCSV(BasicData.class,basicCSV);

        BasicData basicData = basicDataList.get(0);
        Assertions.assertEquals(15,basicData.getIncome());
        Assertions.assertEquals(9,basicData.getExpenses());
        Assertions.assertEquals("Johan",basicData.getName());

    }

    @Test
    void basicWritingReadingTest(){

        BasicData basicData = new BasicData(15,9,"Johan");

        ArrayList<BasicData> basicDataList = new ArrayList<>();
        basicDataList.add(basicData);

        System.out.println(csvConverter.saveAsCSV(BasicData.class,basicDataList));

        List<BasicData> basicDataListRead = csvConverter.loadFromCSV(
                BasicData.class,
                csvConverter.saveAsCSV(BasicData.class,basicDataList)
                );

        BasicData basicDataRead = basicDataListRead.get(0);
        Assertions.assertEquals(15,basicDataRead.getIncome());
        Assertions.assertEquals(9,basicDataRead.getExpenses());
        Assertions.assertEquals("Johan",basicDataRead.getName());

    }

    @Test
    void filesAndArrayTest() throws IOException {
        ArrayList<BasicData> basicDataList = new ArrayList<>();
        basicDataList.add(new BasicData(15,9,"Johan"));
        basicDataList.add(new BasicData(9001,1006,"OVER NINE"));
        basicDataList.add(new BasicData(-68,-80,"Ölaf the Umlaut"));
        basicDataList.add(new BasicData(988,0xFFFFF,"Alex"));
        basicDataList.add(new BasicData(0,0,"..."));

        File file = new File(".test.csv");
        csvConverter.saveAsCSV(BasicData.class,basicDataList,file);
        List<BasicData> result = csvConverter.loadFromCSV(BasicData.class,file);

        Assertions.assertArrayEquals(basicDataList.toArray(),result.toArray());

    }

    @Test
    void superWriteTest(){
        SuperClass superClass = new SuperClass(69,new Country("DE","Many Gers",.19), BigInteger.TEN);

        ArrayList<SuperClass> superList = new ArrayList<>();
        superList.add(superClass);


        System.out.println(csvConverter.saveAsCSV(SuperClass.class,superList));

        Assertions.assertEquals(superCSV,
                csvConverter.saveAsCSV(SuperClass.class,superList)
        );
    }


    @Test
    @Disabled
    void superReadTest(){

    }

    @Test
    @Disabled
    void superWritingReadingTest(){

    }

    @Test
    @Disabled
    void simpleWriteTest(){

    }

    @Test
    @Disabled
    void simpleReadTest(){

    }

    @Test
    @Disabled
    void simpleWritingReadingTest(){

    }

    @Test
    @Disabled
    void complexWriteTest(){

    }
    @Test
    @Disabled
    void complexReadTest(){

    }

    @Test
    @Disabled
    void complexWritingReadingTest(){

    }

    // TODO ADD MORE TESTS



    private final String basicCSV = "income;expenses;name;" + System.lineSeparator() +
               "15;9;Johan;" + System.lineSeparator();

    private final String superCSV = "superId;country.code;country.name;country.VAT;" + System.lineSeparator() +
               "69;DE;Many Gers;0.19;" + System.lineSeparator();

    // not done
    private final String simpleCSV = "superId;country.code;country.name;country.VAT;" + System.lineSeparator() +
               "69;DE;Many Gers;0.19;" + System.lineSeparator();


   /* @CSVValue
    private int income;
    @CSVValue
    private int expenses;
    @CSVValue
    private String name;*/
}
