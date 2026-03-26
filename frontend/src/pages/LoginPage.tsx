import { useState } from "react";
import { useNavigate } from "react-router-dom";

function LoginPage() {
    const navigate = useNavigate();

    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");

    const handleLogin = async () => {
        const loginRequest = {
            email: email.trim(),
            password: password
        };

        try {
            const res = await fetch("http://localhost:8080/api/auth/login", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                credentials: "include",
                body: JSON.stringify(loginRequest)
            });

            if (!res.ok) {
                const errorText = await res.text();
                alert("Login fehlgeschlagen: " + errorText);
                return;
            }

            navigate("/dashboard");
        } catch (error) {
            console.error("Fehler beim Login:", error);
            alert("Login fehlgeschlagen");
        }
    };

    return (
        <div style={styles.container}>
            <div style={styles.card}>
                <h2 style={styles.title}>Login</h2>

                <input
                    style={styles.input}
                    type="email"
                    placeholder="E-Mail"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                />

                <input
                    style={styles.input}
                    type="password"
                    placeholder="Passwort"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                />

                <button style={styles.button} onClick={handleLogin}>
                    Login
                </button>
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
        background: "linear-gradient(135deg, #0f172a, #1e293b)"
    },
    card: {
        width: "25%",
        minWidth: "320px",
        padding: "30px",
        borderRadius: "12px",
        backgroundColor: "#1e293b",
        display: "flex",
        flexDirection: "column" as const,
        gap: "12px",
        color: "white",
        boxShadow: "0 8px 24px rgba(0,0,0,0.25)"
    },
    title: {
        margin: 0,
        marginBottom: "10px",
        textAlign: "center" as const
    },
    input: {
        padding: "10px",
        borderRadius: "8px",
        border: "1px solid #475569",
        outline: "none",
        fontSize: "14px"
    },
    button: {
        padding: "12px",
        borderRadius: "8px",
        border: "none",
        cursor: "pointer",
        fontSize: "15px",
        fontWeight: 600
    }
};

export default LoginPage;