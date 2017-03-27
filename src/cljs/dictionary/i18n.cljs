(ns dictionary.i18n)

;; the idea for this comes from
;; http://www.mattzabriskie.com/blog/detecting-locale
(defn lang-attribute []
  (.. js/document -documentElement (getAttribute "lang")))

(defn default-lang []
  (or (lang-attribute) "de"))

(def translations
  {:en {}
   :de {:brand "Globales Wörterbuch"
        :untranslated "Schwarzschrift"
        :braille "Braille"
        :type "Typ"
        :grade1 "Basisschrift"
        :grade2 "Kurzschrift"
        :search "Suche"
        :edit "Editieren"
        :delete "Löschen"}})

