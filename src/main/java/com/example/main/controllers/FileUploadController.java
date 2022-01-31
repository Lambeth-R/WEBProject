package com.example.main.controllers;

import com.example.main.domain.Videofiles;
import com.example.main.service.FileService;
import org.apache.commons.fileupload.*;
import org.apache.commons.fileupload.util.Streams;
import org.apache.commons.io.IOUtils;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.checkerframework.common.aliasing.qual.Unique;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Controller
public class FileUploadController {
    @Autowired
    private FileService fileService;

    @RequestMapping(value="/doupload", method=RequestMethod.POST)
    public String upload(HttpServletRequest request) throws IOException, FileUploadException {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(
                new File(System.getProperty("java.io.tmpdir")));
        factory.setSizeThreshold(
                DiskFileItemFactory.DEFAULT_SIZE_THRESHOLD);
        factory.setFileCleaningTracker(null);

        ServletFileUpload upload = new ServletFileUpload(factory);

        List items = upload.parseRequest(request);

        Iterator iter = items.iterator();
        while (iter.hasNext()) {
            FileItem item = (FileItem) iter.next();
            String upload_path = "videos/user_upload/"+ auth.getName() +'/';
            File uploadDir = new File(upload_path);
            if (!uploadDir.exists()) {
                if (!uploadDir.mkdir())
                {
                    uploadDir = new File("videos");
                    if(!uploadDir.exists())
                    {
                        uploadDir.mkdir();
                    }
                    uploadDir = new File("videos/user_upload");
                    if(!uploadDir.exists())
                    {
                        uploadDir.mkdir();
                    }
                    uploadDir = new File("videos/user_upload/"+ auth.getName());
                    if(!uploadDir.exists())
                    {
                        uploadDir.mkdir();
                    }
                };
            }
            String resultFilename = (UUID.randomUUID().toString() + "_" + item.getName()).replace(' ','_');
            if (!item.isFormField()) {
                try (
                        InputStream uploadedStream = item.getInputStream();
                        OutputStream out = new FileOutputStream(upload_path + resultFilename);) {
                    IOUtils.copy(uploadedStream, out);
                    fileService.addFile(auth.getName(), upload_path + resultFilename);
                }
            }

        }
        return "redirect:/logged";
    }
}
