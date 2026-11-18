# Pooki Planner

**Smart Study Planner & Study Room Booking App**

An Android application that helps students manage their study tasks and book study rooms, all in one place.

Built with **Java** and **Android SDK** as a project for the *Visual Programming for Smart Devices* lab course.

---

## The Problem

- Students forget study tasks and deadlines.
- It is hard to know which study rooms are available.
- Manual room booking is unorganized and inefficient.

## The Solution

Pooki Planner lets students manage their tasks, view and book study rooms, track their bookings, and stay focused with a built-in study timer.

---

## Features

| Screen | What it does |
| --- | --- |
| **Splash** | Shows the app logo and tagline, then opens Login or Main automatically |
| **Login / Sign up** | Create an account and sign in, with input validation and Toast messages |
| **Home** | Welcome message, upcoming booking, today's tasks, and study streak counter |
| **Tasks** | Add, delete, and mark tasks as completed in a RecyclerView list |
| **Rooms** | View study rooms with capacity and available hours; Green = Available, Red = Booked |
| **Timer** | Pomodoro timer: 25 minutes focus + 5 minutes break |
| **Profile** | Student info, total bookings count, dark mode toggle, and logout with confirmation |

### Extra touches

- Dark mode that is remembered between sessions
- Study streak counter to build a daily habit
- Prevents duplicate bookings for the same room, date, and time
- Date and time pickers for task due dates and bookings

---

## User Flow

```
Splash Screen
     |
Login / Sign up
     |
Main Activity (Bottom Navigation)
     |
 Home | Tasks | Rooms | Profile
          |        |
      Add Task  Add Booking
```

---

## Tech Stack

- **Language:** Java
- **Platform:** Android (Android Studio)
- **Local database:** SQLite
- **Settings and session:** SharedPreferences
- **UI:** Material Design 3, ConstraintLayout, MaterialCardView, RecyclerView, BottomNavigationView

## Android Concepts Used

| Concept | Where it is used |
| --- | --- |
| Activities | Splash, Login, Main, Add Task, Add Booking |
| Intents | Screen-to-screen navigation |
| Fragments | Home, Tasks, Rooms, Profile, Timer tabs inside Main |
| RecyclerView | Tasks list and rooms list |
| Bottom Navigation | Switching between the main tabs |
| SharedPreferences | Login session, dark mode, study streak |
| SQLite | Saving tasks and bookings |
| AlertDialog | Delete task and logout confirmation |
| Date / Time Picker | Task due date and booking date and time |
| Menus | Toolbar menu for quick actions |

---

## Project Structure

```
com.example.pookiplanner
├── activities/    Splash, Login, SignUp, Main, AddTask, AddBooking
├── fragments/     Home, Tasks, Rooms, Profile, Timer
├── adapters/      TaskAdapter, RoomAdapter
├── models/        StudyTask, StudyRoom, Booking
├── database/      DatabaseHelper (SQLite), RoomDataProvider
└── utils/         PrefManager, ValidationUtils, ThemeHelper, DateUtils
```

## Data Storage

**SQLite**

- `tasks` table: id, title, description, due_date, completed
- `bookings` table: id, room_id, room_name, date, time, student_id

**SharedPreferences**

- Login status and student info
- Dark mode preference
- Study streak and last study date

---

## Validation Rules

| Screen | Rule |
| --- | --- |
| Login | Valid input required before signing in |
| Add Task | Title and due date are required |
| Add Booking | Room, date, and time are required |
| Booking | Duplicate room, date, and time bookings are blocked |

## Challenges and Solutions

| Challenge | Solution |
| --- | --- |
| Keeping the user logged in | SharedPreferences |
| Managing lists dynamically | RecyclerView + Adapters |
| Preventing double bookings | SQLite query before insert |
| Supporting dark mode | AppCompatDelegate + theme recreation |
| Organizing multiple screens | Fragments + Bottom Navigation |

---

## How to Run

1. Clone the repository:
   ```
   git clone https://github.com/soolafkiwan12-code/pooki-planner.git
   ```
2. Open the project in **Android Studio**.
3. Wait for Gradle sync to finish.
4. Run the app on an emulator or a real Android device.

## Future Improvements

- Push notifications for booking reminders
- Backend server for real-time room availability
- QR code check-in for booked rooms

---

## Author

**Soolaf Kiwan**
Computer Science student, Al-Balqa Applied University

[LinkedIn](https://www.linkedin.com/in/soolaf-k-a52080283) | [GitHub](https://github.com/soolafkiwan12-code)
