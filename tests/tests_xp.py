from pulsequest.xp import xp_required, calculate_level, add_xp


def test_xp_required_for_level():
    assert xp_required(1) == 100
    assert xp_required(2) == 400
    assert xp_required(3) == 900


def test_new_player_starts_at_level_one():
    assert calculate_level(0) == 1


def test_player_levels_up():
    assert calculate_level(100) == 1
    assert calculate_level(400) == 2
    assert calculate_level(900) == 3


def test_adding_xp():
    user = {
        "id": 1,
        "username": "player1",
        "xp": 50,
        "level": 1,
    }

    updated = add_xp(user, 50)

    assert updated["xp"] == 100
    assert updated["level"] == 1


def test_level_up_after_400_xp():
    user = {
        "id": 1,
        "username": "player1",
        "xp": 350,
        "level": 1,
    }

    updated = add_xp(user, 50)

    assert updated["xp"] == 400
    assert updated["level"] == 2

Run startedInitializing environmentInstalling packagesRunning codeModuleNotFoundError: No module named 'pulsequest'Run completed in 3,228.000ms

(ns pulsequest.core
  (:require
   [cheshire.core :as json]
   [reitit.ring :as ring]
   [ring.adapter.jetty :as jetty]))

;; -------------------------
;; Game state
;; -------------------------

(defonce users
  (atom
   {1 {:id       1
       :username "player1"
       :email    "player@example.com"
       :xp       0
       :level    1}}))

(defonce quests
  (atom
   {1 {:id          1
       :title       "Morning Warrior"
       :description "Complete your morning routine."
       :xp-reward   50
       :completed   false}

    2 {:id          2
       :title       "Deep Focus"
       :description "Work distraction-free for 60 minutes."
       :xp-reward   100
       :completed   false}

    3 {:id          3
       :title       "Knowledge Hunter"
       :description "Learn something new for 30 minutes."
       :xp-reward   75
       :completed   false}}))

;; -------------------------
;; XP system
;; -------------------------

(defn xp-required
  [level]
  (* 100 level level))

(defn calculate-level
  [xp]
  (loop [level 1]
    (if (<= (xp-required (inc level)) xp)
      (recur (inc level))
      level)))

(defn add-xp
  [user amount]
  (let [new-xp    (+ (:xp user) amount)
        new-level (calculate-level new-xp)]
    (assoc user
           :xp new-xp
           :level new-level)))

;; -------------------------
;; HTTP helpers
;; -------------------------

(defn json-response
  ([data]
   (json-response data 200))

  ([data status]
   {:status status
    :headers {"Content-Type" "application/json"}
    :body (json/generate-string data)}))

;; -------------------------
;; API handlers
;; -------------------------

(defn get-quests
  [_request]
  (json-response
   (vals @quests)))

(defn get-user
  [request]
  (let [id (-> request :path-params :id parse-long)
        user (get @users id)]
    (if user
      (json-response user)
      (json-response
       {:error "User not found"}
       404))))

(defn complete-quest
  [request]
  (let [user-id  (-> request :path-params :user-id parse-long)
        quest-id (-> request :path-params :quest-id parse-long)

        user  (get @users user-id)
        quest (get @quests quest-id)]

    (cond
      (nil? user)
      (json-response {:error "User not found"} 404)

      (nil? quest)
      (json-response {:error "Quest not found"} 404)

      (:completed quest)
      (json-response
       {:error "Quest already completed"}
       400)

      :else
      (let [old-level (:level user)

            updated-user
            (add-xp user (:xp-reward quest))

            updated-quest
            (assoc quest :completed true)

            level-up?
            (> (:level updated-user)
               old-level)]

        (swap! users assoc user-id updated-user)
        (swap! quests assoc quest-id updated-quest)

        (json-response
         {:user      updated-user
          :quest     updated-quest
          :xp-earned (:xp-reward quest)
          :level-up  level-up?})))))

;; -------------------------
;; Routes
;; -------------------------

(def app
  (ring/ring-handler
   (ring/router
    [["/api/quests"
      {:get get-quests}]

     ["/api/users/:id"
      {:get get-user}]

     ["/api/users/:user-id/quests/:quest-id/complete"
      {:post complete-quest}]])))

;; -------------------------
;; Server
;; -------------------------

(defn -main
  []
  (println "⚡ PulseQuest running on http://localhost:8080")
  (jetty/run-jetty
   app
   {:port 8080
    :join? true}))
