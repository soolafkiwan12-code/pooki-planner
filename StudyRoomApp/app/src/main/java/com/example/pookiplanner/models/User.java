package com.example.pookiplanner.models;

public class User {
    public static final String EDUCATION_SCHOOL = "school";
    public static final String EDUCATION_UNIVERSITY = "university";
    public static final String PREF_STUDY_OUTSIDE = "outside";
    public static final String PREF_BOOK_ROOM = "room";
    public static final String GOV_IRBID = "irbid";
    public static final String GOV_AMMAN = "amman";

    private long id;
    private String email;
    private String password;
    private String name;
    private int age;
    private String educationType;
    private String universityName;
    private String governorate;
    private String studyPreference;
    private boolean profileComplete;

    public User() {}

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getEducationType() { return educationType; }
    public void setEducationType(String educationType) { this.educationType = educationType; }
    public String getUniversityName() { return universityName; }
    public void setUniversityName(String universityName) { this.universityName = universityName; }
    public String getGovernorate() { return governorate; }
    public void setGovernorate(String governorate) { this.governorate = governorate; }
    public String getStudyPreference() { return studyPreference; }
    public void setStudyPreference(String studyPreference) { this.studyPreference = studyPreference; }
    public boolean isProfileComplete() { return profileComplete; }
    public void setProfileComplete(boolean profileComplete) { this.profileComplete = profileComplete; }
}
