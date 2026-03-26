import { Link, Outlet, useLocation } from "react-router-dom";

export default function DashboardLayout() {
    const location = useLocation();

    const isActive = (path: string) => location.pathname === path;

    return (
        <div className="min-h-screen flex bg-gray-100">
            <aside className="w-64 bg-white border-r border-gray-200 flex flex-col">
                <div className="px-6 py-5 border-b border-gray-200">
                    <h1 className="text-xl font-bold">Zumi</h1>
                </div>

                <nav className="flex-1 p-4">
                    <Link
                        to="/dashboard/subjects"
                        className={`block rounded-lg px-4 py-3 font-medium transition ${
                            isActive("/dashboard/subjects")
                                ? "bg-blue-100 text-blue-700"
                                : "text-gray-700 hover:bg-gray-100"
                        }`}
                    >
                        Fächer
                    </Link>
                </nav>
            </aside>

            <main className="flex-1 p-6">
                <Outlet />
            </main>
        </div>
    );
}