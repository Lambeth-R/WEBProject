package com.example.main.controllers;

import com.example.main.domain.User;
import com.example.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class Security {

    @Autowired
    private UserService userService;

    @RequestMapping(value="/registration", method = RequestMethod.GET)
    public String registration(Map<String, Object> model) {
        model.put("userMessage", "Add new user.");
        //User user = new User();
        return "registration";
    }

    @RequestMapping(value = "/registration", method = RequestMethod.POST)
    public String createNewUser(@Valid User user, @RequestParam(value = "pass_chk") String pass_chk, BindingResult bindingResult, Map<String, Object> model) {
        if (user.getUsername() == null || user.getPassword() == null){
            model.put("userMessage", "Incorrect username or password, try again.");
            return "registration";
        }
        if (!user.getPassword().equals(pass_chk)){
                model.put("userMessage", "Passwords are not equals.");
            return "registration";
        }
        if (!userService.addUser(user)) {
            model.put("userMessage", "There is already a user registered with the user name provided.");
            return "registration";
        }
        else {
            model.put("userMessage", "User has been registered successfully.");
            return "login";
        }
    }
}
