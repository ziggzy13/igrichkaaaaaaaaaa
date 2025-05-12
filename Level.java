package com.knowledgeheroes.model;

import java.util.ArrayList;
import java.util.List;

import com.knowledgeheroes.dao.LevelDAO;

/**
 * Клас модел, представящ ниво в играта
 */
public class Level {
    private int levelId;
    private String name;
    private String description;
    private String difficulty;
    private String unlockRequirement;
    private String backgroundPath;
    
    // Връзки към други обекти
    private List<Puzzle> puzzles;
    private List<Quiz> quizzes;
    
    /**
     * Конструктор по подразбиране
     */
    public Level() {
        this.puzzles = new ArrayList<>();
        this.quizzes = new ArrayList<>();
    }
    
    /**
     * Конструктор с основни параметри
     * 
     * @param name име на нивото
     * @param description описание на нивото
     * @param difficulty трудност на нивото
     */
    public Level(String name, String description, String difficulty) {
        this();
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
    }
    
    /**
     * Конструктор с всички параметри
     * 
     * @param levelId ID на нивото
     * @param name име на нивото
     * @param description описание на нивото
     * @param difficulty трудност на нивото
     * @param unlockRequirement изискване за отключване
     * @param backgroundPath път към фоновото изображение
     */
    public Level(int levelId, String name, String description, String difficulty, 
                String unlockRequirement, String backgroundPath) {
        this();
        this.levelId = levelId;
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.unlockRequirement = unlockRequirement;
        this.backgroundPath = backgroundPath;
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
     * Връща име на нивото
     * 
     * @return име на нивото
     */
    public String getName() {
        return name;
    }
    
    /**
     * Задава име на нивото
     * 
     * @param name име на нивото
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Връща описание на нивото
     * 
     * @return описание на нивото
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * Задава описание на нивото
     * 
     * @param description описание на нивото
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * Връща трудност на нивото
     * 
     * @return трудност на нивото
     */
    public String getDifficulty() {
        return difficulty;
    }
    
    /**
     * Задава трудност на нивото
     * 
     * @param difficulty трудност на нивото
     */
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    
    /**
     * Връща изискване за отключване
     * 
     * @return изискване за отключване
     */
    public String getUnlockRequirement() {
        return unlockRequirement;
    }
    
    /**
     * Задава изискване за отключване
     * 
     * @param unlockRequirement изискване за отключване
     */
    public void setUnlockRequirement(String unlockRequirement) {
        this.unlockRequirement = unlockRequirement;
    }
    
    /**
     * Връща път към фоновото изображение
     * 
     * @return път към фоновото изображение
     */
    public String getBackgroundPath() {
        return backgroundPath;
    }
    
    /**
     * Задава път към фоновото изображение
     * 
     * @param backgroundPath път към фоновото изображение
     */
    public void setBackgroundPath(String backgroundPath) {
        this.backgroundPath = backgroundPath;
    }
    
    /**
     * Връща списък с пъзелите в нивото
     * 
     * @return списък с пъзели
     */
    public List<Puzzle> getPuzzles() {
        return puzzles;
    }
    
    /**
     * Задава списък с пъзелите в нивото
     * 
     * @param puzzles списък с пъзели
     */
    public void setPuzzles(List<Puzzle> puzzles) {
        this.puzzles = puzzles;
    }
    
    /**
     * Добавя пъзел към нивото
     * 
     * @param puzzle пъзел за добавяне
     */
    public void addPuzzle(Puzzle puzzle) {
        if (this.puzzles == null) {
            this.puzzles = new ArrayList<>();
        }
        
        puzzle.setLevelId(this.levelId);
        this.puzzles.add(puzzle);
    }
    
    /**
     * Премахва пъзел от нивото
     * 
     * @param puzzleId ID на пъзела за премахване
     * @return true ако пъзелът е премахнат успешно, false ако не е намерен
     */
    public boolean removePuzzle(int puzzleId) {
        if (this.puzzles == null) {
            return false;
        }
        
        return this.puzzles.removeIf(puzzle -> puzzle.getPuzzleId() == puzzleId);
    }
    
    /**
     * Връща списък с куизовете в нивото
     * 
     * @return списък с куизове
     */
    public List<Quiz> getQuizzes() {
        return quizzes;
    }
    
    /**
     * Задава списък с куизовете в нивото
     * 
     * @param quizzes списък с куизове
     */
    public void setQuizzes(List<Quiz> quizzes) {
        this.quizzes = quizzes;
    }
    
    /**
     * Добавя куиз към нивото
     * 
     * @param quiz куиз за добавяне
     */
    public void addQuiz(Quiz quiz) {
        if (this.quizzes == null) {
            this.quizzes = new ArrayList<>();
        }
        
        quiz.setLevelId(this.levelId);
        this.quizzes.add(quiz);
    }
    
    /**
     * Премахва куиз от нивото
     * 
     * @param quizId ID на куиза за премахване
     * @return true ако куизът е премахнат успешно, false ако не е намерен
     */
    public boolean removeQuiz(int quizId) {
        if (this.quizzes == null) {
            return false;
        }
        
        return this.quizzes.removeIf(quiz -> quiz.getQuizId() == quizId);
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
     * Връща цвят, съответстващ на трудността на нивото
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
     * Връща брой на пъзелите в нивото
     * 
     * @return брой на пъзелите
     */
    public int getPuzzleCount() {
        return puzzles != null ? puzzles.size() : 0;
    }
    
    /**
     * Връща брой на куизовете в нивото
     * 
     * @return брой на куизовете
     */
    public int getQuizCount() {
        return quizzes != null ? quizzes.size() : 0;
    }
    
    /**
     * Проверява дали нивото е отключено за играча
     * 
     * @param playerId ID на играча
     * @param levelDao DAO обект за достъп до информация за нивата
     * @return true ако нивото е отключено, false ако не е
     */
    public boolean isUnlocked(int playerId, LevelDAO levelDao) {
        // Ако няма изискване за отключване, нивото е отключено по подразбиране
        if (unlockRequirement == null || unlockRequirement.isEmpty()) {
            return true;
        }
        
        // Ако изискването е за завършване на предишно ниво
        if (unlockRequirement.startsWith("level:")) {
            String[] parts = unlockRequirement.split(":");
            if (parts.length > 1) {
                try {
                    int requiredLevelId = Integer.parseInt(parts[1]);
                    
                    // Проверка дали играчът е завършил изискваното ниво
                    return levelDao.hasPlayerProgress(playerId, requiredLevelId) && 
                           levelDao.getPlayerProgress(playerId, requiredLevelId).isCompleted();
                } catch (NumberFormatException e) {
                    return false;
                }
            }
        }
        
        // По подразбиране нивото е заключено, ако не може да се анализира изискването
        return false;
    }
    
    /**
     * Връща максимален брой звезди, които могат да бъдат спечелени от нивото
     * 
     * @return максимален брой звезди
     */
    public int getMaxStars() {
        // Всяко ниво може да дава максимум 3 звезди
        return 3;
    }
    
    /**
     * Връща максимален брой точки, които могат да бъдат спечелени от нивото
     * 
     * @return максимален брой точки
     */
    public int getMaxScore() {
        int maxScore = 0;
        
        // Всеки пъзел дава определен брой точки
        if (puzzles != null) {
            for (Puzzle puzzle : puzzles) {
                maxScore += puzzle.getMaxScore();
            }
        }
        
        // Всеки куиз дава определен брой точки
        if (quizzes != null) {
            for (Quiz quiz : quizzes) {
                maxScore += quiz.getMaxScore();
            }
        }
        
        return maxScore;
    }
    
    @Override
    public String toString() {
        return "Level{" +
               "levelId=" + levelId +
               ", name='" + name + '\'' +
               ", difficulty='" + difficulty + '\'' +
               ", puzzles=" + getPuzzleCount() +
               ", quizzes=" + getQuizCount() +
               '}';
    }
}