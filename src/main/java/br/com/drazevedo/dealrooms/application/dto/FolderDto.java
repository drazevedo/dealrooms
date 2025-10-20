package br.com.drazevedo.dealrooms.application.dto;

import br.com.drazevedo.dealrooms.domain.model.Document;
import br.com.drazevedo.dealrooms.domain.model.Folder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class FolderDto {

    private String id;
    private String name;
    private String parentId;
    private List<String> foldersIds;
    private List<String> documentsIds;
    private List<String> viewers;

    public static FolderDto from(Folder room) {
        return FolderDto.builder()
                .id(room.getId())
                .name(room.getName())
                .parentId(room.getParentFolderId())
                .foldersIds(room.getFolders().stream().map(Folder::getId).toList())
                .documentsIds(room.getDocuments().stream().map(Document::getId).toList())
                .viewers(room.getViewers().stream().toList())
                .build();
    }

}
