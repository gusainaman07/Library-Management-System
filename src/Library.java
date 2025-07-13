import java.util.*;

// Transaction class for queue management
class Transaction {
    private String type; // "BORROW" or "RETURN"
    private String memberId;
    private String isbn;
    private Date timestamp;
    
    public Transaction(String type, String memberId, String isbn) {
        this.type = type;
        this.memberId = memberId;
        this.isbn = isbn;
        this.timestamp = new Date();
    }
    
    // Getters
    public String getType() { return type; }
    public String getMemberId() { return memberId; }
    public String getIsbn() { return isbn; }
    public Date getTimestamp() { return timestamp; }
}

// Book class
class Book {
    private String isbn;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;
    private Queue<String> waitingQueue; // Queue of member IDs waiting for this book
    
    public Book(String isbn, String title, String author, int copies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCopies = copies;
        this.availableCopies = copies;
        this.waitingQueue = new LinkedList<>();
    }
    
    // Getters
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    public Queue<String> getWaitingQueue() { return waitingQueue; }
    
    // Methods for managing copies
    public void decreaseAvailableCopies() {
        if (availableCopies > 0) {
            availableCopies--;
        }
    }
    
    public void increaseAvailableCopies() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }
    
    // Queue management
    public void addToQueue(String memberId) {
        waitingQueue.offer(memberId);
    }
    
    public String getNextFromQueue() {
        return waitingQueue.poll();
    }
    
    public boolean hasWaitingMembers() {
        return !waitingQueue.isEmpty();
    }
}

// Member class
class Member {
    private String memberId;
    private String name;
    private String email;
    private Set<String> borrowedBooks; // Set of ISBNs
    private List<Book> borrowedBookObjects; // List of Book objects for easier access
    
    public Member(String memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.borrowedBooks = new HashSet<>();
        this.borrowedBookObjects = new ArrayList<>();
    }
    
    // Getters
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Set<String> getBorrowedBooks() { return borrowedBooks; }
    public List<Book> getBorrowedBookObjects() { return borrowedBookObjects; }
    
    // Borrow a book
    public void borrowBook(Book book) {
        borrowedBooks.add(book.getIsbn());
        borrowedBookObjects.add(book);
    }
    
    // Return a book
    public boolean returnBook(String isbn) {
        if (borrowedBooks.remove(isbn)) {
            borrowedBookObjects.removeIf(book -> book.getIsbn().equals(isbn));
            return true;
        }
        return false;
    }
    
    // Check if member has borrowed a specific book
    public boolean hasBorrowed(String isbn) {
        return borrowedBooks.contains(isbn);
    }
}

// Library class
public class Library {
    private HashMap<String, Book> books; // Key: ISBN, Value: Book
    private HashMap<String, Member> members; // Key: MemberID, Value: Member
    private Queue<Transaction> transactionQueue; // FIFO for borrow/return requests

    public Library() {
        books = new HashMap<>();
        members = new HashMap<>();
        transactionQueue = new LinkedList<>();
    }

    // Add a new book
    public void addBook(String isbn, String title, String author, int copies) {
        if (books.containsKey(isbn)) {
            System.out.println("Book with ISBN " + isbn + " already exists!");
            return;
        }
        Book newBook = new Book(isbn, title, author, copies);
        books.put(isbn, newBook);
        System.out.println("Book added successfully: " + title);
    }

    // Register a new member
    public void addMember(String memberId, String name, String email) {
        if (members.containsKey(memberId)) {
            System.out.println("Member with ID " + memberId + " already exists!");
            return;
        }
        Member newMember = new Member(memberId, name, email);
        members.put(memberId, newMember);
        System.out.println("Member registered successfully: " + name);
    }

    // Borrow a book
    public boolean borrowBook(String memberId, String isbn) {
        // Add transaction to queue
        transactionQueue.offer(new Transaction("BORROW", memberId, isbn));
        
        if (!members.containsKey(memberId)) {
            System.out.println("Member not found!");
            return false;
        }
        if (!books.containsKey(isbn)) {
            System.out.println("Book not found!");
            return false;
        }

        Book book = books.get(isbn);
        Member member = members.get(memberId);
        
        if (book.getAvailableCopies() > 0) {
            book.decreaseAvailableCopies();
            member.borrowBook(book);
            System.out.println("Book borrowed successfully!");
            return true;
        } else {
            book.addToQueue(memberId); // Add to waiting list
            System.out.println("Book not available. Added to waiting queue.");
            return false;
        }
    }

    // Return a book
    public boolean returnBook(String memberId, String isbn) {
        // Add transaction to queue
        transactionQueue.offer(new Transaction("RETURN", memberId, isbn));
        
        if (!members.containsKey(memberId)) {
            System.out.println("Member not found!");
            return false;
        }
        if (!books.containsKey(isbn)) {
            System.out.println("Book not found!");
            return false;
        }

        Book book = books.get(isbn);
        Member member = members.get(memberId);
        
        if (!member.hasBorrowed(isbn)) {
            System.out.println("Member has not borrowed this book!");
            return false;
        }
        
        book.increaseAvailableCopies();
        member.returnBook(isbn);
        
        // Check if anyone is waiting for this book
        if (book.hasWaitingMembers()) {
            String nextMemberId = book.getNextFromQueue();
            Member nextMember = members.get(nextMemberId);
            if (nextMember != null) {
                book.decreaseAvailableCopies();
                nextMember.borrowBook(book);
                System.out.println("Book automatically assigned to " + nextMember.getName() + " from waiting queue!");
            }
        }
        
        System.out.println("Book returned successfully!");
        return true;
    }

    // Search book by ISBN (O(1) due to HashMap)
    public Book searchBook(String isbn) {
        return books.get(isbn);
    }
    
    // Display all books
    public void displayAllBooks() {
        System.out.println("\n===== All Books =====");
        if (books.isEmpty()) {
            System.out.println("No books in the library.");
            return;
        }
        
        for (Book book : books.values()) {
            System.out.println("ISBN: " + book.getIsbn() + 
                             ", Title: " + book.getTitle() + 
                             ", Author: " + book.getAuthor() + 
                             ", Available: " + book.getAvailableCopies() + 
                             "/" + book.getTotalCopies());
        }
    }
    
    // Display all members
    public void displayAllMembers() {
        System.out.println("\n===== All Members =====");
        if (members.isEmpty()) {
            System.out.println("No members registered.");
            return;
        }
        
        for (Member member : members.values()) {
            System.out.println("ID: " + member.getMemberId() + 
                             ", Name: " + member.getName() + 
                             ", Email: " + member.getEmail() + 
                             ", Borrowed Books: " + member.getBorrowedBooks().size());
        }
    }
    
    // Display recent transactions
    public void displayRecentTransactions() {
        System.out.println("\n===== Recent Transactions =====");
        if (transactionQueue.isEmpty()) {
            System.out.println("No transactions recorded.");
            return;
        }
        
        for (Transaction transaction : transactionQueue) {
            System.out.println("Type: " + transaction.getType() + 
                             ", Member: " + transaction.getMemberId() + 
                             ", ISBN: " + transaction.getIsbn() + 
                             ", Time: " + transaction.getTimestamp());
        }
    }
}