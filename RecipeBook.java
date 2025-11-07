package com.gqt.core_java.Mini_projects;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

class Category {
    String name;
    List<String> items = new ArrayList<>();
    List<String> selectedItems = new ArrayList<>();
    List<Integer> itemPrices = new ArrayList<>();

    Category(String name, List<String> items, List<Integer> prices) {
        this.name = name;
        this.items.addAll(items);
        this.itemPrices.addAll(prices);
    }
}

class RecipeMenu {
    Scanner sc = new Scanner(System.in);
    List<Category> categories = new ArrayList<>();

    RecipeMenu() {
        categories.add(new Category("Soups",
                List.of("Tomato Soup", "Sweet Corn Soup", "Chicken Soup", "Hot and Sour Soup"),
                List.of(80, 90, 110, 100)));

        categories.add(new Category("Starters",
                List.of("Paneer Tikka", "Veg Manchurian", "Hara Bhara Kabab", "Chicken Lollipop", "Tandoori Chicken", "Fish Fingers"),
                List.of(120, 100, 110, 150, 160, 170)));

        categories.add(new Category("Veg Curries",
                List.of("Palak Paneer", "Kadai Vegetable", "Aloo Gobi", "Mushroom Masala"),
                List.of(130, 120, 110, 140)));

        categories.add(new Category("Non-Veg Curries",
                List.of("Chicken Curry", "Mutton Curry", "Fish Curry", "Egg Curry"),
                List.of(160, 180, 170, 130)));

        categories.add(new Category("Veg Food Items",
                List.of("Veg Pulao", "Chapathi with Curry", "Poori with Aloo", "Lemon Rice"),
                List.of(110, 100, 90, 80)));

        categories.add(new Category("Non-Veg Food Items",
                List.of("Chicken Fried Rice", "Egg Noodles", "Mutton Keema Paratha", "Chicken Shawarma"),
                List.of(140, 120, 150, 160)));

        categories.add(new Category("Biryanis",
                List.of("Veg Biryani", "Chicken Biryani", "Mutton Biryani", "Egg Biryani"),
                List.of(130, 160, 180, 140)));

        categories.add(new Category("Desserts",
                List.of("Gulab Jamun", "Rasmalai", "Ice Cream", "Kheer"),
                List.of(60, 80, 70, 75)));

        categories.add(new Category("Milkshakes",
                List.of("Chocolate Shake", "Strawberry Shake", "Banana Shake", "Mango Shake"),
                List.of(90, 90, 80, 85)));

        categories.add(new Category("Cold Drinks",
                List.of("Pepsi", "Coke", "Sprite", "Limca"),
                List.of(40, 40, 40, 40)));
    }

    void login() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        if (!username.matches("[a-zA-Z]+") || !password.matches("[\\w@#$%^&+=!]+")) {
            System.out.println("Invalid credentials format. Only characters allowed for username.");
            System.exit(0);
        }

        System.out.println("Login successful!\n");
    }

    void showMenu() {
        System.out.println("\n========= Simple Recipe Book =========");
        for (int i = 0; i < categories.size(); i++) {
            System.out.println((i + 1) + ". " + categories.get(i).name);
        }
        System.out.println((categories.size() + 1) + ". Exit & Save");
    }

    void selectCategory() throws IOException {
        int choice;
        do {
            showMenu();
            System.out.print("Enter your Recipe category choice: ");
            if (sc.hasNextInt()) {
                choice = sc.nextInt();
                sc.nextLine(); // consume newline

                if (choice >= 1 && choice <= categories.size()) {
                    selectItems(categories.get(choice - 1));
                } else if (choice == categories.size() + 1) {
                    displaySelections();
                    saveToFile();
                    System.out.println("Thank you! Your selections are saved.");
                } else {
                    System.out.println("Invalid category choice.");
                }
            } else {
                System.out.println("Enter a valid number.");
                sc.nextLine(); // clear buffer
                choice = 0;
            }
        } while (choice != categories.size() + 1);
    }

    void selectItems(Category category) {
        System.out.println("\n--- " + category.name + " ---");
        for (int i = 0; i < category.items.size(); i++) {
            System.out.println((i + 1) + ". " + category.items.get(i) + " - ₹" + category.itemPrices.get(i));
        }
        System.out.print("Select item numbers (comma-separated, e.g., 1,3): ");
        String[] input = sc.nextLine().split(",");
        boolean validSelection = false;

        for (String s : input) {
            s = s.trim();
            if (s.matches("\\d+")) {
                int index = Integer.parseInt(s) - 1;
                if (index >= 0 && index < category.items.size()) {
                    category.selectedItems.add(category.items.get(index));
                    validSelection = true;
                } else {
                    System.out.println("Invalid item number: " + (index + 1));
                }
            } else {
                System.out.println("Invalid input: " + s);
            }
        }

        if (validSelection) {
            System.out.println("Items added successfully.\n");
        }
    }

    void displaySelections() {
        double total = 0;
        System.out.println("\nYour Selected Recipes (In Menu Order):\n");
        for (Category category : categories) {
            if (!category.selectedItems.isEmpty()) {
                System.out.println("=== " + category.name + " ===");
                for (String item : category.selectedItems) {
                    int index = category.items.indexOf(item);
                    int price = category.itemPrices.get(index);
                    total += price;
                    System.out.println("- " + item + " - ₹" + price);
                }
                System.out.println();
            }
        }

        double discount = total * 0.15;
        double finalTotal = total - discount;

        System.out.printf("Total: ₹%.2f\n", total);
        System.out.printf("15%% Voucher Discount: -₹%.2f\n", discount);
        System.out.printf("Final Amount to Pay: ₹%.2f\n", finalTotal);
    }

    void saveToFile() throws IOException {
        FileWriter writer = new FileWriter("selected_recipes.txt");
        writer.write("Your Selected Recipes (In Menu Order):\n\n");
        double total = 0;

        for (Category category : categories) {
            if (!category.selectedItems.isEmpty()) {
                writer.write("=== " + category.name + " ===\n");
                for (String item : category.selectedItems) {
                    int index = category.items.indexOf(item);
                    int price = category.itemPrices.get(index);
                    total += price;
                    writer.write("- " + item + " - ₹" + price + "\n");
                }
                writer.write("\n");
            }
        }

        double discount = total * 0.15;
        double finalTotal = total - discount;

        writer.write(String.format("Total: ₹%.2f\n", total));
        writer.write(String.format("15%% Voucher Discount: -₹%.2f\n", discount));
        writer.write(String.format("Final Amount to Pay: ₹%.2f\n", finalTotal));

        writer.close();
    }
}

public class RecipeBook {
    public static void main(String[] args) throws IOException {
        RecipeMenu rm = new RecipeMenu();
        rm.login();
        rm.selectCategory();
    }
}























