package com.knowledgeheroes.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Клас модел, представящ запис в класация
 */
public class LeaderboardEntry {
    private int entryId;
    private int leaderboardId;
    private int playerId;
    private String playerName; // Кеширано име на играча за по-лесно показване
    private int score;
    private LocalDateTime date;
    
    /**
     * Конструктор по подразбиране
     */
    public LeaderboardEntry() {
    }
    
    /**
     * Конструктор с основни параметри
     * 
     * @param playerId ID на играча
     * @param playerName име на играча
     * @param score резултат
     */
    public LeaderboardEntry(int playerId, String playerName, int score) {
        this.playerId = playerId;
        this.playerName = playerName;
        this.score = score;
        this.date = LocalDateTime.now();
    }
    
    /**
     * Конструктор с всички параметри
     * 
     * @param entryId ID на записа
     * @param leaderboardId ID на класацията
     * @param playerId ID на играча
     * @param playerName име на играча
     * @param score резултат
     * @param date дата на записа
     */
    public LeaderboardEntry(int entryId, int leaderboardId, int playerId, 
                           String playerName, int score, LocalDateTime date) {
        this.entryId = entryId;
        this.leaderboardId = leaderboardId;
        this.playerId = playerId;
        this.playerName = playerName;
        this.score = score;
        this.date = date;
    }
    
    /**
     * Връща ID на записа
     * 
     * @return ID на записа
     */
    public int getEntryId() {
        return entryId;
    }
    
    /**
     * Задава ID на записа
     * 
     * @param entryId ID на записа
     */
    public void setEntryId(int entryId) {
        this.entryId = entryId;
    }
    
    /**
     * Връща ID на класацията
     * 
     * @return ID на класацията
     */
    public int getLeaderboardId() {
        return leaderboardId;
    }
    
    /**
     * Задава ID на класацията
     * 
     * @param leaderboardId ID на класацията
     */
    public void setLeaderboardId(int leaderboardId) {
        this.leaderboardId = leaderboardId;
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
     * Връща име на играча
     * 
     * @return име на играча
     */
    public String getPlayerName() {
        return playerName;
    }
    
    /**
     * Задава име на играча
     * 
     * @param playerName име на играча
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    /**
     * Връща резултат
     * 
     * @return резултат
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Задава резултат
     * 
     * @param score резултат
     */
    public void setScore(int score) {
        this.score = score;
    }
    
    /**
     * Връща дата на записа
     * 
     * @return дата на записа
     */
    public LocalDateTime getDate() {
        return date;
    }
    
    /**
     * Задава дата на записа
     * 
     * @param date дата на записа
     */
    public void setDate(LocalDateTime date) {
        this.date = date;
    }
    
    /**
     * Връща форматирана дата на записа
     * 
     * @return форматирана дата
     */
    public String getFormattedDate() {
        if (date == null) {
            return "";
        }
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
        return date.format(formatter);
    }
    
    /**
     * Връща форматиран резултат в зависимост от категорията на класацията
     * 
     * @param category категория на класацията
     * @return форматиран резултат
     */
    public String getFormattedScore(String category) {
        if (category == null) {
            return String.valueOf(score);
        }
        
        switch (category.toLowerCase()) {
            case "time":
                // Форматиране на времето (в секунди) като "мин:сек"
                int minutes = score / 60;
                int seconds = score % 60;
                return String.format("%d:%02d", minutes, seconds);
            case "stars":
                // Форматиране на броя звезди
                return score + " ★";
            default:
                return String.valueOf(score);
        }
    }
    
    @Override
    public String toString() {
        return "LeaderboardEntry{" +
               "entryId=" + entryId +
               ", playerName='" + playerName + '\'' +
               ", score=" + score +
               ", date=" + getFormattedDate() +
               '}';
    }
}