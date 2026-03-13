import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class UpdateLoginTheme {
    public static void main(String[] args) throws Exception {

        // 1. Index Page
        Path index = Paths.get("c:/Users/tharu/IdeaProjects/P2_Rev_Workforce/src/main/resources/templates/index.html");
        if (Files.exists(index)) {
            String content = Files.readString(index);
            content = content.replace(
                    "background: linear-gradient(135deg, rgba(79, 70, 229, 0.9) 0%, rgba(13, 148, 136, 0.9) 100%),",
                    "background: linear-gradient(135deg, rgba(224, 231, 255, 0.9) 0%, rgba(248, 250, 252, 0.9) 100%),");
            content = content.replace("color: white;\n            padding: 0 5%;",
                    "color: var(--text-dark);\n            padding: 0 5%;");
            content = content.replace(
                    "style=\"background: white; color: var(--primary); padding: 0 10px; border-radius: 8px;\"",
                    "style=\"background: var(--primary); color: white; padding: 0 10px; border-radius: 8px;\"");
            content = content.replace(
                    "style=\"background: rgba(16, 185, 129, 0.2); border: 1px solid rgba(16, 185, 129, 0.3); color: white;",
                    "style=\"background: #dcfce7; border: 1px solid #86efac; color: #166534;");
            Files.writeString(index, content);
            System.out.println("Updated index.html");
        }

        // 2. Login standard & Register
        String[] standardAuth = { "login.html", "register.html" };
        for (String f : standardAuth) {
            Path p = Paths.get("c:/Users/tharu/IdeaProjects/P2_Rev_Workforce/src/main/resources/templates/", f);
            if (Files.exists(p)) {
                String content = Files.readString(p);
                content = content.replace("background: linear-gradient(135deg, #4f46e5 0%, #0d9488 100%);",
                        "background: #f8fafc;");
                content = content.replace("color: white;", "color: var(--primary);");
                Files.writeString(p, content);
                System.out.println("Updated " + f);
            }
        }

        // 3. Login Role Pages
        String[][] rolePages = {
                { "login-manager.html", "linear-gradient(135deg, #0f172a 0%, #0369a1 100%)" },
                { "login-employee.html", "linear-gradient(135deg, #064e3b 0%, #166534 100%)" },
                { "login-admin.html", "linear-gradient(135deg, #451212 0%, #1e1e1e 100%)" }
        };

        for (String[] role : rolePages) {
            Path p = Paths.get("c:/Users/tharu/IdeaProjects/P2_Rev_Workforce/src/main/resources/templates/", role[0]);
            if (Files.exists(p)) {
                String content = Files.readString(p);
                content = content.replace(role[1], "#f8fafc"); // Light solid background

                // Form card style
                content = content.replace(
                        "background: rgba(255, 255, 255, 0.07); backdrop-filter: blur(25px); -webkit-backdrop-filter: blur(25px); border-radius: 24px; padding: 3rem; border: 1px solid rgba(255, 255, 255, 0.2); box-shadow: 0 30px 60px rgba(0, 0, 0, 0.4);",
                        "background: white; border-radius: 24px; padding: 3rem; border: 1px solid #e2e8f0; box-shadow: 0 10px 25px rgba(0, 0, 0, 0.05);");

                // Text colors
                content = content.replace("color: white;", "color: #334155;");
                content = content.replace("color: rgba(255, 255, 255, 0.7);", "color: #64748b;");

                // Inputs
                content = content.replace(
                        "background: rgba(255, 255, 255, 0.1); border: 1px solid rgba(255, 255, 255, 0.2); border-radius: 12px; color: white;",
                        "background: #f1f5f9; border: 1px solid #cbd5e1; border-radius: 12px; color: #0f172a;");

                // Buttons keep their original primary color but text might have been replaced
                // to dark, fixing that
                content = content.replace(
                        "border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 2rem; margin: 0 auto 1.5rem; box-shadow: 0 10px 25px rgba(14, 165, 233, 0.4);",
                        "color: white; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 2rem; margin: 0 auto 1.5rem; box-shadow: 0 10px 25px rgba(0, 0, 0, 0.1);");
                content = content.replace("border-radius: 50px; color: #334155; font-weight: 700;",
                        "border-radius: 50px; color: white; font-weight: 700;");

                // Error message
                content = content.replace(
                        "background: rgba(239, 68, 68, 0.2); border: 1px solid rgba(239, 68, 68, 0.3); color: #334155;",
                        "background: #fef2f2; border: 1px solid #fecaca; color: #b91c1c;");

                // Back button
                content = content.replace("color: #334155; text-decoration: none; font-size: 0.9rem; opacity: 0.6;",
                        "color: #64748b; text-decoration: none; font-size: 0.9rem; font-weight: 500;");

                Files.writeString(p, content);
                System.out.println("Updated " + role[0]);
            }
        }
    }
}
