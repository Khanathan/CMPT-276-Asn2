package com.kdcurd.curd.controllers;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.kdcurd.curd.models.Student;
import com.kdcurd.curd.models.StudentRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class StudentsController {
    @Autowired
    private StudentRepository studentRepo;

    // Display all students
    @GetMapping("/students/view")
    public String getAllStudents(Model model) {
        System.out.println("Showing all users");
        // get all students from database
        List<Student> students = studentRepo.findAll();
        // Finish getting all students
        model.addAttribute("stus", students);
        return "students/showAll";
    }

    // Choosing a student to delete
    @GetMapping("/students/predelete")
    public String getAllStudentsPredelete(Model model) {
        // get all students from database
        List<Student> students = studentRepo.findAll();
        // Finish getting all students
        model.addAttribute("stus", students);
        return "students/predelete";
    }

    // Deleting a student by name from the database
    @Transactional
    @PostMapping("/students/delete")
    public String deleteStudent(@RequestParam Map<String, String> delStudent, HttpServletResponse response) {
        System.out.println("DELETE student");
        String studentName = delStudent.get("name");
        studentRepo.deleteByName(studentName);
        response.setStatus(201);
        return "students/successful";
    }

    // Choosing which students to edit
    @GetMapping("/students/prepreedit")
    public String prePreEdit(Model model) {
        // get all students from database
        List<Student> students = studentRepo.findAll();
        // Finish getting all students
        model.addAttribute("stus", students);
        return "students/prepreedit";
    }

    @PostMapping("/students/preedit")
    public String preEdit(@RequestParam Map<String, String> studentToEdit, HttpServletResponse response, Model model) {
        String studentName = studentToEdit.get("name");
        List<Student> toEdit = studentRepo.findByName(studentName);
        if (toEdit.size() == 0)
            return "students/unsuccessful";
        model.addAttribute("stuToEdit", toEdit.get(0));
        return "students/preedit";
    }

    @Transactional
    @PostMapping("/students/edit")
    public String postMethodName(@ModelAttribute Student studentToEdit,
            HttpServletResponse response) {
        Student existingStudent = studentRepo.findByName(studentToEdit.getName()).get(0);
        existingStudent.setName(studentToEdit.getName());
        existingStudent.setWeight(studentToEdit.getWeight());
        existingStudent.setHeight(studentToEdit.getHeight());
        existingStudent.setGpa(studentToEdit.getGpa());
        existingStudent.setHairColor(studentToEdit.getHairColor());
        studentRepo.save(existingStudent);
        response.setStatus(201);
        return "students/successful";
    }

    // Adding a student to the database
    @PostMapping("/students/add")
    public String addStudent(@RequestParam Map<String, String> newStudent, HttpServletResponse response) {
        System.out.println("ADD student");
        String newName = newStudent.get("name");
        if (newName.equals("")) {
            return "students/unsuccessful";
        }
        try {
            int newWeight = Integer.parseInt(newStudent.get("weight"));
            int newHeight = Integer.parseInt(newStudent.get("height"));
            double newGpa = Double.parseDouble(newStudent.get("gpa"));
            String newHairColor = newStudent.get("hairColor");
            studentRepo.save(new Student(newName, newWeight, newHeight, newGpa, newHairColor));
            response.setStatus(201);
        } catch (Exception e) {
            System.out.println("Invalid number format.");
            return "students/unsuccessful";
        }
        return "students/successful";
    }
}