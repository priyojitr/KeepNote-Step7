package com.stackroute.keepnote.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.stackroute.keepnote.model.NoteUser;

@Repository
public interface NoteRepository extends MongoRepository<NoteUser, String> {

}
