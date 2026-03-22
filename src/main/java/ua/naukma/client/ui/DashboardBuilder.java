package ua.naukma.client.ui;

import ua.naukma.domain.*;

public class DashboardBuilder {
    private static final String APP_NAME = "Digital University Service";
    private static final String APP_ALIAS = "Ivan Bozhenko, Ivan Lastivka, Bondarenko Hordiy";
    private static final String VERSION = "1.0.1";

    public static String buildMONPanel() {
        return String.format(
                "\uD83D\uDCF1 DigiUni: %s\n" +
                        "📌 Created by: %s\n" +
                        "🛡️ Version: %s",
                APP_NAME, APP_ALIAS, VERSION
        );
    }


    public static String buildUniversityPanel(University u) {
        return String.format(
                "🏢 University: %s\n" +
                        "📍 Location:   %s, %s\n" +
                        "🆔 ID:         %d",
                u.getShortName(), u.getCity(), u.getAddress(), u.getId());
    }

    public static String buildFacultyPanel(Faculty f) {
        return String.format(
                "📚 Faculty: %s\n" +
                        "👨‍🏫 Dean:    %s\n" +
                        "🆔 ID:      %d",
                f.getShortName(), f.getDean() != null ? f.getDean() : "Not assigned", f.getId());
    }

    public static String buildDepartmentPanel(Department d) {
        return String.format(
                "🔬 Department: %s\n" +
                        "📍 Location:   %s\n" +
                        "🆔 ID:         %d",
                d.getName(), d.getLocation(), d.getId());
    }

    public static String buildGroupPanel(Group g, int studentsCount) {
        return String.format(
                "🎓 Group:         %s\n" +
                        "🆔 ID:            %d\n" +
                        "🧑‍🎓 Students count: %d",
                g.getName(), g.getId(), studentsCount);
    }
}