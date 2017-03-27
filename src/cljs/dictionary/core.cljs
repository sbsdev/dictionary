(ns dictionary.core
    (:require [reagent.core :as reagent :refer [atom]]
              [reagent.session :as session]
              [secretary.core :as secretary :include-macros true]
              [accountant.core :as accountant]
              [dictionary.i18n :as i18n]
              [taoensso.tempura :as tempura]))

(def lang (keyword (i18n/default-lang)))
(def tr (partial tempura/tr {:dict i18n/translations} [lang]))

;; The context-path is defined statically in the project file and
;; passed in through a Cljs Compiler
;; Option (https://github.com/clojure/clojurescript/wiki/Compiler-Options#closure-defines)
;; see also https://www.martinklepsch.org/posts/parameterizing-clojurescript-builds.html
(goog-define context-path "")

(defonce app-state
  (reagent/atom
   {:words {:foo {:untranslated "haha" :braille "hehe" :type 1}
            :bar {:untranslated "fip" :braille "fap" :type 1}}
    :grade 1
    :search-term ""}))

(def grade (reagent/cursor app-state [:grade]))
(def search-term (reagent/cursor app-state [:search-term]))

(defn- navbar-ui []
  [:nav.navbar.navbar-default
   [:div.container-fluid
    [:div.navbar-header
     [:button.navbar-toggle.collapsed
      {:type "button"
       :data-toggle "collapse"
       :data-target "#navbar-collapse"
       :aria-expanded "false"}
      [:span.sr-only (tr [:toggle-nav])]
      [:span.icon-bar]
      [:span.icon-bar]
      [:span.icon-bar]]
     [:a.navbar-brand {:href (str context-path "/")} (tr [:brand])]]
    [:div#navbar-collapse.navbar-collapse.collapse
     [:ul.nav.navbar-nav.navbar-left]]]])

(defn word-ui [{:keys [untranslated braille type]}]
  [:tr
   [:td untranslated]
   [:td braille]
   [:td type]
   [:div.btn-group
    [:button.btn.btn-default
     {:on-click identity}
     [:span.glyphicon.glyphicon-edit {:aria-hidden true}] " " (tr [:edit])]
    [:button.btn.btn-default
     {:on-click identity}
     [:span.glyphicon.glyphicon-trash {:aria-hidden true}] " " (tr [:delete])]]])

(defn grade-ui []
  [:div.form-group
   [:select {:value @grade
             :on-change (fn [e]
                          (reset! grade (-> e .-target .-value js/parseInt)))}
    [:option {:value 1} (tr [:grade1])]
    [:option {:value 2} (tr [:grade2])]]])

(defn search-ui [search]
  (let [label (tr [:search])]
    [:div.form-group
     [:label.sr-only {:for "SearchField"} label]
     [:input.form-control
      {:type "text"
       :id "SearchField"
       :placeholder label
       :value @search
       :on-change (fn [e] (reset! search (-> e .-target .-value)))}]]))

;; -------------------------
;; Views

(defn home-page-ui []
  [:div.container
   [navbar-ui]
   [:div.row
    [:div.col-md-12
     [:div.form-inline
      [grade-ui]
      [search-ui search-term]
      [:button.btn.btn-default {:type "submit"} (tr [:search])]]]]
   [:div.row
    [:table#hyphenations.table.table-striped
     [:thead [:tr [:th (tr [:untranslated])] [:th (tr [:braille])] [:th (tr [:type])] [:th ]]]
     [:tbody
      (for [[id word] (:words @app-state)]
        ^{:key id} [word-ui word])]]]])

(defn current-page []
  [:div [(session/get :current-page)]])

;; -------------------------
;; Routes

(secretary/defroute "/" []
  (session/put! :current-page #'home-page-ui))

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [current-page] (.getElementById js/document "app")))

(defn init! []
  (accountant/configure-navigation!
    {:nav-handler
     (fn [path]
       (secretary/dispatch! path))
     :path-exists?
     (fn [path]
       (secretary/locate-route path))})
  (accountant/dispatch-current!)
  (mount-root))
