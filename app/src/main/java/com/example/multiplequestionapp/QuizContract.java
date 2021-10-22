package com.example.multiplequestionapp;

import android.provider.BaseColumns;

// container for different constants that we later need for our sql operations if we later decide a column name we will change it here
//provide constants only
// create inner class for eeach different table in our db
public class QuizContract
{

    private QuizContract()
    {

    }
    //
    public static class CategoriesTable implements BaseColumns{
        public static final String TABLE_NAME_= "quiz_categories";
        public static final String COLUMN_NAME = "name";
    }

    //add a new column COLUMN_CATEGORY_ID where we saw the category of our question in form of number
    //but we will also add a whole new table to our db where we store all the different categories
    //and each category in the table questions will have a corresponding entry in the categories table
    //so each question will have a category id that points to a category  in our category table
    // and to enforce this relation we will use so called foreign keys constraints and these FKs constraints
    //make sure that we cant add any questions that have category id that is not represented in our categroies table
    //so lets say we have a question with categgory id 3 it wont be inserted to db bcz we dont have a category with id 3
    public static class QuestionTable implements BaseColumns
    {
        public static final String TABLE_NAME = "quiz_questions";
        public static final String  COLUMN_QUESTION = "question";
        public static final String  COLUMN_OPTION1 = "option1";
        public static final String  COLUMN_OPTION2 = "option2";
        public static final String  COLUMN_OPTION3 = "option3";
        public static final String  COLUMN_ANSWER_NR = "answer_nr";
        public static final String  COLUMN_DIFFICULTY = "difficulty";
        public static final String COLUMN_CATEGORY_ID = "category_id";
    }

}
