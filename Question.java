package com.knowledgeheroes.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Клас модел, представящ въпрос в играта
 */
public class Question {
    private int questionId;
    private int categoryId;
    private String text;
    private String difficulty;
    
    // Връзки към други обекти
    private List<Answer> answers;
    
    /**
     * Конструктор по подразбиране
     */
    public Question() {
        this.answers = new ArrayList<>();
    }
    
    /**
     * Конструктор с основни параметри
     * 
     * @param text текст на въпроса
     * @param difficulty трудност на въпроса
     */
    public Question(String text, String difficulty) {
        this();
        this.text = text;
        this.difficulty = difficulty;
    }
    
    /**
     * Конструктор с всички параметри
     * 
     * @param questionId ID на въпроса
     * @param categoryId ID на категорията
     * @param text текст на въпроса
     * @param difficulty трудност на въпроса
     */
    public Question(int questionId, int categoryId, String text, String difficulty) {
        this();
        this.questionId = questionId;
        this.categoryId = categoryId;
        this.text = text;
        this.difficulty = difficulty;
    }
    
    /**
     * Връща ID на въпроса
     * 
     * @return ID на въпроса
     */
    public int getQuestionId() {
        return questionId;
    }
    
    /**
     * Задава ID на въпроса
     * 
     * @param questionId ID на въпроса
     */
    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }
    
    /**
     * Връща ID на категорията
     * 
     * @return ID на категорията
     */
    public int getCategoryId() {
        return categoryId;
    }
    
    /**
     * Задава ID на категорията
     * 
     * @param categoryId ID на категорията
     */
    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }
    
    /**
     * Връща текст на въпроса
     * 
     * @return текст на въпроса
     */
    public String getText() {
        return text;
    }
    
    /**
     * Задава текст на въпроса
     * 
     * @param text текст на въпроса
     */
    public void setText(String text) {
        this.text = text;
    }
    
    /**
     * Връща трудност на въпроса
     * 
     * @return трудност на въпроса
     */
    public String getDifficulty() {
        return difficulty;
    }
    
    /**
     * Задава трудност на въпроса
     * 
     * @param difficulty трудност на въпроса
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    /**
     * Връща списък с отговори на въпроса
     * 
     * @return списък с отговори
     */
    public List<Answer> getAnswers() {
        return answers;
    }
    
    /**
     * Задава списък с отговори на въпроса
     * 
     * @param answers списък с отговори
     */
    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }
    
    /**
     * Добавя отговор към въпроса
     * 
     * @param answer отговор за добавяне
     */
    public void addAnswer(Answer answer) {
        if (this.answers == null) {
            this.answers = new ArrayList<>();
        }
        
        answer.setQuestionId(this.questionId);
        this.answers.add(answer);
    }
    
    /**
     * Премахва отговор от въпроса
     * 
     * @param answerId ID на отговора за премахване
     * @return true ако отговорът е премахнат успешно, false ако не е намерен
     */
    public boolean removeAnswer(int answerId) {
        if (this.answers == null) {
            return false;
        }
        
        return this.answers.removeIf(answer -> answer.getAnswerId() == answerId);
    }
    
    /**
     * Връща брой на отговорите на въпроса
     * 
     * @return брой на отговорите
     */
    public int getAnswerCount() {
        return answers != null ? answers.size() : 0;
    }
    
    /**
     * Връща правилния отговор на въпроса
     * 
     * @return правилен отговор или null ако няма такъв
     */
    public Answer getCorrectAnswer() {
        if (answers == null) {
            return null;
        }
        
        for (Answer answer : answers) {
            if (answer.isCorrect()) {
                return answer;
            }
        }
        
        return null;
    }
    
    /**
     * Проверява дали въпросът има правилен отговор
     * 
     * @return true ако има правилен отговор, false ако няма
     */
    public boolean hasCorrectAnswer() {
        return getCorrectAnswer() != null;
    }
    
    /**
     * Проверява дали отговорът е правилен
     * 
     * @param answerId ID на отговора за проверка
     * @return true ако отговорът е правилен, false ако не е
     */
    public boolean isCorrectAnswer(int answerId) {
        if (answers == null) {
            return false;
        }
        
        for (Answer answer : answers) {
            if (answer.getAnswerId() == answerId) {
                return answer.isCorrect();
            }
        }
        
        return false;
    }
    
    /**
     * Връща числова стойност на трудността за сортиране
     * 
     * @return числова стойност на трудността (1-4)
     */
    public int getDifficultyValue() {
        switch (difficulty.toLowerCase()) {
            case "easy":
                return 1;
            case "medium":
                return 2;
            case "hard":
                return 3;
            case "expert":
                return 4;
            default:
                return 0;
        }
    }
    
    /**
     * Връща цвят, съответстващ на трудността на въпроса
     * 
     * @return RGB цвят като String
     */
    public String getDifficultyColor() {
        switch (difficulty.toLowerCase()) {
            case "easy":
                return "#00FF00"; // зелено
            case "medium":
                return "#FFFF00"; // жълто
            case "hard":
                return "#FFA500"; // оранжево
            case "expert":
                return "#FF0000"; // червено
            default:
                return "#C0C0C0"; // сребърно (по подразбиране)
        }
    }
    
    /**
     * Връща броя точки, които носи въпросът в зависимост от трудността
     * 
     * @return брой точки
     */
    public int getPoints() {
        switch (difficulty.toLowerCase()) {
            case "easy":
                return 10;
            case "medium":
                return 20;
            case "hard":
                return 30;
            case "expert":
                return 50;
            default:
                return 10;
        }
    }
    
    /**
     * Разбърква реда на отговорите за въпроса
     */
    public void shuffleAnswers() {
        if (answers == null || answers.size() < 2) {
            return;
        }
        
        List<Answer> shuffled = new ArrayList<>(answers);
        java.util.Collections.shuffle(shuffled);
        this.answers = shuffled;
    }
    
    @Override
    public String toString() {
        return "Question{" +
               "questionId=" + questionId +
               ", text='" + text + '\'' +
               ", difficulty='" + difficulty + '\'' +
               ", answers=" + getAnswerCount() +
               '}';
    }
}