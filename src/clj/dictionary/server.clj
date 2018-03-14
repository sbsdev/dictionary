(ns dictionary.server
  (:require [dictionary.handler :refer [app]]
            [immutant.web :as web])
  (:gen-class))

(defn -main [& args]
  ;; start web server
  (web/run #'app)
  #_(web/run #'api :path "/api"))
