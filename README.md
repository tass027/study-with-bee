# Study with Bee

Android app that helps students organize their tasks, run timed study sessions (Pomodoro technique) and track their progress with goals and achievement badges.

![Platform](https://img.shields.io/badge/Platform-Android-green.svg)
![Language](https://img.shields.io/badge/Language-Java-orange.svg)
![Min SDK](https://img.shields.io/badge/Min%20SDK-21-brightgreen.svg)
![License](https://img.shields.io/badge/License-MIT-blue.svg)

## Screenshots

<table>
  <tr>
    <td align="center"><img src="docs/01-home.png" width="220" alt="Home"><br><sub>Home</sub></td>
    <td align="center"><img src="docs/02-todo-list.png" width="220" alt="To-do list"><br><sub>Task list</sub></td>
    <td align="center"><img src="docs/04-study-timer.png" width="220" alt="Study timer"><br><sub>Study timer</sub></td>
  </tr>
  <tr>
    <td align="center"><img src="docs/05-achievements.png" width="220" alt="Achievements"><br><sub>Achievements and goals</sub></td>
    <td align="center"><img src="docs/06-lecture-notes.png" width="220" alt="Lecture notes"><br><sub>Lecture notes</sub></td>
  </tr>
</table>

## Features

- **Tasks**: create, edit and delete tasks with a due date and a priority (high, medium, low); completed vs. pending tasks; weekly calendar view; color-coded priority badges.
- **Study timer**: sessions of 25, 45 or 60 minutes with start, pause and reset; notification at the end of a session; progress saved automatically; total study time.
- **Goals**: weekly goal (days), daily goal (minutes) and total study time target, with progress bars and automatic goal detection.
- **Achievements**: four badges unlocked by completed tasks or study time (see below).
- **Statistics**: completion rate, total study time, study streak and session history.
- **Lecture notes**: create notes, organize them by subject, search them.
- **Reminders**: custom reminders for tasks, local notifications for upcoming deadlines and daily study reminders.
- **Accounts**: registration, login and user profile.

### Achievement badges

| Badge | Unlocked with |
|---|---|
| Beginner Bee | 5 tasks completed or 60 minutes studied |
| Study Bee | 20 tasks completed or 300 minutes studied |
| Expert Bee | 50 tasks completed or 600 minutes studied |
| Master Bee | 100 tasks completed or 1200 minutes studied |

## Tech stack

| Area | Technologies |
|---|---|
| Language | Java |
| Platform | Android SDK, min API 21 |
| Build | Gradle |
| UI | Material Components, CardView, RecyclerView, ConstraintLayout, CalendarView |
| Storage | SharedPreferences, file storage with JSON serialization |
| Other | CountDownTimer, Android notifications |

The code follows a simple MVC structure: `model` and `controller` packages for tasks, and one activity per screen (11 screens).

## Project structure

```
study-with-bee/
├── app/
│   └── src/main/
│       ├── AndroidManifest.xml
│       ├── java/com/example/myapplication/
│       │   ├── controller/TaskController.java
│       │   ├── model/Task.java
│       │   ├── SplashActivity.java
│       │   ├── LoginActivity.java / RegisterActivity.java
│       │   ├── MainActivity.java
│       │   ├── ToDoListActivity.java / AddTaskActivity.java / ViewTasksActivity.java
│       │   ├── StudyTimerActivity.java
│       │   ├── AchievementsActivity.java
│       │   ├── LectureNotesActivity.java
│       │   └── RemindersActivity.java
│       └── res/   # layouts, drawables, colors, strings, themes
├── docs/          # screenshots used in this README
└── gradle/
```

## Getting started

Requirements: Android Studio Arctic Fox (2020.3.1) or newer, JDK 8 or higher, Android SDK API 21+.

```bash
git clone https://github.com/tass027/study-with-bee.git
```

1. Open the `study-with-bee` folder in Android Studio.
2. Wait for the Gradle sync to finish.
3. Start an emulator (API 21+) or connect a device, then click **Run**.

## Design

The interface is based on the Figma Community file "StudyMate mobile app design", adapted for this project.
<!-- TO COMPLETE: add the author's name and the link to the ORIGINAL Community page (not your private copy), e.g. "by AUTHOR ([source](LINK))" -->

## Roadmap

- Dark mode
- Cloud sync with Firebase
- Statistics with charts
- Home screen widget
- Data export (CSV, PDF)
- Multi-language support (French, Arabic)

## License

MIT License. See the [LICENSE](LICENSE) file.

## Author

Tasnim Elmehrabi — [GitHub](https://github.com/tass027) · [LinkedIn](https://www.linkedin.com/in/VOTRE-PROFIL)
