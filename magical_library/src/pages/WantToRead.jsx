import { useEffect, useState } from "react";
import {getBooksByCategory} from "../api/api.jsx";
import {Link} from "react-router-dom";
import api from "../api/axios.js";

function WantToRead() {
    const [books, setBooks] = useState([]);

    useEffect(() => {
        const fetchBooks = async () => {
            try {
                const token = localStorage.getItem('token');  // Retrieve the JWT token

                const response = await api.get("/api/v1/books/get-author-id", {
                    headers: { "Authorization": `Bearer ${token}` }
                });
                const userId = response.data;
                const data = await getBooksByCategory(userId, "wantToRead");
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
            <h1 className="text-3xl font-bold text-amber-100 mb-6">📖 Want to Read</h1>
            {books.map((book, index) => (
                <div key={index} className="flex items-center mb-6 bg-orange-950 bg-opacity-30 p-4 rounded-lg">
                    <img src={book.coverImage} alt={book.title} className="w-32 h-48 rounded-lg mr-6" />
                    <div>
                        <h3 className="text-xl font-bold text-amber-100">{book.title}</h3>
                        <p className="text-md text-amber-200">{book.author}</p>
                        <p className="text-sm text-gray-300 mt-2">{book.description}</p>
                    </div>
                </div>
            ))}
            <Link to="/dashboard" className="text-yellow-400 underline mt-4 block">🔙 Back to Dashboard</Link>
        </div>
    );
}

export default WantToRead;
