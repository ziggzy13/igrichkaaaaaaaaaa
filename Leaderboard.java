package com.knowledgeheroes.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Клас модел, представящ класация в играта
 */
public class Leaderboard {
    private int leaderboardId;
    private int levelId; // 0 за глобална класация
    private String category;
    private String name;
    
    // Записи в класацията
    private List<LeaderboardEntry> entries;
    
    /**
     * Конструктор по подразбиране
     */
    public Leaderboard() {
        this.entries = new ArrayList<>();
    }
    
    /**
     * Конструктор с основни параметри
     * 
     * @param category категория на класацията
     * @param name име на класацията
     */
    public Leaderboard(String category, String name) {
        this();
        this.category = category;
        this.name = name;
    }
    
    /**
     * Конструктор с всички параметри
     * 
     * @param leaderboardId ID на класацията
     * @param levelId ID на нивото (0 за глобална класация)
     * @param category категория на класацията
     * @param name име на класацията
     */
    public Leaderboard(int leaderboardId, int levelId, String category, String name) {
        this();
        this.leaderboardId = leaderboardId;
        this.levelId = levelId;
        this.category = category;
        this.name = name;
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
     * Връща ID на нивото
     * 
     * @return ID на нивото (0 за глобална класация)
     */
    public int getLevelId() {
        return levelId;
    }
    
    /**
     * Задава ID на нивото
     * 
     * @param levelId ID на нивото (0 за глобална класация)
     */
    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }
    
    /**
     * Връща категория на класацията
     * 
     * @return категория на класацията
     */
    public String getCategory() {
        return category;
    }
    
    /**
     * Задава категория на класацията
     * 
     * @param category категория на класацията
     */
    public void setCategory(String category) {
        this.category = category;
    }
    
    /**
     * Връща име на класацията
     * 
     * @return име на класацията
     */
    public String getName() {
        return name;
    }
    
    /**
     * Задава име на класацията
     * 
     * @param name име на класацията
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Връща списък със записи в класацията
     * 
     * @return списък със записи
     */
    public List<LeaderboardEntry> getEntries() {
        return entries;
    }
    
    /**
     * Задава списък със записи в класацията
     * 
     * @param entries списък със записи
     */
    public void setEntries(List<LeaderboardEntry> entries) {
        this.entries = entries;
    }
    
    /**
     * Добавя запис към класацията
     * 
     * @param entry запис за добавяне
     */
    public void addEntry(LeaderboardEntry entry) {
        if (this.entries == null) {
            this.entries = new ArrayList<>();
        }
        
        entry.setLeaderboardId(this.leaderboardId);
        this.entries.add(entry);
        
        // Сортиране на записите по резултат (в низходящ ред)
        sortEntries();
    }
    
    /**
     * Премахва запис от класацията
     * 
     * @param entryId ID на записа за премахване
     * @return true ако записът е премахнат успешно, false ако не е намерен
     */
    public boolean removeEntry(int entryId) {
        if (this.entries == null) {
            return false;
        }
        
        return this.entries.removeIf(entry -> entry.getEntryId() == entryId);
    }
    
    /**
     * Сортира записите по резултат (в низходящ ред)
     */
    public void sortEntries() {
        if (this.entries == null || this.entries.size() < 2) {
            return;
        }
        
        this.entries.sort((e1, e2) -> Integer.compare(e2.getScore(), e1.getScore()));
    }
    
    /**
     * Връща броя записи в класацията
     * 
     * @return брой записи
     */
    public int getEntryCount() {
        return entries != null ? entries.size() : 0;
    }
    
    /**
     * Връща най-добрия резултат в класацията
     * 
     * @return най-добър резултат или 0 ако няма записи
     */
    public int getTopScore() {
        if (entries == null || entries.isEmpty()) {
            return 0;
        }
        
        // Записите са сортирани по резултат, така че първият е най-добрият
        return entries.get(0).getScore();
    }
    
    /**
     * Проверява дали класацията е глобална
     * 
     * @return true ако класацията е глобална, false ако е за конкретно ниво
     */
    public boolean isGlobal() {
        return levelId == 0;
    }
    
    /**
     * Връща позицията на играч в класацията
     * 
     * @param playerId ID на играча
     * @return позиция (1-базирана) или -1 ако играчът не е в класацията
     */
    public int getPlayerRank(int playerId) {
        if (entries == null || entries.isEmpty()) {
            return -1;
        }
        
        for (int i = 0; i < entries.size(); i++) {
            if (entries.get(i).getPlayerId() == playerId) {
                return i + 1; // Позициите са 1-базирани
            }
        }
        
        return -1;
    }
    
    /**
     * Връща най-добрия резултат на играч в класацията
     * 
     * @param playerId ID на играча
     * @return най-добър резултат или -1 ако играчът не е в класацията
     */
    public int getPlayerBestScore(int playerId) {
        if (entries == null || entries.isEmpty()) {
            return -1;
        }
        
        for (LeaderboardEntry entry : entries) {
            if (entry.getPlayerId() == playerId) {
                return entry.getScore();
            }
        }
        
        return -1;
    }
    
    /**
     * Връща най-добрите N записи в класацията
     * 
     * @param limit максимален брой записи за връщане
     * @return списък с най-добрите N записи
     */
    public List<LeaderboardEntry> getTopEntries(int limit) {
        if (entries == null || entries.isEmpty()) {
            return new ArrayList<>();
        }
        
        int count = Math.min(limit, entries.size());
        return entries.subList(0, count);
    }
    
    /**
     * Връща описание на категорията на класацията
     * 
     * @return описание на категорията
     */
    public String getCategoryDescription() {
        switch (category.toLowerCase()) {
            case "score":
                return "Най-висок резултат";
            case "time":
                return "Най-бързо време";
            case "cards_collected":
                return "Брой събрани карти";
            case "stars":
                return "Брой спечелени звезди";
            case "completed_levels":
                return "Брой завършени нива";
            default:
                return category;
        }
    }
    
    @Override
    public String toString() {
        return "Leaderboard{" +
               "leaderboardId=" + leaderboardId +
               ", name='" + name + '\'' +
               ", category='" + category + '\'' +
               ", entries=" + getEntryCount() +
               '}';
    }
}