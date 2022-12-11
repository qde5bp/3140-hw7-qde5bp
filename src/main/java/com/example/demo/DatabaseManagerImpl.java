package cs;

import java.sql.*;


public class DatabaseManagerImpl{

    Connection connection;
    public static final String STATE_DATABASE_PATH = "Reviews.sqlite3";

    public void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" +
                    STATE_DATABASE_PATH);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect() {
        try{
            connection.close();
        }
        catch(SQLException e){
            throw new RuntimeException(e);
        }
    }

    public void createTables(){
        try{
            if (connection.isClosed()) {
                throw new IllegalStateException();
            }
            else{
                String sql = """
                CREATE TABLE Students ( 
                ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, 
                LoginName varchar(100) NOT NULL, 
                Password varchar(100) NOT NULL);
                
                CREATE TABLE Courses (
                ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
                Department varchar(100) NOT NULL, 
                Catalog_Number varchar(100) NOT NULL);
                
                CREATE TABLE Reviews (
                ID INTEGER NOT NULL PRIMARY KEY,
                Student varchar(100) NOT NULL, 
                Course varchar(100) NOT NULL, 
                TextMessage varchar (100) NOT NULL, 
                Rating int NOT NULL,
                FOREIGN KEY(Student)  references Students(ID) ON DELETE CASCADE,
                FOREIGN KEY(Course) references Courses(ID) ON DELETE CASCADE);
                """;
                Statement statement = connection.createStatement();
                statement.executeUpdate(sql);
            }
        }
        catch(SQLException e){
            throw new RuntimeException("SQL statements couldn't be executed");
        }
    }
    public void addStudentToDatabase(Student student){
        try{
            if(connection.isClosed()){
                throw new IllegalStateException("Connection is closed already");
            }
            String studentLoginName = student.getLoginName();
            String studentPassword = student.getPassword();
            String insertQuery = String.format("""
                    insert into Students(LoginName, Password) values("%s", "%s");""",
                    studentLoginName, studentPassword);
            Statement statement = connection.createStatement();
            statement.executeUpdate(insertQuery);
        }
        catch(SQLException e){
            throw new RuntimeException("SQL statements couldn't be executed");
        }
    }

    public void addCourseToDataBase(Course course){
        try{
            if(connection.isClosed()){
                throw new IllegalStateException("Connection is closed already");
            }
            String courseDepartment = course.getDepartmentName();
            String courseCatalogNumber = course.getCatalogNumber();
            String insertQuery = String.format("""
                    insert into Courses(Department, Catalog_Number) values("%s", "%s");""",
                    courseDepartment, courseCatalogNumber);
            Statement statement = connection.createStatement();
            statement.executeUpdate(insertQuery);
        }
        catch(SQLException e){
            throw new RuntimeException("SQL statements couldn't be executed");
        }
    }

    public void addReviewToDataBase(Review review){
        try{
            if(connection.isClosed()){
                throw new IllegalStateException("Connection is closed already");
            }
            Student student = review.getStudent();
            String insertQuery = String.format("""
                    insert into Reviews(Student, Course, TextMessage, Rating) values("%s", "%s", "%s", %d);""",
                    student.getLoginName(), review.getCourse().toString(),
                    review.getTextMessage(), review.getRating());
            Statement statement = connection.createStatement();
            statement.executeUpdate(insertQuery);
        }
        catch(SQLException e){
            throw new RuntimeException("SQL statements couldn't be executed");
        }
    }

}
