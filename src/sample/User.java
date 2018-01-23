package sample;

public class User {
    private String name;
    private String address;
    private String password;
    private String photoPath;
    private String phoneNumber;

    public User(String name, String address, String password,String phoneNumber, String photoPath) {
        this.name = name;
        this.address = address;
        this.password = password;
        this.photoPath = photoPath;
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return name+","+address+","+password+","+phoneNumber+","+photoPath;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public String getPhotoPath() {
        return photoPath;
    }
}
