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

-- name: global-words-search
-- Get the global words for given grade that match the given search term
SELECT *
FROM dictionary_globalword
WHERE grade = :grade
AND untranslated REGEXP :search
LIMIT 100

-- name: get-global-word-internal
-- Get the global word for given id
SELECT *
FROM dictionary_globalword
WHERE id = :id

-- name: remove-global-word-internal!
-- Delete the given `word` from the dictionary.
DELETE FROM dictionary_globalword
WHERE id = :id
