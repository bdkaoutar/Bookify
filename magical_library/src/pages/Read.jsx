import { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { getBooksByCategory, addBookRating } from "../api/api.jsx";
import api from "../api/axios.js"; // Make sure you have axios configured correctly

function Read() {
    const [ratings, setRatings] = useState({});
    const [books, setBooks] = useState([]);

    // Handle the rating of a book
    const handleRating = async (bookId, stars) => {
        setRatings(prevRatings => ({
            ...prevRatings,
            [bookId]: stars
        }));

        try {
            // Send the rating to the backend to update the book's rating
            const token = localStorage.getItem('token');  // Retrieve the JWT token
            await addBookRating(bookId, stars, token); // Use your API to update the rating
            console.log(`Rated book ${bookId} with ${stars} stars`);

            // Optionally, fetch the updated books to get the new average rating
            fetchBooks();
        } catch (error) {
            console.error("Error updating the book rating:", error);
        }
    };

    // Fetch books based on the user's category (e.g., 'read', 'wantToRead', etc.)
    useEffect(() => {
        const fetchBooks = async () => {
            try {
                const token = localStorage.getItem('token');  // Retrieve the JWT token
                const response = await api.get("/api/v1/books/get-author-id", {
                    headers: { "Authorization": `Bearer ${token}` }
                });
                const userId = response.data;
                const data = await getBooksByCategory(userId, "read");
                setBooks(data);
                console.log(data);
            } catch (error) {
                console.error("Error fetching books:", error);
            }
        };

        fetchBooks();
    }, []);

    return (
        <div className="p-10 text-white"
             style={{
                 backgroundImage: "url('/old-parchment.avif')",
                 backgroundSize: "cover",
                 height: "100vh",
                 backgroundAttachment: "fixed",
                 fontFamily: "'Cinzel', serif",
                 color: "#3E2F21"
             }}>
            <h1 className="text-3xl font-bold text-amber-950 mb-6">✔ Read</h1>
            {books.map((book) => (
                <div key={book.id} className="flex items-center mb-6 bg-orange-950 bg-opacity-30 p-4 rounded-lg">
                    <img src={book.coverImage} alt={book.title} className="w-32 h-48 rounded-lg mr-6" />
                    <div>
                        <h3 className="text-xl font-bold text-amber-100">{book.title}</h3>
                        <p className="text-md text-amber-200">{book.author}</p>
                        <p className="text-sm text-gray-300 mt-2">{book.description}</p>

                        {/* Display average rating */}
                        <div className="mt-3">
                            <span className="text-yellow-400">Average Rating: {book.averageRating || "No rating yet"}</span>
                        </div>

                        {/* Rating System */}
                        <div className="mt-3">
                            {[1, 2, 3, 4, 5].map((star) => (
                                <span
                                    key={star}
                                    className={`cursor-pointer text-2xl ${ratings[book.id] >= star ? "text-yellow-400" : "text-gray-500"}`}
                                    onClick={() => handleRating(book.id, star)}
                                >
                                    ★
                                </span>
                            ))}
                        </div>
                    </div>
                </div>
            ))}
            <Link to="/dashboard" className="text-amber-950 underline mt-4 block">🔙 Back to Dashboard</Link>
        </div>
    );
}

export default Read;
