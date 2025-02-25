import { useEffect, useState } from "react";
import { getLatestBooks } from "../api/api.jsx";
import { useNavigate } from "react-router-dom";
import { Link } from "react-router-dom"; // Ensure this is included
import { addBookToCategory } from "../api/api.jsx";
import api from "../api/axios.js";  // Import API service

function Dashboard() {
    const navigate = useNavigate();
    const [selectedBook, setSelectedBook] = useState(null);
    const [books, setBooks] = useState([]);

    // Fetch the latest 9 books from the backend
    useEffect(() => {
        const fetchBooks = async () => {
            try {
                const data = await getLatestBooks();
                setBooks(data);
            } catch (error) {
                console.error("Error fetching books:", error);
            }
        };
        fetchBooks();
    }, []);

    const handleBookClick = (book, coverImageUrl) => {
        console.log('Selected Book:', book);
        console.log('Cover Image URL:', coverImageUrl);

        // Update the book object with the cover image URL
        const updatedBook = { ...book, coverImage: coverImageUrl };

        // Set the selected book with the updated cover image
        setSelectedBook(updatedBook);
        console.log('Selected Book:', updatedBook);
    };

    const handleCategoryChange = async (category) => {
        const token = localStorage.getItem('token');  // Retrieve the JWT token

        if (selectedBook) {
            const response = await api.get("/api/v1/books/get-author-id", {
                headers: { "Authorization": `Bearer ${token}` }
            });
            const userId = response.data;
            const bookId = selectedBook.id; // Assuming the book object has an 'id' field
            try {
                await addBookToCategory(userId, bookId, category);  // Call the API service
                console.log(`Book added to ${category}`);
                // Optionally, update the UI or navigate to the respective category page
            } catch (error) {
                console.error("Error adding book to category:", error);
            }
        }
    };


    return (
        <div className="flex min-h-screen text-white"
             style={{
                 backgroundImage: "url('/old-parchment.avif')",
                 backgroundSize: "cover",
                 backgroundPosition: "center",
                 backgroundAttachment: "fixed",
                 fontFamily: "'Cinzel', serif",
                 color: "#3E2F21"
             }}>
            {/* Sidebar Navigation */}
            <div className="w-64 p-6 flex flex-col justify-between shadow-xl"
                 style={{
                     backgroundColor: "rgba(66, 42, 15, 0.8)",
                     borderRight: "1px solid rgba(255, 255, 255, 0.2)",
                     padding: "2rem"
                 }}>
                <div>
                    <h2 className="text-2xl font-bold text-amber-100 mb-6">📜 Library Dashboard</h2>
                    <h3 className="text-lg font-semibold text-amber-200 mb-3">My Library</h3>
                    <div className="space-y-4">
                        <Link to="/dashboard/WantToRead" className="block bg-orange-950 bg-opacity-70 p-4 rounded-lg text-amber-200 text-sm font-semibold hover:text-yellow-400">📖 Want to Read</Link>
                        <Link to="/dashboard/CurrentlyReading" className="block bg-orange-950 bg-opacity-70 p-4 rounded-lg text-amber-200 text-sm font-semibold hover:text-yellow-400">📚 Currently Reading</Link>
                        <Link to="/dashboard/Read" className="block bg-orange-950 bg-opacity-70 p-4 rounded-lg text-amber-200 text-sm font-semibold hover:text-yellow-400">✔ Read</Link>
                        <Link to="/dashboard/publish" className="block bg-orange-950 bg-opacity-70 p-4 rounded-lg text-amber-200 text-sm font-semibold hover:text-yellow-400">
                            📖 Publish New Book
                        </Link>
                    </div>
                    <h3 className="text-lg font-semibold text-amber-200 mt-6">General</h3>
                    <div className="space-y-4">
                        <Link to="/dashboard/explore" className="block bg-orange-950 bg-opacity-70 p-4 rounded-lg text-amber-200 text-sm font-semibold hover:text-yellow-400">🔍 Explore</Link>
                        <Link to="/dashboard/profile" className="block bg-orange-950 bg-opacity-70 p-4 rounded-lg text-amber-200 text-sm font-semibold hover:text-yellow-400">👤 Profile</Link>
                    </div>
                </div>
                <button className="w-full py-2 mt-4 bg-amber-900 hover:bg-orange-950 text-white font-bold rounded-lg transition">
                    🚪 Logout
                </button>
            </div>

            {/* Main Content Area */}
            <div className="flex-1 p-10 flex flex-col items-center justify-center"
                 style={{ backgroundColor: "rgba(255, 255, 255, 0.1)", backdropFilter: "blur(4px)" }}>
                {!selectedBook ? (
                    <>
                        <h1 className="text-4xl font-extrabold text-amber-100 mb-6">Welcome to Your Magical Library ✨</h1>
                        <div className="grid grid-cols-3 gap-8 w-full max-w-5xl">
                            {books.map((book, index) => (
                                <div key={index} className="flex flex-col items-center cursor-pointer"
                                     onClick={() => handleBookClick(book, book.coverImage)}>
                                    <img src={book.coverImage} alt="Book Cover"

                                    className="w-48 h-72 rounded-lg shadow-lg object-cover"/>

                                    <h3 className="text-lg font-bold text-amber-100 mt-2">{book.title}</h3>
                                    <p className="text-sm text-amber-200">{book.author}</p>
                                </div>
                            ))}
                        </div>
                    </>
                ) : (
                    <div className="flex flex-col items-center text-center">
                        <img src={`${selectedBook.coverImage}`} alt="Book Cover" className="w-64 h-96 rounded-lg shadow-lg" />
                        <h2 className="text-3xl font-bold text-amber-100 mt-4">{selectedBook.title}</h2>
                        <p className="text-xl text-amber-200">by {selectedBook.author}</p>
                        <p className="text-lg text-yellow-400 mt-2">⭐ {selectedBook.rating}</p>
                        <p className="text-md text-amber-100 mt-4 w-3/4">{selectedBook.description}</p>
                        <div className="flex space-x-4 mt-6">
                            <select className="px-4 py-2 bg-green-700 hover:bg-green-900 text-white rounded-lg"
                                    onChange={(e) => handleCategoryChange(e.target.value)}> // Handle category selection
                                <option value="wantToRead">📖 Want to Read</option>
                                <option value="currentlyReading">📚 Currently Reading</option>
                                <option value="read">✔ Read</option>
                            </select>
                            <button className="px-4 py-2 bg-yellow-700 hover:bg-yellow-900 text-white rounded-lg">🛒 Buy
                                on Amazon
                            </button>
                        </div>
                        <button className="mt-6 text-yellow-400 underline" onClick={() => setSelectedBook(null)}>🔙 Back to Library</button>
                    </div>
                )}
            </div>
        </div>
    );
}

export default Dashboard;
