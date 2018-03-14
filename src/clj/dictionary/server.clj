(ns dictionary.server
  (:require [dictionary.handler :refer [site api]]
            [immutant.web :as web])
  (:gen-class))

(defn -main [& args]
  ;; start web server
  (web/run #'site)
  (web/run #'api :path "/api"))
