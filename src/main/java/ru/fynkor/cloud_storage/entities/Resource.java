package ru.fynkor.cloud_storage.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import ru.fynkor.cloud_storage.enums.ResourceType;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Resource {
    private MultipartFile file;
}

// TODO: заготовка под dto
/*@Getter
public class Resource {
    private String path;
    private String name;
    private Integer size;
    private ResourceType resourceType;

    protected Resource(){}

    public Resource(String path, String name, Integer size, ResourceType resourceType){
        this.path = path;
        this.name = name;
        this.size = size;
        this.resourceType = resourceType;
    }
}*/
