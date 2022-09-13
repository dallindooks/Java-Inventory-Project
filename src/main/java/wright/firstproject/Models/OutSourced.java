package wright.firstproject.Models;
/** Class for defining the Outsourced instance of a part. */
public class OutSourced extends Part{
    public OutSourced(int id, String name, double price, int stock, int min, int max, String company) {
        super(id, name, price, stock, min, max);
        this.company = company;
    }

    private String company;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }
}
