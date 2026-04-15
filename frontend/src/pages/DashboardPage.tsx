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

function DashboardPage() {
    const navigate = useNavigate();
    const [student, setStudent] = useState<LoggedInStudent | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const loadMe = async () => {
            try {
                const res = await fetch("http://localhost:8080/api/auth/me", {
                    method: "GET",
                    credentials: "include"
                });

                if (!res.ok) {
                    navigate("/login");
                    return;
                }

                const data = await res.json();
                setStudent(data);
            } catch (error) {
                console.error("Fehler beim Laden des eingeloggten Users:", error);
                navigate("/login");
            } finally {
                setLoading(false);
            }
        };

        loadMe();
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
                    <div style={styles.card}>
                        <h1 style={styles.title}>Dashboard</h1>
                        <p style={styles.subtitle}>Willkommen zurück, {student.firstName}.</p>

                        <div style={styles.infoBox}>
                            <p style={styles.infoText}>
                                <strong>Voller Name:</strong> {student.firstName} {student.lastName}
                            </p>
                            <p style={styles.infoText}>
                                <strong>E-Mail:</strong> {student.email}
                            </p>
                            <p style={styles.infoText}>
                                <strong>Klasse:</strong> {student.classAcronym}
                            </p>
                            <p style={styles.infoText}>
                                <strong>Abteilung:</strong> {student.department}
                            </p>
                        </div>
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
    card: {
        width: "100%",
        maxWidth: "900px",
        backgroundColor: "rgba(255,255,255,0.08)",
        borderRadius: "20px",
        padding: "40px",
        boxShadow: "0 10px 30px rgba(0,0,0,0.25)"
    },
    title: {
        margin: 0,
        fontSize: "36px",
        marginBottom: "12px"
    },
    subtitle: {
        margin: 0,
        fontSize: "18px",
        color: "#cbd5e1",
        marginBottom: "28px"
    },
    infoBox: {
        display: "flex",
        flexDirection: "column" as const,
        gap: "12px",
        backgroundColor: "rgba(255,255,255,0.05)",
        padding: "24px",
        borderRadius: "14px"
    },
    infoText: {
        margin: 0,
        fontSize: "16px"
    }
};

export default DashboardPage;