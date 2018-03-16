-- name: global-words
-- Get the global words for given grade
SELECT *
FROM dictionary_globalword
WHERE grade = :grade

-- name: global-words-paginated
-- Get the global words for given grade limited by offset and max_rows
SELECT *
FROM dictionary_globalword
WHERE grade = :grade
LIMIT :offset, :max_rows

-- name: global-words-search-regexp
-- Get the global words for given grade that match the given regular expression search term.
-- This query is slower than [[global-words-search]] due to the use of REGEXP which cannot
-- use any indices.
SELECT *
FROM dictionary_globalword
WHERE grade = :grade
AND untranslated REGEXP :search
LIMIT 200

-- name: global-words-search
-- Get the global words for given grade that match the given search term
SELECT *
FROM dictionary_globalword
WHERE grade = :grade
AND untranslated LIKE :search
LIMIT 200

-- name: get-global-word-internal
-- Get the global word for given id
SELECT *
FROM dictionary_globalword
WHERE id = :id

-- name: remove-global-word-internal!
-- Delete word with the given `id` from the dictionary.
DELETE FROM dictionary_globalword
WHERE id = :id

-- name: insert-global-word!
-- Insert the given word into the dictionary
INSERT INTO dictionary_globalword (untranslated, braille, grade, type, homograph_disambiguation)
VALUES (:untranslated, :braille, :grade, :type, :homograph_disambiguation)
