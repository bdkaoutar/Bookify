import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import api from '../api/axios.js'; // Assuming you've set up Axios configuration as earlier

function AuthPage({ isLogin }) {
    const [formData, setFormData] = useState({
        firstName: "",
        lastName: "",
        username: "",
        email: "",
        password: "",
        role: "USER", // Default role
        verificationCode: "", // For verification step
    });
    const [error, setError] = useState("");
    const [step, setStep] = useState(1); // 1 for sign-up, 2 for verification
    const navigate = useNavigate();

    const handleChange = (e) => {
        setFormData({ ...formData, [e.target.name]: e.target.value });
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            let response;

            // Step 1: User registration
            if (step === 1) {
                const userPayload = {
                    email: formData.email,
                    password: formData.password,
                    role: formData.role,
                    ...(isLogin ? {} : { firstName: formData.firstName, lastName: formData.lastName, username: formData.username })
                };

                if (isLogin) {
                    response = await api.post("/api/v1/auth/login", userPayload);
                    const { token } = response.data;
                    console.log(response);
                    localStorage.setItem('email', response.data.email);  // Where `response.data.email` is the user's email.

                    localStorage.setItem("token", token);
                    navigate("/dashboard");
                } else {
                    response = await api.post("/api/v1/auth/signup", userPayload);
                    setStep(2); // Move to step 2 for verification
                }
            }

            // Step 2: Verification code
            if (step === 2) {
                const verifyPayload = {
                    email: formData.email,
                    verificationCode: formData.verificationCode
                };
                response = await api.post("/api/v1/auth/verify", verifyPayload);
                if (response.status === 200) {
                    // Handle successful verification
                    navigate("/login"); // Redirect to login after successful verification
                }
            }
        } catch (err) {
            setError("An error occurred. Please try again.");
            console.error("Error during authentication:", err.response?.data || err.message);
        }
    };

    return (
        <div
            className="min-h-screen flex items-center justify-center bg-gray-900 text-white"
            style={{
                backgroundImage: "url('/fantasy_house.webp')",
                backgroundSize: "cover",
                backgroundPosition: "center",
            }}
        >
            <div
                className="relative p-8 rounded-xl shadow-xl w-[30rem] border glowing-card"
                style={{
                    backgroundColor: "rgba(20, 10, 5, 0.2)",
                    backdropFilter: "blur(5px)",
                    border: "1px solid rgba(255, 255, 255, 0.2)",
                    fontFamily: "'Cinzel', serif",
                    color: "rgba(255, 255, 255, 0.85)",
                    textShadow: "2px 2px 4px rgba(0,0,0,0.5)",
                }}
            >
                <h2 className="text-2xl font-bold text-amber-100 text-center mb-6 mt-[-10px]">
                    {isLogin ? "Welcome Back, Seeker of Stories" : "Join the Library of Forgotten Tales"}
                </h2>

                {/* Step 1: User details */}
                {step === 1 && (
                    <form onSubmit={handleSubmit}>
                        {!isLogin && (
                            <>
                                <input
                                    className="w-full px-4 py-3 mb-5 bg-transparent text-amber-100 border border-lime-950 rounded-lg focus:ring-2 focus:ring-yellow-500"
                                    name="firstName"
                                    placeholder="First Name"
                                    onChange={handleChange}
                                />
                                <input
                                    className="w-full px-4 py-3 mb-5 bg-transparent text-amber-100 border border-lime-950 rounded-lg focus:ring-2 focus:ring-yellow-500"
                                    name="lastName"
                                    placeholder="Last Name"
                                    onChange={handleChange}
                                />
                                <input
                                    className="w-full px-4 py-3 mb-5 bg-transparent text-amber-100 border border-lime-950 rounded-lg focus:ring-2 focus:ring-yellow-500"
                                    name="username"
                                    placeholder="Username"
                                    onChange={handleChange}
                                />
                                <select
                                    className="w-full px-4 py-3 mb-5 bg-transparent text-amber-100 border-lime-950 rounded-lg"
                                    name="role"
                                    onChange={handleChange}
                                >
                                    <option value="USER" className="text-black">USER</option>
                                    <option value="AUTHOR" className="text-black">AUTHOR</option>
                                </select>
                            </>
                        )}
                        <input
                            className="w-full px-4 py-3 mb-5 bg-transparent text-amber-100 border border-lime-950 rounded-lg focus:ring-2 focus:ring-yellow-500"
                            name="email"
                            placeholder="Email"
                            onChange={handleChange}
                        />
                        <input
                            type="password"
                            className="w-full px-4 py-3 mb-5 bg-transparent text-amber-100 border border-lime-950 rounded-lg focus:ring-2 focus:ring-yellow-500"
                            name="password"
                            placeholder="Password"
                            onChange={handleChange}
                        />

                        <button
                            type="submit"
                            className="w-full bg-amber-950 hover:bg-lime-950 text-amber-100 font-bold py-3 px-4 rounded-lg shadow-md transition mt-4"
                        >
                            {isLogin ? "Login" : "Sign Up"}
                        </button>
                    </form>
                )}

                {/* Step 2: Verification Code */}
                {step === 2 && !isLogin && (
                    <form onSubmit={handleSubmit}>
                        <input
                            className="w-full px-4 py-3 mb-5 bg-transparent text-amber-100 border border-lime-950 rounded-lg focus:ring-2 focus:ring-yellow-500"
                            name="verificationCode"
                            placeholder="Enter the verification code"
                            onChange={handleChange}
                        />
                        <button
                            type="submit"
                            className="w-full bg-amber-950 hover:bg-lime-950 text-amber-100 font-bold py-3 px-4 rounded-lg shadow-md transition mt-4"
                        >
                            Verify
                        </button>
                    </form>
                )}

                {error && <p className="mt-4 text-red-500 text-center">{error}</p>}
                <p className="mt-4 text-center">
                    {isLogin ? (
                        <span>Welcome to a new world, Booker.   <Link to="/login" className="text-lime-950">  Login</Link></span>
                    ) : (
                        <span>Already a member? <Link to="/login" className="text-lime-950">Return to the archives</Link></span>
                    )}
                </p>
            </div>
        </div>
    );
}

export default AuthPage;
