# Dictionary

A tentative reimplementation of the [dictionary module](https://github.com/sbsdev/daisyproducer/tree/master/daisyproducer/dictionary) of
[Daisyproducer](https://github.com/sbsdev/daisyproducer).

## Repl-based utilities

To import a corpus of known good braille translations in the form of a
csv file, use a repl

``` lisp
dictionary.repl> (import-csv "foo.csv" db)
2258668
dictionary.repl>
```

The csv is expected to be in the form of the following:

``` csv
*	zündhölzchen	Z8NDH9LZ4C	Z8NDH9LZ4EN
*	zündhölzchens	Z8NDH9LZ4CS	Z8NDH9LZ4ENS
*	zündhölzchenschachtel	Z8NDH9LZ4C5<TY	Z8NDH9LZ4EN5A4TEL
*	zündhölzer	Z8NDH9LZ7	Z8NDH9LZER
*	zündhölzern	Z8NDH9LZ7N	Z8NDH9LZERN
*	zündhölzlein	Z8NDH9LZL6	Z8NDH9LZL3N
*	zündhölzli	Z8NDH9LZLI	Z8NDH9LZLI
*	zündhörner	Z8NDH9RN7	Z8NDH9RNER
```
