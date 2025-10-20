package br.com.drazevedo.dealrooms.domain.model;

import br.com.drazevedo.dealrooms.domain.model.enums.DocumentStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@org.springframework.data.mongodb.core.mapping.Document
@Getter
@Setter
public class Document {
    @MongoId
    private String id;

    private String name;

    private String path;
    private String checksum;
    private DocumentStatus status;

    private Folder parentFolder;

    private Set<String> viewers = new LinkedHashSet<>();

}
