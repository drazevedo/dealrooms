package br.com.drazevedo.dealrooms.domain.model;

import lombok.Getter;
import lombok.Setter;


@org.springframework.data.mongodb.core.mapping.Document
@Getter
@Setter
public class DealRoom extends Folder {

    // A DealRoom is nothing but a root folder
    public void setParentFolder(Folder f){
        throw new UnsupportedOperationException("DealRooms can't have a parentFolder");
    }

}
