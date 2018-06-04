package com.ftec.repositories;

import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.ftec.configs.enums.TutorialSteps;
import com.ftec.entities.User;

public interface UserDAO extends ElasticsearchRepository<User, Long> {

    @Override
    Optional<User> findById(Long id);

    Optional<User> findByUsername(String Username);
    
    Optional<User> findByEmail(String email);
    
    void deleteByEmail(String email);
    
    void deleteById(Long id);
    
    void deleteByCurrentStep(TutorialSteps currentStep);
    
    Optional<User> findByCurrentStep(TutorialSteps currentStep);
    
    Optional<User> findBySubscribeForNews(boolean subscribeForNews);
    
    
}
