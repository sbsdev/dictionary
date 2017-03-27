(ns dictionary.handler
  (:require [compojure
             [core :refer [defroutes GET]]
             [route :refer [not-found resources]]]
            [compojure.api.sweet :as api.sweet]
            [dictionary.middleware :refer [wrap-middleware]]
            [hiccup.page :refer [html5 include-css include-js]]
            [ring.util.http-response :as response]
            [schema.core :as s]
            [dictionary.db :as db]))

(def mount-target
  [:div#app
      [:h3 "ClojureScript has not been compiled!"]
      [:p "please run "
       [:b "lein figwheel"]
       " in order to start the compiler"]])

(defn head []
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge"}]
   [:meta {:name "viewport" :content "width=device-width, initial-scale=1"}]
   (include-css "https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css")])

(defn loading-page []
  (html5
    (head)
    [:body {:class "body-container"}
     mount-target
     (include-js "/js/app.js")]))

(defroutes routes
  (GET "/" [] (loading-page))
  (GET "/about" [] (loading-page))

  (resources "/")
  (not-found "Not Found"))

(def app (wrap-middleware #'routes))
