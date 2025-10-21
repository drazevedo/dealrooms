package br.com.drazevedo.dealrooms.domain.model;

import br.com.drazevedo.dealrooms.domain.model.enums.DocumentStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@org.springframework.data.mongodb.core.mapping.Document
@Getter
@Setter
@NoArgsConstructor
public class Document {
    @MongoId
    private String id;

    private String name;

    private String path;
    private String checksum;
    private DocumentStatus status;

    private String parentFolderId;

    private Set<String> viewers = new LinkedHashSet<>();

    public Document(String name, String path, String checksum, String parentFolderId, String... viewers){
        this.name = name;
        this.path = path;
        this.checksum = checksum;
        this.parentFolderId = parentFolderId;
        this.status = DocumentStatus.UNCHECKED;
        this.viewers.addAll(List.of(viewers));
    }

}
