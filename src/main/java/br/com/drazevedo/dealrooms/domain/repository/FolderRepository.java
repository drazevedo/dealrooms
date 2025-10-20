package br.com.drazevedo.dealrooms.domain.repository;

import br.com.drazevedo.dealrooms.domain.model.DealRoom;
import br.com.drazevedo.dealrooms.domain.model.Folder;
import br.com.drazevedo.dealrooms.domain.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends MongoRepository<Folder, String> {

    // All operations that have "ByParentFolderNull" mean a DealRoom

    Optional<Folder> findByParentFolderNullAndId(String id);
    Optional<Folder> findAnyByParentFolderNullAndName(String name);
    List<Folder> findAllByParentFolderNull();
    List<Folder> findAllByParentFolderNullAndViewersContaining(String user);


}