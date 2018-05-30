package com.ftec.repositories;

import com.ftec.entities.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface UserDAO extends ElasticsearchRepository<User, Long> {
<<<<<<< HEAD

    @Override
    Optional<User> findById(Long id);

    Optional<User> findByUsername(String Username);
=======
	 
	 Optional<User> findById(long id);
	 Optional<User> findByUsername(String Username);
>>>>>>> 482cb66e412aa950c729f2775de3b8b22049cbec
}
