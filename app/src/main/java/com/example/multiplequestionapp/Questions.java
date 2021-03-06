package com.example.multiplequestionapp;
//information about questions  in our database its the bridge of this app and our database
public class Questions
{
    //save our difficulty in form of a text in our db so when we open our db we will see difficulty lvl in form of text
    //static so we can access them without creating an object
    //final so we dont change them
    public static final String DIFFICULTY_EASY = "Easy";
    public static final String DIFFICULTY_MEDIUM = "Medium";
    public static final String DIFFICULTY_HARD = "Hard";

    private int id; //later if we need to make changes or delete a question well need its id to identify it
    private String question;
    private String option1;
    private String option2;
    private String option3;
    private int answerNr;//number 1 if option1 is correct number2 if option 2 is correct
    private String difficulty;
    private int categoryID;

    //empty constructor when we carry our data from our db well create an empty question object and set methods to set values into these object
    public Questions()
    {

    }
    public Questions(String question, String option1, String option2, String option3, int answerNr, String difficulty, int categoryID) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.answerNr = answerNr;
        this.difficulty = difficulty;
        this.categoryID = categoryID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public int getAnswerNr() {
        return answerNr;
    }

    public void setAnswerNr(int answerNr) {
        this.answerNr = answerNr;
    }

    public String getDifficulty() { return difficulty; }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    //return all difficulty levels in form of a string array  when we need to have a list of all difficulty levels we simply  need to call this method only
    public static String[] getAllDifficultyLevels() {
        return new String[]{
            DIFFICULTY_EASY,
                DIFFICULTY_MEDIUM,
                DIFFICULTY_HARD
        };
    }
}
