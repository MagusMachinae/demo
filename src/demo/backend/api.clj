(ns demo.backend.api
  (:require [reitit.coercion.spec]
            [spec-tools.data-spec :as ds]
            [app.auth :as auth]))


(defn create-user-handler
  "A web handler to create a new user"
  [{{{:keys [username password role]} :body} :parameters}]
  (try
    {:status 200
     :body   (if role
               (auth/create-user auth/user-database username password role)
               (auth/create-user auth/user-database username password))}
    (catch Exception e
      {:status 400
       :body   (ex-data e)})))

(defn authenticate-user-handler
  "A web handler to authenticate an existing user"
  [{{{:keys [username password]} :query} :parameters}]
  (try
    {:status 200
     :body   (auth/authenticate-user @auth/user-database username password)}
    (catch Exception e
      {:status 401
       :body   (ex-data e)})))

(defn change-user-password
  "Web handler to change a user's password"
  [{{{:keys [username old-password new-password]} :query} :parameters} :as ctx]
  (try
    (auth/authenticate-user @auth/user-database username old-password)
    {:status 200
     :body (auth/change-user-password @auth/user-database username new-password)}
    (catch Exception e
      {:status 401
       :body   (ex-data e)})))


(def routes
  ["" {:coercion reitit.coercion.spec/coercion}
   ["/user" {:get  {:summary    "Authenticates a user in the database, returning their details"
                    :parameters {:query {:username string?
                                         :password string?}}
                    :handler    authenticate-user-handler}
             :post {:summary    "Creates a new user in the database, returning their details"
                    :parameters {:body {:username      string?
                                        :password      string?
                                        (ds/opt :role) keyword?}}
                    :handler    create-user-handler}}
    ["/password" {:put {:summary    "Updates a user's password."
                         :parameters {:body {:username      string?
                                             :old-password  string?
                                             :new-password  string?
                                             (ds/opt :role) keyword?}}
                         :handler    create-user-handler}}]]])
