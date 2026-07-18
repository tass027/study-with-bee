# Study with Bee - Smart Study Companion

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
[![Java](https://img.shields.io/badge/Language-Java-orange.svg)](https://www.java.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Version](https://img.shields.io/badge/Version-1.0.0-brightgreen.svg)](https://github.com/tass027/study-with-bee)
[![Maintained](https://img.shields.io/badge/Maintained-Yes-success.svg)](https://github.com/tass027/study-with-bee)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg)](https://android-arsenal.com/api?level=21)

An Android application designed to help students organize tasks, track study time, and achieve their academic goals through structured planning and gamification features.

---

## Features

### Task Management
- Create, edit, and delete tasks
- Set due dates and priority levels (High, Medium, Low)
- Track completed versus pending tasks
- Weekly calendar view for task organization
- Color-coded priority badges

### Study Timer (Pomodoro Technique)
- Customizable timer durations (25, 45, or 60 minutes)
- Start, pause, and reset functionality
- Real-time session tracking
- Session completion notifications
- Automatic progress saving
- Total study time calculator

### Achievement and Badge System
Badges unlock as the user progresses:
- **Beginner Bee**: 5 tasks completed or 60 minutes studied
- **Study Bee**: 20 tasks completed or 300 minutes studied
- **Expert Bee**: 50 tasks completed or 600 minutes studied
- **Master Bee**: 100 tasks completed or 1200 minutes studied

### Personalized Goal Setting
- Weekly study goals (number of days)
- Daily study goals (minutes per day)
- Total study time targets
- Visual progress bars
- Automatic goal achievement detection

### Statistics and Analytics
- Total tasks created and completion rate
- Total study time tracking
- Study streak counter
- Session history with detailed logs
- Badge unlock progress
- Visual progress reporting

### Lecture Notes
- Create and organize lecture notes
- Categorize notes by subject
- Search functionality
- Auto-save

### Reminders
- Custom reminders for tasks
- Push notifications for upcoming deadlines
- Daily study reminders

### User Authentication
- Secure login and registration
- User profile management
- Data privacy and security

---

## Technologies and Tools

### Core Technologies
- **Language**: Java
- **Platform**: Android SDK (API 21+)
- **Build System**: Gradle
- **Architecture**: MVC (Model-View-Controller)

### Android Components
- 10+ activities across distinct screens
- Material Design component library
- CardView-based layouts
- RecyclerView for list displays
- ConstraintLayout for responsive UI
- CalendarView for date selection and week view

### Data Persistence
- SharedPreferences for user settings and preferences
- File storage for task and note management
- JSON for data serialization

### Key Implementations
- CountDownTimer for the study timer
- Notification system for reminders
- Custom views for the app's visual identity
- ProgressBar components for progress tracking

---

## Installation and Setup

### Prerequisites
- Android Studio Arctic Fox (2020.3.1) or newer
- JDK 8 or higher
- Android SDK API 21 (Lollipop) or higher
- Gradle 7.0+

### Installation Steps

1. **Clone the repository**
```bash
git clone https://github.com/tass027/study-with-bee.git
cd study-with-bee
```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the cloned directory
   - Click "OK"

3. **Sync Gradle**
   - Wait for Gradle sync to complete
   - Resolve any dependency issues if prompted

4. **Configure Android SDK**
   - Ensure Android SDK API 21+ is installed
   - Set up an Android Virtual Device (AVD) or connect a physical device

5. **Run the application**
   - Click "Run" or press `Shift + F10`
   - Select the target device
   - Wait for the app to install and launch

---

## Project Structure

```
MyApplication/
├── app/
│   ├── manifests/
│   │   └── AndroidManifest.xml
│   ├── java/com/example/myapplication/
│   │   ├── controller/
│   │   │   └── TaskController.java          # Task management logic
│   │   ├── model/
│   │   │   └── Task.java                    # Task data model
│   │   ├── AchievementsActivity.java        # Achievements screen
│   │   ├── AddTaskActivity.java             # Add/Edit task screen
│   │   ├── LectureNotesActivity.java        # Lecture notes screen
│   │   ├── LoginActivity.java               # User login
│   │   ├── MainActivity.java                # Main menu/home
│   │   ├── RegisterActivity.java            # User registration
│   │   ├── RemindersActivity.java           # Reminders screen
│   │   ├── SplashActivity.java              # Splash screen
│   │   ├── StudyTimerActivity.java          # Study timer
│   │   ├── ToDoListActivity.java            # Task list screen
│   │   └── ViewTasksActivity.java           # Task detail view
│   └── res/
│       ├── drawable/                        # Illustrations and icon resources
│       ├── layout/                          # Screen layouts
│       ├── values/                          # Colors, strings, styles, themes
│       └── xml/                             # XML resources
└── gradle/                                   # Gradle wrapper
```

---

## Design

### Color Palette
```xml
<!-- Primary Colors -->
<color name="background_yellow">#FFF9C4</color>
<color name="primary_pink">#E91E63</color>
<color name="button_yellow">#FFD54F</color>

<!-- Text Colors -->
<color name="pink_text">#D81B60</color>
<color name="brown_text">#4E342E</color>
<color name="black">#000000</color>

<!-- Additional Colors -->
<color name="success_green">#81C784</color>
<color name="priority_high">#FF5252</color>
<color name="priority_medium">#FFB74D</color>
<color name="priority_low">#81C784</color>
```

### Typography
- Headers: Bold, 24sp
- Titles: Bold, 18sp
- Body: Regular, 14sp
- Captions: Regular, 12sp

### UI Principles
- Material Design guidelines
- Consistent visual identity throughout
- Responsive layouts for all screen sizes
- Accessible touch targets (48dp minimum)

---

## Usage Guide

### Getting Started

1. **Register or log in**
   - Launch the app
   - Create an account or log in
   - Set up a profile

2. **Create a first task**
   - Open "To-Do List" from the main menu
   - Tap the floating action button
   - Fill in task details: title (required), description (optional), priority, due date
   - Confirm with "Add Task"

3. **Start a study session**
   - Open "Study Timer"
   - Select a duration (25, 45, or 60 minutes)
   - Tap "Start"
   - The timer notifies the user on completion

4. **Track progress**
   - Complete tasks to earn achievements
   - View statistics in "Achievements"
   - Set personal goals
   - Monitor badge progress

### Advanced Features

#### Setting Goals
1. Open "Achievements"
2. Tap "Set Goals"
3. Enter a weekly goal (days per week), a daily goal (minutes per day), and a total study goal (total minutes)
4. Tap "Save"
5. Track progress on the achievements screen

#### Managing Tasks
- **Check/Uncheck**: Tap the checkbox to mark a task complete
- **Modify**: Select a task, tap "Modify Task"
- **Delete**: Select a task, tap "Delete Task"
- **Clear All**: Removes all tasks

#### Viewing History
1. Open "Achievements"
2. Tap "View Progress History"
3. Review completed study sessions, total study time, and completed tasks

---

## Achievement Tiers

| Badge | Requirements | Description |
|-------|--------------|-------------|
| Beginner Bee | 5 tasks or 60 minutes | Getting started |
| Study Bee | 20 tasks or 300 minutes | Building consistent habits |
| Expert Bee | 50 tasks or 600 minutes | Advanced progress |
| Master Bee | 100 tasks or 1200 minutes | Top-tier engagement |

---

## Roadmap

Planned enhancements:
- Dark mode support
- Cloud sync (Firebase integration)
- Advanced statistics with graphs
- Custom notification sounds
- Home screen widget support
- Multi-language support (French, Arabic, Spanish)
- Data export to CSV/PDF
- Social features (study groups, leaderboards)
- Customizable themes
- Recurring tasks support
- Image attachments for notes
- Voice notes
- Wear OS companion app

---

## Contributing

Contributions are welcome. See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m "Add: AmazingFeature"`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.

---

## Author

**Tasnim Elmehrabi** (tass027)
- GitHub: [@tass027](https://github.com/tass027)
- Project: [Study with Bee](https://github.com/tass027/study-with-bee)

---

## Acknowledgments

- Material Design Icons
- Android Documentation
- Pomodoro Technique
- All contributors and users

---

## Support

- Check the [Wiki](https://github.com/tass027/study-with-bee/wiki)
- Open an [Issue](https://github.com/tass027/study-with-bee/issues) for bugs or feature requests

---

## Project Stats

![GitHub stars](https://img.shields.io/github/stars/tass027/study-with-bee?style=social)
![GitHub forks](https://img.shields.io/github/forks/tass027/study-with-bee?style=social)
![GitHub watchers](https://img.shields.io/github/watchers/tass027/study-with-bee?style=social)
