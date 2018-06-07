package com.ftec.repositories;

import com.ftec.resources.enums.TutorialSteps;
import com.ftec.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String Username);
    
    Optional<User> findByEmail(String email);
    
    void deleteByEmail(String email);
    
    void deleteById(Long id);
    
    void deleteByCurrentStep(TutorialSteps currentStep);
    
    Optional<User> findByCurrentStep(TutorialSteps currentStep);
    
    Optional<User> findBySubscribeForEmail(boolean subscribeForNews);

    @Modifying
    @Query("update User u set u.currentStep=?2 where u.id=?1")
    void updateTutorialStepForUser(long userId, TutorialSteps tutorialStep);


    @Query("select u.currentStep from User u where u.id=?1")
    TutorialSteps getTutorialStep(long userId);

    @Query(value = "select id from user where username = ?1",nativeQuery = true)
    long findIdByUsername(String username);

    @Query(value = "select id from user where email = ?1",nativeQuery = true)
    long findIdByEmail(String email);

    @Query(value = "select email from user where username = ?1",nativeQuery = true)
    String findEmailByUsername(String username);

    @Query(value = "select username from user where email = ?1",nativeQuery = true)
    String findUsernameByEmail(String email);
}
