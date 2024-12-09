package org.studyeasy.SpringStarter.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.studyeasy.SpringStarter.Model.User;
import org.studyeasy.SpringStarter.Repository.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        // Check if user is logged in
        User user = (User) session.getAttribute("user");
        model.addAttribute("isLoggedIn", user != null); // Set isLoggedIn in the model
        return "home";
    }

    @GetMapping("/book")
    public String book(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("isLoggedIn", user != null);
        return "book";
    }

    @GetMapping("/buy")
    public String buy(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("isLoggedIn", user != null);
        return "buy";
    }

    @GetMapping("/login")
    public String homeLogin(Model model) {
        return "login";
    }

    @PostMapping("/login")
    public String authenticateUser(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user); // Store user in session
            return "redirect:/profile"; // Redirect to profile page
        } else {
            model.addAttribute("error", "Invalid credentials!");
            return "login"; // Return to login page with error
        }
    }
    
    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String registerUser(@ModelAttribute("user") User user, Model model) {
        // Check if email already exists
        if (userRepository.findByEmail(user.getEmail()) != null) {
            model.addAttribute("error", "Email is already registered!");
            return "signup";
        }

        // Validate full name
        if (user.getFullName() == null || user.getFullName().isEmpty()) {
            model.addAttribute("error", "Full name is required.");
            return "signup";
        }

        // Save user and redirect to login
        userRepository.save(user);
        return "redirect:/login";
    }

    @GetMapping("/smarthome")
    public String smarthome(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        model.addAttribute("isLoggedIn", user != null);
        return "smarthome";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // Clear the session
        return "redirect:/login"; // Redirect to login page after logout
    }

    // Profile page mapping
    @GetMapping("/profile")
public String profile(Model model, HttpSession session) {
    User user = (User) session.getAttribute("user");
    model.addAttribute("isLoggedIn", user != null); // Set isLoggedIn in the model
    if (user != null) {
        model.addAttribute("user", user); // Pass user to the model for display
    }
    return "profile"; // Return the profile view
}

}
