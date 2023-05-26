/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import util.DatabaseConnection;
import bean.Student;
/**
 *
 * @author PC
 */
@WebServlet(name = "studentGradeController", urlPatterns = {"/studentGrade"})
public class studentGradeController extends HttpServlet {

    Connection con;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String subjectCode = request.getParameter("subjectCode");

        try {
            con = DatabaseConnection.connectToDatabase("jdbc:mysql://localhost/abc_university_p", "root", "");

            String query = "SELECT student.student_number, student.student_name, student_subject.grade FROM student "
                    + "INNER JOIN student_subject ON student.student_number = student_subject.student_number "
                    + "WHERE student_subject.subject_code = ?";
            PreparedStatement statement = con.prepareStatement(query);
            statement.setString(1, subjectCode);
            ResultSet resultSet = statement.executeQuery();

            List<Student> subjectStudents = new ArrayList<>();
            while (resultSet.next()) {
                Student subjectStudent = new Student();
                subjectStudent.setNumber(resultSet.getString("student_number"));
                subjectStudent.setName(resultSet.getString("student_name"));
                subjectStudent.setGrade(resultSet.getString("grade"));
                subjectStudents.add(subjectStudent);
            }
            con.close();
            statement.close();
            request.setAttribute("students", subjectStudents);
            request.setAttribute("subjectCode", subjectCode);
            request.getRequestDispatcher("user/studentGrade.jsp").forward(request, response);

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
