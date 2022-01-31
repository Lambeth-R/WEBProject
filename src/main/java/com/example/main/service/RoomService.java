package com.example.main.service;

import com.example.main.domain.Rooms;
import com.example.main.domain.UR;
import com.example.main.domain.User;
import com.example.main.repos.RoomRepo;
import com.example.main.repos.URRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoomService {
    @Autowired
    private RoomRepo roomRepo;
    @Autowired
    private URRepo urRepo;
    public void createRoom(User user){
        Rooms room = new Rooms(user.getUsername());
        for (UR u: urRepo.findAll()){
            if (u.getuID() == user.getId()){
                urRepo.delete(u);
            }
        }
        for (Rooms r: roomRepo.findAll()){
            if (r.getAdmin().equals(user.getUsername())){
                roomRepo.delete(r);
            }
        }
        roomRepo.save(room);
        UR ur = new UR(user.getId(), room.getId());
        urRepo.save(ur);
    }
    public String getUserRoom(User user){
        Rooms room = getRoom(user);
        if (room != null) {
            return room.getName();
        }
        else {
            return null;
        }
    }
    public Rooms getRoom(User user){
        if(user == null)
        {
            return null;
        }
        UR ur = urRepo.findByUserID(user.getId());
        if (ur != null) {
            return roomRepo.findById(ur.getrID());
        }
        return null;
    }
    public void setRoom(User user, String invite){
        Iterable<Rooms> rooms = roomRepo.findAll();
        int rId = 0;
        for (Rooms r: rooms){
            if (r.getName().equals(invite.substring(0,8))){
                rId = r.getId();
            }
        }
        for (UR u: urRepo.findAll()){
            if (u.getuID() == user.getId()){
                urRepo.delete(u);
            }
        }
        UR ur = new UR(user.getId(), rId);
        urRepo.save(ur);
    }
    public boolean setTime(User user, String time){
        Rooms room = roomRepo.findById(urRepo.findByUserID(user.getId()).getrID());
        if (!room.getAdmin().equals(user.getUsername()))
            return false;
        room.setCur_time(time);
        roomRepo.save(room);
        return true;
    }
    public String getTime(User user){
        Rooms room = getRoom(user);
        if (room != null) {
            return room.getCur_time();
        }
        else {
            return null;
        }
    }
    public void setFilename(User user, String fileName){
        Rooms room = getRoom(user);
        if (room == null) {
            return;
        }
        if (user.getUsername().equals(room.getAdmin_name())){
            room.setFilename(fileName);
            roomRepo.save(room);
        }
    }
    public String getFilename(User user){
        Rooms room = getRoom(user);
        if (room != null) {
            return room.getFilename();
        }
        else {
            return null;
        }
    }
    public Rooms getMyRoom(User user){
        var rooms = roomRepo.findAll().iterator();
        while (rooms.hasNext()){
            var troom = rooms.next();
            if (troom.getAdmin().equals(user.getUsername()))
                return troom;
        }
        return null;
    }
    public void delete(Rooms rooms){
        roomRepo.delete(rooms);
    }
}
