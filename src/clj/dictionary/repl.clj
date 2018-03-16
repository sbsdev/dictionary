(ns dictionary.repl
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]
            [clojure.string :as string]
            [dictionary.db :as db]))

(def type-map
  {"*" 0 ; No restriction
   "n" 1 ; Also as a name
   "N" 2 ; Only as a name
   "p" 3 ; Also as a place
   "P" 4 ; Only as a place
   "H" 5}) ; Homograph default

(defn convert-record
  "Convert a dictionary record to a form suitable for inserting into the db"
  [[type-raw untranslated-raw braille1, braille2]]
  (let [type (type-map type-raw)
        untranslated (string/replace untranslated-raw "|" "")
        homograph_disambiguation (if (= type 5) untranslated-raw "")]
    (for [[braille grade] [[braille1 1] [braille2 2]]]
      {:type type
       :untranslated untranslated
       :grade grade
       :homograph_disambiguation homograph_disambiguation
       :braille braille})))

(defn- insert-records [tx records]
  (for [record records]
    (db/insert-global-word! record {:connection tx})))

(defn import-csv
  "Import the given csv file `file-name` and insert as records in given
  `db`. The records are read, converted according
  to [[convert-record]] and [[type-map]]."
  [file-name db]
  (with-open [reader (io/reader file-name)]
    (jdbc/with-db-transaction [tx db]
       (->> (csv/read-csv reader :separator \tab :quote \`)
            (mapcat convert-record)
            (insert-records tx)
            (reduce +)))))
