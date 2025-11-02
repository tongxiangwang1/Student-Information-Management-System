# Student-Information-Management-System
programmer：Tongxiang_wang_24245808，Ricky Zhang 20125369

# Introduction
A simple desktop app to create, view, search, update, and delete student records, courses, and internships.

# Features
Student information can be added, deleted, modified, and searched.
Manage each student’s Courses & Internships
CSV export of student list
Legacy importer for students.txt (pipe-delimited)
Single-file embedded DB (Apache Derby)，auto-creates tables on first run
Transactional save across student + courses + internships
Input validation with clear messages
Unit test example with in-memory Derby

# Screenshot
<img width="1231" height="797" alt="屏幕截图 2025-11-02 215047" src="https://github.com/user-attachments/assets/2cf44079-a70f-4f22-b675-2329dec4d1e7" />

# Project Structure
src/
├─ main/java/studentmanager/gui/
│  ├─ APP.java                          # Main entry
│  ├─ db/DatabaseManager.java           # Derby URL & schema init
│  ├─ dao/
│  │  ├─ CourseDao.java
│  │  ├─ InternshipDao.java
│  │  └─ StudentDao.java
│  ├─ exception/InvalidInputException.java
│  ├─ model/
│  │  ├─ Course.java
│  │  ├─ Internship.java
│  │  ├─ Person.java
│  │  └─ Student.java
│  ├─ service/StudentService.java
│  ├─ ui/
│  │  ├─ CoursesPanel.java
│  │  ├─ InternshipsPanel.java
│  │  ├─ MainFrame.java
│  │  ├─ StudentDetailsPanel.java
│  │  ├─ StudentFormDialog.java
│  │  └─ StudentTableModel.java
│  └─ util/
│     ├─ CsvUtil.java
│     ├─ FileExporter.java
│     ├─ FileImporter.java
│     └─ ValidationUtil.java
└─ test/java/studentmanager/gui/service/StudentServiceTest.java

