package com.pikiranrakyat.prevent.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A FileManager.
 */
@Entity
@Table(name = "file_manager")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "filemanager")
public class FileManager extends AbstractAuditingEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "original", nullable = false)
    private String original;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "path", nullable = false)
    private String path;

    @NotNull
    @Column(name = "extension", nullable = false)
    private String extension;

    @NotNull
    @Column(name = "size", nullable = false)
    private Long size;

    @ManyToOne
    @NotNull
    private User user;

    public FileManager() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOriginal() {
        return original;
    }

    public FileManager original(String original) {
        this.original = original;
        return this;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getName() {
        return name;
    }

    public FileManager name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public FileManager path(String path) {
        this.path = path;
        return this;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getExtension() {
        return extension;
    }

    public FileManager extension(String extension) {
        this.extension = extension;
        return this;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public Long getSize() {
        return size;
    }

    public FileManager size(Long size) {
        this.size = size;
        return this;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public User getUser() {
        return user;
    }

    public FileManager user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FileManager fileManager = (FileManager) o;
        if(fileManager.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, fileManager.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "FileManager{" +
            "id=" + id +
            ", original='" + original + "'" +
            ", name='" + name + "'" +
            ", path='" + path + "'" +
            ", extension='" + extension + "'" +
            ", size='" + size + "'" +
            '}';
    }
}
