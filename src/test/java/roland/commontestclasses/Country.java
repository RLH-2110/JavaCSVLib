package roland.commontestclasses;

public class Country {
    private String code;
    private String name;
    private double VAT;

    public Country(String code, String name, double VAT) {
        this.code = code;
        this.name = name;
        this.VAT = VAT;
    }

    public Country() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getVAT() {
        return VAT;
    }

    public void setVAT(double VAT) {
        this.VAT = VAT;
    }
}
