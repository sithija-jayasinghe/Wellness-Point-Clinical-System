package edu.icet.controller;

import edu.icet.dto.LoginRequest;
import edu.icet.dto.UserRegistrationDto;
import edu.icet.entity.User;
import edu.icet.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserRegistrationDto userDto) {
        userService.registerUser(userDto);
        return "User Registered Successfully!";
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody LoginRequest loginRequest) {
        Boolean isValid = userService.validateUser(loginRequest.getUsername(), loginRequest.getPassword());

        if (isValid) {
            return "Login Success!";
        } else {
            return "Invalid Username or Password";
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody UserRegistrationDto userDto) {
        return userService.updateUser(id, userDto);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User Deleted Successfully!";
    }
}