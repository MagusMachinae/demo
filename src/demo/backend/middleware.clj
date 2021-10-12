(ns deno.backend.middleware
  (:require
    [com.fulcrologic.fulcro.networking.file-upload :as file-upload]
    [com.fulcrologic.fulcro.server.api-middleware :refer [handle-api-request
                                                          wrap-transit-params
                                                          wrap-transit-response
                                                          wrap-multipart-params]]))

(def ^:private not-found-handler
  (fn [req]
    {:status  404
     :headers {"Content-Type" "text/plain"}
     :body    "NOPE"}))

(defn wrap-api [handler uri]
  (fn [request]
    (if (= uri (:uri request))
      (handle-api-request
        (:transit-params request)
        (fn [tx] (parser {:ring/request request} tx)))
      (handler request))))

(def middleware
    (-> not-found-handler
      (wrap-api "/api")
      (file-upload/wrap-mutation-file-uploads {})
      wrap-transit-params
      wrap-transit-response
      (wrap-multipart-params)))
