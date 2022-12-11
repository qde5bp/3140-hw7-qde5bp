package cs;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private Scanner scanner;
    private static ArrayList<Student> students;
    private static ArrayList<Course> courses;
    private static ArrayList<Review> reviews;

    private static DatabaseManagerImpl database;
    public static void main(String[] args) {
        //System.out.println("Make sure you updated the build.gradle " + "file to point to the right main class");
        students = new ArrayList<Student>();
        courses = new ArrayList<Course>();
        reviews = new ArrayList<Review>();

        //Create empty database/////////////////////////////////////////////////////////////////////////////////////////
        database = new DatabaseManagerImpl();
        database.connect();
        database.createTables();
        //Starter Data//////////////////////////////////////////////////////////////////////////////////////////////////
        Student bob = new Student("bob", "bobPassword");
        database.addStudentToDatabase(bob);
        students.add(bob);

        Student bill = new Student("bill", "billPassword");
        database.addStudentToDatabase(bill);
        students.add(bill);

        Course cs2100 = new Course("CS", "2100");
        database.addCourseToDataBase(cs2100);
        courses.add(cs2100);

        Course apma3100 = new Course("APMA", "3100");
        database.addCourseToDataBase(apma3100);
        courses.add(apma3100);

        Review cs2100Review = new Review("good class", 5, bob, cs2100);
        database.addReviewToDataBase(cs2100Review);
        reviews.add(cs2100Review);
        cs2100.addReviewToCourse(cs2100Review);

        Review apma3100Review = new Review("hard class", 2, bob, apma3100);
        database.addReviewToDataBase(apma3100Review);
        reviews.add(apma3100Review);
        apma3100.addReviewToCourse(apma3100Review);


        Review apma3100Review2 = new Review("okay class", 4, bill, apma3100);
        database.addReviewToDataBase(apma3100Review2);
        reviews.add(apma3100Review2);
        apma3100.addReviewToCourse(apma3100Review2);
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Main main = new Main();
        main.run();
    }

    public void run(){
        System.out.println("Welcome to Course Review.");
        initializeScanner();
        login();
    }
    private void initializeScanner() {
        scanner = new Scanner(System.in);
    }
    private void login(){ //this method makes sure a proper input is used
        System.out.println("--------------------------");
        boolean valid = false;
        while (! valid) {
            try {
                String loginOrCreateUser = prompt("LOGIN OPTIONS: \n (1) Login-to existing user \n (2) Create new user");
                if(loginOrCreateUser.equals("1")){
                    valid = true;
                    loginToExistingUser();
                }
                if(loginOrCreateUser.equals("2")){
                    valid = true;
                    createNewUser();
                }
            } catch (Exception e) {
                String loginOrCreateUser = prompt("LOGIN OPTIONS: \n (1) Login-to existing user \n (2) Create new user");
                scanner.next();
            }
        }
    }
    private void loginToExistingUser(){
        boolean val = true;
        Student student = null;
        while(val){
            System.out.println("--------------------------");
            System.out.println("LOGIN TO EXISTING USER");
            System.out.println("Enter login name: ");
            String name = scanner.nextLine();
            System.out.println("Enter password: ");
            String password = scanner.nextLine();
            for(Student s: students){
                if(s.getLoginName().equals(name) && s.getPassword().equals(password)){
                    val = false;
                    student = s;
                }
            }
        }
        mainMenu(student);
    }
    private void createNewUser(){ //need to write methods in DatabaseManagerImpl.java that put login info in database
        System.out.println("--------------------------");
        System.out.println("CREATE NEW USER");
        System.out.println("Enter login name: ");
        String name = scanner.nextLine();
        System.out.println("Enter your new password: ");
        String password = scanner.nextLine();
        System.out.println("Confirm your password: ");
        String confirm = scanner.nextLine();
        if(password.equals(confirm)){
            Student s = new Student(name, password);
            database.addStudentToDatabase(s);
            students.add(s);
            loginToExistingUser();
        }
        else{
            login();
        }

    }
    private void mainMenu(Student student){
        System.out.println("--------------------------");
        System.out.println("Welcome to the Main Menu, " + student.getLoginName());

        boolean valid = false;
        while (!valid) {
            try {
                String option = prompt("MAIN MENU OPTIONS: \n (1) Submit review for a course \n (2) See reviews for a course \n (3) Log out");
                valid = false;
                if(option.equals("1")){
                    submitReview(student);
                    valid = true;
                }
                if(option.equals("2")){
                    seeReview(student);
                    valid = true;
                }
                if(option.equals("3")){
                    login();
                    valid = true;
                }
            } catch (Exception e) {
                String option1 = prompt("MAIN MENU OPTIONS: \n (1) Submit review for a course \n (2) See reviews for a course \n (3) Log out");
                scanner.next();
            }
        }

    }
    private void submitReview(Student student){
        System.out.println("--------------------------");
        System.out.println("SUBMIT REVIEW");
        System.out.println("Course name:");
        String course = scanner.nextLine();
        course = course.toUpperCase();
        Course c = null;
        if(courseExists(course)){
            for(Course course1: courses){
                if(course1.toString().equals(course)){
                   c = course1;
                }
            }
        }
        if(isValidCourse(course) && reviewOnlyOnce(course, student)){
            if(!courseExists(course)){
                String[] arr = course.split(" ");
                String dept = arr[0];
                String catalogNum = arr[1];
                c = new Course(dept, catalogNum);
                courses.add(c);
                database.addCourseToDataBase(c);
            }
            System.out.println("Enter your review message:");
            String message = scanner.nextLine();
            System.out.println("Enter your review for the course:");
            int review = scanner.nextInt();
            while(review < 0 || review > 5){
                System.out.println("Enter your review for the course:");
                review = scanner.nextInt();
            }

            Review newReview = new Review(message, review, student, c);
            student.addReview(newReview);
            database.addReviewToDataBase(newReview);

            String[] splitted = course.split(" ");
            String departmentNameInput = splitted[0];
            String catalogInput = splitted[1];
            for(Course temp: courses) {
                if (temp.getDepartmentName().equals(departmentNameInput) && temp.getCatalogNumber().equals(catalogInput)) { //we have found the course
                    temp.addReviewToCourse(newReview);
                }
            }
        }
        else if(!reviewOnlyOnce(course, student)){
            System.out.println("Course has already been reviewed.");
        }
        else{
            System.out.println("Invalid course name.");
        }
        mainMenu(student);
    }
    private boolean courseExists(String course){
        for(Course course1: courses){
            if(course1.toString().equals(course)){
                return true;
            }
        }
        return false;
    }
    private boolean isValidCourse(String course){
       String[] arr = course.split(" ") ;
       if(arr.length != 2){
           return false;
       }
       else{
           String dept = arr[0];
           String catalogNum = arr[1];
           if(dept.length() <= 4){
                if(catalogNum.length() == 4){
                    for(int i = 0;i<catalogNum.length();i++){
                        if(catalogNum.charAt(i) < '0' || catalogNum.charAt(i) > '9'){
                            return false;
                        }
                    }
                    return true;
                }
                return false;
           }
           else{
               return false;
           }
       }
    }
    private boolean reviewOnlyOnce(String courseString, Student student){

        String[] splitted = courseString.split(" ");
        String departmentNameInput = splitted[0];
        String catalogInput = splitted[1];
        for(Course myCourse: courses) {
            if (myCourse.getDepartmentName().equals(departmentNameInput) && myCourse.getCatalogNumber().equals(catalogInput)) { //we have found the course
                if(myCourse.getCourseReviews().size() == 0) return true; //course has not been reviewed yet
                else{
                    for(Review review: myCourse.getCourseReviews()){
                        if(review.getStudent().equals(student)){
                            return false; //we have found a course review associated with that student
                        }
                    }
                }

            }
        }
        return true;

    }
    private void seeReview(Student student){
        System.out.println("--------------------------");
        System.out.println("SEE REVIEWS");
        String input = prompt("Course name: ");
        input = input.toUpperCase();
        if(isValidCourse(input)){
            String[] splitted = input.split(" ");
            String departmentNameInput = splitted[0];
            String catalogInput = splitted[1];
            //Seeing if this course exists
            boolean foundCourse = false;
            for(Course c: courses){
                if(c.getDepartmentName().equals(departmentNameInput) && c.getCatalogNumber().equals(catalogInput)){ //we have found the course
                    foundCourse = true;
                    if(!c.getCourseReviews().isEmpty()){
                        double reviewCount = 0;
                        for(Review r: c.getCourseReviews()){
                            System.out.println(r.getTextMessage());
                            reviewCount+=r.getRating();
                        }
                        double average = reviewCount / c.getCourseReviews().size();
                        System.out.println("Course Average [" + (average) + "]/5");
                    }

                }

            }
            if(foundCourse == false){  //condition where the course is valid format but has no reviews, isn't an actual "course" object that would be created when a review is made
                System.out.println("Course has no reviews.");
            }
        }
        else{
            System.out.println("Invalid course name.");
        }
        mainMenu(student);
    }
    private String prompt(String s) {
        System.out.println(s);
        return scanner.nextLine();
    }

}
