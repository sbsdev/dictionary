(ns dictionary.repl
  (:require [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            [clojure.java.jdbc :as jdbc]
            [clojure.string :as string]))


(def type-map
  {"*" 0 ; No restriction
   "n" 1 ; Also as a name
   "N" 2 ; Only as a name
   "p" 3 ; Also as a place
   "P" 4 ; Only as a place
   "H" 5}) ; Homograph default

(defn convert-record
  "Convert a dictionary record to a form suitable for inserting into the db"
  [[type-raw untranslated-raw grade2 grade1]]
  (let [type (type-map type-raw)
        untranslated (string/replace untranslated-raw "|" "")
        homograph_disambiguation (if (= type 5) untranslated-raw "")]
    (for [[braille grade] [[grade1 1] [grade2 2]]]
      {:type type
       :untranslated untranslated
       :grade grade
       :homograph_disambiguation homograph_disambiguation
       :braille braille})))

(defn- insert-records
  [tx table records]
  (jdbc/insert-multi! tx table records))

(defn read-csv [file-name]
  (with-open [reader (io/reader file-name)]
    (doall
     (->> (csv/read-csv reader :separator \tab :quote \`)
          (mapcat convert-record)))))

(defn import-csv
  "Import the given csv file `file-name` and insert as records in given
  `db`. The records are read, converted according
  to [[convert-record]] and [[type-map]]."
  [file-name db table]
  (jdbc/with-db-transaction [tx db]
    (->>
     (read-csv file-name)
     (insert-records tx table)
     count)))

