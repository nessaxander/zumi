import { Link } from "react-router-dom";

function LandingPage() {
    return (
        <div style={styles.page}>
            <nav style={styles.navbar}>
                <div style={styles.logo}>Zumi</div>

                <div style={styles.navLinks}>
                    <Link to="/register" style={styles.navLink}>
                        Registrieren
                    </Link>
                    <Link to="/login" style={styles.navLink}>
                        Login
                    </Link>
                </div>
            </nav>

            <main style={styles.hero}>
                <div style={styles.card}>
                    <h1 style={styles.title}>Willkommen bei Zumi</h1>
                    <p style={styles.subtitle}>
                        Deine Plattform für Schüler, Klassen und Organisation.
                    </p>

                    <div style={styles.buttonRow}>
                        <Link to="/register" style={styles.primaryButton}>
                            Registrieren
                        </Link>

                        <Link to="/login" style={styles.secondaryButton}>
                            Login
                        </Link>
                    </div>
                </div>
            </main>
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
    navbar: {
        height: "80px",
        display: "flex",
        justifyContent: "space-between",
        alignItems: "center",
        padding: "0 40px",
        borderBottom: "1px solid rgba(255,255,255,0.1)"
    },
    logo: {
        fontSize: "28px",
        fontWeight: "bold"
    },
    navLinks: {
        display: "flex",
        gap: "20px"
    },
    navLink: {
        color: "white",
        textDecoration: "none",
        fontSize: "16px"
    },
    hero: {
        minHeight: "calc(100vh - 80px)",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        padding: "20px"
    },
    card: {
        width: "100%",
        maxWidth: "700px",
        backgroundColor: "rgba(255,255,255,0.08)",
        borderRadius: "20px",
        padding: "50px 40px",
        textAlign: "center" as const,
        boxShadow: "0 10px 30px rgba(0,0,0,0.25)"
    },
    title: {
        margin: 0,
        fontSize: "42px",
        marginBottom: "16px"
    },
    subtitle: {
        margin: 0,
        fontSize: "18px",
        color: "#cbd5e1",
        marginBottom: "32px"
    },
    buttonRow: {
        display: "flex",
        justifyContent: "center",
        gap: "16px",
        flexWrap: "wrap" as const
    },
    primaryButton: {
        backgroundColor: "#38bdf8",
        color: "#0f172a",
        padding: "14px 28px",
        borderRadius: "10px",
        textDecoration: "none",
        fontWeight: "bold"
    },
    secondaryButton: {
        backgroundColor: "transparent",
        color: "white",
        padding: "14px 28px",
        borderRadius: "10px",
        textDecoration: "none",
        fontWeight: "bold",
        border: "1px solid rgba(255,255,255,0.3)"
    }
};

export default LandingPage;