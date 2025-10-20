package br.com.drazevedo.dealrooms.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@org.springframework.data.mongodb.core.mapping.Document
@Getter
@Setter
@NoArgsConstructor
public class DealRoom extends Folder {

    public DealRoom(String id, String name, String... viewers) {
        super(id, name, viewers);
    }

    // A DealRoom is nothing but a root folder
    public void setParentFolderId(String id){
        throw new UnsupportedOperationException("DealRooms can't have a parentFolder");
    }

}
