package com.pikiranrakyat.prevent.service.dto;

import com.pikiranrakyat.prevent.domain.FileManager;
import com.pikiranrakyat.prevent.domain.User;

/**
 * Created by aseprojali on 26/10/16.
 */

public class UploadDTO {
    private String originalFileName;
    private String newFileName;
    private String path;
    private String extension;
    private long size;
    private User user;

    public UploadDTO() {
    }

    public UploadDTO(FileManager fileManager) {
        this(fileManager.getOriginal(), fileManager.getName(), fileManager.getPath(), fileManager.getExtension(), fileManager.getSize(), fileManager.getUser());
    }

    public UploadDTO(String originalFileName, String newFileName, String path, String extension, long size, User user) {
        this.originalFileName = originalFileName;
        this.newFileName = newFileName;
        this.path = path;
        this.extension = extension;
        this.size = size;
        this.user = user;
    }


    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getNewFileName() {
        return newFileName;
    }

    public void setNewFileName(String newFileName) {
        this.newFileName = newFileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "UploadDTO{" +
            "originalFileName='" + originalFileName + '\'' +
            ", newFileName='" + newFileName + '\'' +
            ", path='" + path + '\'' +
            ", extension='" + extension + '\'' +
            ", size=" + size +
            ", user=" + user +
            '}';
    }
}
