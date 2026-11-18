package com.example.pookiplanner.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.pookiplanner.models.Booking;
import com.example.pookiplanner.models.StudyTask;
import com.example.pookiplanner.models.User;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "pooki_planner.db";
    private static final int DB_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT UNIQUE NOT NULL," +
                "password TEXT NOT NULL," +
                "name TEXT," +
                "age INTEGER DEFAULT 0," +
                "education_type TEXT," +
                "university_name TEXT," +
                "governorate TEXT," +
                "study_preference TEXT," +
                "profile_complete INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "title TEXT NOT NULL," +
                "description TEXT," +
                "due_date TEXT NOT NULL," +
                "completed INTEGER DEFAULT 0)");
        db.execSQL("CREATE TABLE bookings (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "user_id INTEGER NOT NULL," +
                "room_id TEXT NOT NULL," +
                "room_name TEXT NOT NULL," +
                "date TEXT NOT NULL," +
                "time TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS tasks");
        db.execSQL("DROP TABLE IF EXISTS bookings");
        onCreate(db);
    }

    public long registerUser(String email, String password) {
        ContentValues v = new ContentValues();
        v.put("email", email);
        v.put("password", password);
        v.put("profile_complete", 0);
        return getWritableDatabase().insert("users", null, v);
    }

    public boolean emailExists(String email) {
        Cursor c = getReadableDatabase().query("users", new String[]{"id"},
                "email = ?", new String[]{email}, null, null, null);
        boolean exists = c.getCount() > 0;
        c.close();
        return exists;
    }

    public User login(String email, String password) {
        Cursor c = getReadableDatabase().query("users", null,
                "email = ? AND password = ?", new String[]{email, password},
                null, null, null);
        User user = null;
        if (c.moveToFirst()) user = userFromCursor(c);
        c.close();
        return user;
    }

    public User findByEmail(String email) {
        Cursor c = getReadableDatabase().query("users", null,
                "email = ?", new String[]{email}, null, null, null);
        User user = null;
        if (c.moveToFirst()) user = userFromCursor(c);
        c.close();
        return user;
    }

    /** Login or create account for Google / Apple / Guest sign-in */
    public User loginOrRegisterSocial(String email, String displayName, String provider) {
        User existing = findByEmail(email);
        if (existing != null) return existing;

        String password = "oauth_" + provider;
        long id = registerUser(email, password);
        User user = getUserById(id);
        if (user != null && displayName != null && !displayName.trim().isEmpty()) {
            user.setName(displayName.trim());
            ContentValues v = new ContentValues();
            v.put("name", displayName.trim());
            getWritableDatabase().update("users", v, "id = ?",
                    new String[]{String.valueOf(id)});
            user.setName(displayName.trim());
        }
        return user;
    }

    public User getUserById(long id) {
        Cursor c = getReadableDatabase().query("users", null,
                "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        User user = null;
        if (c.moveToFirst()) user = userFromCursor(c);
        c.close();
        return user;
    }

    public void updateProfile(User user) {
        ContentValues v = new ContentValues();
        v.put("name", user.getName());
        v.put("age", user.getAge());
        v.put("education_type", user.getEducationType());
        v.put("university_name", user.getUniversityName());
        v.put("governorate", user.getGovernorate());
        v.put("study_preference", user.getStudyPreference());
        v.put("profile_complete", 1);
        getWritableDatabase().update("users", v, "id = ?",
                new String[]{String.valueOf(user.getId())});
    }

    private User userFromCursor(Cursor c) {
        User u = new User();
        u.setId(c.getLong(c.getColumnIndexOrThrow("id")));
        u.setEmail(c.getString(c.getColumnIndexOrThrow("email")));
        u.setPassword(c.getString(c.getColumnIndexOrThrow("password")));
        u.setName(c.getString(c.getColumnIndexOrThrow("name")));
        u.setAge(c.getInt(c.getColumnIndexOrThrow("age")));
        u.setEducationType(c.getString(c.getColumnIndexOrThrow("education_type")));
        u.setUniversityName(c.getString(c.getColumnIndexOrThrow("university_name")));
        u.setGovernorate(c.getString(c.getColumnIndexOrThrow("governorate")));
        u.setStudyPreference(c.getString(c.getColumnIndexOrThrow("study_preference")));
        u.setProfileComplete(c.getInt(c.getColumnIndexOrThrow("profile_complete")) == 1);
        return u;
    }

    public long addTask(StudyTask task) {
        ContentValues v = new ContentValues();
        v.put("user_id", task.getUserId());
        v.put("title", task.getTitle());
        v.put("description", task.getDescription());
        v.put("due_date", task.getDueDate());
        v.put("completed", task.isCompleted() ? 1 : 0);
        return getWritableDatabase().insert("tasks", null, v);
    }

    public List<StudyTask> getTasksForUser(long userId) {
        List<StudyTask> list = new ArrayList<>();
        Cursor c = getReadableDatabase().query("tasks", null, "user_id = ?",
                new String[]{String.valueOf(userId)}, null, null, "id DESC");
        while (c.moveToNext()) {
            StudyTask t = new StudyTask();
            t.setId(c.getLong(c.getColumnIndexOrThrow("id")));
            t.setUserId(c.getLong(c.getColumnIndexOrThrow("user_id")));
            t.setTitle(c.getString(c.getColumnIndexOrThrow("title")));
            t.setDescription(c.getString(c.getColumnIndexOrThrow("description")));
            t.setDueDate(c.getString(c.getColumnIndexOrThrow("due_date")));
            t.setCompleted(c.getInt(c.getColumnIndexOrThrow("completed")) == 1);
            list.add(t);
        }
        c.close();
        return list;
    }

    public List<StudyTask> getTasksForDate(long userId, String date) {
        List<StudyTask> result = new ArrayList<>();
        for (StudyTask t : getTasksForUser(userId)) {
            if (date.equals(t.getDueDate()) && !t.isCompleted()) result.add(t);
        }
        return result;
    }

    public void updateTaskCompleted(long id, boolean completed) {
        ContentValues v = new ContentValues();
        v.put("completed", completed ? 1 : 0);
        getWritableDatabase().update("tasks", v, "id = ?", new String[]{String.valueOf(id)});
    }

    public void deleteTask(long id) {
        getWritableDatabase().delete("tasks", "id = ?", new String[]{String.valueOf(id)});
    }

    public long addBooking(Booking b) {
        ContentValues v = new ContentValues();
        v.put("user_id", b.getUserId());
        v.put("room_id", b.getRoomId());
        v.put("room_name", b.getRoomName());
        v.put("date", b.getDate());
        v.put("time", b.getTime());
        return getWritableDatabase().insert("bookings", null, v);
    }

    public boolean isRoomBooked(String roomId, String date, String time) {
        Cursor c = getReadableDatabase().query("bookings", new String[]{"id"},
                "room_id = ? AND date = ? AND time = ?",
                new String[]{roomId, date, time}, null, null, null);
        boolean booked = c.getCount() > 0;
        c.close();
        return booked;
    }

    public List<Booking> getBookingsForUser(long userId) {
        List<Booking> list = new ArrayList<>();
        Cursor c = getReadableDatabase().query("bookings", null, "user_id = ?",
                new String[]{String.valueOf(userId)}, null, null, "date ASC");
        while (c.moveToNext()) {
            Booking b = new Booking();
            b.setId(c.getLong(c.getColumnIndexOrThrow("id")));
            b.setUserId(c.getLong(c.getColumnIndexOrThrow("user_id")));
            b.setRoomId(c.getString(c.getColumnIndexOrThrow("room_id")));
            b.setRoomName(c.getString(c.getColumnIndexOrThrow("room_name")));
            b.setDate(c.getString(c.getColumnIndexOrThrow("date")));
            b.setTime(c.getString(c.getColumnIndexOrThrow("time")));
            list.add(b);
        }
        c.close();
        return list;
    }

    public Booking getUpcomingBooking(long userId) {
        List<Booking> list = getBookingsForUser(userId);
        return list.isEmpty() ? null : list.get(0);
    }

    public int getBookingCount(long userId) {
        return getBookingsForUser(userId).size();
    }
}
