package br.com.drazevedo.dealrooms.domain.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.LinkedList;
import java.util.List;

@org.springframework.data.mongodb.core.mapping.Document
@Getter
@Setter
public class DealRoom {

    @MongoId
    private String id;

    @Indexed(unique=true)
    private String name;

    private List<Folder> folders = new LinkedList<>();
    private List<Document> documents =  new LinkedList<>();

    private List<User> viewers = new LinkedList<>();
}
