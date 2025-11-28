# Bookify – A Magical Digital Library for Readers & Authors  
*A full-stack Java + React platform crafted for book lovers.*

Bookify is a web application designed to bring readers and authors into one enchanted place — a personalized digital library where stories are tracked, shared, reviewed, and discovered.  
It merges practicality with fantasy-inspired design, giving every user the feeling of stepping into their own “Library of Forgotten Tales,” as shown in the project report (page 10 and 11) :contentReference[oaicite:1]{index=1}.

---

## What is Bookify?

Reading is one of the most powerful habits to nourish the mind. Bookify was created to make that journey **easier, organized, and more immersive**.

The platform supports two types of users:

### **Readers**
- Create an account and verify via email  
- Organize books into 3 magical shelves:
  - **Read**
  - **Currently Reading**
  - **Want to Read**
- Explore detailed book pages  
- Review books they have finished  
- Browse books by genres (Fantasy, Horror, Philosophy, etc.)

### **Authors**
Includes all reader abilities, plus:
- Publish new books using a dedicated form (page 13) :contentReference[oaicite:2]{index=2}  
- Automatically have their books added to the public collection  

---

## Project Objectives

As described in the report, Bookify aims to:

- Support **digital transformation** of personal libraries  
- Simplify user experience through clean UI/UX  
- Offer **adaptable**, personalized libraries  
- Ensure secure operations using login tokens  
- Provide real-time updates on published and reviewed books  

---

## System Analysis (UML)

Bookify follows object-oriented principles.  
The UML diagrams are included in the project report:

- **Use Case Diagram** (page 5)  
- **Class Diagram** (page 6)  

These diagrams define:
- Relationships between Users, Books, Reviews  
- Author-specific privileges  
- Category-based book management  

---

## Tech Stack

### Backend
- **Spring Boot (Java)**  
  Provides auto-configuration, efficient REST APIs, and secure services.  
  → Detailed explanation in the report (page 7) :contentReference[oaicite:4]{index=4}  

- **PostgreSQL**  
  ACID-compliant relational database storing:
  - Users, roles, reviews, books, categories  
  → Covered on page 9 :contentReference[oaicite:5]{index=5}  

### Frontend
- **React.js**  
  Component-based UI for smooth user experience (page 8)  
- **TailwindCSS**  
  Utility-first styling enabling clean and responsive design

### Tools Used
- **Postman** for API testing (page 9)  
- **JWT-like token system** for secure actions  

---

## Web Interfaces

Bookify’s UI blends clean engineering with fantasy-inspired visuals.

### 🪄 Sign Up & Email Verification
Users create an account, then receive a verification code by email  
<img width="1624" height="819" alt="image" src="https://github.com/user-attachments/assets/534f4a88-8e76-49ef-b762-e3ff9dd8d5e2" />

<img width="1341" height="542" alt="image" src="https://github.com/user-attachments/assets/7c09b229-2086-49d0-b06c-6fe5f4dc38b1" />

### Login & Dashboard
Once verified, readers enter a dashboard filled with books and categories  
<img width="1580" height="797" alt="image" src="https://github.com/user-attachments/assets/cb5ef4cd-d43e-42da-92e9-7fc1ef6ed649" />

<img width="1471" height="742" alt="image" src="https://github.com/user-attachments/assets/e4f55375-7916-41bc-b5ca-6b5efa3dc039" />


### 📖 Book Details
Selecting a book opens a rich details page with:
- Summary  
- Category options  
- Add-to-shelf actions

<img width="1488" height="753" alt="image" src="https://github.com/user-attachments/assets/7aab00fb-aeae-43ec-848e-13945f441b3d" />


### Publish A Book (Authors Only)
Authors fill a form to add new books to the public library (page 13).

<img width="1475" height="740" alt="image" src="https://github.com/user-attachments/assets/fd8dfdac-8aa6-4215-af6a-b5887fbabb28" />

---

## Project Structure

```
Bookify/
│
├── backend/ # Spring Boot backend
│ ├── controllers/
│ ├── repositories/
│ ├── services/
│ ├── models/
│ └── application.properties
│
├── frontend/ # React + Tailwind interface
│ ├── src/components/
│ ├── src/pages/
│ └── public/
│
└── README.md
```

---

## Features

- Secure login & email verification  
- Token-based authentication for all actions  
- Book organization in 3 personalized shelves  
- Genre filtering & dynamic updates  
- Review system linked to “Read” category  
- Fully responsive UI  
- Author-exclusive content publishing  

---

## Future Enhancements

As suggested in the report’s conclusion (page 15) :contentReference[oaicite:7]{index=7}:
- Add **chat rooms** for each book  
- Allow following favorite authors  
- Integrate digital book formats (PDF/ePub)  
- Improve social presence and book discovery  
- Enhance admin features  

---

## Conclusion

Bookify is more than a project — it's a companion for every reader seeking to organize their stories, discover new worlds, and share their thoughts.  
Built with Java, React, and PostgreSQL, it combines strong backend engineering with a beautiful, fantasy-inspired UI.

---

## Author  
**Boudribila Kaoutar**

