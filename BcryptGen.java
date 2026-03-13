import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptGen {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("password: " + encoder.encode("password"));
        System.out.println("2454: " + encoder.encode("2454"));
    }
}
