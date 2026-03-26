import { useEffect, useState } from "react";

type StudentSubject = {
    id: number;
    grade: number | null;
    semester: number;
    subject: {
        id: number;
        subjectNameLong: string;
        subjectNameShort: string;
    };
};

export default function SubjectsPage() {
    const [subjects, setSubjects] = useState<StudentSubject[]>([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState("");

    useEffect(() => {
        const loadSubjects = async () => {
            try {
                const response = await fetch("http://localhost:8080/api/student-subjects/me", {
                    credentials: "include",
                });

                if (!response.ok) {
                    throw new Error("Fächer konnten nicht geladen werden.");
                }

                const data = await response.json();
                setSubjects(data);
            } catch (err) {
                setError("Fehler beim Laden der Fächer.");
                console.log(err);
            } finally {
                setLoading(false);
            }
        };

        loadSubjects();
    }, []);

    if (loading) {
        return <div className="text-gray-600">Fächer werden geladen...</div>;
    }

    if (error) {
        return <div className="text-red-600">{error}</div>;
    }

    return (
        <div>
            <h2 className="text-2xl font-bold mb-6">Meine Fächer</h2>

            {subjects.length === 0 ? (
                <p className="text-gray-600">Keine Fächer gefunden.</p>
            ) : (
                <div className="grid grid-cols-1 sm:grid-cols-2 xl:grid-cols-3 gap-5">
                    {subjects.map((entry) => (
                        <div
                            key={entry.id}
                            className="bg-white rounded-2xl shadow-sm border border-gray-200 p-5"
                        >
                            <div className="flex items-start justify-between mb-3">
                                <h3 className="text-lg font-semibold text-gray-900">
                                    {entry.subject.subjectNameLong}
                                </h3>
                                <span className="text-sm font-medium px-2 py-1 rounded-md bg-gray-100 text-gray-700">
                  {entry.subject.subjectNameShort}
                </span>
                            </div>

                            <div className="space-y-2 text-sm text-gray-700">
                                <p>
                                    <span className="font-medium">Semester:</span> {entry.semester}
                                </p>
                                <p>
                                    <span className="font-medium">Note:</span>{" "}
                                    {entry.grade ?? "Noch keine Note"}
                                </p>
                            </div>
                        </div>
                    ))}
                </div>
            )}
        </div>
    );
}