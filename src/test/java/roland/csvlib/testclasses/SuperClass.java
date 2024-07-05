package roland.csvlib.testclasses;

import roland.commontestclasses.Country;
import roland.csvlib.CSVValue;

import java.math.BigInteger;

public class SuperClass {
    @CSVValue
    int superId;

    @CSVValue
    Country country;

    BigInteger random;

    public SuperClass() {
    }

    public SuperClass(int superId, Country country, BigInteger random) {
        this.superId = superId;
        this.country = country;
        this.random = random;
    }

    public int getSuperId() {
        return superId;
    }

    public void setSuperId(int superId) {
        this.superId = superId;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public BigInteger getRandom() {
        return random;
    }

    public void setRandom(BigInteger random) {
        this.random = random;
    }
}
