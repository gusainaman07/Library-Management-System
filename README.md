# 📚 Library Management System

A Java-based application demonstrating core Data Structures and Algorithms (DSA) concepts with practical implementation.

![Java](https://img.shields.io/badge/Java-17-blue)
![DSA](https://img.shields.io/badge/Data_Structures-HashMap%2C_Queue%2C_LinkedList-green)

## 🚀 Features

- **Book Management** (Add/Remove/Search)
- **Member Management** (Register/Update)
- **Transaction Processing** (Borrow/Return)
- **Waiting Queue** for unavailable books
- **Fine Calculation** for overdue books

## 🛠 Data Structures Used

| Structure       | Application                          |
|----------------|--------------------------------------|
| `HashMap`      | O(1) lookup for books and members    |
| `Queue`        | FIFO processing of transactions      |
| `LinkedList`   | Managing waiting lists               |
| `Binary Tree`  | (Future) Sorting book catalog        |

## 📦 Project Structure
Library-Management-System/
├── src/
│ ├── Book.java # Book entity class
│ ├── Member.java # Member entity class
│ ├── Transaction.java # Transaction records
│ ├── Library.java # Core system logic
│ └── Main.java # Driver program
├── .gitignore
└── README.md