import axios from "axios";



// Fetch books by category
export const getBooksByCategory = async (userId, category) => {
    try {
        const response = await api.get(`users/${userId}/books/${category}`);
        return response.data;  // Return the list of books
    } catch (error) {
        console.error("Error fetching books by category:", error);
        throw error;
    }
};

// Create an axios instance with the base URL
const api = axios.create({
    baseURL: "http://localhost:8080/api/v1/",  // Backend URL
    headers: {
        "Content-Type": "application/json",
        "Authorization": `Bearer ${localStorage.getItem("token")}`,  // Include JWT token from localStorage
    },
});

// Fetch books by genre
export const getBooksByGenre = async (genre) => {
    try {
        const response = await api.get(`books/genre/${genre}`);
        return response.data;  // Return the list of books
    } catch (error) {
        console.error("Error fetching books by genre:", error);
        throw error;
    }
};

// Add review to a book
export const addReview = async (bookId, userId, rating, comment) => {
    try {
        const response = await api.post(`books/${bookId}/review`, null, {
            params: { userId, rating, comment },
        });
        return response.data;
    } catch (error) {
        console.error("Error adding review:", error);
        throw error;
    }
};

// Publish a new book
export const publishBook = async (bookId, bookData) => {
    const token = localStorage.getItem("authToken"); // or get it from wherever you store the token
    try {
        const response = await axios.post(
            `http://localhost:8080/api/v1/books/publish/${bookId}`,
            bookData,
            {
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            }
        );
        console.log('Book published successfully:', response.data);
    } catch (error) {
        console.error('Error publishing book:', error.response || error);
    }
};

// // Add a book to a category
// export const addBookToCategory = async (userId, bookId, category) => {
//     try {
//         const response = await api.post(`users/${userId}/books/${bookId}/add`, null, {
//             params: { category },
//         });
//         return response.data;
//     } catch (error) {
//         console.error("Error adding book to category:", error);
//         throw error;
//     }
// };

// Get the authenticated user
export const getAuthenticatedUser = async () => {
    try {
        const response = await api.get("users/me");
        return response.data;  // Return user info
    } catch (error) {
        console.error("Error fetching authenticated user:", error);
        throw error;
    }
};

// Fetch the latest 9 books added to the collection
export const getLatestBooks = async () => {
    try {
        const response = await api.get("books/latest");
        return response.data; // Return the list of books
    } catch (error) {
        console.error("Error fetching latest books:", error);
        throw error;
    }
};

// New method to get authorId based on email
export const getAuthorIdByEmail = async (email) => {
    try {
        const response = await api.get(`auth/get-author-id`, { params: { email } });
        return response.data;  // Return authorId
    } catch (error) {
        console.error("Error fetching author ID:", error);
        throw error;
    }
};

// API to add book to category
export const addBookToCategory = async (userId, bookId, category) => {
    try {
        const response = await api.post(`users/${userId}/books/${bookId}/add`, null, {
            params: { category },
        });
        return response.data;  // Return success message or response data
    } catch (error) {
        console.error("Error adding book to category:", error);
        throw error;
    }
};

export const addBookRating = async (bookId, stars, token) => {
    try {
        const response = await api.post(
            `/api/v1/books/${bookId}/review`,  // The endpoint to rate a book
            { rating: stars },
            {
                headers: { Authorization: `Bearer ${token}` }
            }
        );
        return response.data; // Return response (e.g., the updated book with new average rating)
    } catch (error) {
        console.error("Error adding rating:", error);
        throw error; // Re-throw the error to be handled in the component
    }
};





