import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Dashboard from "./pages/Dashboard"; // Your Dashboard component
import AuthPage from "./pages/signup_login.jsx";
import WantToRead from "./pages/WantToRead";
import CurrentlyReading from "./pages/CurrentlyReading";
import Read from "./pages/Read";
import PublishBook from "./pages/PublishBook"; // Import the new page
import VerifyPage from "./pages/VerifyPage"; // Import the VerifyPage component


function App() {
    return (
        <Router>
            <Routes>
                {/* Public Routes */}
                <Route path="/" element={<AuthPage />} />
                <Route path="/login" element={<AuthPage isLogin />} />
                <Route path="/signup" element={<AuthPage />} />
                <Route path="/verify" element={<VerifyPage />} /> {/* Add this route */}


                {/* Remove the ProtectedRoute, and directly render the Dashboard */}
                <Route path="/dashboard" element={<Dashboard />} />
                <Route path="/dashboard/wantToRead" element={<WantToRead />} />
                <Route path="/dashboard/currentlyReading" element={<CurrentlyReading />} />
                <Route path="/dashboard/read" element={<Read />} />
                <Route path="/dashboard/publish" element={<PublishBook />} /> {/* Add new route */}
            </Routes>
        </Router>
    );
}

export default App;
