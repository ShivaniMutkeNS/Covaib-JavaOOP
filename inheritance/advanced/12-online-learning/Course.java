public abstract class Course {
    protected String courseId;
    protected String title;
    protected String instructor;
    protected double price;
    protected String status;
    
    public Course(String courseId, String title, String instructor, double price) {
        this.courseId = courseId;
        this.title = title;
        this.instructor = instructor;
        this.price = price;
        this.status = "Available";
    }
    
    public abstract boolean enroll(String studentId);
    public abstract String getMaterials();
    public abstract String getCourseFeatures();
    
    public String getCourseInfo() {
        return String.format("Course: %s, Title: %s, Instructor: %s, Price: $%.2f, Status: %s", 
                           courseId, title, instructor, price, status);
    }
}
