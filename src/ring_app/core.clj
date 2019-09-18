(ns ring-app.core
  (:require [ring.adapter.jetty :as jetty]
            [ring.util.http-response :as response]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.format :refer [wrap-restful-format]]))
(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

;; (defn handler [request-map]
;;   (response/ok
;;    (str "<html><body> your IP is: "
;;         (:remote-addr request-map)
;;         "</body></html>")))

(defn handler [request]
  (response/ok
   {:result (-> request :params :id)}))

(defn wrap-nocache [handler]
  (fn [request]
    (-> request
        handler
        (assoc-in [:headers "Pragma"] "no-cache"))))

(defn wrap-formats [handler]
  (wrap-restful-format
   handler
   {:formats [:json-kw :transit-json :transit-msgpack]}))

(defn -main []
  (jetty/run-jetty
   (-> #'handler wrap-nocache wrap-reload wrap-formats)
   {:port 3000
    :join? false}))
