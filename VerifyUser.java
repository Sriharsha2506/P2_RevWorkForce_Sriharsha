import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class VerifyUser {
    public static void main(String[] args) {
        try {
            Class.forName("org.h2.Driver");
            Connection conn = DriverManager.getConnection("jdbc:h2:tcp://localhost:9092/mem:workforce", "system",
                    "2454");
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT email, password, role FROM users WHERE email='harsha@rev.com'");
            while (rs.next()) {
                System.out.println("User {" + rs.getString("email") + "} Password: [" + rs.getString("password")
                        + "] Role: [" + rs.getString("role") + "]");
            }
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
