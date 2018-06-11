package com.ftec.services.Implementations;

import com.ftec.entities.Token;
import com.ftec.exceptions.token.TokenException;
import com.ftec.repositories.TokenDAO;
import com.ftec.services.interfaces.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TokenServiceImpl implements TokenService {

    private final TokenDAO tokenDAO;
    private final static int EXPIRATION_TIME = 86400000;

    @Autowired
    public TokenServiceImpl(TokenDAO tokenDAO) {
        this.tokenDAO = tokenDAO;
    }

    @Transactional
    public void processToken(String token)  throws TokenException{
        verifyToken(token);
        long userId = TokenService.getUserIdFromToken(token);
        deleteExcessiveToken(userId);

        updateExpirationDate(token);
    }

    @Scheduled(cron = "0 0 12 * * ?")
    @Transactional
    public void deleteExpiredTokens() {
        tokenDAO.deleteAllExpiredToken(new Date());
    }

    public String createSaveAndGetNewToken(Long id) {
        String token = TokenService.generateToken(id);
        Date expiration = new Date();

        setExpirationTime(expiration);
        tokenDAO.save(new Token(token, expiration, id));

        return token;
    }

    private void setExpirationTime(Date expiration) {
        expiration.setTime(expiration.getTime() + EXPIRATION_TIME);
    }

    private void verifyToken(String token) throws TokenException{
        Token tokenEntity = getTokenFromDB(token);

        Date expirationTime = tokenEntity.getExpirationTime();

        TokenService.checkIfTokenExpired(expirationTime);
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

    @Override
    @Transactional
    public void deleteByUserId(long idByHash) {
        tokenDAO.deleteByUserId(idByHash);
    }

    @Override
    @Modifying
    public void deleteAll() {
        tokenDAO.deleteAll();
    }

    @Override
    public void save(Token token) {
        tokenDAO.save(token);
    }

    @Override
    public List<Token> findAllByUserId(long userId) {
        return tokenDAO.findAllByUserId(userId);
    }

    @Override
    public List<Token> getAll() {
        return tokenDAO.getAll();
    }
}
