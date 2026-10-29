
public class Customer implements Cloneable {

    private String name;
    private String email;
    private String mobile;

    private final String customerId;

    private Address address;

    private static long customerCounter = 100;

    // Generate customer ID
    private static String generateCustomerId() {
        customerCounter++;
        return "CUST" + customerCounter;
    }

    // Constructor
    public Customer(String name, String email, String mobile) {
        this.customerId = generateCustomerId();
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    // Getter methods
    public String getCustomerId() {
        return customerId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getMobile() {
        return mobile;
    }

    // Set address
    public void setAddress(Address address) {
        this.address = address;
    }

    // Get address
    public Address getAddress() {
        return address;
    }

    // Clone method
    @Override
    public Customer clone() {
        try {
            return (Customer) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    // Nested Address class
    public static class Address {

        private String line;
        private String city;
        private String pincode;

        // Constructor
        public Address(String line, String city, String pincode) {
            this.line = line;
            this.city = city;
            this.pincode = pincode;
        }

        // Getters
        public String getLine() {
            return line;
        }

        public String getCity() {
            return city;
        }

        public String getPincode() {
            return pincode;
        }

        @Override
        public String toString() {
            return line + ", " + city + " - " + pincode;
        }
    }
}

