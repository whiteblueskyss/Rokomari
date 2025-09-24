import java.io.*;
import java.util.Scanner;
import java.util.InputMismatchException;

// CUSTOM EXCEPTIONS - User-defined exceptions

// Custom Checked Exception
class InsufficientFundsException extends Exception {
    private double amount;
    private double balance;
    
    public InsufficientFundsException(double amount, double balance) {
        super("Insufficient funds: Trying to withdraw $" + amount + " but balance is only $" + balance);
        this.amount = amount;
        this.balance = balance;
    }
    
    public double getAmount() { return amount; }
    public double getBalance() { return balance; }
}

// Custom Unchecked Exception
class InvalidAgeException extends RuntimeException {
    public InvalidAgeException(int age) {
        super("Invalid age: " + age + ". Age must be between 0 and 150");
    }
}

// Custom Exception for Product Management
class ProductNotAvailableException extends Exception {
    private String productName;
    private int requestedQuantity;
    private int availableQuantity;
    
    public ProductNotAvailableException(String productName, int requested, int available) {
        super("Product '" + productName + "' not available. Requested: " + requested + ", Available: " + available);
        this.productName = productName;
        this.requestedQuantity = requested;
        this.availableQuantity = available;
    }
    
    public String getProductName() { return productName; }
    public int getRequestedQuantity() { return requestedQuantity; }
    public int getAvailableQuantity() { return availableQuantity; }
}



// CLASSES FOR DEMONSTRATION

class BankAccount {
    private String accountNumber;
    private double balance;
    
    public BankAccount(String accountNumber, double initialBalance) {
        this.accountNumber = accountNumber;
        this.balance = initialBalance;
    }
    
    public void withdraw(double amount) throws InsufficientFundsException {
        if (amount > balance) {
            throw new InsufficientFundsException(amount, balance);
        }
        balance -= amount;
        System.out.println("Successfully withdrew $" + amount + ". New balance: $" + balance);
    }
    
    public void deposit(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }
        balance += amount;
        System.out.println("Successfully deposited $" + amount + ". New balance: $" + balance);
    }
    
    public double getBalance() { return balance; }
    public String getAccountNumber() { return accountNumber; }
}

class Person {
    private String name;
    private int age;
    
    public Person(String name, int age) throws InvalidAgeException {
        this.name = name;
        setAge(age); // Use setter for validation
    }
    
    public void setAge(int age) throws InvalidAgeException {
        if (age < 0 || age > 150) {
            throw new InvalidAgeException(age);
        }
        this.age = age;
    }
    
    public String getName() { return name; }
    public int getAge() { return age; }
    
    @Override
    public String toString() {
        return "Person{name='" + name + "', age=" + age + "}";
    }
}

class Product {
    private String name;
    private int quantity;
    
    public Product(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }
    
    public void purchase(int requestedQuantity) throws ProductNotAvailableException {
        if (requestedQuantity > quantity) {
            throw new ProductNotAvailableException(name, requestedQuantity, quantity);
        }
        quantity -= requestedQuantity;
        System.out.println("Successfully purchased " + requestedQuantity + " " + name + "(s). Remaining: " + quantity);
    }
    
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
}


public class ExceptionHandling {
    
    // 1. BASIC TRY-CATCH EXAMPLES
    
    public static void demonstrateBasicTryCatch() {
        
        // Example 1: ArithmeticException (Division by zero)
        System.out.println("a) Division by Zero:");
        try {
            int result = 10 / 0;
            System.out.println("Result: " + result);
        } catch (ArithmeticException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cannot divide by zero!");
        }
        
        // Example 2: ArrayIndexOutOfBoundsException
        System.out.println("\nb) Array Index Out of Bounds:");
        try {
            int[] numbers = {1, 2, 3, 4, 5};
            System.out.println("Accessing index 10: " + numbers[10]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Array index is out of bounds!");
        }
        
        // Example 3: NumberFormatException
        System.out.println("\nc) Number Format Exception:");
        try {
            String invalidNumber = "abc123";
            int number = Integer.parseInt(invalidNumber);
            System.out.println("Parsed number: " + number);
        } catch (NumberFormatException e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Cannot convert string to number!");
        }
    }
    
  
    // 2. MULTIPLE CATCH BLOCKS
      
    public static void demonstrateMultipleCatch() {
        
        String[] testInputs = {"10", "0", "abc", "5"};
        
        for (String input : testInputs) {
            try {
                System.out.println("\nTesting with input: '" + input + "'");
                
                int number = Integer.parseInt(input);
                int result = 100 / number;
                int[] array = {1, 2, 3};
                
                System.out.println("100 / " + number + " = " + result);
                System.out.println("Array element at index " + (number-1) + ": " + array[number-1]);
                
            } catch (NumberFormatException e) {
                System.out.println("NumberFormatException: Invalid number format - " + input);
            } catch (ArithmeticException e) {
                System.out.println("ArithmeticException: " + e.getMessage());
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println("ArrayIndexOutOfBoundsException: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("General Exception: " + e.getMessage());
            }
        }
    }
    
    

    // 3. TRY-CATCH-FINALLY
    
    public static void demonstrateTryCatchFinally() {
        
        FileWriter writer = null;
        
        try {
            System.out.println("Opening file for writing...");
            writer = new FileWriter("test.txt");
            writer.write("Hello, Exception Handling!");
            System.out.println("Data written to file successfully");
            
            // Simulating an error
            int result = 10 / 0; // This will throw ArithmeticException
            
        } catch (IOException e) {
            System.out.println("IOException occurred: " + e.getMessage());
        } catch (ArithmeticException e) {
            System.out.println("ArithmeticException occurred: " + e.getMessage());
        } finally {
            // This block ALWAYS executes
            System.out.println("Finally block: Cleaning up resources...");
            if (writer != null) {
                try {
                    writer.close();
                    System.out.println("File writer closed successfully");
                } catch (IOException e) {
                    System.out.println("Error closing file: " + e.getMessage());
                }
            }
        }
        
        System.out.println("Method execution completed");
    }
    
    
    // 5. THROWING EXCEPTIONS (throw keyword)
  
    public static void demonstrateThrowingExceptions() {
        // Testing custom exceptions
        BankAccount account = new BankAccount("ACC123", 500.0);
        
        System.out.println("Initial balance: $" + account.getBalance());
        
        // Valid withdrawal
        try {
            account.withdraw(200.0);
        } catch (InsufficientFundsException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
        }
        
        // Invalid withdrawal (will throw custom exception)
        try {
            account.withdraw(400.0);
        } catch (InsufficientFundsException e) {
            System.out.println("Withdrawal failed: " + e.getMessage());
            System.out.println("Available balance: $" + e.getBalance());
            System.out.println("Requested amount: $" + e.getAmount());
        }
        
        // Testing RuntimeException (unchecked)
        try {
            account.deposit(-50.0); // This will throw IllegalArgumentException
        } catch (IllegalArgumentException e) {
            System.out.println("Deposit failed: " + e.getMessage());
        }
    }
    

    // 6. METHODS THAT DECLARE EXCEPTIONS (throws keyword)
    
    public static void demonstrateMethodsWithThrows() {
        
        // Testing Person creation with age validation
        try {
            Person person1 = new Person("Alice", 25);
            System.out.println("Created: " + person1);
            
            Person person2 = new Person("Bob", -5); // This will throw exception
            System.out.println("Created: " + person2);
            
        } catch (InvalidAgeException e) {
            System.out.println("Person creation failed: " + e.getMessage());
        }
        
        // Testing product purchase
        Product laptop = new Product("Laptop", 5);
        System.out.println("\nInitial stock: " + laptop.getQuantity() + " " + laptop.getName() + "(s)");
        
        try {
            laptop.purchase(3); // Valid purchase
            laptop.purchase(4); // This will fail - not enough stock
        } catch (ProductNotAvailableException e) {
            System.out.println("Purchase failed: " + e.getMessage());
            System.out.println("Try purchasing " + e.getAvailableQuantity() + " or less");
        }
    }
    
    // 8. CHECKED VS UNCHECKED EXCEPTIONS
    
    public static void demonstrateCheckedVsUnchecked() {
        
        System.out.println("a) Checked Exceptions (must be handled or declared):");
        
        // Checked exception - must be handled with try-catch or declared with throws
        try {
            FileReader file = new FileReader("nonexistent.txt");
            System.out.println("File opened successfully");
        } catch (FileNotFoundException e) {
            System.out.println("FileNotFoundException (Checked): " + e.getMessage());
        }
        
        System.out.println("\nb) Unchecked Exceptions (optional handling):");
        
        // Unchecked exception - handling is optional but recommended
        try {
            String text = null;
            int length = text.length(); // NullPointerException
        } catch (NullPointerException e) {
            System.out.println("NullPointerException (Unchecked): " + e.getMessage());
        }
        
        // Another unchecked exception
        try {
            int[] array = new int[3];
            array[5] = 10; // ArrayIndexOutOfBoundsException
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("ArrayIndexOutOfBoundsException (Unchecked): " + e.getMessage());
        }
    }
    

    // 9. NESTED TRY-CATCH BLOCKS
    
    public static void demonstrateNestedTryCatch() {
        System.out.println("\n9. NESTED TRY-CATCH BLOCKS:");
        System.out.println("-".repeat(50));
        
        try {
            System.out.println("Outer try block");
            
            int[] numbers = {1, 2, 3, 4, 5};
            
            try {
                System.out.println("Inner try block");
                int result = numbers[2] / 0; // ArithmeticException
                System.out.println("Result: " + result);
                
            } catch (ArithmeticException e) {
                System.out.println("Inner catch: ArithmeticException - " + e.getMessage());
                
                // Intentionally cause another exception in catch block
                int badIndex = numbers[10]; // This will cause ArrayIndexOutOfBoundsException
            }
            
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Outer catch: ArrayIndexOutOfBoundsException - " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Outer catch: General Exception - " + e.getMessage());
        }
        
        System.out.println("Nested try-catch demonstration completed");
    }
    
    // 10. EXCEPTION HANDLING BEST PRACTICES
    
    public static void demonstrateBestPractices() {

        // Practice 1: Specific exception handling
        System.out.println("a) Handle specific exceptions first:");
        try {
            int result = Integer.parseInt("abc") / 0;
        } catch (NumberFormatException e) {
            System.out.println(" Specific: NumberFormatException handled");
        } catch (ArithmeticException e) {
            System.out.println(" Specific: ArithmeticException handled");
        } catch (Exception e) {
            System.out.println(" General: Fallback exception handled");
        }
        
        // Practice 2: Don't ignore exceptions
        System.out.println("\nb) Don't ignore exceptions:");
        try {
            int value = 10 / 0;
        } catch (ArithmeticException e) {
            // DON'T DO: catch (Exception e) { } // Empty catch block
            System.out.println("✓ Exception logged and handled properly");
            // Log the exception, show user-friendly message, etc.
        }
        
        // Practice 3: Use appropriate exception types
        System.out.println("\nc) Use appropriate exception types:");
        try {
            validateAge(-5);
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Appropriate exception type used: " + e.getMessage());
        }
        
        // Practice 4: Provide meaningful error messages
        System.out.println("\nd) Provide meaningful error messages:");
        try {
            int a = 10/0;
        } catch (IllegalArgumentException e) {
            System.out.println("✓ Meaningful message: " + e.getMessage());
        }
    }
    
    private static void validateAge(int age) {
        if (age < 0) {
            throw new IllegalArgumentException("Age cannot be negative. Provided: " + age);
        }
    }
    
  
    // MAIN METHOD - Run all demonstrations
    
    public static void main(String[] args) {
        
        // Run all demonstrations
        demonstrateBasicTryCatch();
        demonstrateMultipleCatch();
        demonstrateTryCatchFinally();
        demonstrateThrowingExceptions();
        demonstrateMethodsWithThrows();
        demonstrateCheckedVsUnchecked();
        demonstrateNestedTryCatch();
        demonstrateBestPractices();
        
    }
}