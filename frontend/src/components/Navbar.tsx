import { Link, useNavigate } from "react-router-dom";

type LoggedInStudent = {
    firstName: string;
    lastName: string;
};

type NavbarProps = {
    student: LoggedInStudent | null;
};

function Navbar({ student }: NavbarProps) {
    const navigate = useNavigate();

    const handleLogout = async () => {
        try {
            await fetch("http://localhost:8080/api/auth/logout", {
                method: "POST",
                credentials: "include"
            });
        } catch (error) {
            console.error("Fehler beim Logout:", error);
        } finally {
            navigate("/");
        }
    };

    return (
        <nav style={styles.navbar}>
            <div style={styles.logo} onClick={() => navigate("/")}>
                Zumi
            </div>

            <div style={styles.rightSection}>
                {student ? (
                    <>
            <span style={styles.fullName}>
              {student.firstName} {student.lastName}
            </span>
                        <button style={styles.logoutButton} onClick={handleLogout}>
                            Logout
                        </button>
                    </>
                ) : (
                    <>
                        <Link to="/login" style={styles.link}>
                            Login
                        </Link>
                        <Link to="/register" style={styles.link}>
                            Registrieren
                        </Link>
                    </>
                )}
            </div>
        </nav>
    );
}

const styles = {
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
        fontWeight: "bold",
        cursor: "pointer"
    },
    rightSection: {
        display: "flex",
        alignItems: "center",
        gap: "16px"
    },
    fullName: {
        fontSize: "16px",
        fontWeight: 600
    },
    logoutButton: {
        padding: "10px 16px",
        borderRadius: "8px",
        border: "none",
        cursor: "pointer",
        fontSize: "14px",
        fontWeight: 600
    },
    link: {
        color: "white",
        textDecoration: "none",
        fontSize: "16px"
    }
};

export default Navbar;