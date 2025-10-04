package Multithreading;

/**
 * Enhanced Bank Account System demonstrating proper Runnable implementation
 * with concurrent operations and synchronization.
 * 
 * This example shows:
 * 1. How to use Runnable interface for concurrent banking operations
 * 2. Proper synchronization to prevent race conditions
 * 3. Real-world scenario with multiple customers performing operations simultaneously
 * 4. Thread safety in shared resources (bank accounts)
 */

public class FirstBankAccountSystem {
    public static void main(String[] args) {
        // Create bank accounts
        TheBankAccount account1 = new TheBankAccount(1000.0, "Bach Lee");
        TheBankAccount account2 = new TheBankAccount(500.0, "Ben");
        TheBankAccount account3 = new TheBankAccount(300.0, "Charlie");

        System.out.println("=== Initial Account Balances ===");
        System.out.println(account1.toString());
        System.out.println(account2.toString());
        System.out.println(account3.toString());
        System.out.println("\n=== Starting Concurrent Banking Operations ===\n");

        // Create Runnable tasks for concurrent operations
        BankingOperations ops1 = new BankingOperations(account1, account2, "Bach-Operations");
        BankingOperations ops2 = new BankingOperations(account2, account3, "Ben-Operations");
        BankingOperations ops3 = new BankingOperations(account3, account1, "Charlie-Operations");
        
        // Create and start threads
        Thread thread1 = new Thread(ops1);
        Thread thread2 = new Thread(ops2);
        Thread thread3 = new Thread(ops3);
        
        thread1.setName("Bach-Thread");
        thread2.setName("Ben-Thread");
        thread3.setName("Charlie-Thread");
        
        // Start all threads concurrently
        thread1.start();
        thread2.start();
        thread3.start();
        
        try {
            // Wait for all operations to complete
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            System.err.println("Main thread interrupted: " + e.getMessage());
        }
        
        System.out.println("\n=== Final Account Balances ===");
        System.out.println(account1.toString());
        System.out.println(account2.toString());
        System.out.println(account3.toString());
        System.out.println("\nAll concurrent operations completed successfully!");
    }
}

class TheBankAccount {
    private double balance;
    private String accountHolderName;

    public TheBankAccount(double initialBalance, String accountHolderName) {
        this.balance = initialBalance;
        this.accountHolderName = accountHolderName;
    }

    public synchronized boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            return true;
        } else {
            return false;
        }
    }

    public synchronized void deposit(double amount) {
        balance += amount;   
    }

    public synchronized double transferFunds(double amount, TheBankAccount targetAccount) {
        if (withdraw(amount)) {
            targetAccount.deposit(amount);
            return amount;
        } else {
            System.err.println("Transfer failed due to insufficient funds.");
        }
        return 0;
    }

    public synchronized double getBalance() {
        return balance;
    }
    public synchronized void setBalance(double balance) {
        this.balance = balance;
    }
    public String getAccountHolderName() {
        return accountHolderName;
    }
    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public String toString() {
        return "Account Holder: " + accountHolderName + ", Balance: $" + balance;
    }
}

/**
 * BankingOperations class implements Runnable interface
 * Demonstrates concurrent banking operations between accounts
 * 
 * KEY BENEFITS OF USING RUNNABLE:
 * 1. Separation of concerns - Task logic separate from thread management
 * 2. Flexibility - Can be executed by different thread executors
 * 3. Reusability - Same task can be used with thread pools
 * 4. Better OOP design - Allows class to extend other classes if needed
 */
class BankingOperations implements Runnable {
    private TheBankAccount sourceAccount;
    private TheBankAccount targetAccount;
    private String operationName;
    
    public BankingOperations(TheBankAccount sourceAccount, TheBankAccount targetAccount, String operationName) {
        this.sourceAccount = sourceAccount;
        this.targetAccount = targetAccount;
        this.operationName = operationName;
    }
    
    @Override
    public void run() {
        System.out.println(operationName + " started by " + Thread.currentThread().getName());
        
        try {
            // Perform deposit operation
            double depositAmount = 100.0;
            sourceAccount.deposit(depositAmount);
            System.out.println(operationName + " - Deposited $" + depositAmount + " to " + 
                             sourceAccount.getAccountHolderName() + "'s account");
            
            Thread.sleep(500); // Simulate processing time
            
            // Perform withdrawal operation  
            double withdrawAmount = 150.0;
            if (sourceAccount.withdraw(withdrawAmount)) {
                System.out.println(operationName + " - Withdrew $" + withdrawAmount + " from " + 
                                 sourceAccount.getAccountHolderName() + "'s account");
            } else {
                System.out.println(operationName + " - Withdrawal failed for " + 
                                 sourceAccount.getAccountHolderName() + " - Insufficient funds");
            }
            
            Thread.sleep(300);
            
            // Perform transfer operation
            double transferAmount = 75.0;
            double transferred = sourceAccount.transferFunds(transferAmount, targetAccount);
            if (transferred > 0) {
                System.out.println(operationName + " - Transferred $" + transferred + " from " + 
                                 sourceAccount.getAccountHolderName() + " to " + 
                                 targetAccount.getAccountHolderName());
            }
            
        } catch (InterruptedException e) {
            System.err.println(operationName + " interrupted: " + e.getMessage());
        }
        
        System.out.println(operationName + " completed by " + Thread.currentThread().getName());
    }
}