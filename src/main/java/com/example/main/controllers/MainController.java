package com.example.main.controllers;

import com.example.main.domain.Rooms;
import com.example.main.domain.User;
import com.example.main.domain.Videofiles;
import com.example.main.repos.FileRepo;
import com.example.main.service.FileService;
import com.example.main.service.RoomService;
import com.example.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
public class
MainController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @RequestMapping(value="/", method=RequestMethod.GET)
    public String main(Map<String,Object> model){
        model.put("message", "Welcome, stranger!"); return "index";
    }

    @GetMapping(value="/logged")
    public String home(Map<String, Object> model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) userService.loadUserByUsername(auth.getName());
        String rName = roomService.getUserRoom(user);
        model.put("userName", "Login: " + user.getUsername());
        model.put("userRoom", "Your room is: " + rName);
        Iterable<Videofiles> videofiles = fileService.getuserFiles(user);
        model.put("files",videofiles);
        return "logged";
    }

    @GetMapping("/croom")
    public String create(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        roomService.createRoom((User)userService.loadUserByUsername(auth.getName()));
        return "redirect:/logged";
    }
    @PostMapping("/join_invite")
    public String join(@RequestParam(value = "join_invite") String join_invite){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) userService.loadUserByUsername(auth.getName());
        roomService.setRoom(user,join_invite);
        return "redirect:/logged";
    }
    @GetMapping("/acc_manage")
    public String acc_manage(Map<String, Object> model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) userService.loadUserByUsername(auth.getName());
        model.put("userMessage", "Hello " + user.getUsername());
        return "acc_manage";
    }
    @PostMapping("/chg_psw")
    public String change_password(Map<String,Object> model,
                                  @RequestParam(value = "old_password") String op,
                                  @RequestParam(value = "new_password") String np,
                                  @RequestParam(value = "new_password_check") String npr){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) userService.loadUserByUsername(auth.getName());

        if (!bCryptPasswordEncoder.matches(op,user.getPassword())) {
            model.put("userMessage", "Old password is incorrect.");
            return "redirect:/login";
        }
        if (np.equals(npr)){
            user.setPassword(bCryptPasswordEncoder.encode(np));
            model.put("userMessage", "Password successfully changed.");
            return "redirect:/login";
        }
        model.put("userMessage", "Passwords are not equal.");
        return "redirect:/logged";
    }
    @PostMapping("/erase_me")
    public String del_acc(Map<String,Object> model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) userService.loadUserByUsername(auth.getName());
        Rooms room = roomService.getMyRoom(user);
        if (room != null)
            roomService.delete(room);
        userService.deleteUser(user);
        model.put("userMessage", "User is deleted");
        return "redirect:/login";
    }
}
