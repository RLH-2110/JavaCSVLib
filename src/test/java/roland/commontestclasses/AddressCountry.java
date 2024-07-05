package roland.commontestclasses;

public class AddressCountry {
    private Country country;
    private Address address;

    public AddressCountry(Country country, Address address) {
        this.country = country;
        this.address = address;
    }

    public AddressCountry() {}

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
