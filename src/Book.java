import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

public class Book {
    private String isbn;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;
    private LinkedList<String> borrowersQueue;
    private List<String> currentBorrowers; // Track who has borrowed the book
    private String genre;
    private String publisher;
    private int publicationYear;
    private boolean isActive; // Book can be deactivated

    // Constructor (your original)
    public Book(String isbn, String title, String author, int totalCopies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.borrowersQueue = new LinkedList<>();
        this.currentBorrowers = new ArrayList<>();
        this.genre = "General";
        this.publisher = "Unknown";
        this.publicationYear = 2024;
        this.isActive = true;
    }

    // Enhanced constructor
    public Book(String isbn, String title, String author, int totalCopies, 
                String genre, String publisher, int publicationYear) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCopies = totalCopies;
        this.availableCopies = totalCopies;
        this.borrowersQueue = new LinkedList<>();
        this.currentBorrowers = new ArrayList<>();
        this.genre = genre;
        this.publisher = publisher;
        this.publicationYear = publicationYear;
        this.isActive = true;
    }

    // Getters
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    public LinkedList<String> getBorrowersQueue() { return borrowersQueue; }
    public List<String> getCurrentBorrowers() { return currentBorrowers; }
    public String getGenre() { return genre; }
    public String getPublisher() { return publisher; }
    public int getPublicationYear() { return publicationYear; }
    public boolean isActive() { return isActive; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public void setPublicationYear(int publicationYear) { this.publicationYear = publicationYear; }
    public void setActive(boolean active) { this.isActive = active; }

    // Enhanced copy management with validation
    public boolean decreaseAvailableCopies() {
        if (availableCopies > 0) {
            availableCopies--;
            return true;
        }
        System.out.println("No available copies to decrease for book: " + title);
        return false;
    }

    public boolean increaseAvailableCopies() {
        if (availableCopies < totalCopies) {
            availableCopies++;
            return true;
        }
        System.out.println("Cannot increase copies beyond total copies for book: " + title);
        return false;
    }

    // Add more copies to the book
    public void addCopies(int numberOfCopies) {
        if (numberOfCopies > 0) {
            totalCopies += numberOfCopies;
            availableCopies += numberOfCopies;
            System.out.println("Added " + numberOfCopies + " copies to book: " + title);
        }
    }

    // Remove copies from the book
    public boolean removeCopies(int numberOfCopies) {
        if (numberOfCopies > 0 && numberOfCopies <= availableCopies) {
            totalCopies -= numberOfCopies;
            availableCopies -= numberOfCopies;
            System.out.println("Removed " + numberOfCopies + " copies from book: " + title);
            return true;
        }
        System.out.println("Cannot remove " + numberOfCopies + " copies. Only " + availableCopies + " available.");
        return false;
    }

    // Queue management
    public void addToQueue(String memberId) {
        if (!borrowersQueue.contains(memberId)) {
            borrowersQueue.add(memberId);
            System.out.println("Member " + memberId + " added to waiting queue for book: " + title);
        } else {
            System.out.println("Member " + memberId + " is already in the queue for book: " + title);
        }
    }

    public String getNextInQueue() {
        String nextMember = borrowersQueue.poll();
        if (nextMember != null) {
            System.out.println("Next member in queue: " + nextMember + " for book: " + title);
        }
        return nextMember;
    }

    public boolean removeFromQueue(String memberId) {
        boolean removed = borrowersQueue.remove(memberId);
        if (removed) {
            System.out.println("Member " + memberId + " removed from queue for book: " + title);
        }
        return removed;
    }

    public int getQueueSize() {
        return borrowersQueue.size();
    }

    public boolean isInQueue(String memberId) {
        return borrowersQueue.contains(memberId);
    }

    // Borrower tracking
    public void addBorrower(String memberId) {
        if (!currentBorrowers.contains(memberId)) {
            currentBorrowers.add(memberId);
        }
    }

    public boolean removeBorrower(String memberId) {
        return currentBorrowers.remove(memberId);
    }

    public boolean isBorrowedBy(String memberId) {
        return currentBorrowers.contains(memberId);
    }

    public int getBorrowedCopiesCount() {
        return totalCopies - availableCopies;
    }

    // Utility methods
    public boolean isAvailable() {
        return isActive && availableCopies > 0;
    }

    public boolean hasWaitingList() {
        return !borrowersQueue.isEmpty();
    }

    public double getAvailabilityPercentage() {
        if (totalCopies == 0) return 0.0;
        return (double) availableCopies / totalCopies * 100;
    }

    // Display methods
    public void displayBookInfo() {
        System.out.println("=== Book Information ===");
        System.out.println("ISBN: " + isbn);
        System.out.println("Title: " + title);
        System.out.println("Author: " + author);
        System.out.println("Genre: " + genre);
        System.out.println("Publisher: " + publisher);
        System.out.println("Publication Year: " + publicationYear);
        System.out.println("Total Copies: " + totalCopies);
        System.out.println("Available Copies: " + availableCopies);
        System.out.println("Borrowed Copies: " + getBorrowedCopiesCount());
        System.out.println("Availability: " + String.format("%.1f", getAvailabilityPercentage()) + "%");
        System.out.println("Queue Size: " + getQueueSize());
        System.out.println("Status: " + (isActive ? "Active" : "Inactive"));
    }

    public void displayQueue() {
        System.out.println("\n=== Waiting Queue for: " + title + " ===");
        if (borrowersQueue.isEmpty()) {
            System.out.println("No one in the waiting queue.");
        } else {
            int position = 1;
            for (String memberId : borrowersQueue) {
                System.out.println(position + ". Member ID: " + memberId);
                position++;
            }
        }
    }

    public void displayCurrentBorrowers() {
        System.out.println("\n=== Current Borrowers of: " + title + " ===");
        if (currentBorrowers.isEmpty()) {
            System.out.println("No current borrowers.");
        } else {
            for (String memberId : currentBorrowers) {
                System.out.println("- Member ID: " + memberId);
            }
        }
    }

    // String representation
    @Override
    public String toString() {
        return "Book{" +
                "ISBN='" + isbn + '\'' +
                ", Title='" + title + '\'' +
                ", Author='" + author + '\'' +
                ", Genre='" + genre + '\'' +
                ", Available=" + availableCopies + "/" + totalCopies +
                ", Queue=" + getQueueSize() +
                ", Status=" + (isActive ? "Active" : "Inactive") +
                '}';
    }

    // Equality based on ISBN
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return isbn.equals(book.isbn);
    }

    @Override
    public int hashCode() {
        return isbn.hashCode();
    }
}