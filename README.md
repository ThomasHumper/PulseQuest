# ⚡ PulseQuest

> **Turn everyday life into a game.**

PulseQuest is a gamified productivity platform that transforms personal goals, habits, and learning into engaging quests.

Instead of staring at a boring checklist, users complete challenges, earn XP, level up, unlock achievements, and build their own evolving virtual world.

---

## 🛠️ Tech Stack

- **Next.js**
- **React**
- **TypeScript**
- **CSS**
- **Node.js / npm**

## 🚀 Running Locally

### 1. Clone the repository

```bash
git clone https://github.com/yourusername/pulsequest.git
cd pulsequest
```

### 2. Install dependencies

Make sure you have **Node.js 18+** installed, then run:

```bash
npm install
```

### 3. Start the development server

```bash
npm run dev
```

The app will start in development mode.

Open the following URL in your browser:

```text
http://localhost:3000
```

You should now see the PulseQuest dashboard.

### 4. Build for production

To create a production build:

```bash
npm run build
```

Then start the production server:

```bash
npm start
```

The production app will be available at:

```text
http://localhost:3000
```

## 📁 Project Structure

```text
pulsequest/
├── app/
│   ├── globals.css
│   ├── layout.tsx
│   └── page.tsx
├── public/
├── .gitignore
├── next-env.d.ts
├── package.json
├── README.md
└── tsconfig.json
```

## 🎮 How It Works

```text
Set a Goal
    ↓
Create a Quest
    ↓
Complete the Quest
    ↓
Earn XP
    ↓
Level Up
    ↓
Unlock Achievements
    ↓
Expand Your World
```

## 🔮 Planned Features

- [ ] PostgreSQL database
- [ ] Prisma ORM
- [ ] User authentication
- [ ] Persistent quests and XP
- [ ] AI-powered quest generation
- [ ] Friends and squads
- [ ] Leaderboards
- [ ] Seasonal events
- [ ] Custom avatars
- [ ] Mobile app

## 📄 License

MIT

## ✨ Features

### 🎯 Daily Quests
Get personalized challenges based on your goals, habits, and interests.

### ⚡ XP & Levels
Complete quests to earn experience points and progress through increasingly challenging levels.

### 🗺️ Adventure Map
Your progress unlocks new areas, locations, stories, and challenges.

### 👥 Squads
Team up with friends and complete cooperative quests together.

### 🔥 Streak Events
Participate in limited-time challenges to maintain momentum and earn special rewards.

### 🏆 Achievements
Unlock achievements for reaching unique milestones.

Examples:

- `First Quest` — Complete your first quest
- `Early Bird` — Complete 5 quests before noon
- `7-Day Streak` — Complete a quest every day for a week
- `Quest Master` — Complete 100 quests

### 🤖 AI Quest Generator
Turn broad goals into actionable quests.

**Example:**

```text
Goal:
Learn Python

↓ AI Quest Generator

🐣 Write your first Python program
🧩 Solve 3 beginner exercises
🔢 Build a calculator
🎮 Create a small text adventure
⚔
