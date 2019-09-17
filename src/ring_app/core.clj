(ns ring-app.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.http-response :as response]
            [ring.middleware.reload :refer [wrap-reload]]))
(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn handler [request-map]
  (response/ok
   (str "<html><body> your IP is: "
        (:remote-addr request-map)
        "</body></html>")))

(defn wrap-nocache [handler]
  (fn [request]
    (-> request
        handler
        (assoc-in [:headers "Pragma"] "no-cache"))))

(defn -main []
  (jetty/run-jetty
   (-> handler var wrap-nocache wrap-reload)
   {:port 3000
    :join? false}))
