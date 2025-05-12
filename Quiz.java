package com.knowledgeheroes.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Клас модел, представящ куиз в играта
 */
public class Quiz {
    private int quizId;
    private int levelId;
    private String name;
    private String description;
    private int timeLimit;
    
    // Връзки към други обекти
    private List<Question> questions;
    
    /**
     * Конструктор по подразбиране
     */
    public Quiz() {
        this.questions = new ArrayList<>();
    }
    
    /**
     * Конструктор с основни параметри
     * 
     * @param name име на куиза
     * @param description описание на куиза
     */
    public Quiz(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }
    
    /**
     * Конструктор с всички параметри
     * 
     * @param quizId ID на куиза
     * @param levelId ID на нивото
     * @param name име на куиза
     * @param description описание на куиза
     * @param timeLimit времево ограничение
     */
    public Quiz(int quizId, int levelId, String name, String description, int timeLimit) {
        this();
        this.quizId = quizId;
        this.levelId = levelId;
        this.name = name;
        this.description = description;
        this.timeLimit = timeLimit;
    }
    
    /**
     * Връща ID на куиза
     * 
     * @return ID на куиза
     */
    public int getQuizId() {
        return quizId;
    }
    
    /**
     * Задава ID на куиза
     * 
     * @param quizId ID на куиза
     */
    public void setQuizId(int quizId) {
        this.quizId = quizId;
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
     * Връща име на куиза
     * 
     * @return име на куиза
     */
    public String getName() {
        return name;
    }
    
    /**
     * Задава име на куиза
     * 
     * @param name име на куиза
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Връща описание на куиза
     * 
     * @return описание на куиза
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Задава описание на куиза
     * 
     * @param description описание на куиза
     */
    public void setDescription(String description) {
        this.description = description;
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
     * Връща списък с въпроси в куиза
     * 
     * @return списък с въпроси
     */
    public List<Question> getQuestions() {
        return questions;
    }
    
    /**
     * Задава списък с въпроси в куиза
     * 
     * @param questions списък с въпроси
     */
    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
    
    /**
     * Добавя въпрос към куиза
     * 
     * @param question въпрос за добавяне
     */
    public void addQuestion(Question question) {
        if (this.questions == null) {
            this.questions = new ArrayList<>();
        }
        
        this.questions.add(question);
    }
    
    /**
     * Премахва въпрос от куиза
     * 
     * @param questionId ID на въпроса за премахване
     * @return true ако въпросът е премахнат успешно, false ако не е намерен
     */
    public boolean removeQuestion(int questionId) {
        if (this.questions == null) {
            return false;
        }
        
        return this.questions.removeIf(question -> question.getQuestionId() == questionId);
    }
    
    /**
     * Връща брой на въпросите в куиза
     * 
     * @return брой на въпросите
     */
    public int getQuestionCount() {
        return questions != null ? questions.size() : 0;
    }
    
    /**
     * Изчислява резултат за куиза въз основа на броя правилни отговори и времето
     * 
     * @param correctAnswers брой правилни отговори
     * @param totalAnswers общ брой отговори
     * @param solveTime време за решаване в секунди
     * @return резултат
     */
    public int calculateScore(int correctAnswers, int totalAnswers, int solveTime) {
        if (totalAnswers == 0) {
            return 0;
        }
        
        // Базови точки въз основа на процента правилни отговори
        double accuracy = (double) correctAnswers / totalAnswers;
        int baseScore = (int) (accuracy * 100);
        
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
     * Връща максимален брой точки, които могат да бъдат спечелени от куиза
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
     * Проверява дали куизът има времево ограничение
     * 
     * @return true ако има времево ограничение, false ако няма
     */
    public boolean hasTimeLimit() {
        return timeLimit > 0;
    }
    
    /**
     * Връща ключови характеристики на куиза като форматиран низ
     * 
     * @return характеристики на куиза
     */
    public String getFeatures() {
        StringBuilder features = new StringBuilder();
        
        features.append("Въпроси: ").append(getQuestionCount());
        
        if (hasTimeLimit()) {
            features.append(", Време: ").append(getFormattedTimeLimit());
        }
        
        features.append(", Точки: ").append(getMaxScore());
        
        return features.toString();
    }
    
    @Override
    public String toString() {
        return "Quiz{" +
               "quizId=" + quizId +
               ", name='" + name + '\'' +
               ", questions=" + getQuestionCount() +
               '}';
    }
}