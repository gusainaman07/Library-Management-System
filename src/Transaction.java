import java.util.Date;
import java.util.UUID;

public class Transaction {
    private String transactionId;
    private String memberId;
    private String bookIsbn;
    private Date issueDate;
    private Date dueDate;
    private Date returnDate;
    private double fine;
    private boolean isReturned;

    public Transaction(String memberId, String bookIsbn, Date issueDate, Date dueDate) {
        this.transactionId = UUID.randomUUID().toString(); // Auto-generate unique ID
        this.memberId = memberId;
        this.bookIsbn = bookIsbn;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = null;
        this.fine = 0.0;
        this.isReturned = false;
    }

    // Alternative constructor with custom transaction ID
    public Transaction(String transactionId, String memberId, String bookIsbn, Date issueDate, Date dueDate) {
        this.transactionId = transactionId;
        this.memberId = memberId;
        this.bookIsbn = bookIsbn;
        this.issueDate = issueDate;
        this.dueDate = dueDate;
        this.returnDate = null;
        this.fine = 0.0;
        this.isReturned = false;
    }

    // Calculate fine based on return date
    public void calculateFine(Date returnDate) {
        if (returnDate.after(dueDate)) {
            long diff = returnDate.getTime() - dueDate.getTime();
            long daysLate = diff / (24 * 60 * 60 * 1000);
            this.fine = daysLate * 5.0; // ₹5 per day fine
        } else {
            this.fine = 0.0; // No fine if returned on time
        }
    }

    // Mark book as returned
    public void markAsReturned(Date returnDate) {
        this.returnDate = returnDate;
        this.isReturned = true;
        calculateFine(returnDate);
    }

    // Check if book is overdue
    public boolean isOverdue() {
        if (isReturned) {
            return false; // Can't be overdue if already returned
        }
        return new Date().after(dueDate);
    }

    // Get days until due (negative if overdue)
    public long getDaysUntilDue() {
        Date today = new Date();
        long diff = dueDate.getTime() - today.getTime();
        return diff / (24 * 60 * 60 * 1000);
    }

    // Get days overdue (0 if not overdue)
    public long getDaysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        Date today = new Date();
        long diff = today.getTime() - dueDate.getTime();
        return diff / (24 * 60 * 60 * 1000);
    }

    // Calculate current fine (for books not yet returned)
    public double getCurrentFine() {
        if (isReturned) {
            return fine;
        }
        if (isOverdue()) {
            long daysOverdue = getDaysOverdue();
            return daysOverdue * 5.0;
        }
        return 0.0;
    }

    // Getters
    public String getTransactionId() { return transactionId; }
    public String getMemberId() { return memberId; }
    public String getBookIsbn() { return bookIsbn; }
    public Date getIssueDate() { return issueDate; }
    public Date getDueDate() { return dueDate; }
    public Date getReturnDate() { return returnDate; }
    public double getFine() { return fine; }
    public boolean isReturned() { return isReturned; }

    // Setters (if needed)
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public void setFine(double fine) { this.fine = fine; }

    // String representation for easy printing
    @Override
    public String toString() {
        return "Transaction{" +
                "ID='" + transactionId + '\'' +
                ", Member='" + memberId + '\'' +
                ", Book='" + bookIsbn + '\'' +
                ", Issued=" + issueDate +
                ", Due=" + dueDate +
                ", Returned=" + (returnDate != null ? returnDate : "Not returned") +
                ", Fine=₹" + fine +
                ", Status=" + (isReturned ? "Returned" : "Active") +
                '}';
    }

    // Check if transactions are equal (based on transaction ID)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Transaction that = (Transaction) obj;
        return transactionId.equals(that.transactionId);
    }

    @Override
    public int hashCode() {
        return transactionId.hashCode();
    }
}