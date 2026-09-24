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

package pulsequest

object XpSystem:

  def xpRequiredForLevel(level: Int): Int =
    100 * level * level

  def levelFromXp(xp: Int): Int =
    Iterator
      .from(1)
      .takeWhile(level => xpRequiredForLevel(level + 1) <= xp)
      .max

  def addXp(user: User, amount: Int): User =
    val newXp = user.xp + amount
    val newLevel = levelFromXp(newXp)

    user.copy(
      xp = newXp,
      level = newLevel
    )
