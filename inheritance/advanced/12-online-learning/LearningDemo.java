public class LearningDemo {
    public static void main(String[] args) {
        System.out.println("🎓 ONLINE LEARNING PLATFORM 🎓");
        System.out.println("=" .repeat(50));
        
        Course[] courses = {
            new VideoCourse("VID001", "Java Programming", "Dr. Smith", 99.99, 20)
        };
        
        System.out.println("\n📋 COURSE INFORMATION:");
        for (Course course : courses) {
            System.out.println(course.getCourseInfo());
        }
        
        System.out.println("\n🎯 ENROLLMENT DEMONSTRATION:");
        for (Course course : courses) {
            course.enroll("STU001");
            System.out.println(course.getMaterials());
            System.out.println(course.getCourseFeatures());
        }
        
        System.out.println("\n✨ DEMONSTRATION COMPLETE! ✨");
    }
}
