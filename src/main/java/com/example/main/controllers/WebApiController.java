package com.example.main.controllers;

import com.example.main.component.SpringRequestHelper;
import com.example.main.domain.User;
import com.example.main.domain.Videofiles;
import com.example.main.service.FileService;
import com.example.main.service.RoomService;
import com.example.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class WebApiController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private SpringRequestHelper requestHelper;

    @RequestMapping(value="/get_csrf_api", method = RequestMethod.GET)
    public Map<String, String> getLoginApiJson(HttpServletRequest request) {
        return requestHelper.getTokenInfo(request);
    }
    @GetMapping("/what_roomname")
    public String what_roomname(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) userService.loadUserByUsername(auth.getName());
        return roomService.getUserRoom(user);
    }
    @GetMapping("/videofiles")
    public Map<String, List<String>> videofiles(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) userService.loadUserByUsername(auth.getName());
        Iterable<Videofiles> videofiles = fileService.getuserFiles(user);
        Map<String, List<String>> model = new HashMap<>();
        List<String> file_list = new ArrayList<>();
        var t = videofiles.iterator();
        while (t.hasNext()) {
            file_list.add(t.next().getMask());
        }
        model.put("files",file_list);
        return model;
    }
}
