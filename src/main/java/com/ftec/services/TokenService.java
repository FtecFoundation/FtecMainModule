package com.ftec.services;

import com.ftec.entities.Token;
import com.ftec.exceptions.token.InvalidTokenException;
import com.ftec.exceptions.token.TokenException;
import com.ftec.exceptions.token.TokenExpiredException;
import com.ftec.repositories.TokenDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Service
public class TokenService {
    public static final String TOKEN_NAME = "TOKEN-X-AUTH";
    private final TokenDAO tokenDAO;
    private final static int EXPIRATION_TIME = 86400000;

    @Autowired
    public TokenService(TokenDAO tokenDAO) {
        this.tokenDAO = tokenDAO;
    }

    @Scheduled(cron = "0 0 12 * * ?") //TODO test it x3 how
    public void deleteExpiredToken() {
        tokenDAO.deleteAllExpiredToken();
    }

    public static Long getUserIdFromToken(String token) throws InvalidTokenException {
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

    public String createSaveAndGetNewToken(Long id) {
        String token = generateToken(id);
        Date expiration = new Date();

        setExpirationTime(expiration);
        tokenDAO.save(new Token(token, expiration, id));

        return token;
    }

    private void setExpirationTime(Date expiration) {
        expiration.setTime(expiration.getTime() + EXPIRATION_TIME);
    }

    static String generateToken(Long id) {
        return id.toString() + "_" + generateRandomString();
    }

    private static String generateRandomString() {
        int leftLimit = 97;
        int rightLimit = 122;
        int targetStringLength = 18;
        Random random = new Random();
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    public void verifyToken(String token) throws TokenException{
        Token tokenEntity = getTokenFromDB(token);

        Date expirationTime = tokenEntity.getExpirationTime();

        checkIfTokenExpired(expirationTime);
    }

    public static void checkIfTokenExpired(Date expirationTime) throws TokenExpiredException {
        if(!expirationTime.after(new Date())) throw new TokenExpiredException("Token has been expired!");
    }

    private Token getTokenFromDB(String token) throws TokenException{
        Optional<Token> userToken = tokenDAO.findByToken(token);

        if(!userToken.isPresent())	throw new TokenException("Can't find token in the DB!");
        return userToken.get();
    }

    @Transactional
    public void deleteByToken(String token){
        tokenDAO.deleteByToken(token);
    }

    public Optional<Token> findByToken(String token) {
        return tokenDAO.findByToken(token);
    }

    @Transactional
    public void deleteExcessiveToken(long id){
        tokenDAO.deleteExcessiveToken(id);
    }

    @Transactional
    public void updateExpirationDate(String token){
        Date oneDay = new Date();
        setExpirationTime(oneDay);

        tokenDAO.updateExpirationDate(oneDay,token);
    }
}
