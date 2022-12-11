package cs;

public class Review {
    private String textMessage;
    private int rating;
    private Student student;
    private Course course;
    public Review(String text, int rating, Student student, Course course){
        this.textMessage = text;
        this.rating = rating;
        this.student = student;
        this.course = course;
    }
    public String getTextMessage(){
        return textMessage;
    }
    public int getRating(){
        return rating;
    }
    public Student getStudent(){
        return student;
    }
    public Course getCourse(){
        return course;
    }
    public void setTextMessage(String text){
        this.textMessage = text;
    }
    public void setRating(int rating){
        this.rating = rating;
    }
    public void setStudent(Student newStudent){
        this.student = newStudent;
    }
    public void setCourse(Course newCourse){
        this.course = newCourse;
    }
}
