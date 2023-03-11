package com.group9.firestore.controller;

import com.group9.firestore.document.User;
import com.group9.firestore.dto.CreateUserDto;
import com.group9.firestore.dto.UpdateUserDto;
import com.group9.firestore.mapper.UserMapper;
import com.group9.firestore.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createUser(@RequestBody final CreateUserDto createUserDto) throws ExecutionException, InterruptedException {
        return userService.createUser(UserMapper.toUser(createUserDto));
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public User getUser(@PathVariable final UUID id) throws ExecutionException, InterruptedException {
        var user = userService.getUserById(id.toString());
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return user;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateUser(@PathVariable final UUID id, @RequestBody final UpdateUserDto updateUserDto) throws ExecutionException, InterruptedException {
        var user = userService.getUserById(id.toString());
        userService.updateUser(UserMapper.toUser(updateUserDto, user));
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<User> getUsers(
            @Nullable @RequestParam final String firstName,
            @Nullable @RequestParam final String lastName,
            @Nullable @RequestParam final String streetName,
            @Nullable @RequestParam final String city
    ) throws ExecutionException, InterruptedException {
        return userService.getUsers(firstName, lastName, streetName, city);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllUsers() {
        userService.deleteAllUsers();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUserById(@PathVariable final UUID id) {
        userService.deleteUserById(id.toString());
    }

    @GetMapping("/listen/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void listen(@PathVariable final UUID id) {
        userService.listenToDocuments(id.toString());
    }
}