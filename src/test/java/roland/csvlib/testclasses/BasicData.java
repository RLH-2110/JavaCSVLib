package roland.csvlib.testclasses;

import roland.csvlib.CSVValue;

public class BasicData {

    @CSVValue
    private int income;
    @CSVValue
    private int expenses;
    @CSVValue
    private String name;

    public BasicData(){}

    public BasicData(int income, int expenses, String name) {
        this.income = income;
        this.expenses = expenses;
        this.name = name;
        total = income - expenses;
    }

    private int total;

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public int getExpenses() {
        return expenses;
    }

    public void setExpenses(int expenses) {
        this.expenses = expenses;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object other){
        if(!(other instanceof SimpleData))
            return false;

        SimpleData otherData = (SimpleData) other;

        if (otherData.getIncome() != this.getIncome())
            return false;

        if (otherData.getExpenses() != this.getExpenses())
            return false;

        if (!otherData.getName().equals(this.getName()))
            return false;

        return true;
    }

}
