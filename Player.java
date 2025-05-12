package com.knowledgeheroes.model;

import java.time.LocalDateTime;

/**
 * Клас модел, представящ играч в системата
 */
public class Player {
    private int playerId;
    private String username;
    private String password;
    private String email;
    private LocalDateTime registrationDate;
    private LocalDateTime lastLogin;
    
    /**
     * Конструктор по подразбиране
     */
    public Player() {
    }
    
    /**
     * Конструктор с параметри за създаване на нов играч
     * 
     * @param username потребителско име
     * @param password парола (хеширана)
     * @param email email адрес
     */
    public Player(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.registrationDate = LocalDateTime.now();
    }
    
    /**
     * Конструктор с всички параметри
     * 
     * @param playerId ID на играча
     * @param username потребителско име
     * @param password парола (хеширана)
     * @param email email адрес
     * @param registrationDate дата на регистрация
     * @param lastLogin дата на последно влизане
     */
    public Player(int playerId, String username, String password, String email, 
                 LocalDateTime registrationDate, LocalDateTime lastLogin) {
        this.playerId = playerId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.registrationDate = registrationDate;
        this.lastLogin = lastLogin;
    }
    
    /**
     * Връща ID на играча
     * 
     * @return ID на играча
     */
    public int getPlayerId() {
        return playerId;
    }
    
    /**
     * Задава ID на играча
     * 
     * @param playerId ID на играча
     */
    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
    
    /**
     * Връща потребителското име
     * 
     * @return потребителско име
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * Задава потребителско име
     * 
     * @param username потребителско име
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    /**
     * Връща паролата (хеширана)
     * 
     * @return парола (хеширана)
     */
    public String getPassword() {
        return password;
    }
    
    /**
     * Задава парола (хеширана)
     * 
     * @param password парола (хеширана)
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    /**
     * Връща email адрес
     * 
     * @return email адрес
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Задава email адрес
     * 
     * @param email email адрес
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * Връща дата на регистрация
     * 
     * @return дата на регистрация
     */
    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }
    
    /**
     * Задава дата на регистрация
     * 
     * @param registrationDate дата на регистрация
     */
    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }
    
    /**
     * Връща дата на последно влизане
     * 
     * @return дата на последно влизане
     */
    public LocalDateTime getLastLogin() {
        return lastLogin;
    }
    
    /**
     * Задава дата на последно влизане
     * 
     * @param lastLogin дата на последно влизане
     */
    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }
    
    /**
     * Обновява датата на последно влизане до сегашния момент
     */
    public void updateLastLogin() {
        this.lastLogin = LocalDateTime.now();
    }
    
    /**
     * Проверява дали имейл адресът е валиден
     * 
     * @return true ако имейлът е валиден, false ако не е
     */
    public boolean isValidEmail() {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return email != null && email.matches(emailRegex);
    }
    
    /**
     * Проверява дали потребителското име е валидно
     * 
     * @return true ако потребителското име е валидно, false ако не е
     */
    public boolean isValidUsername() {
        // Проверка за минимална дължина (3 символа) и съдържание (букви, цифри, _, -)
        String usernameRegex = "^[A-Za-z0-9_-]{3,}$";
        return username != null && username.matches(usernameRegex);
    }
    
    /**
     * Проверява дали паролата е валидна (минимална дължина)
     * 
     * @return true ако паролата е валидна, false ако не е
     */
    public boolean isValidPassword() {
        return password != null && password.length() >= 6;
    }
    
    @Override
    public String toString() {
        return "Player{" +
               "playerId=" + playerId +
               ", username='" + username + '\'' +
               ", email='" + email + '\'' +
               '}';
    }
}