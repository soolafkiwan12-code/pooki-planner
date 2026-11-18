package com.example.pookiplanner.database;

import com.example.pookiplanner.models.CoffeeHouse;
import com.example.pookiplanner.models.StudyRoom;
import com.example.pookiplanner.models.User;

import java.util.ArrayList;
import java.util.List;

public final class DataProvider {

    private DataProvider() {}

    public static List<CoffeeHouse> getCoffeeHouses(String governorate) {
        List<CoffeeHouse> all = new ArrayList<>();
        if (User.GOV_IRBID.equals(governorate)) {
            all.add(new CoffeeHouse("ir1", "Books & Beans Irbid", User.GOV_IRBID, "University Street", "8 AM - 11 PM", true, 5));
            all.add(new CoffeeHouse("ir2", "Matcha Corner", User.GOV_IRBID, "Downtown", "9 AM - 10 PM", true, 4));
            all.add(new CoffeeHouse("ir3", "Study Hub Café", User.GOV_IRBID, "Yarmouk Area", "24 Hours", true, 5));
            all.add(new CoffeeHouse("ir4", "Strawberry Latte", User.GOV_IRBID, "City Center", "10 AM - 9 PM", true, 4));
        } else if (User.GOV_AMMAN.equals(governorate)) {
            all.add(new CoffeeHouse("am1", "Pooki Study Café Amman", User.GOV_AMMAN, "Abdali", "8 AM - 12 AM", true, 5));
            all.add(new CoffeeHouse("am2", "Rainbow Street Roasters", User.GOV_AMMAN, "Jabal Amman", "9 AM - 11 PM", true, 5));
            all.add(new CoffeeHouse("am3", "University Café", User.GOV_AMMAN, "Swefieh", "7 AM - 10 PM", true, 4));
            all.add(new CoffeeHouse("am4", "Quiet Cup", User.GOV_AMMAN, "Shmeisani", "8 AM - 9 PM", true, 4));
            all.add(new CoffeeHouse("am5", "Matcha & More", User.GOV_AMMAN, "Khalda", "10 AM - 11 PM", true, 5));
        }
        return all;
    }

    public static List<StudyRoom> getStudyRooms(String governorate) {
        List<StudyRoom> rooms = new ArrayList<>();
        if (User.GOV_IRBID.equals(governorate)) {
            rooms.add(new StudyRoom("ir_a101", "Room A101 - Irbid", User.GOV_IRBID, 4, "8 AM - 8 PM"));
            rooms.add(new StudyRoom("ir_silent", "Silent Room Irbid", User.GOV_IRBID, 2, "24 Hours"));
            rooms.add(new StudyRoom("ir_group", "Group Room Irbid", User.GOV_IRBID, 8, "10 AM - 6 PM"));
        } else if (User.GOV_AMMAN.equals(governorate)) {
            rooms.add(new StudyRoom("am_b202", "Room B202 - Amman", User.GOV_AMMAN, 6, "9 AM - 9 PM"));
            rooms.add(new StudyRoom("am_silent", "Silent Room Amman", User.GOV_AMMAN, 2, "24 Hours"));
            rooms.add(new StudyRoom("am_group", "Group Room Amman", User.GOV_AMMAN, 10, "10 AM - 8 PM"));
        }
        return rooms;
    }

    public static String getGovernorateDisplayName(String gov, boolean arabic) {
        if (User.GOV_IRBID.equals(gov)) return arabic ? "إربد" : "Irbid";
        if (User.GOV_AMMAN.equals(gov)) return arabic ? "عمان" : "Amman";
        return gov;
    }
}
