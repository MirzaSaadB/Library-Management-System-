package Library_Managment_System;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/login")
public class LogIn extends HttpServlet {

    Connection con = null;
    PreparedStatement ps = null;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String userEmail = req.getParameter("userEmail");
        String userPassword = req.getParameter("userPassword");

        // Email and password validation patterns
//        String emailPattern = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
//        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$";
//
//        // Server-side validation
//        if (!Pattern.matches(emailPattern, userEmail)) {
//            req.setAttribute("errorMessage", "Invalid email format. Please try again.");
//            RequestDispatcher rd = req.getRequestDispatcher("login.html");
//            rd.forward(req, resp);
//            return;
//        }
//
//        if (!Pattern.matches(passwordPattern, userPassword)) {
//            req.setAttribute("errorMessage", "Invalid password format. Password must be at least 8 characters long and contain at least one letter and one number.");
//            RequestDispatcher rd = req.getRequestDispatcher("login.html");
//            rd.forward(req, resp);
//            return;
//        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/library", "root", "root");
            ps = con.prepareStatement("SELECT * FROM userdata WHERE userEmail=? AND userPassword=?");
            ps.setString(1, userEmail);
            ps.setString(2, userPassword);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                System.out.println("Login successful for user: " + userEmail);
                RequestDispatcher rd = req.getRequestDispatcher("home.html");
                rd.forward(req, resp);
            } else {
                System.out.println("Login failed for user: " + userEmail);
                req.setAttribute("errorMessage", "Invalid email or password. Please try again.");
                RequestDispatcher rd = req.getRequestDispatcher("login.html");
                rd.forward(req, resp);
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Database connection problem.");
            RequestDispatcher rd = req.getRequestDispatcher("login.html");
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
