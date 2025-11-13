# 🐝 Study with Bee - Your Smart Study Companion

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com/)
[![Java](https://img.shields.io/badge/Language-Java-orange.svg)](https://www.java.com/)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Version](https://img.shields.io/badge/Version-1.0.0-brightgreen.svg)](https://github.com/tass027/study-with-bee)
[![Maintained](https://img.shields.io/badge/Maintained-Yes-success.svg)](https://github.com/tass027/study-with-bee)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg)](https://android-arsenal.com/api?level=21)

> 📚 A beautiful and motivating Android app to help students organize tasks, track study time, and achieve their academic goals with gamification features!

---

## 📱 Screenshots

| Splash Screen | Login | Main Menu | Study Timer |
|--------------|-------|-----------|-------------|
| 🐝 | 📝 | 🏠 | ⏱️ |

| To-Do List | Add Task | Achievements | Lecture Notes |
|-----------|----------|--------------|---------------|
| ✅ | ➕ | 🏆 | 📓 |

*Screenshots coming soon! Stay tuned!* 📸

---

## ✨ Features

### 📝 **Complete Task Management**
- ✅ Create, edit, and delete tasks with ease
- 📅 Set due dates and priority levels (High, Medium, Low)
- 🎨 Beautiful bee-themed UI design
- 📊 Track completed vs pending tasks
- 🗓️ Weekly calendar view for task organization
- ⭐ Priority badges with color coding

### ⏱️ **Study Timer (Pomodoro Technique)**
- ⏰ Customizable timer durations (25, 45, or 60 minutes)
- ▶️ Start, pause, and reset functionality
- 📈 Real-time session tracking
- 🔔 Session completion notifications
- 💾 Automatic progress saving
- 📊 Total study time calculator

### 🏆 **Achievement & Badge System**
Unlock badges as you progress:
- 🐝 **Beginner Bee**: Complete 5 tasks or study 60 minutes
- 📚 **Study Bee**: Complete 20 tasks or study 300 minutes
- 🎓 **Expert Bee**: Complete 50 tasks or study 600 minutes
- 👑 **Master Bee**: Complete 100 tasks or study 1200 minutes

### 🎯 **Personalized Goal Setting**
- 📅 Set weekly study goals (number of days)
- ⏰ Set daily study goals (minutes per day)
- 🎯 Set total study time targets
- 📊 Visual progress bars for goal tracking
- ✅ Automatic goal achievement detection

### 📊 **Statistics & Analytics**
- 📈 Total tasks created and completion rate
- ⏱️ Total study time tracking
- 🔥 Study streak counter
- 📚 Session history viewer with detailed logs
- 🏆 Badge unlock progress
- 📊 Visual progress visualization

### 📓 **Lecture Notes**
- 📝 Take and organize lecture notes
- 🗂️ Categorize notes by subject
- 🔍 Search functionality
- 💾 Auto-save feature

### 🔔 **Reminders System**
- ⏰ Set custom reminders for tasks
- 🔔 Push notifications for upcoming deadlines
- 📅 Daily study reminders

### 🔐 **User Authentication**
- 📧 Secure login and registration
- 👤 User profile management
- 🔒 Data privacy and security

---

## 🚀 Technologies & Tools Used

### **Core Technologies**
- **Language**: Java
- **Platform**: Android SDK (API 21+)
- **Build System**: Gradle
- **Architecture**: MVC (Model-View-Controller)

### **Android Components**
- ✅ **Activities**: 10+ activities for different screens
- 🎨 **Material Design**: Material Components Library
- 📦 **CardView**: Beautiful card-based layouts
- 📋 **RecyclerView**: Efficient list displays
- 🎯 **ConstraintLayout**: Flexible and responsive UI
- 🗓️ **CalendarView**: Date selection and week view

### **Data Persistence**
- 💾 **SharedPreferences**: User settings and preferences
- 📝 **File Storage**: Task and note management
- 🔄 **JSON**: Data serialization

### **Key Features**
- ⏱️ **CountDownTimer**: Study timer implementation
- 🔔 **Notifications**: Reminder system
- 🎨 **Custom Views**: Bee-themed UI elements
- 📊 **ProgressBar**: Visual progress tracking

---

## 📦 Installation & Setup

### Prerequisites
- **Android Studio** Arctic Fox (2020.3.1) or newer
- **JDK** 8 or higher
- **Android SDK** API 21 (Lollipop) or higher
- **Gradle** 7.0+

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

5. **Run the app**
   - Click the "Run" button (▶️) or press `Shift + F10`
   - Select your target device
   - Wait for the app to install and launch

---

## 🎨 Project Structure

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
│   │   └── ViewTasksActivity.java           # View tasks details
│   └── res/
│       ├── drawable/
│       │   ├── bee1.png - bee6.jpg          # Bee illustrations
│       │   ├── ic_*.xml                     # Icon resources
│       │   └── *.xml                        # Custom drawables
│       ├── layout/
│       │   ├── activity_achievements.xml     # Achievements layout
│       │   ├── activity_add_task.xml        # Add task layout
│       │   ├── activity_lecture_notes.xml   # Lecture notes layout
│       │   ├── activity_login.xml           # Login layout
│       │   ├── activity_main.xml            # Main menu layout
│       │   ├── activity_register.xml        # Register layout
│       │   ├── activity_reminders.xml       # Reminders layout
│       │   ├── activity_splash.xml          # Splash screen layout
│       │   ├── activity_study_timer.xml     # Study timer layout
│       │   ├── activity_todo_list.xml       # To-do list layout
│       │   ├── activity_view_tasks.xml      # View tasks layout
│       │   ├── dialog_bee_message.xml       # Bee message dialog
│       │   ├── dialog_set_goals.xml         # Goals setting dialog
│       │   └── item_task.xml                # Task item layout
│       ├── values/
│       │   ├── colors.xml                   # Color definitions
│       │   ├── strings.xml                  # String resources
│       │   ├── styles.xml                   # App styles
│       │   └── themes/                      # App themes
│       └── xml/                             # XML resources
└── gradle/                                   # Gradle wrapper
```

---

## 🎨 Design & Color Palette

### Color Scheme
```xml
<!-- Primary Colors -->
<color name="background_yellow">#FFF9C4</color>      <!-- Light Yellow Background -->
<color name="primary_pink">#E91E63</color>           <!-- Pink Primary -->
<color name="button_yellow">#FFD54F</color>          <!-- Button Yellow -->

<!-- Text Colors -->
<color name="pink_text">#D81B60</color>              <!-- Dark Pink Text -->
<color name="brown_text">#4E342E</color>             <!-- Brown Text -->
<color name="black">#000000</color>                  <!-- Black Text -->

<!-- Additional Colors -->
<color name="success_green">#81C784</color>          <!-- Completed Tasks -->
<color name="priority_high">#FF5252</color>          <!-- High Priority -->
<color name="priority_medium">#FFB74D</color>        <!-- Medium Priority -->
<color name="priority_low">#81C784</color>           <!-- Low Priority -->
```

### Typography
- **Headers**: Bold, 24sp
- **Titles**: Bold, 18sp
- **Body**: Regular, 14sp
- **Captions**: Regular, 12sp

### UI Principles
- ✨ Material Design guidelines
- 🐝 Consistent bee theme throughout
- 🎨 Cheerful and motivating color scheme
- 📱 Responsive layouts for all screen sizes
- ♿ Accessible touch targets (48dp minimum)

---

## 🎯 Usage Guide

### Getting Started

1. **Register/Login**
   - Launch the app
   - Create a new account or login
   - Set up your profile

2. **Create Your First Task**
   - Tap on "To-Do List" from the main menu
   - Tap the floating action button (+)
   - Fill in task details:
     - Title (required)
     - Description (optional)
     - Priority (High/Medium/Low)
     - Due date
   - Tap "Add Task"

3. **Start Studying**
   - Go to "Study Timer"
   - Select duration (25/45/60 minutes)
   - Tap "Start" to begin
   - Focus on your studies!
   - Timer will notify you when complete

4. **Track Your Progress**
   - Complete tasks to earn achievements
   - View your statistics in "Achievements"
   - Set personal goals
   - Watch your badges unlock!

### Advanced Features

#### Setting Goals
1. Go to "Achievements" screen
2. Tap "Set Goals" button
3. Enter:
   - Weekly goal (days per week)
   - Daily goal (minutes per day)
   - Total study goal (total minutes)
4. Tap "Save"
5. Track progress on achievement screen

#### Managing Tasks
- **Check/Uncheck**: Tap checkbox to mark complete
- **Modify**: Select task, tap "Modify Task"
- **Delete**: Select task, tap "Delete Task"
- **Clear All**: Tap "Clear All" to remove all tasks

#### Viewing History
1. Go to "Achievements"
2. Tap "View Progress History"
3. See all completed:
   - Study sessions
   - Total study time
   - Completed tasks list

---

## 🏆 Achievement Tiers

| Badge | Requirements | Description |
|-------|-------------|-------------|
| 🐝 **Beginner Bee** | 5 tasks OR 60 minutes | Just starting your journey! |
| 📚 **Study Bee** | 20 tasks OR 300 minutes | Building good habits! |
| 🎓 **Expert Bee** | 50 tasks OR 600 minutes | Becoming a master! |
| 👑 **Master Bee** | 100 tasks OR 1200 minutes | You're a study champion! |

---

## 🐛 Known Issues & Roadmap

### Known Issues
- None reported yet! 🎉

### Future Enhancements
- [ ] 🌙 Dark mode support
- [ ] ☁️ Cloud sync (Firebase integration)
- [ ] 📊 Advanced statistics with graphs
- [ ] 🎵 Custom notification sounds
- [ ] 📱 Widget support for home screen
- [ ] 🌍 Multi-language support (French, Arabic, Spanish)
- [ ] 📤 Export data to CSV/PDF
- [ ] 👥 Social features (study groups, leaderboards)
- [ ] 🎨 Customizable themes
- [ ] 🔄 Recurring tasks support
- [ ] 📸 Attach images to notes
- [ ] 🗣️ Voice notes feature
- [ ] ⌚ Wear OS companion app

---

## 🤝 Contributing

We welcome contributions! See [CONTRIBUTING.md](CONTRIBUTING.md) for guidelines.

### Quick Start
1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m '✨ Add: AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Yasser** (tass027)
- GitHub: [@tass027](https://github.com/tass027)
- Project: [Study with Bee](https://github.com/tass027/study-with-bee)

---

## 🙏 Acknowledgments

- 🎨 Material Design Icons
- 📚 Android Documentation
- ⏱️ Pomodoro Technique inspiration
- 🐝 Bee illustrations
- 💛 All contributors and users!

---

## 📞 Support & Contact

### Get Help
- 📖 Check the [Wiki](https://github.com/tass027/study-with-bee/wiki)
- 💬 Open an [Issue](https://github.com/tass027/study-with-bee/issues)
- ⭐ Star this repo if you find it helpful!

### Found a Bug?
Please report it [here](https://github.com/tass027/study-with-bee/issues/new?template=bug_report.md)

### Have a Feature Request?
Suggest it [here](https://github.com/tass027/study-with-bee/issues/new?template=feature_request.md)

---

## 📈 Project Stats

![GitHub stars](https://img.shields.io/github/stars/tass027/study-with-bee?style=social)
![GitHub forks](https://img.shields.io/github/forks/tass027/study-with-bee?style=social)
![GitHub watchers](https://img.shields.io/github/watchers/tass027/study-with-bee?style=social)

---

<div align="center">
  
### ⭐ Star this repo if you find it helpful!

**Made with ❤️ and 🐝**

*Happy Studying! 📚✨*

</div>