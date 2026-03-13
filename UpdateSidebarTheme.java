import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class UpdateSidebarTheme {
    public static void main(String[] args) throws Exception {
        List<String> files = Arrays.asList(
                "src/main/resources/templates/admin/reports/leaves.html",
                "src/main/resources/templates/admin/reports/employees.html",
                "src/main/resources/templates/admin/leaves/types.html",
                "src/main/resources/templates/admin/leaves/holidays.html",
                "src/main/resources/templates/admin/leaves/balances.html",
                "src/main/resources/templates/admin/employee-form.html");

        String rootTarget = ":root {\n" +
                "            --primary: #6366f1;\n" +
                "            --primary-hover: #4f46e5;\n" +
                "            --bg: #f8fafc;\n" +
                "            --sidebar-bg: #0f172a;\n" +
                "            --card-bg: #ffffff;\n" +
                "            --text: #1e293b;\n" +
                "            --text-muted: #64748b;\n" +
                "            --border: #e2e8f0;\n" +
                "        }";

        String rootReplacement = ":root {\n" +
                "            --primary: #818cf8;\n" +
                "            --primary-hover: #6366f1;\n" +
                "            --bg: #f8fafc;\n" +
                "            --sidebar-bg: #ffffff;\n" +
                "            --sidebar-border: #e2e8f0;\n" +
                "            --card-bg: #ffffff;\n" +
                "            --text: #334155;\n" +
                "            --text-muted: #64748b;\n" +
                "            --border: #e2e8f0;\n" +
                "        }";

        String sidebarTarget = ".sidebar {\n" +
                "            width: 260px;\n" +
                "            background-color: var(--sidebar-bg);\n" +
                "            color: white;\n" +
                "            display: flex;\n" +
                "            flex-direction: column;\n" +
                "            padding: 2rem 1.5rem;\n" +
                "            position: fixed;\n" +
                "            height: 100vh;\n" +
                "        }";

        String sidebarReplacement = ".sidebar {\n" +
                "            width: 260px;\n" +
                "            background-color: var(--sidebar-bg);\n" +
                "            color: var(--text);\n" +
                "            border-right: 1px solid var(--sidebar-border);\n" +
                "            display: flex;\n" +
                "            flex-direction: column;\n" +
                "            padding: 2rem 1.5rem;\n" +
                "            position: fixed;\n" +
                "            height: 100vh;\n" +
                "        }";

        String navItemTarget = ".nav-item {\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            padding: 0.75rem 1rem;\n" +
                "            color: #94a3b8;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 0.5rem;\n" +
                "            margin-bottom: 0.5rem;\n" +
                "            transition: all 0.2s;\n" +
                "        }";

        String navItemReplacement = ".nav-item {\n" +
                "            display: flex;\n" +
                "            align-items: center;\n" +
                "            padding: 0.75rem 1rem;\n" +
                "            color: var(--text-muted);\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 0.5rem;\n" +
                "            margin-bottom: 0.5rem;\n" +
                "            transition: all 0.2s;\n" +
                "        }";

        String navHoverTarget = ".nav-item:hover,\n" +
                "        .nav-item.active {\n" +
                "            color: white;\n" +
                "            background-color: rgba(255, 255, 255, 0.1);\n" +
                "        }";

        String navHoverReplacement = ".nav-item:hover,\n" +
                "        .nav-item.active {\n" +
                "            color: var(--primary);\n" +
                "            background-color: #e0e7ff;\n" +
                "        }";

        for (String file : files) {
            Path path = Paths.get("c:/Users/tharu/IdeaProjects/P2_Rev_Workforce", file);
            if (Files.exists(path)) {
                String content = Files.readString(path);
                content = content.replace(rootTarget, rootReplacement);
                content = content.replace(sidebarTarget, sidebarReplacement);
                content = content.replace(navItemTarget, navItemReplacement);
                content = content.replace(navHoverTarget, navHoverReplacement);
                Files.writeString(path, content);
                System.out.println("Updated " + file);
            } else {
                System.out.println("Could not find " + file);
            }
        }
    }
}
