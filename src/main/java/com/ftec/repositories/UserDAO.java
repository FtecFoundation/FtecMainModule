package com.ftec.repositories;

import java.util.Optional;


import com.ftec.configs.enums.TutorialSteps;
import com.ftec.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
    
    
}
