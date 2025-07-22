package edu.com.project.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.com.project.model.Book;
import edu.com.project.model.CartItem;
import edu.com.project.model.Order;
import edu.com.project.model.OrderDetail;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "BookSale.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Users table
        db.execSQL("CREATE TABLE Users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "email TEXT" + ");");

        // Books table
        db.execSQL("CREATE TABLE Books (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "title TEXT NOT NULL," +
                "author TEXT," +
                "price REAL NOT NULL," +
                "description TEXT," +
                "imageUrl TEXT" + ");");

        // Cart table
        db.execSQL("CREATE TABLE Cart (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER NOT NULL," +
                "bookId INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "FOREIGN KEY(userId) REFERENCES Users(id)," +
                "FOREIGN KEY(bookId) REFERENCES Books(id)" + ");");

        // Order table
        db.execSQL("CREATE TABLE Orders (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "userId INTEGER NOT NULL," +
                "total REAL NOT NULL," +
                "date TEXT NOT NULL," +
                "FOREIGN KEY(userId) REFERENCES Users(id)" + ");");

        // OrderDetails table (for books in each order)
        db.execSQL("CREATE TABLE OrderDetails (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "orderId INTEGER NOT NULL," +
                "bookId INTEGER NOT NULL," +
                "quantity INTEGER NOT NULL," +
                "price REAL NOT NULL," +
                "FOREIGN KEY(orderId) REFERENCES Orders(id)," +
                "FOREIGN KEY(bookId) REFERENCES Books(id)" + ");");

        // Insert sample data for Users
        db.execSQL("INSERT INTO Users (username, password, email) VALUES ('user1', 'pass1', 'user1@email.com');");
        db.execSQL("INSERT INTO Users (username, password, email) VALUES ('user2', 'pass2', 'user2@email.com');");

        // Insert sample data for Books
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Android Programming: The Big Nerd Ranch Guide', 'Bill Phillips', 32.99, 'Comprehensive guide to Android app development, suitable for beginners and intermediate developers.', 'https://m.media-amazon.com/images/I/71dLKjgyYCL._UF1000,1000_QL80_.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Kotlin Programming: The Big Nerd Ranch Guide', 'Josh Skeen', 29.95, 'A practical introduction to Kotlin for Android and beyond.', 'https://m.media-amazon.com/images/I/51spCEuJ0BS._UF1000,1000_QL80_.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Head First Android Development', 'Dawn Griffiths', 27.50, 'A visually rich and engaging guide to Android development.', 'https://m.media-amazon.com/images/I/91wFYsG+yGL._UF1000,1000_QL80_.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Android Apprentice', 'Raywenderlich Tutorial Team', 34.99, 'A hands-on guide to building real Android apps using Kotlin.', 'https://assets.alexandria.raywenderlich.com/books/961b6dd3ede3cb8ecbaacbd68de040cd78eb2ed5889130cceb4c49268ea4d506/images/5e8e25e2c8ef67369b94fa5b42eb7d10/original.png');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Professional Android', 'Reto Meier', 36.50, 'An advanced-level guide for experienced developers looking to master Android.', 'https://m.media-amazon.com/images/I/61txV0N5lIL._UF350,350_QL50_.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Android Studio 4.2 Development Essentials', 'Neil Smyth', 25.00, 'Comprehensive tutorial covering Android Studio and app development.', 'https://m.media-amazon.com/images/I/618KTm4LQPS._UF1000,1000_QL80_.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Kotlin for Android Developers', 'Antonio Leiva', 28.99, 'Learn Kotlin while developing an Android app step-by-step.', 'https://d2sofvawe08yqg.cloudfront.net/kotlin-for-android-developers/s_hero2x?1620479571');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Jetpack Compose by Tutorials', 'Raywenderlich Tutorial Team', 31.49, 'Build declarative UIs in Android using Jetpack Compose.', 'https://m.media-amazon.com/images/I/61W5FgKISbL.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('The Busy Coder''s Guide to Android Development', 'Mark L. Murphy', 39.00, 'Extensive guide that covers virtually all aspects of Android app development.', 'https://commonsware.com/Android/cover_small.png');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Android Internals:: A Confectioner''s Cookbook', 'Jonathan Levin', 44.95, 'In-depth look at the internals of Android OS for advanced developers.', 'https://newandroidbook.com/aivi2cover.png');");

        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Android Programming with Kotlin for Beginners', 'John Horton', 26.99, 'Step-by-step Kotlin-first introduction to Android development.', 'https://m.media-amazon.com/images/I/61ZPNhC2hSL._UF1000,1000_QL80_.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Kotlin in Action', 'Dmitry Jemerov & Svetlana Isakova', 37.95, 'Comprehensive guide to Kotlin language for Android and general programming.', 'https://m.media-amazon.com/images/I/81pjrEnTwJL._UF1000,1000_QL80_.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Android Programming: Pushing the Limits', 'Erik Hellman', 29.99, 'Advanced Android techniques covering performance, services, sensors, multimedia.', 'https://m.media-amazon.com/images/I/51nzlDhs0lL.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Reactive Programming with RxJava', 'Tomasz Nurkiewicz & Ben Christensen', 34.99, 'Learn RxJava patterns for Android and reactive app architectures.', 'https://m.media-amazon.com/images/I/91ZKQThhy5L._UF1000,1000_QL80_.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Android UI Fundamentals', 'Adam Adamczyk', 24.50, 'Explore material design, layouts, animations, and custom views.', 'https://m.media-amazon.com/images/I/81uqM6G2TRL._AC_UF1000,1000_QL80_.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Android Security Internals', 'Nikolay Elenkov', 49.99, 'Deep dive into Android security architecture and exploit prevention.', 'https://m.media-amazon.com/images/I/81Q+PD+2QGL.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Android Performance Patterns', 'Google Developer Experts', 0.00, 'Free e‑book series covering performance tips from the Android team.', 'https://m.media-amazon.com/images/I/61sg72hypDL._UF1000,1000_QL80_.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Pro Android 5', 'Dave MacLean & Jeff Friesen', 42.00, 'Expert content on modern Android 11 APIs and app distribution.', 'https://m.media-amazon.com/images/I/41dtQL-6TML._AC_UF1000,1000_QL80_.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Android Studio Meerkat Essentials', 'Dr. M. Ilyas & Raghavendra Kodali', 30.99, 'Hands‑on projects using the latest Android Studio.', 'https://m.media-amazon.com/images/I/61yQ+ivysmL._UF1000,1000_QL80_.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Modern Android App Architecture', 'Joe Birch', 34.99, 'MVVM, Clean Architecture, dependency injection explained.', 'https://m.media-amazon.com/images/I/61VM811hB3L._UF1000,1000_QL80_.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Android NDK Beginners Guide', 'Sylvain Ratabouil', 28.99, 'Build native C/C++ code for Android with the NDK.', 'https://m.media-amazon.com/images/I/71nkdhJImXL._UF1000,1000_QL80_.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Pro Android with Kotlin', 'Peter Späth', 39.99, 'Advanced Kotlin usage for professional Android development.', 'https://media.springernature.com/full/springer-static/cover-hires/book/978-1-4842-8745-3');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Android Studio 4.1 Development Essentials — Java Edition', 'Neil Smyth', 27.50, 'Covers Java‑based Android app development fundamentals.', 'https://www.oreilly.com/library/cover/9781801815161/1200w630h/');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Wearable Android', 'Sanjay M. Mishra', 24.95, 'Develop apps for wearables using Wear OS.', 'https://m.media-amazon.com/images/I/51m7BXtgrpL._UF1000,1000_QL80_.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Pro Reactive Android', 'Teejay Baruah', 32.00, 'Advanced reactive and concurrent app patterns in Android.', 'https://media.springernature.com/full/springer-static/cover-hires/book/978-1-4302-1597-4');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Android User Interface Design', 'Ian G. Clifton', 29.99, 'Designing engaging and effective user interfaces on Android.', 'https://m.media-amazon.com/images/I/41OF+aOZvNL._UF1000,1000_QL80_.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Mastering Firebase for Android Development', 'Ashok Kumar S', 22.99, 'Implement push notifications and real‑time messaging in Android.', 'https://m.media-amazon.com/images/I/71Nu+7b7b7L._UF1000,1000_QL80_.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Clean Architecture for Android', 'Eran Boudjnah', 36.99, 'Structure large-scale apps with modules, CI/CD, testing.', 'https://m.media-amazon.com/images/I/81jGOEGQmtL.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Advanced Android Development', 'Dan Galpin', 44.00, 'Enterprise‑grade Android patterns, security, native integration.', 'https://images-na.ssl-images-amazon.com/images/S/compressed.photo.goodreads.com/books/1418916987i/23942899.jpg');");
        db.execSQL("INSERT INTO Books (title, author, price, description, imageUrl) VALUES ('Hands on: Microservices with Kotlin', 'Juan Antonio Medina Iglesias', 45.00, 'Connect Android apps with cloud‑native backends and microservices.', 'https://m.media-amazon.com/images/I/7146gfpVQML._UF1000,1000_QL80_.jpg');");


        // Insert sample data for Cart
        db.execSQL("INSERT INTO Cart (userId, bookId, quantity) VALUES (1, 1, 2);");
        db.execSQL("INSERT INTO Cart (userId, bookId, quantity) VALUES (2, 2, 1);");

        // Insert sample data for Orders
        db.execSQL("INSERT INTO Orders (userId, total, date) VALUES (1, 39.98, '2025-06-30');");
        db.execSQL("INSERT INTO Orders (userId, total, date) VALUES (2, 29.99, '2025-06-29');");

        // Insert sample data for OrderDetails
        db.execSQL("INSERT INTO OrderDetails (orderId, bookId, quantity, price) VALUES (1, 1, 2, 19.99);");
        db.execSQL("INSERT INTO OrderDetails (orderId, bookId, quantity, price) VALUES (2, 2, 1, 29.99);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS OrderDetails");
        db.execSQL("DROP TABLE IF EXISTS Orders");
        db.execSQL("DROP TABLE IF EXISTS Cart");
        db.execSQL("DROP TABLE IF EXISTS Books");
        db.execSQL("DROP TABLE IF EXISTS Users");
        onCreate(db);
    }

    // Add a new user (for Sign Up)
    public boolean addUser(String username, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("password", password);
        values.put("email", email);
        long result = db.insert("Users", null, values);
        return result != -1;
    }

    public boolean checkUserExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE username = ?", new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Check user credentials (for Login)
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM Users WHERE username=? AND password=?", new String[]{username, password});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        return exists;
    }

    // Fetch all books from the database
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Books", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
                books.add(new Book(id, title, author, price, description, imageUrl));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return books;
    }

    // Fetch a single book by ID
    public Book getBookById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Books WHERE id=?", new String[]{String.valueOf(id)});
        Book book = null;
        if (cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
            String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
            book = new Book(id, title, author, price, description, imageUrl);
        }
        cursor.close();
        return book;
    }

    // Get user ID by username
    public int getUserIdByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM Users WHERE username=?", new String[]{username});
        int userId = -1;
        if (cursor.moveToFirst()) {
            userId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }
        cursor.close();
        return userId;
    }

    // Add or update a book in the cart for a user
    public void addOrUpdateCartItem(int userId, int bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, quantity FROM Cart WHERE userId=? AND bookId=?", new String[]{String.valueOf(userId), String.valueOf(bookId)});
        if (cursor.moveToFirst()) {
            int cartId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
            ContentValues values = new ContentValues();
            values.put("quantity", quantity + 1);
            db.update("Cart", values, "id=?", new String[]{String.valueOf(cartId)});
        } else {
            ContentValues values = new ContentValues();
            values.put("userId", userId);
            values.put("bookId", bookId);
            values.put("quantity", 1);
            db.insert("Cart", null, values);
        }
        cursor.close();
    }

    // Get all cart items for a user (with book details)
    public List<CartItem> getCartItemsForUser(int userId) {
        List<CartItem> cartItems = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Cart.id, Books.title, Books.author, Books.price, Cart.quantity, Books.imageUrl " +
                "FROM Cart INNER JOIN Books ON Cart.bookId = Books.id " +
                "WHERE Cart.userId = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
                cartItems.add(new CartItem(id, title, author, price, quantity, imageUrl));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return cartItems;
    }

    // Update cart item quantity
    public void updateCartItemQuantity(int cartId, int newQuantity) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (newQuantity > 0) {
            ContentValues values = new ContentValues();
            values.put("quantity", newQuantity);
            db.update("Cart", values, "id=?", new String[]{String.valueOf(cartId)});
        } else {
            db.delete("Cart", "id=?", new String[]{String.valueOf(cartId)});
        }
    }

    // Remove cart item
    public void removeCartItem(int cartId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Cart", "id=?", new String[]{String.valueOf(cartId)});
    }

    // Create order, add order details, and clear cart
    public void createOrder(int userId, double total, String date, List<CartItem> cartItems) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues orderValues = new ContentValues();
            orderValues.put("userId", userId);
            orderValues.put("total", total);
            orderValues.put("date", date);
            long orderId = db.insert("Orders", null, orderValues);
            for (CartItem item : cartItems) {
                ContentValues detailValues = new ContentValues();
                detailValues.put("orderId", orderId);
                detailValues.put("bookId", getBookIdByTitle(item.getTitle()));
                detailValues.put("quantity", item.getQuantity());
                detailValues.put("price", item.getPrice());
                db.insert("OrderDetails", null, detailValues);
            }
            db.delete("Cart", "userId=?", new String[]{String.valueOf(userId)});
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    // Helper to get bookId by title (for order details)
    private int getBookIdByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id FROM Books WHERE title=?", new String[]{title});
        int bookId = -1;
        if (cursor.moveToFirst()) {
            bookId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
        }
        cursor.close();
        return bookId;
    }

    // Get all orders for a user
    public List<Order> getOrdersForUser(int userId) {
        List<Order> orders = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, total, date FROM Orders WHERE userId=? ORDER BY date DESC", new String[]{String.valueOf(userId)});
        if (cursor.moveToFirst()) {
            do {
                int orderId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                double total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                orders.add(new Order(orderId, total, date, null));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return orders;
    }

    // Get order details (books in an order)
    public List<OrderDetail> getOrderDetails(int orderId) {
        List<OrderDetail> details = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT Books.title, OrderDetails.quantity, OrderDetails.price " +
                "FROM OrderDetails INNER JOIN Books ON OrderDetails.bookId = Books.id " +
                "WHERE OrderDetails.orderId = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(orderId)});
        if (cursor.moveToFirst()) {
            do {
                String bookTitle = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                int quantity = cursor.getInt(cursor.getColumnIndexOrThrow("quantity"));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
                details.add(new OrderDetail(bookTitle, quantity, price));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return details;
    }

    // Add to DatabaseHelper.java
    public Book getBookByTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Books WHERE title=?", new String[]{title});
        Book book = null;
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
            String author = cursor.getString(cursor.getColumnIndexOrThrow("author"));
            double price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"));
            String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
            String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow("imageUrl"));
            book = new Book(id, title, author, price, description, imageUrl);
        }
        cursor.close();
        return book;
    }
}
