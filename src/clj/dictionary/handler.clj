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

(s/defschema GlobalWord
  {:id s/Int
   :untranslated s/Str
   :braille s/Str
   :grade (s/enum 0 1 2)
   :type (s/enum 0 1 2 3 4)
   :homograph_disambiguation s/Str})

(def app
  (api.sweet/api
    {:swagger
     {:ui "/api-docs"
      :spec "/swagger.json"
      :data {:info {:title "Global Word API"
                    :description "API to the global corpus of certified braille translations"}
             :tags [{:name "api", :description "Certified braille translations"}]}}}

    (api.sweet/context "/globalwords" []
      :tags ["api"]

      (api.sweet/GET "/" []
        :return [GlobalWord]
        :query-params [grade :- Long offset :- Long max-rows :- Long]
        :summary "Gets all GlobalWords with given grade"
        (response/ok (db/read-global-words-paginated grade offset max-rows)))

      (api.sweet/GET "/search" []
        :return [GlobalWord]
        :query-params [grade :- Long term :- String]
        :summary "Gets all GlobalWords matching the given grade and search term"
        (response/ok (db/search-global-words grade term)))

      (api.sweet/GET "/search-regexp" []
        :return [GlobalWord]
        :query-params [grade :- Long term :- String]
        :summary "Gets all GlobalWords matching the given grade and regular expression search term"
        (response/ok (db/search-global-words-regexp grade term)))

      (api.sweet/GET "/:id" []
        :return GlobalWord
        :path-params [id :- Long]
        :summary "Gets a GlobalWord"
        (response/ok (db/get-global-word id))))))

