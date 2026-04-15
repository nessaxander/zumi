import { Link, useLocation } from "react-router-dom";

function DashboardSidebar() {
    const location = useLocation();

    const isActive = (path: string) => location.pathname === path;

    return (
        <aside style={styles.sidebar}>
            <div style={styles.sidebarHeader}>
                <h2 style={styles.sidebarTitle}>Zumi</h2>
            </div>

            <nav style={styles.nav}>
                <Link
                    to="/dashboard"
                    style={{
                        ...styles.link,
                        ...(isActive("/dashboard") ? styles.activeLink : {})
                    }}
                >
                    Dashboard
                </Link>

                <Link
                    to="/subjects"
                    style={{
                        ...styles.link,
                        ...(isActive("/subjects") ? styles.activeLink : {})
                    }}
                >
                    Fächer
                </Link>
            </nav>
        </aside>
    );
}

const styles = {
    sidebar: {
        width: "240px",
        minHeight: "calc(100vh - 80px)",
        backgroundColor: "rgba(15, 23, 42, 0.95)",
        borderRight: "1px solid rgba(255,255,255,0.08)",
        padding: "24px 16px",
        boxSizing: "border-box" as const
    },
    sidebarHeader: {
        marginBottom: "24px"
    },
    sidebarTitle: {
        margin: 0,
        fontSize: "24px",
        fontWeight: 700,
        color: "white"
    },
    nav: {
        display: "flex",
        flexDirection: "column" as const,
        gap: "10px"
    },
    link: {
        textDecoration: "none",
        color: "#cbd5e1",
        padding: "12px 14px",
        borderRadius: "12px",
        fontSize: "16px",
        fontWeight: 500,
        transition: "0.2s",
        backgroundColor: "transparent"
    },
    activeLink: {
        backgroundColor: "rgba(255,255,255,0.10)",
        color: "white"
    }
};

export default DashboardSidebar;