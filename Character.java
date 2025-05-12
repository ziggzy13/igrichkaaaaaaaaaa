package com.knowledgeheroes.model;

import javax.smartcardio.Card;

/**
 * Клас модел, представящ геройскя герой на играч
 */
public class Character {
    private int characterId;
    private int playerId;
    private String name;
    private int level;
    private int experience;
    private int intelligence;
    private int strength;
    private int agility;
    private int wisdom;
    private String avatarPath;
    
    /**
     * Конструктор по подразбиране
     */
    public Character() {
        // Инициализация със стандартни стойности за нов герой
        this.level = 1;
        this.experience = 0;
        this.intelligence = 5;
        this.strength = 5;
        this.agility = 5;
        this.wisdom = 5;
    }
    
    /**
     * Конструктор за създаване на нов герой
     * 
     * @param playerId ID на играча, на когото принадлежи героя
     * @param name име на героя
     */
    public Character(int playerId, String name) {
        this();
        this.playerId = playerId;
        this.name = name;
    }
    
    /**
     * Конструктор с всички параметри
     * 
     * @param characterId ID на героя
     * @param playerId ID на играча
     * @param name име на героя
     * @param level ниво на героя
     * @param experience опит на героя
     * @param intelligence интелигентност на героя
     * @param strength сила на героя
     * @param agility ловкост на героя
     * @param wisdom мъдрост на героя
     * @param avatarPath път към аватара на героя
     */
    public Character(int characterId, int playerId, String name, int level, int experience,
                    int intelligence, int strength, int agility, int wisdom, String avatarPath) {
        this.characterId = characterId;
        this.playerId = playerId;
        this.name = name;
        this.level = level;
        this.experience = experience;
        this.intelligence = intelligence;
        this.strength = strength;
        this.agility = agility;
        this.wisdom = wisdom;
        this.avatarPath = avatarPath;
    }
    
    /**
     * Връща ID на героя
     * 
     * @return ID на героя
     */
    public int getCharacterId() {
        return characterId;
    }
    
    /**
     * Задава ID на героя
     * 
     * @param characterId ID на героя
     */
    public void setCharacterId(int characterId) {
        this.characterId = characterId;
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
     * Връща име на героя
     * 
     * @return име на героя
     */
    public String getName() {
        return name;
    }
    
    /**
     * Задава име на героя
     * 
     * @param name име на героя
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Връща ниво на героя
     * 
     * @return ниво на героя
     */
    public int getLevel() {
        return level;
    }
    
    /**
     * Задава ниво на героя
     * 
     * @param level ниво на героя
     */
    public void setLevel(int level) {
        this.level = level;
    }
    
    /**
     * Връща опит на героя
     * 
     * @return опит на героя
     */
    public int getExperience() {
        return experience;
    }
    
    /**
     * Задава опит на героя
     * 
     * @param experience опит на героя
     */
    public void setExperience(int experience) {
        this.experience = experience;
    }
    
    /**
     * Връща интелигентност на героя
     * 
     * @return интелигентност на героя
     */
    public int getIntelligence() {
        return intelligence;
    }
    
    /**
     * Задава интелигентност на героя
     * 
     * @param intelligence интелигентност на героя
     */
    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }
    
    /**
     * Връща сила на героя
     * 
     * @return сила на героя
     */
    public int getStrength() {
        return strength;
    }
    
    /**
     * Задава сила на героя
     * 
     * @param strength сила на героя
     */
    public void setStrength(int strength) {
        this.strength = strength;
    }
    
    /**
     * Връща ловкост на героя
     * 
     * @return ловкост на героя
     */
    public int getAgility() {
        return agility;
    }
    
    /**
     * Задава ловкост на героя
     * 
     * @param agility ловкост на героя
     */
    public void setAgility(int agility) {
        this.agility = agility;
    }
    
    /**
     * Връща мъдрост на героя
     * 
     * @return мъдрост на героя
     */
    public int getWisdom() {
        return wisdom;
    }
    
    /**
     * Задава мъдрост на героя
     * 
     * @param wisdom мъдрост на героя
     */
    public void setWisdom(int wisdom) {
        this.wisdom = wisdom;
    }
    
    /**
     * Връща път към аватара на героя
     * 
     * @return път към аватара на героя
     */
    public String getAvatarPath() {
        return avatarPath;
    }
    
    /**
     * Задава път към аватара на героя
     * 
     * @param avatarPath път към аватара на героя
     */
    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }
    
    /**
     * Добавя опит към героя и проверява за качване на ниво
     * 
     * @param amount количество опит за добавяне
     * @return true ако героят е качил ниво, false ако не
     */
    public boolean addExperience(int amount) {
        this.experience += amount;
        
        // Проверка за качване на ниво
        int expForNextLevel = calculateRequiredExpForLevel(this.level + 1);
        
        if (this.experience >= expForNextLevel) {
            levelUp();
            return true;
        }
        
        return false;
    }
    
    /**
     * Качва ниво на героя и обновява атрибутите му
     */
    public void levelUp() {
        this.level++;
        
        // Увеличаване на атрибутите при качване на ниво
        // Стойностите могат да се настроят според балансирането на играта
        this.intelligence += 1;
        this.strength += 1;
        this.agility += 1;
        this.wisdom += 1;
    }
    
    /**
     * Изчислява необходимия опит за достигане на определено ниво
     * 
     * @param targetLevel целево ниво
     * @return необходим опит
     */
    public int calculateRequiredExpForLevel(int targetLevel) {
        // Проста формула за изчисляване на необходим опит
        // 1000 за ниво 2, 2100 за ниво 3, 3300 за ниво 4 и т.н.
        if (targetLevel <= 1) {
            return 0;
        }
        
        return 1000 * (targetLevel - 1) + 100 * (targetLevel - 2) * (targetLevel - 1) / 2;
    }
    
    /**
     * Връща процент до следващото ниво
     * 
     * @return процент до следващото ниво (0-100)
     */
    public int getPercentToNextLevel() {
        int currentLevelExp = calculateRequiredExpForLevel(this.level);
        int nextLevelExp = calculateRequiredExpForLevel(this.level + 1);
        int expForCurrentLevel = this.experience - currentLevelExp;
        int expRequiredForNextLevel = nextLevelExp - currentLevelExp;
        
        return (int) (100.0 * expForCurrentLevel / expRequiredForNextLevel);
    }
    
    /**
     * Проверява дали героят може да използва карта, в зависимост от нейните изисквания
     * 
     * @param card карта за проверка
     * @return true ако героят може да използва картата, false ако не може
     */
    public boolean canUseCard(Card card) {
        // Тук може да се добави логика за проверка на изискванията за картата
        // Например, ако картата изисква определено ниво интелигентност
        return true;
    }
    
    /**
     * Връща бонус за интелигентност, в зависимост от стойността на атрибута
     * 
     * @return бонус за интелигентност
     */
    public int getIntelligenceBonus() {
        return calculateAttributeBonus(intelligence);
    }
    
    /**
     * Връща бонус за сила, в зависимост от стойността на атрибута
     * 
     * @return бонус за сила
     */
    public int getStrengthBonus() {
        return calculateAttributeBonus(strength);
    }
    
    /**
     * Връща бонус за ловкост, в зависимост от стойността на атрибута
     * 
     * @return бонус за ловкост
     */
    public int getAgilityBonus() {
        return calculateAttributeBonus(agility);
    }
    
    /**
     * Връща бонус за мъдрост, в зависимост от стойността на атрибута
     * 
     * @return бонус за мъдрост
     */
    public int getWisdomBonus() {
        return calculateAttributeBonus(wisdom);
    }
    
    /**
     * Изчислява бонус за атрибут, в зависимост от стойността му
     * 
     * @param attributeValue стойност на атрибута
     * @return бонус за атрибута
     */
    private int calculateAttributeBonus(int attributeValue) {
        // Проста формула за изчисляване на бонус
        return attributeValue / 5;
    }
    
    @Override
    public String toString() {
        return "Character{" +
               "characterId=" + characterId +
               ", name='" + name + '\'' +
               ", level=" + level +
               ", exp=" + experience + "/" + calculateRequiredExpForLevel(level + 1) +
               ", intelligence=" + intelligence +
               ", strength=" + strength +
               ", agility=" + agility +
               ", wisdom=" + wisdom +
               '}';
    }
}