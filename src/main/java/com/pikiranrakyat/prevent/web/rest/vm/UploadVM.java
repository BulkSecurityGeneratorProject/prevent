package com.pikiranrakyat.prevent.web.rest.vm;

import com.pikiranrakyat.prevent.domain.FileManager;
import com.pikiranrakyat.prevent.service.dto.UploadDTO;

import java.time.ZonedDateTime;

/**
 * Created by aseprojali on 26/10/16.
 */
public class UploadVM extends UploadDTO {

    private Long id;
    private String createdBy;
    private ZonedDateTime createdDate;
    private String lastModifiedBy;
    private ZonedDateTime lastModifiedDate;

    public UploadVM() {
    }

    public UploadVM(FileManager fileManager) {
        super(fileManager);
        this.id = fileManager.getId();
        this.createdBy = fileManager.getCreatedBy();
        this.createdDate = fileManager.getCreatedDate();
        this.lastModifiedBy = fileManager.getLastModifiedBy();
        this.lastModifiedDate = fileManager.getLastModifiedDate();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ZonedDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public ZonedDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(ZonedDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public String toString() {
        return "UploadVM{" +
            "id=" + id +
            ", createdBy='" + createdBy + '\'' +
            ", createdDate=" + createdDate +
            ", lastModifiedBy='" + lastModifiedBy + '\'' +
            ", lastModifiedDate=" + lastModifiedDate +
            '}';
    }
}
