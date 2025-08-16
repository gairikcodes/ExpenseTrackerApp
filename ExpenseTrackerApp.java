import java.io.*;
import java.time.LocalDate;
import java.util.*;

class Expense {
    private String category;
    private double amount;
    private LocalDate date;

    public Expense(String category, double amount, LocalDate date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    public String getCategory() { return category; }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }

    @Override
    public String toString() {
        return date + " | " + category + " | $" + amount;
    }
}

class ExpenseManager {
    private List<Expense> expenses = new ArrayList<>();

    public void addExpense(String category, double amount) {
        expenses.add(new Expense(category, amount, LocalDate.now()));
    }

    public void viewExpenses() {
        if (expenses.isEmpty()) {
            System.out.println("No expenses recorded.");
        } else {
            expenses.forEach(System.out::println);
        }
    }

    public void monthlyReport() {
        double total = 0;
        Map<String, Double> categoryTotal = new HashMap<>();
        for (Expense e : expenses) {
            total += e.getAmount();
            categoryTotal.put(e.getCategory(),
                    categoryTotal.getOrDefault(e.getCategory(), 0.0) + e.getAmount());
        }
        System.out.println("\n📊 Monthly Report:");
        categoryTotal.forEach((k, v) -> System.out.println(k + ": $" + v));
        System.out.println("Total: $" + total);
    }

    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Expense e : expenses) {
                bw.write(e.getDate() + "," + e.getCategory() + "," + e.getAmount());
                bw.newLine();
            }
        }
    }

    public void loadFromFile(String filename) throws IOException {
        expenses.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                expenses.add(new Expense(parts[1], Double.parseDouble(parts[2]), LocalDate.parse(parts[0])));
            }
        }
    }
}

public class ExpenseTrackerApp {
    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);
        ExpenseManager manager = new ExpenseManager();
        String filename = "expenses.csv";

        // Load previous data
        try { manager.loadFromFile(filename); } catch (Exception ignored) {}

        while (true) {
            System.out.println("\n1. Add Expense\n2. View Expenses\n3. Monthly Report\n4. Save & Exit");
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    System.out.print("Enter category: ");
                    String category = sc.next();
                    System.out.print("Enter amount: ");
                    double amount = sc.nextDouble();
                    manager.addExpense(category, amount);
                    break;
                case 2: manager.viewExpenses(); break;
                case 3: manager.monthlyReport(); break;
                case 4:
                    manager.saveToFile(filename);
                    System.out.println("Data saved. Goodbye!");
                    return;
                default: System.out.println("Invalid choice.");
            }
        }
    }
}
