package yuyu.clientmanager.model;

public class Client {
    private int databaseId;
    private final String birthNumber;
    private int age;
    private final String name;
    private final String surname;

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
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
