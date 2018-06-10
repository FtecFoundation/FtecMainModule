package com.ftec.services.interfaces;

import com.ftec.entities.Token;
import com.ftec.exceptions.token.InvalidTokenException;
import com.ftec.exceptions.token.TokenException;
import com.ftec.exceptions.token.TokenExpiredException;
import com.ftec.utils.RandomHashGenerator;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TokenService {
    String TOKEN_NAME = "TOKEN-X-AUTH";

     void processToken(String token)  throws TokenException;

     static Long getUserIdFromToken(String token) throws InvalidTokenException{
         checkTokenFormat(token);
         return Long.valueOf(extractUserID(token));

     }
     static void checkTokenFormat(String token) {
        if(!token.contains("_")) throw new InvalidTokenException("Invalid token format! {UserID}_{Hash} expected.");

        String userId = extractUserID(token);

        try {
            Long.valueOf(userId);
        } catch(Exception e) {
            throw new InvalidTokenException("Invalid token format! Exception while convert userID to Long.");
        }

    }

    static String extractUserID(String token) {
        return token.substring(0, token.indexOf("_"));
    }

    static String generateToken(Long id) {
        return id.toString() + "_" + RandomHashGenerator.generateRandomString();
    }

    static void checkIfTokenExpired(Date expirationTime) throws TokenExpiredException {
        if(!expirationTime.after(new Date())) throw new TokenExpiredException("Token has been expired!");
    }

    String createSaveAndGetNewToken(Long id);

    void deleteByToken(String token);

    Optional<Token> findByToken(String token);

    void deleteExcessiveToken(long id);

    void updateExpirationDate(String token);

    void deleteByUserId(long idByHash);

    void deleteAll();

    void save(Token token);

    List<Token> findAllByUserId(long userId);

    List<Token> getAll();
}
