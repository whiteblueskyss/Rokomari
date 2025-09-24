import java.util.*;
import java.util.stream.*;

// Employee class for examples
class Employee {
    private String name;
    private String department;
    private double salary;
    
    public Employee(String name, String department, double salary) {
        this.name = name;
        this.department = department;
        this.salary = salary;
    }
    
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getSalary() { return salary; }
    
    @Override
    public String toString() {
        return name + " (" + department + ", $" + salary + ")";
    }
}

// STREAM API TUTORIAL - TRADITIONAL vs STREAM APPROACH
public class StreamAPI {
    
    // 1. BASIC STREAM CONCEPTS
    public static void basicStreamConcepts() {
        System.out.println("1. BASIC STREAM CONCEPTS");
        System.out.println("-".repeat(50));
        
        List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");
        
        // Traditional way - filter names longer than 3 chars and make uppercase
        System.out.println("TRADITIONAL WAY - filter long names and make uppercase:");
        List<String> traditionalResult = new ArrayList<>();
        for (String name : names) {                    // Loop through each name
            if (name.length() > 3) {                   // Check length condition
                traditionalResult.add(name.toUpperCase()); // Transform and add
            }
        }
        System.out.println("Traditional Result: " + traditionalResult);
        
        // Stream way - same operation
        System.out.println("\nSTREAM WAY - filter long names and make uppercase:");
        List<String> streamResult = names.stream()                    // Create stream
                                        .filter(name -> name.length() > 3)  // Keep names > 3 chars
                                        .map(String::toUpperCase)            // Convert to uppercase
                                        .collect(Collectors.toList());       // Collect to list
        System.out.println("Stream Result: " + streamResult);
        System.out.println("Both results are identical: " + traditionalResult.equals(streamResult));
    }
    
    // 2. FILTERING OPERATIONS
    public static void filteringOperations() {
        System.out.println("\n2. FILTERING OPERATIONS");
        System.out.println("-".repeat(50));
        
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        
        // Example 1: Get even numbers
        System.out.println("Original numbers: " + numbers);
        
        // Traditional way
        System.out.println("\nTRADITIONAL WAY - Get even numbers:");
        List<Integer> traditionalEvens = new ArrayList<>();
        for (Integer num : numbers) {          // Loop through each number
            if (num % 2 == 0) {               // Check if even
                traditionalEvens.add(num);    // Add to result list
            }
        }
        System.out.println("Traditional Even numbers: " + traditionalEvens);
        
        // Stream way
        System.out.println("\nSTREAM WAY - Get even numbers:");
        List<Integer> streamEvens = numbers.stream()
                                         .filter(n -> n % 2 == 0)        // Keep even numbers only
                                         .collect(Collectors.toList());  // Collect to list
        System.out.println("Stream Even numbers: " + streamEvens);
        
        // Example 2: Get numbers greater than 5
        // Traditional way
        System.out.println("\nTRADITIONAL WAY - Numbers > 5:");
        List<Integer> traditionalGreater = new ArrayList<>();
        for (Integer num : numbers) {          // Loop through each number
            if (num > 5) {                    // Check if greater than 5
                traditionalGreater.add(num);  // Add to result
            }
        }
        System.out.println("Traditional > 5: " + traditionalGreater);
        
        // Stream way
        System.out.println("\nSTREAM WAY - Numbers > 5:");
        List<Integer> streamGreater = numbers.stream()
                                           .filter(n -> n > 5)           // Keep numbers > 5
                                           .collect(Collectors.toList()); // Collect to list
        System.out.println("Stream > 5: " + streamGreater);
    }
    
    // 3. TRANSFORMING OPERATIONS (MAP)
    public static void transformingOperations() {
        System.out.println("\n3. TRANSFORMING OPERATIONS (MAP)");
        System.out.println("-".repeat(50));
        
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        
        // Example 1: Square each number
        System.out.println("Original numbers: " + numbers);
        
        // Traditional way
        System.out.println("\nTRADITIONAL WAY - Square each number:");
        List<Integer> traditionalSquares = new ArrayList<>();
        for (Integer num : numbers) {              // Loop through each number
            int square = num * num;                // Calculate square
            traditionalSquares.add(square);        // Add to result
        }
        System.out.println("Traditional Squares: " + traditionalSquares);
        
        // Stream way
        System.out.println("\nSTREAM WAY - Square each number:");
        List<Integer> streamSquares = numbers.stream()
                                           .map(n -> n * n)               // Transform each to square
                                           .collect(Collectors.toList()); // Collect to list
        System.out.println("Stream Squares: " + streamSquares);
        
        // Example 2: Convert numbers to strings
        // Traditional way
        System.out.println("\nTRADITIONAL WAY - Convert to strings:");
        List<String> traditionalStrings = new ArrayList<>();
        for (Integer num : numbers) {              // Loop through each number
            traditionalStrings.add(num.toString()); // Convert to string and add
        }
        System.out.println("Traditional Strings: " + traditionalStrings);
        
        // Stream way
        System.out.println("\nSTREAM WAY - Convert to strings:");
        List<String> streamStrings = numbers.stream()
                                          .map(Object::toString)          // Convert each to string
                                          .collect(Collectors.toList());  // Collect to list
        System.out.println("Stream Strings: " + streamStrings);
    }
    
    // 4. REMOVING DUPLICATES (DISTINCT)
    public static void removingDuplicates() {
        System.out.println("\n4. REMOVING DUPLICATES (DISTINCT)");
        System.out.println("-".repeat(50));
        
        List<Integer> numbersWithDuplicates = Arrays.asList(1, 2, 2, 3, 3, 3, 4, 5, 5);
        System.out.println("Original with duplicates: " + numbersWithDuplicates);
        
        // Traditional way
        System.out.println("\nTRADITIONAL WAY - Remove duplicates:");
        Set<Integer> traditionalSet = new HashSet<>();   // Use Set to remove duplicates
        for (Integer num : numbersWithDuplicates) {      // Loop through each number
            traditionalSet.add(num);                     // Set automatically handles duplicates
        }
        List<Integer> traditionalUnique = new ArrayList<>(traditionalSet); // Convert back to list
        System.out.println("Traditional Unique: " + traditionalUnique);
        
        // Stream way
        System.out.println("\nSTREAM WAY - Remove duplicates:");
        List<Integer> streamUnique = numbersWithDuplicates.stream()
                                                        .distinct()                    // Remove duplicates
                                                        .collect(Collectors.toList()); // Collect to list
        System.out.println("Stream Unique: " + streamUnique);
    }
    
    // 5. SORTING OPERATIONS
    public static void sortingOperations() {
        System.out.println("\n5. SORTING OPERATIONS");
        System.out.println("-".repeat(50));
        
        List<String> words = Arrays.asList("banana", "apple", "cherry", "date");
        System.out.println("Original words: " + words);
        
        // Traditional way
        System.out.println("\nTRADITIONAL WAY - Sort alphabetically:");
        List<String> traditionalSorted = new ArrayList<>(words); // Create copy
        Collections.sort(traditionalSorted);                    // Sort the copy
        System.out.println("Traditional Sorted: " + traditionalSorted);
        
        // Stream way
        System.out.println("\nSTREAM WAY - Sort alphabetically:");
        List<String> streamSorted = words.stream()
                                       .sorted()                      // Sort in natural order
                                       .collect(Collectors.toList()); // Collect to list
        System.out.println("Stream Sorted: " + streamSorted);
        
        // Reverse sorting example
        // Traditional way
        System.out.println("\nTRADITIONAL WAY - Sort in reverse:");
        List<String> traditionalReverse = new ArrayList<>(words);     // Create copy
        Collections.sort(traditionalReverse, Collections.reverseOrder()); // Sort reverse
        System.out.println("Traditional Reverse: " + traditionalReverse);
        
        // Stream way
        System.out.println("\nSTREAM WAY - Sort in reverse:");
        List<String> streamReverse = words.stream()
                                        .sorted(Collections.reverseOrder()) // Sort in reverse
                                        .collect(Collectors.toList());      // Collect to list
        System.out.println("Stream Reverse: " + streamReverse);
    }
    
    // 6. COUNTING OPERATIONS
    public static void countingOperations() {
        System.out.println("\n6. COUNTING OPERATIONS");
        System.out.println("-".repeat(50));
        
        List<String> words = Arrays.asList("apple", "banana", "cherry", "date", "elderberry");
        System.out.println("Words: " + words);
        
        // Example 1: Count words longer than 5 characters
        // Traditional way
        System.out.println("\nTRADITIONAL WAY - Count words longer than 5 chars:");
        int traditionalCount = 0;
        for (String word : words) {       // Loop through each word
            if (word.length() > 5) {      // Check length condition
                traditionalCount++;       // Increment counter
            }
        }
        System.out.println("Traditional count > 5 chars: " + traditionalCount);
        
        // Stream way
        System.out.println("\nSTREAM WAY - Count words longer than 5 chars:");
        long streamCount = words.stream()
                              .filter(word -> word.length() > 5) // Keep words > 5 chars
                              .count();                          // Count remaining elements
        System.out.println("Stream count > 5 chars: " + streamCount);
        
        // Example 2: Count total characters in all words
        // Traditional way
        System.out.println("\nTRADITIONAL WAY - Count total characters:");
        int traditionalTotalChars = 0;
        for (String word : words) {           // Loop through each word
            traditionalTotalChars += word.length(); // Add word length
        }
        System.out.println("Traditional total chars: " + traditionalTotalChars);
        
        // Stream way
        System.out.println("\nSTREAM WAY - Count total characters:");
        int streamTotalChars = words.stream()
                                  .mapToInt(String::length) // Convert each word to its length
                                  .sum();                   // Sum all lengths
        System.out.println("Stream total chars: " + streamTotalChars);
    }
    
    // 7. FINDING OPERATIONS
    public static void findingOperations() {
        System.out.println("\n7. FINDING OPERATIONS");
        System.out.println("-".repeat(50));
        
        List<Integer> numbers = Arrays.asList(1, 3, 5, 8, 9, 12);
        System.out.println("Numbers: " + numbers);
        
        // Example 1: Find first even number
        // Traditional way
        System.out.println("\nTRADITIONAL WAY - Find first even number:");
        Integer traditionalFirstEven = null;
        for (Integer num : numbers) {     // Loop through each number
            if (num % 2 == 0) {          // Check if even
                traditionalFirstEven = num; // Found first even
                break;                   // Stop searching
            }
        }
        System.out.println("Traditional first even: " + 
            (traditionalFirstEven != null ? traditionalFirstEven : "Not found"));
        
        // Stream way
        System.out.println("\nSTREAM WAY - Find first even number:");
        Optional<Integer> streamFirstEven = numbers.stream()
                                                 .filter(n -> n % 2 == 0) // Keep even numbers
                                                 .findFirst();            // Find first one
        System.out.println("Stream first even: " + streamFirstEven.orElse(null));
        
        // Example 2: Find minimum and maximum
        // Traditional way
        System.out.println("\nTRADITIONAL WAY - Find min and max:");
        Integer traditionalMin = numbers.get(0); // Start with first element
        Integer traditionalMax = numbers.get(0);
        for (Integer num : numbers) {             // Loop through all numbers
            if (num < traditionalMin) {           // Found smaller number
                traditionalMin = num;
            }
            if (num > traditionalMax) {           // Found larger number
                traditionalMax = num;
            }
        }
        System.out.println("Traditional Min: " + traditionalMin + ", Max: " + traditionalMax);
        
        // Stream way
        System.out.println("\nSTREAM WAY - Find min and max:");
        Optional<Integer> streamMin = numbers.stream().min(Integer::compareTo);
        Optional<Integer> streamMax = numbers.stream().max(Integer::compareTo);
        System.out.println("Stream Min: " + streamMin.orElse(null) + 
                         ", Max: " + streamMax.orElse(null));
    }
    
    // 8. WORKING WITH OBJECTS - EMPLOYEE EXAMPLES
    public static void workingWithObjects() {
        System.out.println("\n8. WORKING WITH OBJECTS - EMPLOYEE EXAMPLES");
        System.out.println("-".repeat(50));
        
        List<Employee> employees = Arrays.asList(
            new Employee("Alice", "IT", 75000),
            new Employee("Bob", "Finance", 65000),
            new Employee("Charlie", "IT", 80000),
            new Employee("Diana", "HR", 60000),
            new Employee("Eve", "Finance", 70000)
        );
        
        System.out.println("All employees:");
        employees.forEach(System.out::println);
        
        // Example 1: Filter IT employees
        // Traditional way
        System.out.println("\nTRADITIONAL WAY - Get IT employees:");
        List<Employee> traditionalIT = new ArrayList<>();
        for (Employee emp : employees) {              // Loop through each employee
            if (emp.getDepartment().equals("IT")) {   // Check if IT department
                traditionalIT.add(emp);               // Add to result
            }
        }
        System.out.println("Traditional IT employees:");
        for (Employee emp : traditionalIT) {          // Print each IT employee
            System.out.println("  " + emp);
        }
        
        // Stream way
        System.out.println("\nSTREAM WAY - Get IT employees:");
        System.out.println("Stream IT employees:");
        employees.stream()
                .filter(emp -> emp.getDepartment().equals("IT")) // Keep IT employees only
                .forEach(emp -> System.out.println("  " + emp)); // Print each one
        
        // Example 2: Get names of high salary employees (>70000)
        // Traditional way
        System.out.println("\nTRADITIONAL WAY - High salary employee names:");
        List<String> traditionalHighSalaryNames = new ArrayList<>();
        for (Employee emp : employees) {                    // Loop through each employee
            if (emp.getSalary() > 70000) {                 // Check salary condition
                traditionalHighSalaryNames.add(emp.getName()); // Add name to result
            }
        }
        System.out.println("Traditional high salary names: " + traditionalHighSalaryNames);
        
        // Stream way
        System.out.println("\nSTREAM WAY - High salary employee names:");
        List<String> streamHighSalaryNames = employees.stream()
                                                    .filter(emp -> emp.getSalary() > 70000) // Keep high salary
                                                    .map(Employee::getName)                 // Extract names
                                                    .collect(Collectors.toList());          // Collect to list
        System.out.println("Stream high salary names: " + streamHighSalaryNames);
        
        // Example 3: Calculate average salary
        // Traditional way
        System.out.println("\nTRADITIONAL WAY - Calculate average salary:");
        double traditionalTotal = 0;
        for (Employee emp : employees) {          // Loop through each employee
            traditionalTotal += emp.getSalary();  // Add salary to total
        }
        double traditionalAverage = traditionalTotal / employees.size(); // Calculate average
        System.out.println("Traditional average salary: $" + String.format("%.2f", traditionalAverage));
        
        // Stream way
        System.out.println("\nSTREAM WAY - Calculate average salary:");
        double streamAverage = employees.stream()
                                      .mapToDouble(Employee::getSalary) // Convert to salary stream
                                      .average()                        // Calculate average
                                      .orElse(0.0);                    // Default if empty
        System.out.println("Stream average salary: $" + String.format("%.2f", streamAverage));
        
        // Example 4: Find highest paid employee
        // Traditional way
        System.out.println("\nTRADITIONAL WAY - Find highest paid employee:");
        Employee traditionalHighest = employees.get(0); // Start with first employee
        for (Employee emp : employees) {                 // Loop through all employees
            if (emp.getSalary() > traditionalHighest.getSalary()) { // Compare salaries
                traditionalHighest = emp;                // Update if higher salary found
            }
        }
        System.out.println("Traditional highest paid: " + traditionalHighest);
        
        // Stream way
        System.out.println("\nSTREAM WAY - Find highest paid employee:");
        Optional<Employee> streamHighest = employees.stream()
                                                  .max(Comparator.comparingDouble(Employee::getSalary)); // Find max by salary
        System.out.println("Stream highest paid: " + streamHighest.orElse(null));
    }
    
    // 9. COMPLEX CHAINING OPERATIONS
    public static void complexChaining() {
        System.out.println("\n9. COMPLEX CHAINING OPERATIONS");
        System.out.println("-".repeat(50));
        
        List<String> words = Arrays.asList("apple", "banana", "APPLE", "cherry", "BANANA", "date");
        System.out.println("Original words: " + words);
        
        // Complex operation: Convert to lowercase, remove duplicates, keep words > 4 chars, sort
        
        // Traditional way
        System.out.println("\nTRADITIONAL WAY - Complex processing:");
        // Step 1: Convert to lowercase
        List<String> traditionalLower = new ArrayList<>();
        for (String word : words) {
            traditionalLower.add(word.toLowerCase());
        }
        
        // Step 2: Remove duplicates
        Set<String> traditionalSet = new HashSet<>(traditionalLower);
        List<String> traditionalUnique = new ArrayList<>(traditionalSet);
        
        // Step 3: Keep words longer than 4 characters
        List<String> traditionalFiltered = new ArrayList<>();
        for (String word : traditionalUnique) {
            if (word.length() > 4) {
                traditionalFiltered.add(word);
            }
        }
        
        // Step 4: Sort
        Collections.sort(traditionalFiltered);
        
        System.out.println("Traditional result: " + traditionalFiltered);
        
        // Stream way - all in one chain
        System.out.println("\nSTREAM WAY - Complex processing (chained):");
        List<String> streamResult = words.stream()
                                       .map(String::toLowerCase)          // Convert to lowercase
                                       .distinct()                        // Remove duplicates
                                       .filter(word -> word.length() > 4) // Keep long words
                                       .sorted()                          // Sort alphabetically
                                       .collect(Collectors.toList());     // Collect to list
        System.out.println("Stream result: " + streamResult);
        
        System.out.println("Results are equal: " + traditionalFiltered.equals(streamResult));
    }
    
    // MAIN METHOD
    public static void main(String[] args) {
        System.out.println("=".repeat(70));
        System.out.println("STREAM API TUTORIAL - TRADITIONAL vs STREAM COMPARISON");
        System.out.println("=".repeat(70));
        
        basicStreamConcepts();
        filteringOperations();
        transformingOperations();
        removingDuplicates();
        sortingOperations();
        countingOperations();
        findingOperations();
        workingWithObjects();
        complexChaining();
        
    }
}