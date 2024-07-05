package roland.commontestclasses;

/**
 * Test class that has primitive, wrapper and class types
 * It also has fields with both getters and setters, only setters, only getters
 * and neither getters nor setters
 *
 * There is also a setter with 2 attributes
 */
public class TestClass{
    private String name = "";
    private int name_lenght = 0;
    private Byte age = 0;
    private Boolean readOnly = false;
    private Address address = null;
    private boolean writeOnly = false;
    private String internalPassword = "root";
    private String readOnlyPasscode = "";

    private int fav_number = 0;
    private Integer least_fav_number = -1;

    private char favLetter = ' ';
    private StringBuilder titles = new StringBuilder();

    private boolean isTerran = true;
    private Boolean diarrhea = false;

    private String topSecret = "";
    private Country agentFor = null;
    private Address secretAddress = null; //getter only
    private String hash; // dummy for getHash

    public String getHash(){
        return this.getHash();
    }

    public void setWriteOnly(boolean writeOnly) {
        this.writeOnly = writeOnly;
    }

    public String getName() {
            return name;
    }

    public void setName(String name) {
            this.name = name;
            setName_lenght(this.name);

    }

    private void setName_lenght(String name) {
            this.name_lenght = name.length();
    }

    public byte getAge() {
            return age;
    }


    public void setAge(String age) {
            this.age = Byte.parseByte(age);
    }

    public void setAge(Byte age) {
            this.age = age;
    }



    public boolean isReadOnly() {
            return readOnly;
    }

    // double __ so its not picked up by scans
    public void set__ReadOnly(boolean readOnly,String passcode) {
        if (!writeOnly) {
            this.readOnly = readOnly;
            readOnlyPasscode = passcode;
        }else if (readOnlyPasscode.equals(passcode)) {
            this.readOnly = readOnly;
        }
    }

    public Address getAddress() {
            return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }


    public void setFav_number(int fav_number) {
            this.fav_number = fav_number;
    }

    public int getFav_number() {
            return fav_number;
    }


     public void setLeast_fav_number(int fav_number) {
            this.fav_number = fav_number;
    }

    public int getLeast_fav_number() {
            return fav_number;
    }


    public StringBuilder getTitles() {
            return titles;
    }

    public void setTitles(StringBuilder titles) {
            this.titles = titles;
    }

    public char getFavLetter() {
            return favLetter;
    }

    public void setFavLetter(char favLetter) {
            this.favLetter = favLetter;
    }

    public boolean isTerran() {
            return isTerran;
    }

    public void setTerran(boolean terran) {
            isTerran = terran;
    }

    public Boolean getDiarrhea() {
            return diarrhea;
    }

    public void setDiarrhea(Boolean diarrea) {
            this.diarrhea = diarrea;
    }

    private void setTopSecret(String s){
            this.topSecret = s;
    }

    private String getTopSecret() {
            return topSecret;
    }

    private Country getAgentFor() {
        return agentFor;
    }

    private void setAgentFor(Country agentFor) {
            this.agentFor = agentFor;
    }

    private Address getSecretAddress(){
        return secretAddress;
    }
}