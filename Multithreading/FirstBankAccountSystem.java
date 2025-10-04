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
        System.out.println("\n=== Starting Different Concurrent Banking Operations ===\n");

        // Create SPECIFIC Runnable tasks - each customer has different behavior!
        CustomerDepositor bachDepositor = new CustomerDepositor(account1);        // Bach: Saves money
        CustomerWithdrawer benWithdrawer = new CustomerWithdrawer(account2);      // Ben: Spends money  
        AccountTransfer charlieTransfer = new AccountTransfer(account3,           // Charlie: Transfers money
                                                    new TheBankAccount[]{account1, account2});
        
        // Create threads with descriptive names
        Thread bachThread = new Thread(bachDepositor, "Bach-Depositor-Thread");
        Thread benThread = new Thread(benWithdrawer, "Ben-Withdrawer-Thread");
        Thread charlieThread = new Thread(charlieTransfer, "Charlie-Transfer-Thread");
        
        // Start all threads concurrently - each doing different operations!
        bachThread.start();      // Bach starts depositing
        benThread.start();       // Ben starts withdrawing
        charlieThread.start();   // Charlie starts transferring
        
        try {
            // Wait for all operations to complete
            bachThread.join();      // Wait for Bach's deposits to finish
            benThread.join();       // Wait for Ben's withdrawals to finish  
            charlieThread.join();   // Wait for Charlie's transfers to finish
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
 * CustomerDepositor - Represents a customer who frequently deposits money
 * Bach Lee's behavior: Makes regular deposits to build savings
 */
class CustomerDepositor implements Runnable {
    private TheBankAccount account;
    private String customerName;
    
    public CustomerDepositor(TheBankAccount account) {
        this.account = account;
        this.customerName = account.getAccountHolderName();
    }
    
    @Override
    public void run() {
        System.out.println("=== " + customerName + " (Depositor) started ===");
        
        // Bach makes multiple deposits - saving money behavior
        double[] deposits = {250.0, 150.0, 300.0, 100.0};
        
        for (double amount : deposits) {
            try {
                account.deposit(amount);
                System.out.println(customerName + " deposited $" + amount + 
                                 " | Balance: $" + account.getBalance());
                Thread.sleep(800); // Simulate time between deposits
            } catch (InterruptedException e) {
                System.err.println(customerName + " deposit interrupted: " + e.getMessage());
                break;
            }
        }
        
        System.out.println("=== " + customerName + " (Depositor) finished ===");
    }
}

/**
 * CustomerWithdrawer - Represents a customer who frequently withdraws money  
 * Ben's behavior: Makes regular withdrawals for expenses
 */
class CustomerWithdrawer implements Runnable {
    private TheBankAccount account;
    private String customerName;
    
    public CustomerWithdrawer(TheBankAccount account) {
        this.account = account;
        this.customerName = account.getAccountHolderName();
    }
    
    @Override
    public void run() {
        System.out.println("=== " + customerName + " (Withdrawer) started ===");
        
        // Ben makes multiple withdrawals - spending money behavior
        double[] withdrawals = {100.0, 200.0, 150.0, 75.0};
        
        for (double amount : withdrawals) {
            try {
                if (account.withdraw(amount)) {
                    System.out.println(customerName + " withdrew $" + amount + 
                                     " | Balance: $" + account.getBalance());
                } else {
                    System.out.println(customerName + " FAILED to withdraw $" + amount + 
                                     " | Insufficient funds! Balance: $" + account.getBalance());
                }
                Thread.sleep(600); // Simulate time between withdrawals
            } catch (InterruptedException e) {
                System.err.println(customerName + " withdrawal interrupted: " + e.getMessage());
                break;
            }
        }
        
        System.out.println("=== " + customerName + " (Withdrawer) finished ===");
    }
}

/**
 * AccountTransfer - Represents a customer who transfers money between accounts
 * Charlie's behavior: Manages money transfers between different accounts
 */
class AccountTransfer implements Runnable {
    private TheBankAccount sourceAccount;
    private TheBankAccount[] targetAccounts;
    private String customerName;
    
    public AccountTransfer(TheBankAccount sourceAccount, TheBankAccount[] targetAccounts) {
        this.sourceAccount = sourceAccount;
        this.targetAccounts = targetAccounts;
        this.customerName = sourceAccount.getAccountHolderName();
    }
    
    @Override
    public void run() {
        System.out.println("=== " + customerName + " (Transfer Manager) started ===");
        
        // Charlie transfers money to different accounts
        double[] transferAmounts = {50.0, 75.0, 100.0};
        
        for (int i = 0; i < transferAmounts.length && i < targetAccounts.length; i++) {
            try {
                double amount = transferAmounts[i];
                TheBankAccount target = targetAccounts[i];
                
                double transferred = sourceAccount.transferFunds(amount, target);
                if (transferred > 0) {
                    System.out.println(customerName + " transferred $" + transferred + 
                                     " to " + target.getAccountHolderName() + 
                                     " | " + customerName + " Balance: $" + sourceAccount.getBalance());
                } else {
                    System.out.println(customerName + " FAILED to transfer $" + amount + 
                                     " | Insufficient funds! Balance: $" + sourceAccount.getBalance());
                }
                Thread.sleep(1000); // Simulate transfer processing time
            } catch (InterruptedException e) {
                System.err.println(customerName + " transfer interrupted: " + e.getMessage());
                break;
            }
        }
        
        System.out.println("=== " + customerName + " (Transfer Manager) finished ===");
    }
}