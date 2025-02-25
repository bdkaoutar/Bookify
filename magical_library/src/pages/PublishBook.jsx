import { useState } from "react";
import { Link, useNavigate } from 'react-router-dom';
import api from '../api/axios.js'; // Assuming api is configured as before
import { publishBook } from '../api/api.jsx';  // Import the function

function PublishBook() {
    const navigate = useNavigate(); // Hook for navigation
    const [formData, setFormData] = useState({
        title: "",
        cover: "",  // Keep as URL string
        description: "",
        genres: [],
        language: "English",
        publicationDate:""
    });

    const genres = [
        "Fantasy",
        "Romance",
        "Thriller",
        "Science Fiction",
        "Mystery",
        "Non-fiction"
    ];

    const languagesList = ["English", "French", "Spanish", "German", "Italian", "Japanese", "Chinese", "Arabic"];

    const handleChange = (e) => {
        const { name, value, checked } = e.target;
        if (name === "genres") {
            // Toggle the genre in the genres array
            setFormData((prevData) => {
                const genres = checked
                    ? [...prevData.genres, value]  // Add genre if checked
                    : prevData.genres.filter(genre => genre !== value); // Remove genre if unchecked

                return { ...prevData, genres };
            });
        } else {
            setFormData({...formData, [name]: value});
        }
    };

    const handleFileChange = (e) => {
        setFormData({ ...formData, cover: e.target.files[0] });
    };

    const handleGenreChange = (e) => {
        const { value, checked } = e.target;
        setFormData((prevData) => {
            // Toggle the genre in the genres array
            const updatedGenres = checked
                ? [...prevData.genres, value]  // Add genre if checked
                : prevData.genres.filter(genre => genre !== value); // Remove genre if unchecked

            return { ...prevData, genres: updatedGenres };
        });
    };


    const handleSubmit = async (e) => {
        e.preventDefault();

        const email = localStorage.getItem('email');  // Get email from localStorage
        const token = localStorage.getItem('token');  // Retrieve the JWT token

        if (!email || !token) {
            console.error("Email or Token is missing");
            return;  // Prevent submission if email or token is not found
        }

        try {
            // Fetch authorId using the email (new backend endpoint)
            const response = await api.get("/api/v1/books/get-author-id", {
                headers: { "Authorization": `Bearer ${token}` }
            });
            const authorId = response.data;  // Get the authorId from the response

            const bookData = {
                title: formData.title,
                coverImage: formData.cover,  // URL of the cover image
                description: formData.description,
                genres: formData.genres,  // Array of selected genres
                language: formData.language,
                publicationDate: formData.publicationDate
            };
            console.log(bookData)

            // Now, publish the book using the authorId and the book data
            const publishResponse = await api.post(
                `/api/v1/books/publish/${authorId}`, // Assuming authorId is the bookId
                bookData,
                {
                    headers: {
                        Authorization: `Bearer ${token}`,  // Pass token for authentication
                    },
                }
            );

            console.log("Book published successfully:", publishResponse.data);

            // Redirect to the dashboard or another page
            navigate('/dashboard');  // Navigate to published books or dashboard
        } catch (error) {
            console.error("Error publishing book", error);
        }
    };



    return (
        <div className="flex min-h-screen items-center justify-center text-white"
             style={{
                 backgroundImage: "url('/old-parchment.avif')",
                 backgroundSize: "cover",
                 backgroundPosition: "center",
                 backgroundAttachment: "fixed",
                 fontFamily: "'Cinzel', serif",
                 color: "#3E2F21"
             }}>
            <div className="w-[40rem] p-8 rounded-xl shadow-xl border bg-opacity-80 backdrop-blur-md"
                 style={{backgroundColor: "rgba(66, 42, 15, 0.9)", border: "1px solid rgba(255, 255, 255, 0.2)"}}>
                <h2 className="text-2xl font-bold text-amber-100 text-center mb-6">📖 Publish a New Book</h2>
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        name="title"
                        placeholder="Book Title"
                        value={formData.title}
                        onChange={handleChange}
                        className="w-full px-4 py-3 mb-4 bg-transparent text-amber-100 border border-lime-950 rounded-lg"
                        required/>

                    <input
                        type="text"
                        name="cover"
                        placeholder="Cover Image URL"
                        value={formData.cover}
                        onChange={handleChange}
                        className="w-full px-4 py-3 mb-4 bg-transparent text-amber-100 border border-lime-950 rounded-lg"
                        required/>

                    <textarea
                        name="description"
                        placeholder="Book Description"
                        value={formData.description}
                        onChange={handleChange}
                        className="w-full px-4 py-3 mb-4 bg-transparent text-amber-100 border border-lime-950 rounded-lg h-32"
                        required></textarea>

                    {/* Genre checkboxes */}
                    <div className="flex flex-col space-y-4">
                        <label className="text-amber-100">Select Genres:</label>
                        <div className="genres-section flex flex-wrap space-x-4">
                            <label className="text-amber-200">
                                <input type="checkbox" value="Fantasy" onChange={handleGenreChange}/> Fantasy
                            </label>
                            <label className="text-amber-200">
                                <input type="checkbox" value="Romance" onChange={handleGenreChange}/> Romance
                            </label>
                            <label className="text-amber-200">
                                <input type="checkbox" value="Thriller" onChange={handleGenreChange}/> Thriller
                            </label>
                            <label className="text-amber-200">
                                <input type="checkbox" value="Science Fiction" onChange={handleGenreChange}/> Science Fiction
                            </label>
                            <label className="text-amber-200">
                                <input type="checkbox" value="Mystery" onChange={handleGenreChange}/> Mystery
                            </label>
                            <label className="text-amber-200">
                                <input type="checkbox" value="Non-Fiction" onChange={handleGenreChange}/> Non-Fiction
                            </label>
                        </div>
                    </div>

                    <select name="language" value={formData.language} onChange={handleChange}
                            className="w-full px-4 py-3 mb-4 bg-transparent text-amber-100 border border-lime-950 rounded-lg">
                        {languagesList.map((lang) => (
                            <option key={lang} value={lang}>{lang}</option>
                        ))}
                    </select>

                    <button type="submit"
                            className="w-full bg-amber-900 hover:bg-orange-950 text-amber-100 font-bold py-3 px-4 rounded-lg shadow-md transition">
                        📚 Publish Book
                    </button>

                </form>
                <button
                    // onClick={handleViewBooks}
                    className="w-full bg-yellow-700 text-white px-4 py-2 rounded-lg mt-4 hover:bg-yellow-800 "
                >
                    View Published Books
                </button>
                <p className="mt-4 text-center">
                    <Link to="/dashboard" className="text-yellow-400">🔙 Back to Dashboard</Link>
                </p>
            </div>
        </div>
    );
}

export default PublishBook;
