package br.com.drazevedo.dealrooms.application.service;

import br.com.drazevedo.dealrooms.application.dto.FolderDto;
import br.com.drazevedo.dealrooms.domain.model.DealRoom;
import br.com.drazevedo.dealrooms.domain.model.Folder;
import br.com.drazevedo.dealrooms.domain.model.User;

import java.util.List;

public interface FolderService {

    List<Folder> findAllRootFolders();

    List<Folder> findAllRootFoldersByUser(User user);

    DealRoom createRoom(String roomName);

    FolderDto addVieversToRoom(String roomId, List<String> viewersUsernames);

    FolderDto deleteViewersFromRoomOrFolder(String roomId, List<String> viewersUsernames);

    FolderDto getInfoFromRoomOrFolder(String roomId);

    Folder createFolderOn(String folderId, String folderName);

    FolderDto addVieversToFolder(String folderId, List<String> viewersUsernames);
}
