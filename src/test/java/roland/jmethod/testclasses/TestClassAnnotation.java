package roland.jmethod.testclasses;
import roland.csvlib.testclasses.SuperClass;
import roland.commontestclasses.Address;
import java.util.ArrayList;

public class TestClassAnnotation extends SuperClass {

    // string value
    @testAnnotation
    public String name;

    // basic type value
    @testAnnotation
    int age;

    // primitive Wrapper
    @testAnnotation
    Integer birthYear;

    // object
    @testAnnotation
    Address address;

    // only setter
    @testAnnotation
    String hash;

    //only getter
    @testAnnotation
    boolean yourMom;

    // no getter or setter
    @testAnnotation
    private ArrayList<String> neighbors;

    // no annotation
    int notFound = 404;

    public TestClassAnnotation() {
    }

    public TestClassAnnotation(String name, int age, Address address, String hash, boolean yourMom, ArrayList<String> neighbors) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.hash = hash;
        this.yourMom = yourMom;
        this.neighbors = neighbors;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public boolean isYourMom() {
        return yourMom;
    }

     public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }
}

