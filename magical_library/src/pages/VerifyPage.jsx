import { useState } from "react";
import api from "../api/axios.js"; // Assuming you've set up Axios configuration
import { useNavigate } from "react-router-dom";

function VerifyPage() {
    const [email, setEmail] = useState("");
    const [verificationCode, setVerificationCode] = useState("");
    const [error, setError] = useState("");
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            const response = await api.post("/api/v1/auth/verify", { email, verificationCode });
            if (response.status === 200) {
                // Handle successful verification
                navigate("/login"); // Redirect to login after successful verification
            }
        } catch (err) {
            setError("Invalid verification code. Please try again.");
        }
    };

    return (
        <div className="verify-page">
            <h2>Verify Your Account</h2>
            <form onSubmit={handleSubmit}>
                <input
                    type="email"
                    placeholder="Enter your email"
                    value={email}
                    onChange={(e) => setEmail(e.target.value)}
                    required
                />
                <input
                    type="text"
                    placeholder="Enter the verification code"
                    value={verificationCode}
                    onChange={(e) => setVerificationCode(e.target.value)}
                    required
                />
                <button type="submit">Verify</button>
            </form>
            {error && <p>{error}</p>}
        </div>
    );
}

export default VerifyPage;
