public class check_JDK_version {
    public static void main(String[] args) {
        System.out.println("=== Java Runtime Information ===");
        System.out.println("Java Version: " + System.getProperty("java.version"));
        System.out.println("Java Vendor: " + System.getProperty("java.vendor"));
        System.out.println("Java Home: " + System.getProperty("java.home"));
        System.out.println("Java Runtime Version: " + System.getProperty("java.runtime.version"));
        System.out.println("Java VM Name: " + System.getProperty("java.vm.name"));
        System.out.println("Java VM Version: " + System.getProperty("java.vm.version"));
        System.out.println("Java VM Vendor: " + System.getProperty("java.vm.vendor"));
        System.out.println("Operating System: " + System.getProperty("os.name"));
        System.out.println("OS Version: " + System.getProperty("os.version"));
        System.out.println("OS Architecture: " + System.getProperty("os.arch"));
        System.out.println("User Name: " + System.getProperty("user.name"));
        System.out.println("User Home: " + System.getProperty("user.home"));
        System.out.println("Current Working Directory: " + System.getProperty("user.dir"));
        System.out.println("Java Class Path: " + System.getProperty("java.class.path"));
    }
}