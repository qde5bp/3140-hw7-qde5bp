package cs;
import java.util.*;
public class Course {
    private String departmentName;
    private String catalog_number;

    private ArrayList<Review> listOfReviews;
    public Course(String department, String catalog){
        this.departmentName = department.toUpperCase();
        this.catalog_number = catalog;
        this.listOfReviews = new ArrayList<Review>();
    }
    public String getDepartmentName(){
        return departmentName;
    }
    public String getCatalogNumber(){
        return catalog_number;
    }
    public void setDepartmentName(String dept){
        this.departmentName = dept;
    }
    public void setCatalogNumber(String newCatalog){
        this.catalog_number = newCatalog;
    }
    public String toString(){
        return departmentName + " " + catalog_number;
    }
    public ArrayList<Review> getCourseReviews(){
        return listOfReviews;
    }
    public void addReviewToCourse(Review review){
        listOfReviews.add(review);
    }
}
