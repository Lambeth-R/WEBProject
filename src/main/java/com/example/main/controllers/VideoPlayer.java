package com.example.main.controllers;

import com.example.main.Streamin.MultipartFileSender;
import com.example.main.domain.Rooms;
import com.example.main.domain.User;
import com.example.main.domain.Videofiles;
import com.example.main.repos.FileRepo;
import com.example.main.service.FileService;
import com.example.main.service.RoomService;
import com.example.main.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/videos")
public class VideoPlayer {
    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private FileService fileService;

    @RequestMapping(value="/setvideo/{path}", method = RequestMethod.POST)
    public void setVideo(HttpServletRequest request, HttpServletResponse response,@PathVariable("path") String path) throws Exception {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) userService.loadUserByUsername(auth.getName());
        Videofiles videofile = fileService.FindByFilename(path);
        roomService.setFilename(user,videofile.getPath());
    }
    @GetMapping(value="/{fileName}")
    public void getVideo(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileName") String fileName) throws Exception {
        Videofiles file = fileService.FindByFilename(fileName);
        MultipartFileSender.fromPath(Path.of(file.getPath()))
                .with(request)
                .with(response)
                .serveResource();
    }
    @RequestMapping(value="/some", method=RequestMethod.GET)
    public String getRoomFile(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) userService.loadUserByUsername(auth.getName());
        Videofiles file = fileService.FindByFilename(roomService.getFilename(user));
        return file.getMask();
    }
    @RequestMapping(value="/meAdmin", method=RequestMethod.GET)
    public String isAdmin(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) userService.loadUserByUsername(auth.getName());
        Rooms room = roomService.getRoom(user);
        if (room != null && room.getAdmin().equals(user.getUsername())){
            return "true";
        }
        return "false";
    }
}
