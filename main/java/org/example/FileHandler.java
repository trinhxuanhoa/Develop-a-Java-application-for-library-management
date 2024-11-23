package org.example;
import java.io.*;
import java.util.ArrayList;

public class FileHandler {

        private final String fileName;

        // Constructor nhận tên tệp
        public FileHandler(String fileName) {
            this.fileName = fileName;
        }

        // Phương thức ghi tệp, thêm giá trị mới vào cuối tệp
        public void writeToFile(String value) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Dell\\IdeaProjects\\library\\src\\main\\text\\"+fileName, true))) {
                writer.write(value);
                writer.newLine(); // Xuống dòng sau mỗi giá trị
            } catch (IOException e) {
                System.out.println("Lỗi khi ghi tệp: " + e.getMessage());
            }
        }

        // Phương thức đọc tệp, trả về ArrayList<String>
        public ArrayList<String> readFromFile() {
            ArrayList<String> data = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\Dell\\IdeaProjects\\library\\src\\main\\text\\"+fileName))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    data.add(line);
                }
            } catch (IOException e) {
                System.out.println("Lỗi khi đọc tệp: " + e.getMessage());
            }
            return data;
        }

        // Phương thức xóa nội dung tệp (nếu cần)
        public void clearFile() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Dell\\IdeaProjects\\library\\src\\main\\text\\"+fileName))) {
                // Mở tệp với `new FileWriter` để xóa toàn bộ nội dung
            } catch (IOException e) {
                System.out.println("Lỗi khi xóa nội dung tệp: " + e.getMessage());
            }
        }

        // Phương thức kiểm tra nếu tệp tồn tại
        public boolean fileExists() {
            File file = new File("C:\\Users\\Dell\\IdeaProjects\\library\\src\\main\\text\\"+fileName);
            return file.exists();
        }
        }
