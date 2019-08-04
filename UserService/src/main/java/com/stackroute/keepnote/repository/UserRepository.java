package com.stackroute.keepnote.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.stackroute.keepnote.model.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

}
