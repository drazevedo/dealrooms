package br.com.drazevedo.dealrooms.domain.repository;

import br.com.drazevedo.dealrooms.domain.model.Document;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends MongoRepository<Document, String> {

    List<Document> findByParentFolderId(String parentFolderId);
}