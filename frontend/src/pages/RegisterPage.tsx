import { useState } from "react";
import {useNavigate} from "react-router-dom";

function RegisterPage() {
    const [firstName, setFirstName] = useState("");
    const [lastName, setLastName] = useState("");
    const [classAcronym, setClassAcronym] = useState("");
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [passwordRepeat, setPasswordRepeat] = useState("");

    const navigate = useNavigate();

    const isValidClass = (cls: string): boolean => {
        return /^[A-Z]{4,6}\d{2}$/.test(cls.trim().toUpperCase());
    };

    const handleRegister = async () => {
        if (password !== passwordRepeat) {
            alert("Passwörter stimmen nicht überein");
            return;
        }

        if (!isValidClass(classAcronym)) {
            alert("Ungültiges Klassenkürzel");
            return;
        }

        const registerRequest = {
            firstName: firstName.trim(),
            lastName: lastName.trim(),
            classAcronym: classAcronym.trim().toUpperCase(),
            email: email.trim(),
            password: password
        };

        try {
            const res = await fetch("http://localhost:8080/api/students/register", {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                credentials: "include",
                body: JSON.stringify(registerRequest)
            });

            if (!res.ok) {
                const errorText = await res.text();
                alert("Registrierung fehlgeschlagen: " + errorText);
                return;
            }

            alert("Registrierung erfolgreich");

            setFirstName("");
            setLastName("");
            setClassAcronym("");
            setEmail("");
            setPassword("");
            setPasswordRepeat("");
        } catch (error) {
            console.error("Fehler bei Registrierung:", error);
            alert("Registrierung fehlgeschlagen");
        }
        navigate("/");
    };

    return (
        <div style={styles.container}>
            <div style={styles.card}>
                <h2 style={styles.title}>Registrieren</h2>

                <input
                    style={styles.input}
                    type="text"
                    placeholder="Vorname"
                    value={firstName}
                    onChange={(e) => setFirstName(e.target.value)}
                />

                <input
                    style={styles.input}
                    type="text"
                    placeholder="Nachname"
                    value={lastName}
                    onChange={(e) => setLastName(e.target.value)}
                />

                <input
                    style={styles.input}
                    type="text"
                    placeholder="Klassenkürzel (z. B. BHIF22)"
                    value={classAcronym}
                    onChange={(e) => setClassAcronym(e.target.value)}
                />

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

                <input
                    style={styles.input}
                    type="password"
                    placeholder="Passwort wiederholen"
                    value={passwordRepeat}
                    onChange={(e) => setPasswordRepeat(e.target.value)}
                />

                <button style={styles.button} onClick={handleRegister}>
                    Registrieren
                </button>
            </div>
        </div>
    );
}

const styles = {
    container: {
        height: "100vh",
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        backgroundColor: "#0f172a"
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

export default RegisterPage;