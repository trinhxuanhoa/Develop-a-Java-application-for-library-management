package org.example;
import java.util.*;
public class LibraryManager {

        /*private ArrayList<Document> documents = new ArrayList<>();
        private ArrayList<User> users = new ArrayList<>();
        private Scanner scanner = new Scanner(System.in);

        public void addDocument() {
            System.out.print("Enter Document ID: ");
            String id = scanner.nextLine();
            System.out.print("Enter Title: ");
            String title = scanner.nextLine();
            System.out.print("Enter Author: ");
            String author = scanner.nextLine();
            System.out.print("Enter Quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine());

            documents.add(new Document(id, title, author, , quantity));
            System.out.println("Document added successfully!");
        }

        public void removeDocument() {
            System.out.print("Enter Document ID to remove: ");
            String id = scanner.nextLine();
            documents.removeIf(doc -> doc.getId().equals(id));
            System.out.println("Document removed successfully!");
        }

        public void displayDocuments() {
            if (documents.isEmpty()) {
                System.out.println("No documents available.");
            } else {
                documents.forEach(Document::printInfo);
            }
        }

        public void addUser() {
            System.out.print("Enter User ID: ");
            String userId = scanner.nextLine();
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();

            users.add(new User(userId, name));
            System.out.println("User added successfully!");
        }

        public void displayUsers() {
            if (users.isEmpty()) {
                System.out.println("No users available.");
            } else {
                users.forEach(User::displayInfo);
            }
        }

        public void borrowDocument() {
            System.out.print("Enter User ID: ");
            String userId = scanner.nextLine();
            System.out.print("Enter Document ID: ");
            String docId = scanner.nextLine();

            Document doc = findDocumentById(docId);
            if (doc != null && doc.getQuantity() > 0) {
                User user = findUserById(userId);
                if (user != null) {
                    user.borrowDocument(docId);
                    doc.setQuantity(doc.getQuantity() - 1);
                    System.out.println("Document borrowed successfully!");
                } else {
                    System.out.println("User not found.");
                }
            } else {
                System.out.println("Document not available.");
            }
        }

        public void returnDocument() {
            System.out.print("Enter User ID: ");
            String userId = scanner.nextLine();
            System.out.print("Enter Document ID: ");
            String docId = scanner.nextLine();

            User user = findUserById(userId);
            if (user != null) {
                user.returnDocument(docId);
                Document doc = findDocumentById(docId);
                if (doc != null) doc.setQuantity(doc.getQuantity() + 1);
                System.out.println("Document returned successfully!");
            } else {
                System.out.println("User not found.");
            }
        }

        private Document findDocumentById(String id) {
            return documents.stream().filter(doc -> doc.getId().equals(id)).findFirst().orElse(null);
        }

        private User findUserById(String id) {
            return users.stream().filter(user -> user.getUserId().equals(id)).findFirst().orElse(null);
        }

        public void run() {
            while (true) {
                System.out.println("\nWelcome to My Library!");
                System.out.println("[0] Exit");
                System.out.println("[1] Add Document");
                System.out.println("[2] Remove Document");
                System.out.println("[3] Display Documents");
                System.out.println("[4] Add User");
                System.out.println("[5] Display Users");
                System.out.println("[6] Borrow Document");
                System.out.println("[7] Return Document");

                System.out.print("Enter your choice: ");
                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 0: System.out.println("Exiting..."); return;
                    case 1: addDocument(); break;
                    case 2: removeDocument(); break;
                    case 3: displayDocuments(); break;
                    case 4: addUser(); break;
                    case 5: displayUsers(); break;
                    case 6: borrowDocument(); break;
                    case 7: returnDocument(); break;
                    default: System.out.println("Action is not supported.");
                }
            }
        }*/
    }


