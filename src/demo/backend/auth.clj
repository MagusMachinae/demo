(ns demo.backend.auth
  (:require [clojure.string :as str]))

; an in memory database of registered users
(defonce user-database (atom {"defaultuser" {:id       "defaultuser"
                                             :password "password@123"
                                             :role     :user}}))


(defn password-rules
  "Given a password, return a set of keywords for any rules that are not satisfied"
  [password]
  (cond-> #{}

          ; password must be at least 8 characters long
          (<= (count password) 8)
          (conj :password.error/too-short)

          ; password must contain a special character
          (empty? (re-find #"[!@#$%^&*(),.?\":{}|<>]" password))
          (conj :password.error/missing-special-character)

          ; password contains at least one lower case letter
          (empty? (re-find #".*[a-z]" password))
          (conj :password.error/missing-lowercase)

          ; password contains at least one upper case letter
          (empty? (re-find #".*[A-Z]" password))
          (conj :password.error/missing-uppercase)))

(defn authenticate-user
  "Returns a user map when a username and password are correct, or nil when incorrect"
  [db username password]
  (when-let [{known-password :password :as user} (get db (str/lower-case username))]
    (if (= password known-password)
      (dissoc user :password)
      (throw (ex-info "Invalid username or password"
                      {:reason :login.error/invalid-credentials})))))

(defn create-user
  "Create a user by adding them to the database, and returns the user's details except for their password"
  ([db-atom username password]
   ; recur with a default role of :user
   (create-user db-atom username password :user))
  ([db-atom username password role]
   ; if there are any password violations
   (if-let [password-violations (not-empty (password-rules password))]
     ; then throw an exception with the violation codes
     (throw (ex-info "Password does not meet criteria"
                     {:reason     :create-user.error/password-violations
                      :violations password-violations}))
     ; otherwise check if the user exists
     (if (get @db-atom username)
       ; and if they do then return an error code
       (throw (ex-info "User already exists"
                       {:reason :create-user.error/already-exists}))
       ; otherwise return a success message with the user's details
       (-> db-atom
           ; put the user in the database
           (swap! assoc username {:id username :role role :password password})
           ; select the user in the database
           (get username)
           ; strip their password
           (dissoc :password))))))

(defn change-user-password
  [db-atom username password]
  (if-let [password-violations (not-empty (password-rules password))]
    (throw (ex-info "Password does not meet criteria"
                  {:reason     :create-user.error/password-violations
                   :violations password-violations}))
    (if (get @db-atom username)
      (-> db-atom
          (swap! assoc-in  [username :password] password)
          (get username)
          (dissoc :password))
      (throw (ex-info "User does not exist"
                      {:reason :change-user-password.error/does-not-exist})))))
