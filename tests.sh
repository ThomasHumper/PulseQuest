Run everything with:
pip install pytest
pytest

You should eventually have tests covering authentication, quests, XP, levels, streaks, achievements, rewards, and API endpoints. A particularly useful next step would be Python integration tests that hit the Scala/Clojure API, so the Python tests can verify the actual backend rather than only testing copied game logic.
