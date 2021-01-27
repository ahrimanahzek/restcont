package com.kang.restcont.controller;

import com.kang.restcont.model.Role;
import com.kang.restcont.model.User;
import com.kang.restcont.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class UserController extends HttpServlet {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "login")
    public String loginGet() {
        return "login";
    }


    @GetMapping(value = "admin")
    public String adminGet(ModelMap model, HttpSession httpSession) {

        model.addAttribute("user", httpSession.getAttribute("user"));

        List<User> allUsers = userService.getAllUsers();
        model.addAttribute("users", allUsers);

        List<Role> allRoles = userService.getAllRoles();
        model.addAttribute("roles", allRoles);

        Map<User, List<List<String>>> usersWithRoles = new HashMap<>();
        allUsers.forEach(user -> usersWithRoles.put(user, userService.getUserRoles(allRoles, user)));
        model.addAttribute("usersWithRoles", usersWithRoles);

        return "admin";
    }


    @PostMapping(value = "admin/add")
    public String adminAddPost(User user, String[] roleIds) {

        user.setRole(userService.getRoles(roleIds));
        userService.insert(user);

        return "redirect:/admin";
    }


    @GetMapping(value = "admin/edit")
    public String adminEditGet(ModelMap model, @RequestParam("id") Integer id) {

        User user = userService.getUser(id);
        model.addAttribute("user", user);
        List<Role> roles = userService.getAllRoles();

        roles.forEach(role -> role.setInUser(user.isRoleInUser(role)));
        model.addAttribute("roles", roles);

        return "admin/edit";
    }


    @PostMapping(value = "admin/edit")
    public String adminEditPost(User user, String[] roleIds) {

        user.setRole(userService.getRoles(roleIds));
        userService.update(user);

        return "redirect:/admin";
    }


    @PostMapping(value = "admin/delete")
    public String adminDeletePost(@RequestParam("id") Integer id) {

        userService.deleteUser(id);

        return "redirect:/admin";
    }


    @GetMapping(value = "user")
    public String userGet(ModelMap modelMap, HttpSession httpSession) {

        modelMap.addAttribute("user", httpSession.getAttribute("user"));

        return "user";
    }

}