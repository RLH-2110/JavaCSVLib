package roland.csvlib.testclasses;

import roland.commontestclasses.Country;
import roland.csvlib.CSVValue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ComplexData{
    @CSVValue
    private SimpleData[] simpleData;
    @CSVValue
    private Country countryData;
    @CSVValue
    private ComplexData selfReference;
    @CSVValue
    private ArrayList<Country> countries;


    private Boolean pipi;

    public ComplexData(){}

    public ComplexData(Boolean pipi, ComplexData selfReference, Country countryData, SimpleData[] simpleData, ArrayList<Country> countries) {
        this.pipi = pipi;
        this.selfReference = selfReference;
        this.countryData = countryData;
        this.simpleData = simpleData;
        this.countries = countries;
    }

    public ArrayList<Country> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<Country> countries) {
        this.countries = countries;
    }

    public SimpleData[] getSimpleData() {
        return simpleData;
    }

    public void setSimpleData(SimpleData[] simpleData) {
        this.simpleData = simpleData;
    }

    public Country getCountryData() {
        return countryData;
    }

    public void setCountryData(Country countryData) {
        this.countryData = countryData;
    }

    public ComplexData getSelfReference() {
        return selfReference;
    }

    public void setSelfReference(ComplexData selfReference) {
        this.selfReference = selfReference;
    }

    public Boolean getPipi() {
        return pipi;
    }

    public void setPipi(Boolean pipi) {
        this.pipi = pipi;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ComplexData that = (ComplexData) object;
        return Objects.deepEquals(simpleData, that.simpleData) && Objects.equals(countryData, that.countryData) && Objects.equals(selfReference, that.selfReference) && Objects.equals(countries, that.countries) && Objects.equals(pipi, that.pipi);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(simpleData), countryData, selfReference, countries, pipi);
    }
}
