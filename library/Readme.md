# **Library Management API – My Learning Journey**

## **My Goal**

I'm building a Library Management system to manage **Books, Authors, Reviews, and Users**. Basically I am learning spring boot by making thies APIs.

## **Phase 1: Setup & Basic CRUD**

Created two main entities:

- **Book**: `id, title, genre`
- **Author**: `id, name`

**What I have built:**

1. I have set up my Spring Boot project with necessary dependencies
2. I have created `BookRepository` and `AuthorRepository` using **JdbcTemplate**
3. I have implemented service and controller layers for both entities
4. I have built basic CRUD APIs:

   - `GET /books` - retrieve all books
   - `POST /books` - create a new book
   - `GET /books/{id}` - get a specific book
   - `PUT /books/{id}` - update a book
   - `DELETE /books/{id}` - delete a book.

   And same for authors.

---
