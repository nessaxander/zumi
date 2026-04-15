package at.htlkaindorf.backend.config;

import at.htlkaindorf.backend.pojos.Department;

import java.util.List;

public class SubjectMapping {

    public static final List<String> INFORMATIK_SUBJECTS = List.of(
            "RK",
            "D",
            "E",
            "GES",
            "GEO",
            "BSPU",
            "AM",
            "NW2C",
            "NW2P",
            "CABS",
            "POS",
            "DBI",
            "NSCS",
            "WMC",
            "DSAI",
            "BWMR",
            "BWMB",
            "SYP",
            "VERT",
            "SOPK"
    );

    public static final List<String> ROBOTIK_SUBJECTS = List.of(
            "RK",
            "D",
            "E",
            "GES",
            "GEO",
            "BSPU",
            "AM",
            "NW2C",
            "NW2P",
            "BWMR",
            "BWMB",
            "SYP",
            "SOPK"
    );

    public static final List<String> AUTOMATISIERUNG_SUBJECTS = List.of(
            "RK",
            "D",
            "E",
            "GES",
            "GEO",
            "BSPU",
            "AM",
            "NW2C",
            "NW2P",
            "BWMR",
            "BWMB",
            "SYP",
            "SOPK"
    );

    public static final List<String> MECHATRONIK_SUBJECTS = List.of(
            "RK",
            "D",
            "E",
            "GES",
            "GEO",
            "BSPU",
            "AM",
            "NW2C",
            "NW2P",
            "BWMR",
            "BWMB",
            "SYP",
            "SOPK"
    );

    private SubjectMapping() {
    }

    public static List<String> getSubjectsForDepartment(Department department) {
        return switch (department) {
            case INFORMATIK -> INFORMATIK_SUBJECTS;
            case ROBOTIK -> ROBOTIK_SUBJECTS;
            case AUTOMATISIERUNG -> AUTOMATISIERUNG_SUBJECTS;
            case MECHATRONIK -> MECHATRONIK_SUBJECTS;
        };
    }
}