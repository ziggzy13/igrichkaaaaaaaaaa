package com.knowledgeheroes.model;

/**
 * Клас модел, представящ отговор на въпрос в играта
 */
public class Answer {
    private int answerId;
    private int questionId;
    private String text;
    private boolean correct;
    
    /**
     * Конструктор по подразбиране
     */
    public Answer() {
    }
    
    /**
     * Конструктор с основни параметри
     * 
     * @param text текст на отговора
     * @param correct дали отговорът е правилен
     */
    public Answer(String text, boolean correct) {
        this.text = text;
        this.correct = correct;
    }
    
    /**
     * Конструктор с всички параметри
     * 
     * @param answerId ID на отговора
     * @param questionId ID на въпроса
     * @param text текст на отговора
     * @param correct дали отговорът е правилен
     */
    public Answer(int answerId, int questionId, String text, boolean correct) {
        this.answerId = answerId;
        this.questionId = questionId;
        this.text = text;
        this.correct = correct;
    }
    
    /**
     * Връща ID на отговора
     * 
     * @return ID на отговора
     */
    public int getAnswerId() {
        return answerId;
    }
    
    /**
     * Задава ID на отговора
     * 
     * @param answerId ID на отговора
     */
    public void setAnswerId(int answerId) {
        this.answerId = answerId;
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
     * Връща текст на отговора
     * 
     * @return текст на отговора
     */
    public String getText() {
        return text;
    }
    
    /**
     * Задава текст на отговора
     * 
     * @param text текст на отговора
     */
    public void setText(String text) {
        this.text = text;
    }
    
    /**
     * Проверява дали отговорът е правилен
     * 
     * @return true ако отговорът е правилен, false ако не е
     */
    public boolean isCorrect() {
        return correct;
    }
    
    /**
     * Задава дали отговорът е правилен
     * 
     * @param correct дали отговорът е правилен
     */
    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
    
    /**
     * Връща стилизиран текст на отговора в зависимост от това дали е правилен
     * 
     * @param showCorrectness дали да се покаже индикация за правилност
     * @return стилизиран текст на отговора
     */
    public String getStyledText(boolean showCorrectness) {
        if (!showCorrectness) {
            return text;
        }
        
        if (correct) {
            return "✓ " + text;
        } else {
            return "✗ " + text;
        }
    }
    
    /**
     * Връща цвят за отговора в зависимост от това дали е правилен
     * 
     * @param showCorrectness дали да се покаже индикация за правилност
     * @return RGB цвят като String
     */
    public String getColor(boolean showCorrectness) {
        if (!showCorrectness) {
            return "#FFFFFF"; // бяло (нормален цвят)
        }
        
        if (correct) {
            return "#00FF00"; // зелено (правилен)
        } else {
            return "#FF0000"; // червено (грешен)
        }
    }
    
    @Override
    public String toString() {
        return "Answer{" +
               "answerId=" + answerId +
               ", text='" + text + '\'' +
               ", correct=" + correct +
               '}';
    }
}