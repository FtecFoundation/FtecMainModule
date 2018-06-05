package com.ftec.repositories;

import com.ftec.resources.enums.TutorialSteps;
import com.ftec.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
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
    
    Optional<User> findBySubscribeForNews(boolean subscribeForNews);

    @Query("update User set currentStep=?2 where id=?1")
    void updateTutorialStepForUser(long userId, TutorialSteps tutorialStep);

    @Query("select currentStep from User where id=?1")
    TutorialSteps getTutorialStep(long userId);
}
