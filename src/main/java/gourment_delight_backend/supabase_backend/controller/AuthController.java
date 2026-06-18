package gourment_delight_backend.supabase_backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import gourment_delight_backend.supabase_backend.entity.User;
import gourment_delight_backend.supabase_backend.repository.UserRepository;
import gourment_delight_backend.supabase_backend.dto.LoginRequest;
import gourment_delight_backend.supabase_backend.security.JwtUtil;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public String register(@RequestBody User user) {

        userRepository.save(user);

        return "User Registered Successfully!";
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PutMapping("/update/{id}")
    public User updateUser(
            @PathVariable Long id,
            @RequestBody User updatedUser) {

        User user = userRepository.findById(id).orElseThrow();

        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        user.setPassword(updatedUser.getPassword());

        return userRepository.save(user);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {

        userRepository.deleteById(id);

        return "User Deleted Successfully";
    }

    @PostMapping("/login")
public String login(@RequestBody LoginRequest loginRequest) {

    User user = userRepository.findByEmail(loginRequest.getEmail())
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (!user.getPassword().equals(loginRequest.getPassword())) {
        throw new RuntimeException("Invalid Password");
    }

    return JwtUtil.generateToken(user.getEmail());
}

@GetMapping("/profile")
public String profile(
        @RequestHeader("Authorization") String authHeader) {

    String token = authHeader.replace("Bearer ", "");

    String email = JwtUtil.validateToken(token);

    return "Welcome " + email;
}

}