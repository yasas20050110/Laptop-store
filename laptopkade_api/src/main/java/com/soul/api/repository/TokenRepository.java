package com.soul.api.repository;

import com.soul.api.model.Token;
import com.soul.api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    
    Optional<Token> findByTokenValue(String tokenValue);
    
    List<Token> findByUser(User user);
    
    List<Token> findByUserAndRevokedFalse(User user);
    
    List<Token> findByUserIdAndRevokedFalse(Long userId);
    
    void deleteByUser(User user);
}
