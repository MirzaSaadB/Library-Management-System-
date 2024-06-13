package Library_Managment_System;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/signUp")
public class SignUp extends HttpServlet {

    Connection con = null;
    PreparedStatement ps = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userName = req.getParameter("userName");
        String userEmail = req.getParameter("userEmail");
        String userContact = req.getParameter("userContact");
        String userPassword = req.getParameter("userPassword");

        // Name, email, contact number, and password validation patterns
//        String namePattern = "^[A-Za-z\\s]+$";
//        String emailPattern = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
//        String contactPattern = "^\\d{10}$";
//        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
//
//        // Server-side validation
//        if (!Pattern.matches(namePattern, userName)) {
//            req.setAttribute("errorMessage", "Invalid name format. Please try again.");
//            RequestDispatcher rd = req.getRequestDispatcher("signup.html");
//            rd.forward(req, resp);
//            return;
//        }
//
//        if (!Pattern.matches(emailPattern, userEmail)) {
//            req.setAttribute("errorMessage", "Invalid email format. Please try again.");
//            RequestDispatcher rd = req.getRequestDispatcher("signup.html");
//            rd.forward(req, resp);
//            return;
//        }
//
//        if (!Pattern.matches(contactPattern, userContact)) {
//            req.setAttribute("errorMessage", "Invalid contact number format. Please enter a 10-digit number.");
//            RequestDispatcher rd = req.getRequestDispatcher("signup.html");
//            rd.forward(req, resp);
//            return;
//        }
//
//        if (!Pattern.matches(passwordPattern, userPassword)) {
//            req.setAttribute("errorMessage", "Invalid password format. Password must be at least 8 characters long and contain at least one letter and one number.");
//            RequestDispatcher rd = req.getRequestDispatcher("signup.html");
//            rd.forward(req, resp);
//            return;
//        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "root");
            ps = con.prepareStatement("INSERT INTO userdata (userName, userEmail, userContact, userPassword) VALUES (?, ?, ?, ?)");
            ps.setString(1, userName);
            ps.setString(2, userEmail);
            ps.setString(3, userContact);
            ps.setString(4, userPassword);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("User registered successfully: " + userName);
                RequestDispatcher rd = req.getRequestDispatcher("login.html");
                rd.forward(req, resp);
            } else {
                req.setAttribute("errorMessage", "Registration failed. Please try again later.");
                RequestDispatcher rd = req.getRequestDispatcher("signup.html");
                rd.forward(req, resp);
                System.out.println("Not save");
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Database connection problem.");
            RequestDispatcher rd = req.getRequestDispatcher("signup.html");
            rd.forward(req, resp);
        } finally {
            try {
                if (ps != null) ps.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
