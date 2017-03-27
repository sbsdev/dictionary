(ns dictionary.db
  "Persistence for the dictionary"
  (:require [yesql.core :refer [defqueries]])
  (:import java.sql.SQLSyntaxErrorException))

(def ^:private db {:name "java:jboss/datasources/dictionary"})

(defqueries "db/queries.sql" {:connection db})

(defn read-global-words
  "Return a coll of words for given `grade`"
  [grade]
  (-> {:grade grade} global-words))

(defn read-global-words-paginated
  "Return a coll of words for given `grade` using `max-rows` and `offset` for pagination"
  [grade offset max-rows]
  (-> {:grade grade :max_rows max-rows :offset offset}
      global-words-paginated))

(defn search-global-words
  "Return a coll of words for given `grade` and given `search` term"
  [grade search]
  (try
    (-> {:grade grade :search search} global-words-search)
    ;; ignore exceptions due to incomplete regexps
    (catch SQLSyntaxErrorException e ())))

;; (defn save-word!
;;   "Persist `word` with given `hyphenation` and `grade`"
;;   [word hyphenation grade]
;;   (-> {:word word :hyphenation hyphenation :grade grade}
;;       save-word-internal!))

(defn get-global-word [id]
  (-> {:id id} (get-global-word-internal {:result-set-fn first})))

(defn remove-global-word! [id]
  (-> {:id id} remove-global-word-internal!))
