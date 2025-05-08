package org.example.controllers;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.example.entities.User;
import org.example.entities.Employee;
import org.example.services.AuthService;
import org.example.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/login")
    public String showLoginForm() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "auth/register";
        }

        try {
            authService.registerUser(user);
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please login.");
            return "redirect:/auth/login";
        } catch (RuntimeException e) {
            result.rejectValue("username", "error.user", e.getMessage());
            return "auth/register";
        }
    }

    @GetMapping("/employee-login")
    public String showEmployeeLoginPage(HttpSession session) {
        // If already logged in as employee, redirect to dashboard
        if (session.getAttribute("isEmployee") != null && (Boolean) session.getAttribute("isEmployee")) {
            return "redirect:/employees/dashboard";
        }
        return "auth/employee-login";
    }

    @PostMapping("/employee-login")
    public String processEmployeeLogin(@RequestParam String username,
                                     @RequestParam String password,
                                     HttpSession session,
                                     RedirectAttributes redirectAttributes) {
        try {
            Employee employee = employeeService.findByUsername(username);
            
            if (employee != null && passwordEncoder.matches(password, employee.getPassword())) {
                // Store employee information in session
                session.setAttribute("employeeId", employee.getEmployeeId());
                session.setAttribute("employeeName", employee.getFirstName() + " " + employee.getLastName());
                session.setAttribute("isEmployee", true);
                session.setMaxInactiveInterval(30 * 60); // 30 minutes
                
                // Redirect to dashboard
                return "redirect:/employees/dashboard";
            }

            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/auth/employee-login?error=true";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "An error occurred during login. Please try again.");
            return "redirect:/auth/employee-login?error=true";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        try {
            // Clear session attributes
            session.removeAttribute("employeeId");
            session.removeAttribute("employeeName");
            session.removeAttribute("isEmployee");
            session.invalidate();
        } catch (Exception e) {
            // Log the error but continue with logout
            System.err.println("Error during logout: " + e.getMessage());
        }
        
        return "redirect:/auth/employee-login";
    }
} 