package com.example.demo.repo;

import com.example.demo.MySQL.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends CrudRepository<com.example.demo.MySQL.User,Integer>  {
    //User findByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.username = :username")
    public User getUserByUsername(@Param("username") String username);
    @Query("SELECT u FROM User u WHERE u.activationCode = :activationcode")
    public User findUserByActivationCode(@Param("activationcode")String code);
//    @Query("SELECT u FROM User u WHERE u.activationcode = :activationcode")
//    public User getUserByActivationCode(@Param("activationcode") String activationcode);
}
