package br.com.drazevedo.dealrooms.application.dto;

import br.com.drazevedo.dealrooms.domain.model.DealRoom;
import br.com.drazevedo.dealrooms.domain.model.Document;
import br.com.drazevedo.dealrooms.domain.model.Folder;
import br.com.drazevedo.dealrooms.domain.model.User;
import br.com.drazevedo.dealrooms.domain.model.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class DealRoomDto {

    private String id;
    private String roomName;
    private List<String> foldersIds;
    private List<String> documentsIds;

    public static DealRoomDto from(DealRoom room) {
        return DealRoomDto.builder()
                .id(room.getId())
                .roomName(room.getName())
                .foldersIds(room.getFolders().stream().map(Folder::getId).toList())
                .documentsIds(room.getDocuments().stream().map(Document::getId).toList())
                .build();
    }

}
