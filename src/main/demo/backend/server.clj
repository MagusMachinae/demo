(ns demo.backend.server
  (:require [ring.adapter.jetty :as jetty]
            [ring.middleware.params :as params]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [muuntaja.core :as m]
            [reitit.ring.coercion :as coercion]
            [reitit.ring :as ring]
            [app.api]))

(def app
  (ring/ring-handler
    (ring/router
      [app.api/routes]
      {:data {:muuntaja m/instance
              :middleware [params/wrap-params
                           muuntaja/format-middleware
                           coercion/coerce-exceptions-middleware
                           coercion/coerce-request-middleware
                           coercion/coerce-response-middleware]}})
    (ring/create-default-handler)))

(defn -main [& args]
  (jetty/run-jetty #'app {:port 3000, :join? false})
  (println "Server running on port 3000"))

(comment
  (-main))
