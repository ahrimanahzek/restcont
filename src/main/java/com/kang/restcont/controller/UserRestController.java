package com.kang.restcont.controller;



import com.google.gson.Gson;
import com.kang.restcont.model.User;
import com.kang.restcont.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class UserRestController {

    private final UserService userService;

    public UserRestController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping
    public List<User> showAllUsers() {
        return userService.getAllUsers();
    }

    @PostMapping("admin/add")
    public User addUser(String user, String roleIds){
        User userModel = getUser(user, roleIds);
        userService.insert(userModel);

        return userModel;
    }


    @PostMapping("admin/edit")
    public User editUser(String user, String roleIds) {
        User userModel = getUser(user, roleIds);
        userService.update(userModel);

        return userModel;
    }


    @PostMapping("admin/delete")
    public String deleteUser(String id){
        userService.deleteUser(Integer.parseInt(id));

        return id;
    }

    private User getUser(String user, String roleIds) {
        Gson gson = new Gson();
        User userFromJson = gson.fromJson(user, User.class);

        List<String> roleIdsFromJson = gson.fromJson(roleIds, ArrayList.class);
        String[] roleIdsFromList = new String[roleIdsFromJson.size()];

        for (int i = 0; i < roleIdsFromList.length; i++) {
            roleIdsFromList[i] = roleIdsFromJson.get(i);
        }
        userFromJson.setRole(userService.getRoles(roleIdsFromList));

        return userFromJson;
    }
}
