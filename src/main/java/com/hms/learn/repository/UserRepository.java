package com.hms.learn.repository;

import com.hms.learn.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String> {

    UserEntity getUserEntityByUsername(String username);

}
