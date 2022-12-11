package cs;
import java.util.*;
public class Student {
    private String loginName;
    private String password;
    private ArrayList<Review> listOfReviews;
    //creating new user constructor
    public Student(String loginName, String password){
        this.loginName = loginName;
        this.password = password;
        this.listOfReviews = new ArrayList<>();
    }
    //existing user constructor
    public Student(String loginName, String password, ArrayList<Review> listOfReviews){
        this.loginName = loginName;
        this.password = password;
        this.listOfReviews = listOfReviews;
    }
    public String getLoginName(){
        return loginName;
    }
    public String getPassword(){
        return password;
    }
    public ArrayList<Review> getListOfReviews(){
        return listOfReviews;
    }
    public void setLoginName(String loginName){
        this.loginName = loginName;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void addReview(Review r){
        listOfReviews.add(r);
    }
}
