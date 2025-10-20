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
public class DocumentDto {

    private String id;
    private String name;
    private String parentId;
    private String status;
    private String checksum;
    private String path;
    private List<String> viewers;

    public static DocumentDto from(Document document) {
        return DocumentDto.builder()
                .id(document.getId())
                .name(document.getName())
                .parentId(document.getParentFolder().getId())
                .status(document.getStatus().name())
                .checksum(document.getChecksum())
                .path(document.getPath())
                .viewers(document.getViewers().stream().toList())
                .build();
    }

}
