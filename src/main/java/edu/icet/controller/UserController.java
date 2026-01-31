package edu.icet.controller;

import edu.icet.dto.LoginRequest;
import edu.icet.dto.UserRegistrationDto;
import edu.icet.entity.User;
import edu.icet.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public void registerUser(@Valid @RequestBody UserRegistrationDto userDto) {
        userService.registerUser(userDto);
    }

    @PostMapping("/login")
    public String loginUser(@RequestBody LoginRequest loginRequest) {
        boolean isValid = userService.validateUser(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );
        return isValid ? "Login Success!" : "Invalid Username or Password";
    }

    @GetMapping("/get-all")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @PutMapping("/update/{id}")
    public void updateUser(@PathVariable Long id, @Valid @RequestBody UserRegistrationDto userDto) {
        userService.updateUser(id, userDto);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}