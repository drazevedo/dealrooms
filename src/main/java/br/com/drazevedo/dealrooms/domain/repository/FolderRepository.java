package br.com.drazevedo.dealrooms.domain.repository;

import br.com.drazevedo.dealrooms.domain.model.Folder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FolderRepository extends MongoRepository<Folder, String> {

    // All operations that have "ByParentFolderNull" mean a DealRoom

    Optional<Folder> findByParentFolderIdNullAndId(String id);
    Optional<Folder> findAnyByParentFolderIdNullAndName(String name);
    List<Folder> findAllByParentFolderIdNull();
    List<Folder> findAllByParentFolderIdNullAndViewersContaining(String user);


}