package br.com.drazevedo.dealrooms.application.service;

import br.com.drazevedo.dealrooms.application.dto.FolderDto;
import br.com.drazevedo.dealrooms.domain.model.DealRoom;
import br.com.drazevedo.dealrooms.domain.model.Folder;
import br.com.drazevedo.dealrooms.domain.model.User;
import br.com.drazevedo.dealrooms.domain.repository.FolderRepository;
import br.com.drazevedo.dealrooms.domain.repository.UserRepository;
import br.com.drazevedo.dealrooms.interfaces.controllers.utils.LoggedUserUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FolderServiceImpl implements FolderService {

    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    public FolderServiceImpl(FolderRepository folderRepository, UserRepository userRepository) {
        this.folderRepository = folderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Folder> findAllRootFolders() {
        return folderRepository.findAllByParentFolderNull();
    }

    @Override
    public List<Folder> findAllRootFoldersByUser(User user) {
        return folderRepository.findAllByParentFolderNullAndViewersContaining(user.getUsername());
    }

    @Override
    public DealRoom createRoom(String roomName) {
        User loggedUser = LoggedUserUtils.getLoggedUser();

        folderRepository.findAnyByParentFolderNullAndName(roomName).ifPresent(dealRoom -> {
            throw new RuntimeException("Room already exists");
        });

        DealRoom room = new DealRoom();
        room.setName(roomName);
        room.getViewers().add(loggedUser.getUsername());

        return folderRepository.save(room);
    }

    @Override
    public FolderDto addVieversToRoom(String roomId, List<String> viewersUsernames) {

        Folder room = folderRepository.findByParentFolderNullAndId(roomId).orElseThrow();
        List<User> newViewers = userRepository.findAllByUsernameIn(viewersUsernames);
        room.getViewers().addAll(newViewers.stream().map(User::getUsername).toList());
        folderRepository.save(room);

        return FolderDto.from(room);
    }

    @Override
    public FolderDto deleteViewersFromRoomOrFolder(String folderId, List<String> viewersUsernames) {

        Folder room = folderRepository.findById(folderId).orElseThrow();
        List<User> newViewers = userRepository.findAllByUsernameIn(viewersUsernames);
        newViewers.stream().map(User::getUsername).forEach(room.getViewers()::remove);
        folderRepository.save(room);

        return FolderDto.from(room);
    }

    @Override
    public FolderDto getInfoFromRoomOrFolder(String roomId) {
        Folder room = folderRepository.findById(roomId).orElseThrow();
        return FolderDto.from(room);
    }

    @Override
    public Folder createFolderOn(String folderId, String folderName) {
        Folder parent = folderRepository.findById(folderId).orElseThrow();

        // subfolders at the same level should have different names
        parent.getFolders().stream()
                .map(Folder::getName)
                .filter(name -> name.equals(folderName))
                .findFirst().ifPresent(folder -> {
                    throw new RuntimeException("Folder already exists");
        });

        Folder folder = new Folder();
        folder.setName(folderName);
        folder.setParentFolder(parent);

        return folderRepository.save(folder);
    }

    @Override
    public FolderDto addVieversToFolder(String folderId, List<String> viewersUsernames) {
        Folder folder = folderRepository.findById(folderId).orElseThrow();
        Folder parentFolder = folderRepository.findById(folder.getParentFolder().getId()).orElseThrow();

        List<String> invalidUserNames = viewersUsernames.stream()
                .filter(viewer -> !parentFolder.getViewers().contains(viewer))
                .toList();

        List<String> validUserNames = new ArrayList<>(viewersUsernames);
        validUserNames.removeAll(invalidUserNames);

        List<User> newViewers = userRepository.findAllByUsernameIn(validUserNames);
        folder.getViewers().addAll(newViewers.stream().map(User::getUsername).toList());
        folderRepository.save(folder);

        if(!invalidUserNames.isEmpty()) {
            throw new RuntimeException("These users don't have permission to view this folder: " + invalidUserNames);
        }
        return FolderDto.from(folder);
    }
}
