package edu.icet.controller;

import edu.icet.dto.LoginRequest;
import edu.icet.dto.UserRegistrationDto;
import edu.icet.entity.User;
import edu.icet.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    @Autowired
    private final UserServiceImpl userService;

    @PostMapping("/register")
    public void registerUser(@RequestBody UserRegistrationDto userDto) {
        userService.registerUser(userDto);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody LoginRequest loginRequest) {
        Boolean isValid = userService.validateUser(loginRequest.getUsername(), loginRequest.getPassword());
        return isValid ? "Login Success!" : "Invalid Username or Password";
    }

    @GetMapping("/get-all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/update/{id}")
    public void updateUser(@PathVariable Long id, @RequestBody UserRegistrationDto userDto) {
        userService.updateUser(id, userDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}