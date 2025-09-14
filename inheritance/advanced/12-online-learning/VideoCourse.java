public class VideoCourse extends Course {
    private int videoCount;
    private int totalDuration;
    private String[] videos;
    
    public VideoCourse(String courseId, String title, String instructor, double price, int videoCount) {
        super(courseId, title, instructor, price);
        this.videoCount = videoCount;
        this.totalDuration = videoCount * 30; // 30 minutes per video
        this.videos = new String[videoCount];
    }
    
    @Override
    public boolean enroll(String studentId) {
        System.out.println("Student " + studentId + " enrolled in video course: " + title);
        return true;
    }
    
    @Override
    public String getMaterials() {
        return "Video Materials: " + videoCount + " videos, Total Duration: " + totalDuration + " minutes";
    }
    
    @Override
    public String getCourseFeatures() {
        return "Video Course Features: Videos: " + videoCount + ", Duration: " + totalDuration + 
               " min, Downloadable: Yes, Subtitles: Yes";
    }
}
