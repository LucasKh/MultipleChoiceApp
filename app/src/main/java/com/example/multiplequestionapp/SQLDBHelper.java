package com.example.multiplequestionapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.multiplequestionapp.QuizContract.*;

import java.util.ArrayList;
import java.util.List;

    public class SQLDBHelper extends SQLiteOpenHelper {
            private static final String DATABASE_NAME = "MultipleChoice";
            //whenever we need to change the table we should increment the version to 2 and call the onUpgrade method
        //ad drop the table and create again or we can uninstall the app
            private static final int DATABASE_VERSION = 1;

            private static SQLDBHelper instance;//Since we add the spinner of categories we need to access the db there
        // and if we initialize SQLiteOpenHelper multiple times from different activites we actually open multiple databse connections which is the memory leak
        //so we transform it to singleton well have only one instance of dbHelper

        private  final static String TABLE_NAME_USERS = "TBL_USERS";
        private final static String COLUMN_ID = "USER_ID";
        private final static String COLUMN_USERNAME = "USER_NAME";
        private final static String COLUMN_PASSWORD = "USER_PASSWORD";
        private final static String COLUMN_CONF_PASSWORD = "USER_CONF_PASSWORD";
        

            private SQLiteDatabase db;
            private SQLDBHelper(Context context) { //we dont want to be able to create new objects of it instead return the same one
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }

            //synchronized is there if we want to access it from multiple threads
            public static synchronized SQLDBHelper getInstance(Context context) {
                if(instance == null)
                {
                    instance = new SQLDBHelper(context.getApplicationContext());
                }
                return instance;
            }
            @Override
            public void onCreate(SQLiteDatabase db) {
                this.db = db;

                final String SQL_CREATE_CATEGORIES_TABLE = "CREATE TABLE " +
                        CategoriesTable.TABLE_NAME_ + " ( " +
                        CategoriesTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        CategoriesTable.COLUMN_NAME + " TEXT " +
                        ")";
                final String SQL_CREATE_QUESTIONS_TABLE = "CREATE TABLE " +
                        QuestionTable.TABLE_NAME + " ( " +
                        QuestionTable._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        QuestionTable.COLUMN_QUESTION + " TEXT, " +
                        QuestionTable.COLUMN_OPTION1 + " TEXT, " +
                        QuestionTable.COLUMN_OPTION2 + " TEXT, " +
                        QuestionTable.COLUMN_OPTION3 + " TEXT, " +
                        QuestionTable.COLUMN_ANSWER_NR + " INTEGER, " +
                        QuestionTable.COLUMN_DIFFICULTY + " TEXT, " +
                        QuestionTable.COLUMN_CATEGORY_ID + " INTEGER, " +
                        "FOREIGN KEY(" + QuestionTable.COLUMN_CATEGORY_ID + ") REFERENCES " +
                        CategoriesTable.TABLE_NAME_ + "(" + CategoriesTable._ID + ")" + "ON DELETE CASCADE" +
                        ")";//category id in question table which is integer value and this category in questions table is a FK
                //which references ID in categories table
                //ONDELETE we define whats gonna happen when we delete a category from categories table
                //CASCADE if we delete a category from our categories table we delete also questions from questions table
                //that reference to this category id

                final String SQL_CREATE_TABLE_USERS = "CREATE TABLE " + TABLE_NAME_USERS + " ( " + COLUMN_ID + " INTEGER PRIMARY KEY, " +
                              COLUMN_USERNAME + " TEXT, " + COLUMN_PASSWORD + " TEXT, "
                              + COLUMN_CONF_PASSWORD + " TEXT " + ")";
                db.execSQL(SQL_CREATE_QUESTIONS_TABLE);
                db.execSQL(SQL_CREATE_CATEGORIES_TABLE);
                db.execSQL(SQL_CREATE_TABLE_USERS);
                fillCategoriesTable();
                fillQuestionsTable();

            }
            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + QuestionTable.TABLE_NAME);
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USERS);
                db.execSQL("DROP TABLE IF EXISTS " + CategoriesTable.TABLE_NAME_);
                onCreate(db);
            }


        public long insertUser(Users user)
        {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COLUMN_USERNAME, user.getUsername());
            values.put(COLUMN_PASSWORD, user.getPassword());
            values.put(COLUMN_CONF_PASSWORD, user.getConfPassword());

            // insert row
            long id = db.insert(TABLE_NAME_USERS, null, values);

            return id;
        }
//this allows to enable foreign key cnstraints
        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
        }
        private void fillCategoriesTable()
        {
            Category c1 = new Category("Programming");
            addCategory(c1);
            Category c2 = new Category("Feedback");
            addCategory(c2);

        }
        private void addCategory(Category category)
        {
            ContentValues cv = new ContentValues();
            cv.put(CategoriesTable.COLUMN_NAME, category.getName());
            db.insert(CategoriesTable.TABLE_NAME_, null, cv);
        }

        private void fillQuestionsTable()
        {
            Questions q1 = new Questions("Who developed android?",
                        "Android INC", "Apple", "Google", 1,
                        Questions.DIFFICULTY_EASY, Category.PROGRAMMING);
                addQuestion(q1);
            Questions q2 = new Questions("What method do we use to kill an activity?",
                    "onResume()", "finish()", "kill()", 2,
                    Questions.DIFFICULTY_MEDIUM, Category.PROGRAMMING);
            addQuestion(q2);
            Questions q3 = new Questions("Android purchased by Google in which year?",
                    "2001", "2004", "2005", 3,
                    Questions.DIFFICULTY_MEDIUM, Category.PROGRAMMING);
            addQuestion(q3);
            Questions q4 = new Questions("Android is linux based,because \n of ____ ?",
                    "both, portability and security", "Portability", "Security", 1,
                    Questions.DIFFICULTY_HARD, Category.PROGRAMMING);
            addQuestion(q4);
            Questions q5 = new Questions("Which file in Android application \n holds to use the internet?",
                    "System file", "Create file", "Manifest file", 3,
                    Questions.DIFFICULTY_HARD, Category.PROGRAMMING);
            addQuestion(q5);
            Questions q6 = new Questions("Who teaches the Mobile Course",
                    "Tony", "Charbel", "Barbar", 1,
                    Questions.DIFFICULTY_EASY, Category.FEEDBACK);
            addQuestion(q6);
            Questions q7 = new Questions("Who's is the best doctor? ",
                    "X", "Tony", "Y", 2,
                    Questions.DIFFICULTY_EASY, Category.FEEDBACK);
            addQuestion(q7);
            Questions q8 = new Questions("Who's gonna give us an A? ",
                    "Y", "X", "Tony", 3,
                    Questions.DIFFICULTY_EASY, Category.FEEDBACK);
            addQuestion(q8);
            Questions q9 = new Questions("How did you find the project?",
                    "Good", "Very Good", "Excellent", 3,
                    Questions.DIFFICULTY_MEDIUM, Category.FEEDBACK);
            addQuestion(q9);
            Questions q10 = new Questions("Who's leaving the university?",
                    "Tony", "Jean", "Louay", 1,
                    Questions.DIFFICULTY_MEDIUM, Category.FEEDBACK);
            addQuestion(q10);
            Questions q11 = new Questions(" Layouts in android?",
                    "Frame ", "Linear", "Both", 3,
                    Questions.DIFFICULTY_MEDIUM, Category.PROGRAMMING);
            addQuestion(q11);
            Questions q12 = new Questions("How much did we get?",
                    "100", "99", "98", 1,
                    Questions.DIFFICULTY_HARD, Category.FEEDBACK);
            addQuestion(q12);
            Questions q13 = new Questions("Storage Methods in Android:",
                    "SQLite Databases", "Shared Preferences", "All of the Above", 3,
                    Questions.DIFFICULTY_MEDIUM, Category.PROGRAMMING);
            addQuestion(q13);
            }
            private void addQuestion(Questions question) {
                ContentValues cv = new ContentValues();
                cv.put(QuestionTable.COLUMN_QUESTION, question.getQuestion());
                cv.put(QuestionTable.COLUMN_OPTION1, question.getOption1());
                cv.put(QuestionTable.COLUMN_OPTION2, question.getOption2());
                cv.put(QuestionTable.COLUMN_OPTION3, question.getOption3());
                cv.put(QuestionTable.COLUMN_ANSWER_NR, question.getAnswerNr());
                cv.put(QuestionTable.COLUMN_DIFFICULTY, question.getDifficulty());
                cv.put(QuestionTable.COLUMN_CATEGORY_ID, question.getCategoryID());
                db.insert(QuestionTable.TABLE_NAME, null, cv);
            }
        public ArrayList<Category> getAllCategories() {
            ArrayList<Category> cateoryList = new ArrayList<>();
            db = getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT * FROM " + CategoriesTable.TABLE_NAME_, null);

            if (cursor.moveToFirst()) {
                do {
                    Category category = new Category();
                    category.setId(cursor.getInt(cursor.getColumnIndex(CategoriesTable._ID)));
                    category.setName(cursor.getString(cursor.getColumnIndex(CategoriesTable.COLUMN_NAME)));
                    cateoryList.add(category);
                } while (cursor.moveToNext());
            }
            cursor.close();
            return cateoryList;
        }
            //retreive all questions
            public ArrayList<Questions> getAllQuestions() {
                ArrayList<Questions> questionList = new ArrayList<>();
                db = getReadableDatabase();
                Cursor c = db.rawQuery("SELECT * FROM " + QuestionTable.TABLE_NAME, null);
                if (c.moveToFirst()) {
                    do {
                        Questions question = new Questions();
                        question.setId(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                        question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                        question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                        question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                        question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                        question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));
                        question.setDifficulty(c.getString(c.getColumnIndex(QuestionTable.COLUMN_DIFFICULTY)));
                        question.setCategoryID(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CATEGORY_ID)));
                        questionList.add(question);
                    } while (c.moveToNext());
                }
                c.close();
                return questionList;
            }

            public ArrayList<Users> getAllUsers() {
                ArrayList<Users> usersList = new ArrayList<>();
                db = getReadableDatabase();
                Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME_USERS, null);

                if (cursor.moveToFirst()) {
                    do {
                        Users u = new Users();
                        u.setId(cursor.getLong(cursor.getColumnIndex(COLUMN_ID)));
                        u.setUsername(cursor.getString(cursor.getColumnIndex(COLUMN_USERNAME)));
                        u.setPassword(cursor.getString(cursor.getColumnIndex(COLUMN_PASSWORD)));
                        u.setConfPassword(cursor.getString(cursor.getColumnIndex(COLUMN_CONF_PASSWORD)));
                        usersList.add(u);
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return usersList;
            }
            //so when we call this method we need to pass difficulty to it
        public ArrayList<Questions> getQuestions(int categoryId, String difficulty) {
            ArrayList<Questions> questionList = new ArrayList<>();
            db = getReadableDatabase();

            String selection = QuestionTable.COLUMN_CATEGORY_ID + " =? " +
                    " AND " + QuestionTable.COLUMN_DIFFICULTY + " =? ";

            //we need to filter for both category id and difficulty level we will replace these 2 ? with selectionArgs
            //both should be string so use valueOf to transform the id number into a string
            String [] selectionArgs = new String[] {String.valueOf(categoryId), difficulty};

            //query is used for more complicated queries
            Cursor c = db.query(
                    QuestionTable.TABLE_NAME, //retrieve questions
                    null,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    null
            );
//
//            //instead of retreiving all questions we need the ones that fit to outr difficulty levels
//            String [] selectionArgs = new String[]{difficulty};
//            //? is just a placeholder which then we place it by the difficulty levels
//            //get all questions from our db table where difficulty levels fits to whatever level we pass here
//            Cursor c = db.rawQuery("SELECT * FROM " + QuestionTable.TABLE_NAME +
//                    " WHERE " + QuestionTable.COLUMN_DIFFICULTY + " = ?", selectionArgs);
            if (c.moveToFirst()) {
                do {
                    Questions question = new Questions();
                    question.setId(c.getInt(c.getColumnIndex(QuestionTable._ID)));
                    question.setQuestion(c.getString(c.getColumnIndex(QuestionTable.COLUMN_QUESTION)));
                    question.setOption1(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION1)));
                    question.setOption2(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION2)));
                    question.setOption3(c.getString(c.getColumnIndex(QuestionTable.COLUMN_OPTION3)));
                    question.setAnswerNr(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_ANSWER_NR)));
                    question.setDifficulty(c.getString(c.getColumnIndex(QuestionTable.COLUMN_DIFFICULTY)));
                    question.setCategoryID(c.getInt(c.getColumnIndex(QuestionTable.COLUMN_CATEGORY_ID)));
                    questionList.add(question);
                } while (c.moveToNext());
            }
            c.close();
            return questionList;
        }
    }