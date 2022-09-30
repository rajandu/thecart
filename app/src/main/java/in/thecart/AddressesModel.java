package in.thecart;

public class AddressesModel {

    private String fullname;
    private String pincode;
    private String address;
    private Boolean selected;

    public AddressesModel(String fullname, String address, String pincode,Boolean selected) {
        this.fullname = fullname;
        this.pincode = pincode;
        this.address = address;
        this.selected = selected;
    }

    public Boolean getSelected() {
        return selected;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
