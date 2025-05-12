package com.knowledgeheroes.model;

/**
 * Клас модел, представящ пъзел в играта
 */
public class Puzzle {
    private int puzzleId;
    private int levelId;
    private String name;
    private String description;
    private String puzzleType;
    private String data;
    private String solution;
    private int timeLimit;
    
    /**
     * Конструктор по подразбиране
     */
    public Puzzle() {
    }
    
    /**
     * Конструктор с основни параметри
     * 
     * @param name име на пъзела
     * @param description описание на пъзела
     * @param puzzleType тип на пъзела
     */
    public Puzzle(String name, String description, String puzzleType) {
        this.name = name;
        this.description = description;
        this.puzzleType = puzzleType;
    }
    
    /**
     * Конструктор с всички параметри
     * 
     * @param puzzleId ID на пъзела
     * @param levelId ID на нивото
     * @param name име на пъзела
     * @param description описание на пъзела
     * @param puzzleType тип на пъзела
     * @param data данни за пъзела
     * @param solution решение на пъзела
     * @param timeLimit времево ограничение
     */
    public Puzzle(int puzzleId, int levelId, String name, String description, 
                 String puzzleType, String data, String solution, int timeLimit) {
        this.puzzleId = puzzleId;
        this.levelId = levelId;
        this.name = name;
        this.description = description;
        this.puzzleType = puzzleType;
        this.data = data;
        this.solution = solution;
        this.timeLimit = timeLimit;
    }
    
    /**
     * Връща ID на пъзела
     * 
     * @return ID на пъзела
     */
    public int getPuzzleId() {
        return puzzleId;
    }
    
    /**
     * Задава ID на пъзела
     * 
     * @param puzzleId ID на пъзела
     */
    public void setPuzzleId(int puzzleId) {
        this.puzzleId = puzzleId;
    }
    
    /**
     * Връща ID на нивото
     * 
     * @return ID на нивото
     */
    public int getLevelId() {
        return levelId;
    }
    
    /**
     * Задава ID на нивото
     * 
     * @param levelId ID на нивото
     */
    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }
    
    /**
     * Връща име на пъзела
     * 
     * @return име на пъзела
     */
    public String getName() {
        return name;
    }
    
    /**
     * Задава име на пъзела
     * 
     * @param name име на пъзела
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Връща описание на пъзела
     * 
     * @return описание на пъзела
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Задава описание на пъзела
     * 
     * @param description описание на пъзела
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Връща тип на пъзела
     * 
     * @return тип на пъзела
     */
    public String getPuzzleType() {
        return puzzleType;
    }
    
    /**
     * Задава тип на пъзела
     * 
     * @param puzzleType тип на пъзела
     */
    public void setPuzzleType(String puzzleType) {
        this.puzzleType = puzzleType;
    }
    
    /**
     * Връща данни за пъзела
     * 
     * @return данни за пъзела
     */
    public String getData() {
        return data;
    }
    
    /**
     * Задава данни за пъзела
     * 
     * @param data данни за пъзела
     */
    public void setData(String data) {
        this.data = data;
    }
    
    /**
     * Връща решение на пъзела
     * 
     * @return решение на пъзела
     */
    public String getSolution() {
        return solution;
    }
    
    /**
     * Задава решение на пъзела
     * 
     * @param solution решение на пъзела
     */
    public void setSolution(String solution) {
        this.solution = solution;
    }
    
    /**
     * Връща времево ограничение
     * 
     * @return времево ограничение в секунди
     */
    public int getTimeLimit() {
        return timeLimit;
    }
    
    /**
     * Задава времево ограничение
     * 
     * @param timeLimit времево ограничение в секунди
     */
    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
    }
    
    /**
     * Проверява дали въведеното решение е правилно
     * 
     * @param userSolution въведено решение от потребителя
     * @return true ако решението е правилно, false ако не е
     */
    public boolean checkSolution(String userSolution) {
        // Проста проверка за равенство на решенията
        // В реалната имплементация може да има по-сложна логика
        return this.solution != null && this.solution.equals(userSolution);
    }
    
    /**
     * Изчислява резултат за пъзела въз основа на времето за решаване и точността
     * 
     * @param solveTime време за решаване в секунди
     * @param isCorrect дали решението е правилно
     * @return резултат (0 ако решението не е правилно)
     */
    public int calculateScore(int solveTime, boolean isCorrect) {
        if (!isCorrect) {
            return 0;
        }
        
        // Базови точки за правилно решение
        int baseScore = 100;
        
        // Бонус точки за бързо решаване
        int timeBonus = 0;
        
        if (timeLimit > 0 && solveTime < timeLimit) {
            // Процент от оставащото време, превърнат в бонус точки
            double timeRatio = 1.0 - (double) solveTime / timeLimit;
            timeBonus = (int) (baseScore * timeRatio * 0.5); // Максимум 50% допълнителни точки
        }
        
        return baseScore + timeBonus;
    }
    
    /**
     * Връща максимален брой точки, които могат да бъдат спечелени от пъзела
     * 
     * @return максимален брой точки
     */
    public int getMaxScore() {
        // Базови точки (100) + максимален бонус (50)
        return 150;
    }
    
    /**
     * Връща форматирано времево ограничение
     * 
     * @return форматирано времево ограничение (мин:сек)
     */
    public String getFormattedTimeLimit() {
        if (timeLimit <= 0) {
            return "Без ограничение";
        }
        
        int minutes = timeLimit / 60;
        int seconds = timeLimit % 60;
        
        return String.format("%d:%02d", minutes, seconds);
    }
    
    /**
     * Проверява дали пъзелът има времево ограничение
     * 
     * @return true ако има времево ограничение, false ако няма
     */
    public boolean hasTimeLimit() {
        return timeLimit > 0;
    }
    
    /**
     * Връща ключови характеристики на пъзела като форматиран низ
     * 
     * @return характеристики на пъзела
     */
    public String getFeatures() {
        StringBuilder features = new StringBuilder();
        
        features.append("Тип: ").append(puzzleType);
        
        if (hasTimeLimit()) {
            features.append(", Време: ").append(getFormattedTimeLimit());
        }
        
        features.append(", Точки: ").append(getMaxScore());
        
        return features.toString();
    }
    
    @Override
    public String toString() {
        return "Puzzle{" +
               "puzzleId=" + puzzleId +
               ", name='" + name + '\'' +
               ", puzzleType='" + puzzleType + '\'' +
               '}';
    }
}