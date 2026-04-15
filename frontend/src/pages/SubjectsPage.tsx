import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import Navbar from "../components/Navbar";
import DashboardSidebar from "../components/DashboardSidebar";

type LoggedInStudent = {
    id: number;
    firstName: string;
    lastName: string;
    classAcronym: string;
    email: string;
    department: string;
};

type StudentSubject = {
    id: number;
    longName: string;
    shortName: string;
    semester: number;
    grade: number | null;
};

function SubjectsPage() {
    const navigate = useNavigate();

    const [student, setStudent] = useState<LoggedInStudent | null>(null);
    const [subjects, setSubjects] = useState<StudentSubject[]>([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadData = async () => {
            try {
                const meRes = await fetch("http://localhost:8080/api/auth/me", {
                    method: "GET",
                    credentials: "include"
                });

                if (!meRes.ok) {
                    navigate("/login");
                    return;
                }

                const meData = await meRes.json();
                setStudent(meData);

                const subjectsRes = await fetch("http://localhost:8080/api/students/me/subjects", {
                    method: "GET",
                    credentials: "include"
                });

                if (!subjectsRes.ok) {
                    throw new Error("Subjects could not be loaded");
                }

                const subjectsData = await subjectsRes.json();
                setSubjects(subjectsData);
            } catch (error) {
                console.error("Fehler beim Laden der Subjects:", error);
            } finally {
                setLoading(false);
            }
        };

        loadData();
    }, [navigate]);

    if (loading || !student) {
        return null;
    }

    return (
        <div style={styles.page}>
            <Navbar student={student} />

            <div style={styles.contentWrapper}>
                <DashboardSidebar />

                <main style={styles.main}>
                    <div style={styles.headerBox}>
                        <h1 style={styles.title}>Meine Fächer</h1>
                        <p style={styles.subtitle}>
                            Hier siehst du alle Fächer, die dir automatisch zugewiesen wurden.
                        </p>
                    </div>

                    <div style={styles.grid}>
                        {subjects.map((subject) => (
                            <div key={subject.id} style={styles.subjectCard}>
                                <div style={styles.subjectTopRow}>
                                    <h3 style={styles.subjectTitle}>{subject.longName}</h3>
                                    <span style={styles.shortBadge}>{subject.shortName}</span>
                                </div>

                                <p style={styles.subjectInfo}>
                                    <strong>Semester:</strong> {subject.semester}
                                </p>
                                <p style={styles.subjectInfo}>
                                    <strong>Note:</strong> {subject.grade ?? "Keine Note"}
                                </p>
                            </div>
                        ))}
                    </div>
                </main>
            </div>
        </div>
    );
}

const styles = {
    page: {
        minHeight: "100vh",
        background: "linear-gradient(135deg, #0f172a, #1e293b)",
        color: "white",
        fontFamily: "Arial, sans-serif"
    },
    contentWrapper: {
        display: "flex",
        minHeight: "calc(100vh - 80px)"
    },
    main: {
        flex: 1,
        padding: "32px",
        boxSizing: "border-box" as const
    },
    headerBox: {
        marginBottom: "28px"
    },
    title: {
        margin: 0,
        fontSize: "36px",
        marginBottom: "10px"
    },
    subtitle: {
        margin: 0,
        color: "#cbd5e1",
        fontSize: "17px"
    },
    grid: {
        display: "grid",
        gridTemplateColumns: "repeat(auto-fit, minmax(260px, 1fr))",
        gap: "20px"
    },
    subjectCard: {
        backgroundColor: "rgba(255,255,255,0.08)",
        borderRadius: "18px",
        padding: "22px",
        boxShadow: "0 10px 25px rgba(0,0,0,0.18)"
    },
    subjectTopRow: {
        display: "flex",
        justifyContent: "space-between",
        alignItems: "flex-start",
        gap: "12px",
        marginBottom: "16px"
    },
    subjectTitle: {
        margin: 0,
        fontSize: "20px",
        lineHeight: 1.3
    },
    shortBadge: {
        backgroundColor: "rgba(255,255,255,0.10)",
        borderRadius: "10px",
        padding: "6px 10px",
        fontSize: "13px",
        color: "#e2e8f0",
        whiteSpace: "nowrap" as const
    },
    subjectInfo: {
        margin: "8px 0",
        color: "#e2e8f0",
        fontSize: "15px"
    }
};

export default SubjectsPage;