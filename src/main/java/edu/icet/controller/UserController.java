package edu.icet.controller;

import edu.icet.dto.UserRegistrationDto;
import edu.icet.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
}