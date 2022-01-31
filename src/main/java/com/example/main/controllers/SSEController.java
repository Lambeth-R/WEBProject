package com.example.main.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Null;

import com.example.main.domain.Rooms;
import com.example.main.domain.User;
import com.example.main.service.RoomService;
import com.example.main.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping(value = "/sse")
public class SSEController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoomService roomService;

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Long SseTimeoutDay = 86400000L;

    private HashMap<String, Vector<SseEmitter>> sseEndpoints = new HashMap<>();
    private HashMap<String, Vector<SseEmitter>> sseAdminEndpoints = new HashMap<>();
    private HashMap<String, SseEmitter> sseUsers = new HashMap<>();

    private String getRoomName(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) userService.loadUserByUsername(auth.getName());
        return roomService.getUserRoom(user);
    }

    private void adminControlHandle(String savetime, String action){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) userService.loadUserByUsername(auth.getName());
        roomService.setTime(user,savetime);
        Rooms room = roomService.getRoom(user);
        sseEmit(room.getName(),room, action);
    }
    private void userCheck()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) userService.loadUserByUsername(auth.getName());
        var temp = sseUsers.get(user.getuserName());
        if (temp != null)
            temp.complete();
    }

    @RequestMapping(value="/admin_notify", method=RequestMethod.GET)
    public SseEmitter userHereNotyfier(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) userService.loadUserByUsername(auth.getName());
        Rooms room = roomService.getRoom(user);
        String roomName = room.getName();
        if (!room.getAdmin().equals(user.getUsername())){
            return null;
        }
        userCheck();
        SseEmitter sseEmitter = new SseEmitter(SseTimeoutDay);
        if (!sseAdminEndpoints.containsKey(roomName)){
            Vector<SseEmitter> newVec = new Vector<>();
            sseAdminEndpoints.put(roomName,newVec);
        }/*
        sseEmitter.onCompletion(() -> {
            this.sseAdminEndpoints.get(roomName).remove(sseEmitter);
        });
        sseEmitter.onTimeout(() -> {
            this.sseAdminEndpoints.get(roomName).remove(sseEmitter);
        });
        sseEmitter.onError(err -> {
            this.sseAdminEndpoints.get(roomName).remove(sseEmitter);
        });*/
        sseAdminEndpoints.get(roomName).add(sseEmitter);
        sseUsers.put(user.getuserName(),sseEmitter);
        return sseEmitter;
    }

    @RequestMapping(value="/action_notify", method=RequestMethod.GET)
    public SseEmitter sseNotyfier(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) userService.loadUserByUsername(auth.getName());
        String roomName = getRoomName();
        SseEmitter emitter = new SseEmitter(SseTimeoutDay);
        userCheck();
        if (!sseEndpoints.containsKey(roomName)){
            Vector<SseEmitter> newVec = new Vector<>();
            sseEndpoints.put(roomName, newVec);
        }/*
        emitter.onCompletion(() -> {
            this.sseEndpoints.get(roomName).remove(emitter);
        });
        emitter.onTimeout(() -> {
            this.sseEndpoints.get(roomName).remove(emitter);
        });
        emitter.onError(err -> {
            this.sseEndpoints.get(roomName).remove(emitter);
            logger.info("Closed/error");
        });*/
        sseEndpoints.get(roomName).add(emitter);
        sseUsers.put(user.getuserName(),emitter);
        return emitter;
    }

    @RequestMapping(value="/stime/{savetime}", method= RequestMethod.POST)
    public void setRoomTime(HttpServletRequest request, HttpServletResponse response, @PathVariable("savetime") String savetime) throws IOException {
        adminControlHandle(savetime,"ctime");
    }
    @RequestMapping(value="/seek/{savetime}", method=RequestMethod.POST)
    public void seekALL(HttpServletRequest request, HttpServletResponse response, @PathVariable("savetime") String savetime)
    {
        adminControlHandle(savetime, "seeked");
    }
    @RequestMapping(value="/play/{savetime}", method=RequestMethod.POST)
    public void playALL(HttpServletRequest request, HttpServletResponse response, @PathVariable("savetime") String savetime)
    {
        adminControlHandle(savetime, "played");
    }
    @RequestMapping(value="/pause/{savetime}", method=RequestMethod.POST)
    public void pauseALL(HttpServletRequest request, HttpServletResponse response, @PathVariable("savetime") String savetime)
    {
        adminControlHandle(savetime, "paused");
    }

    @RequestMapping(value="/immahere", method=RequestMethod.POST)
    public void userHere(HttpServletRequest request, HttpServletResponse response)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) userService.loadUserByUsername(auth.getName());

        Iterator<SseEmitter> sseIter = sseAdminEndpoints.get(getRoomName()).iterator();

        while (sseIter.hasNext()){
            SseEmitter emitter = sseIter.next();

            SseEmitter.SseEventBuilder sseEventBuilder = SseEmitter.event()
                    .name("user_here")
                    .data(user.getUsername());
            try{
                emitter.send(sseEventBuilder);
            } catch (Exception e) {
                emitter.complete();
                sseIter.remove();
            }

        }
    }
    private void sseEmit(String roomName, Rooms room, String action)
    {
        Vector<SseEmitter> roomEmitters = sseEndpoints.get(roomName);
        if (roomEmitters == null)
            return;
        Iterator<SseEmitter> emitterIter = roomEmitters.iterator();
        while (emitterIter.hasNext()){
            SseEmitter emitter = emitterIter.next();
            SseEmitter.SseEventBuilder sseEventBuilder = SseEmitter.event()
                    .name(action)
                    .data(room.getCur_time());
            try {
                emitter.send(sseEventBuilder);
            }
            catch (Exception e){
                emitter.complete();
                emitterIter.remove();
            }
        }
    }


}