package br.com.drazevedo.dealrooms.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@org.springframework.data.mongodb.core.mapping.Document
@Getter
@Setter
@NoArgsConstructor
public class Folder {

    @MongoId
    private String id;

    @Indexed(unique=true)
    private String name;

    private Set<Folder> folders = new LinkedHashSet<>();
    private Set<Document> documents = new LinkedHashSet<>();

    private Folder parentFolder = null;

    private Set<String> viewers = new LinkedHashSet<>();

    public Folder(String id, String name, String... viewers) {
        this.id = id;
        this.name = name;
        this.viewers.addAll(List.of(viewers));
    }

}
