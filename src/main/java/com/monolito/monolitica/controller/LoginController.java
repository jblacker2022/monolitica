package com.monolito.monolitica.controller;


import com.monolito.monolitica.entity.User;
import com.monolito.monolitica.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/login")
    public String login() {
        return "Login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return "home"; // Redirigir a la página de inicio
        }
        return "Login"; // Si no es correcto, mostrar el formulario de login nuevamente
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username, @RequestParam String password) {
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password); // Asegúrate de encriptar la contraseña
        userRepository.save(newUser);
        return "Login";
    }
}
