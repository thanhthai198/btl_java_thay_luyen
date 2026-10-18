import java.util.ArrayList;
import java.util.Comparator;
import java.util.InputMismatchException;
import java.util.Locale;
import java.util.Scanner;

public class StudentController {

    public static final int INVALID_CHOICE = -1;

    private static final double MIN_GPA = 0.0;
    private static final double MAX_GPA = 4.0;

    private ArrayList<Student> studentList;
    private Scanner scanner;

    public StudentController() {
        this.studentList = new ArrayList<>();
        this.scanner = new Scanner(System.in);
        this.scanner.useLocale(Locale.US);
    }

    public int inputMenuChoice() {
        try {
            int choice = scanner.nextInt();
            scanner.nextLine();
            return choice;
        } catch (InputMismatchException ex) {
            scanner.nextLine();
            System.out.println("Lỗi định dạng: vui lòng nhập một số nguyên từ 1 đến 7.");
            return INVALID_CHOICE;
        }
    }

    public void addStudent() {
        System.out.println();
        System.out.println("----- THÊM MỚI SINH VIÊN -----");

        String id = readNonEmptyString("Nhập mã sinh viên: ");
        if (findStudentById(id) != null) {
            System.out.println("Thêm thất bại: mã sinh viên \"" + id + "\" đã tồn tại.");
            return;
        }

        String fullName = readNonEmptyString("Nhập họ và tên: ");
        String email = readNonEmptyString("Nhập email: ");
        String phone = readNonEmptyString("Nhập số điện thoại: ");
        double gpa = readGpa("Nhập điểm GPA (0.0 - 4.0, dùng dấu chấm): ");

        Student student = new Student(id, fullName, email, phone, gpa);
        studentList.add(student);

        System.out.println("Thêm sinh viên thành công.");
        printTableHeader();
        student.displayData();
    }

    public void printStudentList() {
        System.out.println();
        System.out.println("----- DANH SÁCH SINH VIÊN -----");

        if (studentList.isEmpty()) {
            System.out.println("Danh sách sinh viên đang trống.");
            return;
        }

        printTableHeader();
        for (Student student : studentList) {
            student.displayData();
        }
        System.out.println("Tổng số sinh viên: " + studentList.size());
    }

    public void searchStudentByName() {
        System.out.println();
        System.out.println("----- TÌM KIẾM SINH VIÊN THEO TÊN -----");

        if (studentList.isEmpty()) {
            System.out.println("Danh sách sinh viên đang trống.");
            return;
        }

        String keyword = readNonEmptyString("Nhập tên cần tìm: ");
        String keywordLower = keyword.toLowerCase(Locale.ROOT);
        ArrayList<Student> results = new ArrayList<>();

        for (Student student : studentList) {
            String nameLower = student.getFullName().toLowerCase(Locale.ROOT);
            if (nameLower.contains(keywordLower)) {
                results.add(student);
            }
        }

        if (results.isEmpty()) {
            System.out.println("Không tìm thấy sinh viên nào có tên chứa \"" + keyword + "\".");
            return;
        }

        System.out.println("Tìm thấy " + results.size() + " sinh viên:");
        printTableHeader();
        for (Student student : results) {
            student.displayData();
        }
    }

    public void updateStudentGpa() {
        System.out.println();
        System.out.println("----- CẬP NHẬT ĐIỂM GPA -----");

        if (studentList.isEmpty()) {
            System.out.println("Danh sách sinh viên đang trống.");
            return;
        }

        String id = readNonEmptyString("Nhập mã sinh viên cần cập nhật: ");
        Student student = findStudentById(id);
        if (student == null) {
            System.out.println("Không tìm thấy sinh viên có mã \"" + id + "\".");
            return;
        }

        System.out.println("Sinh viên hiện tại:");
        printTableHeader();
        student.displayData();

        double newGpa = readGpa("Nhập GPA mới (0.0 - 4.0, dùng dấu chấm): ");
        student.setGpa(newGpa);
        System.out.println("Cập nhật GPA thành công.");
        printTableHeader();
        student.displayData();
    }

    public void deleteStudent() {
        System.out.println();
        System.out.println("----- XÓA SINH VIÊN -----");

        if (studentList.isEmpty()) {
            System.out.println("Danh sách sinh viên đang trống.");
            return;
        }

        String id = readNonEmptyString("Nhập mã sinh viên cần xóa: ");
        Student student = findStudentById(id);
        if (student == null) {
            System.out.println("Không tìm thấy sinh viên có mã \"" + id + "\".");
            return;
        }

        studentList.remove(student);
        System.out.println("Đã xóa sinh viên \"" + student.getFullName() + "\" (mã " + student.getId() + ").");
    }

    public void sortStudentsByGpaDescending() {
        System.out.println();
        System.out.println("----- SẮP XẾP THEO GPA GIẢM DẦN -----");

        if (studentList.isEmpty()) {
            System.out.println("Danh sách sinh viên đang trống, không thể sắp xếp.");
            return;
        }

        studentList.sort(Comparator.comparingDouble(Student::getGpa).reversed());
        System.out.println("Đã sắp xếp danh sách sinh viên theo GPA giảm dần.");
        printStudentList();
    }

    private Student findStudentById(String id) {
        for (Student student : studentList) {
            if (student.getId().equalsIgnoreCase(id)) {
                return student;
            }
        }
        return null;
    }

    private void printTableHeader() {
        System.out.println(
                "----------------------------------------------------------------------------------------------------");
        System.out.printf("%-12s | %-24s | %-28s | %-14s | %4s%n",
                "Mã SV", "Họ và tên", "Email", "Số điện thoại", "GPA");
        System.out.println(
                "----------------------------------------------------------------------------------------------------");
    }

    private String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Dữ liệu không được để trống. Vui lòng nhập lại.");
        }
    }

    private double readGpa(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                double gpa = scanner.nextDouble();
                scanner.nextLine();
                if (gpa < MIN_GPA || gpa > MAX_GPA) {
                    System.out.println("GPA phải nằm trong khoảng 0.0 đến 4.0.");
                    continue;
                }
                return gpa;
            } catch (InputMismatchException ex) {
                scanner.nextLine();
                System.out.println("Lỗi định dạng: GPA phải là số thực, ví dụ 3.25.");
            }
        }
    }
}
