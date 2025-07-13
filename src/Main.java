import java.util.*;

// Book class
class Book {
    private String isbn;
    private String title;
    private String author;
    private int totalCopies;
    private int availableCopies;
    
    public Book(String isbn, String title, String author, int copies) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.totalCopies = copies;
        this.availableCopies = copies;
    }
    
    // Getters
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getTotalCopies() { return totalCopies; }
    public int getAvailableCopies() { return availableCopies; }
    
    // Methods for borrowing and returning
    public boolean borrowCopy() {
        if (availableCopies > 0) {
            availableCopies--;
            return true;
        }
        return false;
    }
    
    public void returnCopy() {
        if (availableCopies < totalCopies) {
            availableCopies++;
        }
    }
    
    public boolean isAvailable() {
        return availableCopies > 0;
    }
}

// Member class
class Member {
    private String memberId;
    private String name;
    private String email;
    private List<String> borrowedBooks;
    
    public Member(String memberId, String name, String email) {
        this.memberId = memberId;
        this.name = name;
        this.email = email;
        this.borrowedBooks = new ArrayList<>();
    }
    
    // Getters
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public List<String> getBorrowedBooks() { return borrowedBooks; }
    
    // Methods for borrowing and returning
    public void borrowBook(String isbn) {
        borrowedBooks.add(isbn);
    }
    
    public boolean returnBook(String isbn) {
        return borrowedBooks.remove(isbn);
    }
    
    public boolean hasBorrowed(String isbn) {
        return borrowedBooks.contains(isbn);
    }
}

// Library class
class Library {
    private Map<String, Book> books;
    private Map<String, Member> members;
    private Map<String, Queue<String>> waitingList; // ISBN -> Queue of member IDs
    
    public Library() {
        books = new HashMap<>();
        members = new HashMap<>();
        waitingList = new HashMap<>();
    }
    
    // Add a book to the library
    public void addBook(String isbn, String title, String author, int copies) {
        if (books.containsKey(isbn)) {
            System.out.println("Book already exists! Use update functionality if needed.");
        } else {
            books.put(isbn, new Book(isbn, title, author, copies));
            System.out.println("Book added successfully!");
        }
    }
    
    // Add a member to the library
    public void addMember(String memberId, String name, String email) {
        if (members.containsKey(memberId)) {
            System.out.println("Member already exists!");
        } else {
            members.put(memberId, new Member(memberId, name, email));
            System.out.println("Member added successfully!");
        }
    }
    
    // Search for a book by ISBN
    public Book searchBook(String isbn) {
        return books.get(isbn);
    }
    
    // Borrow a book
    public boolean borrowBook(String memberId, String isbn) {
        Member member = members.get(memberId);
        Book book = books.get(isbn);
        
        if (member == null) {
            System.out.println("Member not found!");
            return false;
        }
        
        if (book == null) {
            System.out.println("Book not found!");
            return false;
        }
        
        if (book.borrowCopy()) {
            member.borrowBook(isbn);
            return true;
        } else {
            // Add to waiting list
            waitingList.computeIfAbsent(isbn, k -> new LinkedList<>()).offer(memberId);
            return false;
        }
    }
    
    // Return a book
    public boolean returnBook(String memberId, String isbn) {
        Member member = members.get(memberId);
        Book book = books.get(isbn);
        
        if (member == null || book == null) {
            return false;
        }
        
        if (member.hasBorrowed(isbn)) {
            member.returnBook(isbn);
            book.returnCopy();
            
            // Check waiting list
            Queue<String> waitingMembers = waitingList.get(isbn);
            if (waitingMembers != null && !waitingMembers.isEmpty()) {
                String nextMemberId = waitingMembers.poll();
                Member nextMember = members.get(nextMemberId);
                if (nextMember != null) {
                    book.borrowCopy();
                    nextMember.borrowBook(isbn);
                    System.out.println("Book automatically assigned to " + nextMember.getName() + " from waiting list!");
                }
            }
            
            return true;
        }
        
        return false;
    }
}

// Main class
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Library library = new Library();
        
        while (true) {
            System.out.println("\n===== Library Management System =====");
            System.out.println("1. Add Book");
            System.out.println("2. Add Member");
            System.out.println("3. Borrow Book");
            System.out.println("4. Return Book");
            System.out.println("5. Search Book");
            System.out.println("6. Exit");
            System.out.print("Enter choice: ");
            
            int choice = sc.nextInt();
            sc.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter ISBN: ");
                    String isbn = sc.nextLine();
                    System.out.print("Enter Title: ");
                    String title = sc.nextLine();
                    System.out.print("Enter Author: ");
                    String author = sc.nextLine();
                    System.out.print("Enter Copies: ");
                    int copies = sc.nextInt();
                    library.addBook(isbn, title, author, copies);
                    break;

                case 2:
                    System.out.print("Enter Member ID: ");
                    String memberId = sc.nextLine();
                    System.out.print("Enter Name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter Email: ");
                    String email = sc.nextLine();
                    library.addMember(memberId, name, email);
                    break;

                case 3:
                    System.out.print("Enter Member ID: ");
                    memberId = sc.nextLine();
                    System.out.print("Enter ISBN: ");
                    isbn = sc.nextLine();
                    if (library.borrowBook(memberId, isbn)) {
                        System.out.println("Book borrowed successfully!");
                    } else {
                        System.out.println("Book not available. Added to waiting list.");
                    }
                    break;

                case 4:
                    System.out.print("Enter Member ID: ");
                    memberId = sc.nextLine();
                    System.out.print("Enter ISBN: ");
                    isbn = sc.nextLine();
                    if (library.returnBook(memberId, isbn)) {
                        System.out.println("Book returned successfully!");
                    } else {
                        System.out.println("Invalid member or book.");
                    }
                    break;

                case 5:
                    System.out.print("Enter ISBN: ");
                    isbn = sc.nextLine();
                    Book book = library.searchBook(isbn);
                    if (book != null) {
                        System.out.println("Book found: " + book.getTitle() + 
                                         " by " + book.getAuthor() +
                                         " (" + book.getAvailableCopies() + 
                                         "/" + book.getTotalCopies() + " available)");
                    } else {
                        System.out.println("Book not found!");
                    }
                    break;

                case 6:
                    System.out.println("Exiting system...");
                    sc.close();
                    System.exit(0);

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}