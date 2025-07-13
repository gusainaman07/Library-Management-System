import java.util.HashMap;
import java.util.Collection;
import java.util.Date;

public class Member {
    private String memberId;
    private String name;
    private String email;
    private String phoneNumber;
    private Date membershipDate;
    private HashMap<String, Book> borrowedBooks; // Books currently borrowed
    private double totalFinesPaid;
    private boolean isActive;
    private int maxBooksAllowed;

    public Member(String memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phoneNumber = "";
        this.membershipDate = new Date();
        this.borrowedBooks = new HashMap<>();
        this.totalFinesPaid = 0.0;
        this.isActive = true;
        this.maxBooksAllowed = 5; // Default limit
    }

    // Constructor with additional details
    public Member(String memberId, String name, String email, String phoneNumber, int maxBooksAllowed) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.membershipDate = new Date();
        this.borrowedBooks = new HashMap<>();
        this.totalFinesPaid = 0.0;
        this.isActive = true;
        this.maxBooksAllowed = maxBooksAllowed;
    }

    // Getters
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhoneNumber() { return phoneNumber; }
    public Date getMembershipDate() { return membershipDate; }
    public HashMap<String, Book> getBorrowedBooks() { return borrowedBooks; }
    public double getTotalFinesPaid() { return totalFinesPaid; }
    public boolean isActive() { return isActive; }
    public int getMaxBooksAllowed() { return maxBooksAllowed; }

    // Setters
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public void setMaxBooksAllowed(int maxBooksAllowed) { this.maxBooksAllowed = maxBooksAllowed; }
    public void setActive(boolean active) { this.isActive = active; }

    // Your original methods (kept exactly the same)
    public void borrowBook(Book book) {
        if (!isActive) {
            System.out.println("Member account is inactive. Cannot borrow books.");
            return;
        }
        
        if (borrowedBooks.size() >= maxBooksAllowed) {
            System.out.println("Maximum book limit reached. Cannot borrow more books.");
            return;
        }
        
        if (borrowedBooks.containsKey(book.getIsbn())) {
            System.out.println("Book already borrowed by this member.");
            return;
        }
        
        borrowedBooks.put(book.getIsbn(), book);
    }

    public void returnBook(String isbn) {
        borrowedBooks.remove(isbn);
    }

    public boolean hasBook(String isbn) {
        return borrowedBooks.containsKey(isbn);
    }

    // Additional useful methods (without Transaction dependency)
    public Book getBorrowedBook(String isbn) {
        return borrowedBooks.get(isbn);
    }

    public Collection<Book> getAllBorrowedBooks() {
        return borrowedBooks.values();
    }

    public int getBorrowedBooksCount() {
        return borrowedBooks.size();
    }

    public boolean canBorrowMoreBooks() {
        return isActive && borrowedBooks.size() < maxBooksAllowed;
    }

    public int getRemainingBookSlots() {
        return maxBooksAllowed - borrowedBooks.size();
    }

    public void payFine(double amount) {
        totalFinesPaid += amount;
    }

    // Display methods
    public void displayMemberInfo() {
        System.out.println("=== Member Information ===");
        System.out.println("ID: " + memberId);
        System.out.println("Name: " + name);
        System.out.println("Email: " + email);
        System.out.println("Phone: " + phoneNumber);
        System.out.println("Membership Date: " + membershipDate);
        System.out.println("Status: " + (isActive ? "Active" : "Inactive"));
        System.out.println("Books Borrowed: " + borrowedBooks.size() + "/" + maxBooksAllowed);
        System.out.println("Total Fines Paid: ₹" + totalFinesPaid);
    }

    public void displayBorrowedBooks() {
        System.out.println("\n=== Currently Borrowed Books ===");
        if (borrowedBooks.isEmpty()) {
            System.out.println("No books currently borrowed.");
        } else {
            for (Book book : borrowedBooks.values()) {
                System.out.println("- " + book.getTitle() + " by " + book.getAuthor() + " (ISBN: " + book.getIsbn() + ")");
            }
        }
    }

    @Override
    public String toString() {
        return "Member{" +
                "ID='" + memberId + '\'' +
                ", Name='" + name + '\'' +
                ", Email='" + email + '\'' +
                ", Active=" + isActive +
                ", Borrowed Books=" + borrowedBooks.size() + "/" + maxBooksAllowed +
                ", Total Fines Paid=₹" + totalFinesPaid +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Member member = (Member) obj;
        return memberId.equals(member.memberId);
    }

    @Override
    public int hashCode() {
        return memberId.hashCode();
    }
}