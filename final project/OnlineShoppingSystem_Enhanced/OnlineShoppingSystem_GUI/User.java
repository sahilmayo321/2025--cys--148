/**
 * Represents the customer using the shopping application.
 * Now supports password for Login/Signup functionality.
 */
public class User {
    private final String name;
    private final String email;
    private String password;
    private String phone;
    private String address;

    public User(String name, String email) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        this.name = name.trim();
        this.email = email.trim();
        this.password = "";
        this.phone = "";
        this.address = "";
    }

    public User(String name, String email, String password, String phone, String address) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Customer name cannot be empty.");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty.");
        }
        this.name = name.trim();
        this.email = email.trim();
        this.password = (password == null) ? "" : password;
        this.phone = (phone == null) ? "" : phone.trim();
        this.address = (address == null) ? "" : address.trim();
    }

    public String getName()    { return name; }
    public String getEmail()   { return email; }
    public String getPassword(){ return password; }
    public String getPhone()   { return phone; }
    public String getAddress() { return address; }

    public void setPassword(String password) { this.password = password; }
    public void setPhone(String phone)       { this.phone = (phone == null) ? "" : phone.trim(); }
    public void setAddress(String address)   { this.address = (address == null) ? "" : address.trim(); }
}
