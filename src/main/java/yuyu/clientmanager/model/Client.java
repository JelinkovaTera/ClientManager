package yuyu.clientmanager.model;

public class Client {
    private int databaseId;
    private String birthNumber;
    private int age;
    private String name;
    private String surname;

    public Client(String birthNumber, String name, String surname) {
        this.birthNumber = birthNumber;
        this.name = name;
        this.surname = surname;
    }

    public int getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(int databaseId) {
        this.databaseId = databaseId;
    }

    public String getBirthNumber() {
        return birthNumber;
    }

    public void setBirthNumber(String birthNumber) {
        this.birthNumber = birthNumber;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    @Override
    public String toString() {
        return "Client{" +
                "databaseId=" + databaseId +
                ", birthNumber='" + birthNumber + '\'' +
                ", age='" + age + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
