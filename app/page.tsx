"use client";

import { useMemo, useState } from "react";

type Quest = {
  id: number;
  title: string;
  description: string;
  xp: number;
  difficulty: "Easy" | "Medium" | "Hard";
  done: boolean;
};

const initialQuests: Quest[] = [
  { id: 1, title: "Start the adventure", description: "Write down one goal you want to accomplish this week.", xp: 50, difficulty: "Easy", done: false },
  { id: 2, title: "Focus sprint", description: "Work on your most important task for 25 uninterrupted minutes.", xp: 100, difficulty: "Medium", done: false },
  { id: 3, title: "Build something", description: "Spend 30 minutes making progress on a personal project.", xp: 150, difficulty: "Medium", done: false },
  { id: 4, title: "Boss quest", description: "Finish one meaningful task you have been putting off.", xp: 300, difficulty: "Hard", done: false }
];

export default function Home() {
  const [quests, setQuests] = useState(initialQuests);
  const [goal, setGoal] = useState("");
  const [streak, setStreak] = useState(3);

  const xp = useMemo(() => quests.filter(q => q.done).reduce((sum, q) => sum + q.xp, 0), [quests]);
  const level = Math.floor(xp / 250) + 1;
  const levelProgress = xp % 250;

  function completeQuest(id: number) {
    setQuests(current => current.map(q => q.id === id ? { ...q, done: !q.done } : q));
  }

  function generateQuest() {
    if (!goal.trim()) return;
    setQuests(current => [
      ...current,
      {
        id: Date.now(),
        title: `Quest: ${goal.trim()}`,
        description: `Make a small, concrete step toward "${goal.trim()}".`,
        xp: 125,
        difficulty: "Medium",
        done: false
      }
    ]);
    setGoal("");
  }

  return (
    <main className="shell">
      <nav className="nav">
        <div className="brand"><span className="logo">⚡</span> PulseQuest</div>
        <div className="navLinks"><span>Dashboard</span><span>Quests</span><span>Achievements</span><span>Profile</span></div>
        <button className="avatar">PQ</button>
      </nav>

      <section className="hero">
        <div>
          <p className="eyebrow">YOUR ADVENTURE AWAITS</p>
          <h1>Turn your goals<br />into <span>quests.</span></h1>
          <p className="subtitle">Complete real-world challenges, earn XP, level up, and build your own adventure.</p>
          <div className="goalBox">
            <input value={goal} onChange={e => setGoal(e.target.value)} onKeyDown={e => e.key === "Enter" && generateQuest()} placeholder="What do you want to accomplish?" />
            <button onClick={generateQuest}>Create Quest ✦</button>
          </div>
        </div>
        <div className="world">
          <div className="planet">🌎</div>
          <div className="orbit o1">✦</div>
          <div className="orbit o2">◆</div>
          <div className="island">🏰</div>
        </div>
      </section>

      <section className="stats">
        <div className="stat"><span>LEVEL</span><strong>{level}</strong><small>Adventurer</small></div>
        <div className="stat wide"><span>XP PROGRESS</span><div className="progress"><i style={{ width: `${(levelProgress / 250) * 100}%` }} /></div><small>{levelProgress} / 250 XP to Level {level + 1}</small></div>
        <div className="stat"><span>STREAK</span><strong>🔥 {streak}</strong><small>days in a row</small></div>
        <div className="stat"><span>TOTAL XP</span><strong>{xp}</strong><small>earned this session</small></div>
      </section>

      <section className="content">
        <div className="quests">
          <div className="sectionHead"><div><p className="eyebrow">TODAY</p><h2>Your Quests</h2></div><span className="count">{quests.filter(q => q.done).length}/{quests.length}</span></div>
          <div className="questList">
            {quests.map(q => (
              <article className={`quest ${q.done ? "done" : ""}`} key={q.id}>
                <button className="check" onClick={() => completeQuest(q.id)}>{q.done ? "✓" : ""}</button>
                <div className="questText"><h3>{q.title}</h3><p>{q.description}</p></div>
                <span className={`difficulty ${q.difficulty.toLowerCase()}`}>{q.difficulty}</span>
                <strong className="questXp">+{q.xp} XP</strong>
              </article>
            ))}
          </div>
        </div>

        <aside>
          <div className="card">
            <p className="eyebrow">YOUR WORLD</p>
            <h2>Adventure Map</h2>
            <div className="map">
              <div>🌲</div><div>⛰️</div><div>🏰</div><div>🌊</div><div>🌋</div>
              <span className="you">YOU</span>
            </div>
            <p className="muted">Complete quests to unlock new regions.</p>
          </div>
          <div className="card achievement">
            <p className="eyebrow">NEXT ACHIEVEMENT</p>
            <h2>🏆 Getting Started</h2>
            <p>Complete 5 quests to unlock your first badge.</p>
            <div className="miniProgress"><i style={{ width: `${Math.min((quests.filter(q => q.done).length / 5) * 100, 100)}%` }} /></div>
          </div>
        </aside>
      </section>
    </main>
  );
}