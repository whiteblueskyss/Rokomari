// 1. CLASSES & OBJECTS - Person Class with constructors and fields
class Person {
    private String name;
    private int age;
    private String email;
    
    // Default Constructor
    public Person() {
        this.name = "Unknown";
        this.age = 0;
        this.email = "no-email@example.com";
        System.out.println("Person created with default constructor");
    }
    
    // Parameterized Constructor
    public Person(String name, int age, String email) {
        this.name = name;
        this.age = age;
        this.email = email;
        System.out.println("Person created: " + name);
    }
    
    
    // Display method
    public void displayInfo() {
        System.out.println("Name: " + name + ", Age: " + age + ", Email: " + email);
    }
    
    // Getters and Setters for Person class
    public String getName() { return name; }
    public void setName(String name) { 
        if (name != null) {
            this.name = name; 
        }
    }
    
    public int getAge() { return age; }
    public void setAge(int age) { 
        if (age>0) {
            this.age = age; 
        }
    }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { 
        if (email.contains("@")) {
            this.email = email; 
        }
    }
}

// 2. METHODS - Calculator with different access modifiers and method overloading

class Calculator {

    public int add(int a, int b) {
        System.out.println("Adding two integers: " + a + " + " + b);
        return a + b;
    }
    
    // Method overloading - same name, different parameters
    public double add(double a, double b) {
        System.out.println("Adding two doubles: " + a + " + " + b);
        return a + b;
    }
    
}

// Advanced Calculator extending Calculator
class AdvancedCalculator extends Calculator {
    public double power(double base, double exponent) {
        return Math.pow(base, exponent);
    }
    
    public double squareRoot(double number) {
        return Math.sqrt(number);
    }
}




// 3. MUTABLE CLASS - BankAccount (state can be changed after creation)
class BankAccount {
    private String accountNumber;
    private String accountHolderName;
    private double balance;
    
    public BankAccount(String accountNumber, String accountHolderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
    }
    
    // Mutable operations - these change the object's state
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;
        }
    }
    
    public boolean withdraw(double amount) {
        if (amount > 0 && amount <= balance) {
            balance -= amount;
            return true;
        }
        System.out.println("Insufficient funds or invalid amount");
        return false;
    }
    
    public void updateAccountHolderName(String newName) {
        if (newName.trim().isEmpty()) {
            this.accountHolderName = newName;
        }
    }
    
    // Getters
    public String getAccountNumber() { return accountNumber; }
    public String getAccountHolderName() { return accountHolderName; }
    public double getBalance() { return balance; }
    
    public void displayAccountInfo() {
        System.out.println("Account: " + accountNumber);
        System.out.println("Holder: " + accountHolderName);
        System.out.println("Balance: $" + balance);
    }
}


// 4. IMMUTABLE CLASS - Point (state cannot be changed after creation)
final class Point {
    private final int x;
    private final int y;
    
    // Constructor - only way to set values
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    // Only getters, no setters (immutable)
    public int getX() { return x; }
    public int getY() { return y; }
    
    // Methods that return new Point objects instead of modifying this one
    public Point translate(int dx, int dy) {
        return new Point(x + dx, y + dy);
    }
    
    public double distanceFromOrigin() {
        return Math.sqrt(x * x + y * y);
    }
}


public class BasicJava {
    public static void main(String[] args) {
        System.out.println("COMPREHENSIVE JAVA OOP CONCEPTS DEMONSTRATION");
                
        // Creating objects using different constructors
        Person person1 = new Person();
        Person person2 = new Person("Alice Johnson", 25, "alice@example.com");
        
        person1.displayInfo();
        person2.displayInfo();
        
        // 2. METHODS DEMONSTRATION

        
        Calculator calc = new Calculator();
        AdvancedCalculator advCalc = new AdvancedCalculator();
        
        // // Method overloading
        System.out.println("Integer addition: " + calc.add(5, 3));
        System.out.println("Double addition: " + calc.add(5.5, 3.2));
        
        
        // Inheritance
        System.out.println("Power calculation: " + advCalc.power(2, 3));
        System.out.println("Square root: " + advCalc.squareRoot(16));
        
        // 3. MUTABLE OBJECTS DEMONSTRATION

        
        BankAccount account = new BankAccount("ACC123", "John Doe", 1000.0);
        account.displayAccountInfo();
        
        System.out.println("\nPerforming transactions...");
        account.deposit(500.0);
        account.withdraw(200.0);
        account.updateAccountHolderName("John Smith");
        
        account.displayAccountInfo();

        
        // 4. IMMUTABLE OBJECTS DEMONSTRATION
        
        // Point class (immutable)
        Point originalPoint = new Point(3, 4);
        System.out.println("Original point: " + originalPoint);
        System.out.println("Distance from origin: " + originalPoint.distanceFromOrigin());
        
        // These operations return NEW objects, don't modify the original
        Point translatedPoint = originalPoint.translate(2, 1);
        
        System.out.println("Original point after operations: " + originalPoint); // Unchanged!
        System.out.println("Translated point: " + translatedPoint);
        

    }
}