package com.example.main.service;

import com.example.main.domain.User;
import com.example.main.domain.Videofiles;
import com.example.main.repos.FileRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;

@Service
public class FileService {
    @Autowired
    private FileRepo fileRepo;
    private Iterable<Videofiles> sort (ArrayList<Videofiles> files){
        ArrayList<Videofiles> res = new ArrayList<>();
        var iter = files.iterator();
        while (iter.hasNext())
        {
            var it = files.iterator();
            var temp = it.next();
            while (it.hasNext()){
                var item = it.next();
                if (item.getPath().length() > temp.getPath().length()){
                    temp = item;
                }
            }
            res.add(temp);
            files.remove(temp);
        }
        return res;
    }
    public Iterable<Videofiles> getuserFiles(User user){
        Iterable<Videofiles> videofiles = fileRepo.findAll();
        var i = videofiles.iterator();
        while (i.hasNext()){
            String file_owner = i.next().getOwner();
            if (file_owner != null && !file_owner.equals(user.getUsername())){
                i.remove();
            }
        }
        return sort((ArrayList<Videofiles>) videofiles);
    }
    public void addFile(String user, String filepath)
    {
        Videofiles Vfile = new Videofiles();
        Vfile.setOwner(user);
        String filename = filepath;
        int stringDiv = filename.indexOf('/');
        while (stringDiv != -1) {
            filename = filename.substring(filename.indexOf('/') + 1);
            stringDiv = filename.indexOf('/');
        }
        Vfile.setMask(filename);
        Vfile.setPath(filepath);
        fileRepo.save(Vfile);
    }
    public FileService(FileRepo fileRepo) {
        fileRepo.deleteAll();
        String default_path = "videos/";
        File file = new File(default_path);
        var sub_files = file.listFiles();
        for (int i = 0; i < sub_files.length;i++){
            if (!sub_files[i].isDirectory()){
                Videofiles videofile = new Videofiles();
                videofile.setOwner(null);
                videofile.setMask(sub_files[i].getName());
                videofile.setPath(default_path + sub_files[i].getName());
                fileRepo.save(videofile);
            }
            else {
                var s_files = sub_files[i].listFiles();
                for (int j = 0; j < s_files.length;j++){
                    var user_files = s_files[j].listFiles();
                    for (int k = 0; k < user_files.length;k++){
                        Videofiles Vfile = new Videofiles();
                        Vfile.setOwner(s_files[j].getName());
                        /*int stringDiv = user_files[k].getName().indexOf('_');
                        if (stringDiv == -1)
                            stringDiv = 0;*/
                        Vfile.setMask(user_files[k].getName());//.substring(stringDiv));
                        Vfile.setPath(default_path + sub_files[i].getName()+ '/' + s_files[j].getName() + '/' +user_files[k].getName());
                        fileRepo.save(Vfile);
                    }
                }
            }
        }
    }
    public Videofiles FindByFilename(String path)
    {
        while (path.indexOf('/') != -1){
            path = path.substring(path.indexOf('/') + 1);
        }
        return fileRepo.findByMask(path);
    }
}
