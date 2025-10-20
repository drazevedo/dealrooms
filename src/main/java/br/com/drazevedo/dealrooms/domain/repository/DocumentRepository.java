package br.com.drazevedo.dealrooms.domain.repository;

import br.com.drazevedo.dealrooms.application.dto.DocumentDto;
import br.com.drazevedo.dealrooms.domain.model.Document;
import br.com.drazevedo.dealrooms.domain.model.Folder;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DocumentRepository extends MongoRepository<Document, String> {

    List<Document> findByParentFolderId(String parentFolderId);
}