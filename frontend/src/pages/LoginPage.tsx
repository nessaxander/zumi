function LoginPage() {
    return (
        <div style={styles.container}>
            <div style={styles.card}>
                <h2 style={styles.title}>Login</h2>
                <p style={styles.text}>Login kommt als Nächstes.</p>
            </div>
        </div>
    );
}

const styles = {
    container: {
        minHeight: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        background: "linear-gradient(135deg, #0f172a, #1e293b)",
        color: "white"
    },
    card: {
        backgroundColor: "rgba(255,255,255,0.08)",
        padding: "40px",
        borderRadius: "16px",
        textAlign: "center" as const,
        minWidth: "320px"
    },
    title: {
        marginTop: 0,
        marginBottom: "12px"
    },
    text: {
        margin: 0,
        color: "#cbd5e1"
    }
};

export default LoginPage;