# Ranked trees

[![Clojars
Project](https://img.shields.io/clojars/v/dvlopt/rktree.svg)](https://clojars.org/dvlopt/rktree)

[![cljdoc badge](https://cljdoc.org/badge/dvlopt/rktree)](https://cljdoc.org/d/dvlopt/rktree)

![CircleCI](https://circleci.com/gh/helins/rktree.cljc.svg?style=shield)

Compatible with Clojurescript.

Feel free to clone this repo, the examples below located are in the
[dvlopt.rktree.example](../main/src/example/dvlopt/rktree/example.cljc)
namespace. See the "Development" section at the end of this document.


## Usage

A `ranked tree` is a peculiar but interesting data structure. It is a form of
nested maps where leaves are located both in time and space. It comes in handy
for problems that needs some form of prioritization.

More specifically, it is a complex of nested maps where former levels are sorted
and latter levels are unsorted.


Following this definition, the following qualifies as a `ranked tree`:

```clojure
(def my-tree
     (sorted-map 0 (sorted-map 1 {:a {:b 'leaf-1}})
                 5 {:a {:d {:e 'leaf-2}}}))
```

Each leaf has two paths, one "horizontal" and one "vertical" (borrowing
vocabulary from the litterature). We shall rather talk (respectively) about
`ranks` providing a notion of time and a `path` providing a notion of space.

Thus, `'leaf-1` is said to be located at `[:a :b]` and ranked at `[0 1]`.
Similarly, `'leaf-2` is said to be located at `[:c :d :e]` and ranked at `[5]`.
We specify both the `ranks` and the `path` when we want to `get` something out
of this tree:

```clojure
(require '[dvlopt.rktree :as rktree])

(= 'leaf-1
   (rktree/get my-tree
               [0 1]
               [:a :b]))
```

Ranks provid prioritization, a lower rank meaning a higher priority, 0 being the
highest priority. When we `pop` the tree, we receive whatever resides at the
ranks with the highest priority. More precisely, we receive `[popped-tree ranks
unsorted-node]`:

```clojure
(= (rktree/pop my-tree)

   [(sorted-map 5 {:a {:d {:e 'leaf-2}}})
    [0 1]
    {:a {:b 'leaf-1}}])
```

Interestingly, as you might have noticed, different leaves can have ranks of
different length. This library handles that automagically. Remember we already
have `'leaf-1` located at `[:a :b]` and ranked at `[0 1]`. What if we `assoc`
something past those ranks?

```Clojure
(def my-tree-2
     (rktree/assoc my-tree
                   [0 1 0 0 0 5]
                   [:possible?]
                   true))

;; Notice that 'leaf has been re-prioritized from [0 1] to [0 1 0 0 0 0].
;; Order is actually maintained as before, but we can account for the new
;; addition above.

(= 'leaf-1
   (rktree/get my-tree-2
               [0 1 0 0 0 0]
               [:a :b]))

;; But notice that we can still use the original ranks!

(= 'leaf-1
   (rktree/get my-tree-2
               [0 1]
               [:a :b]))
```

We have discovered a few recognizable functions such as `assoc` and `get`. The
[API](https://cljdoc.org/d/dvlopt/rktree) provide other ones (`dissoc`,
`update`, and friends), all acting on this idea of having `ranks` and a `path`.


## Serialization

Some serializers make a distinction between sorted maps and unsorted ones. For
instance, [Nippy](https://github.com/ptaoussanis/nippy) does.

But Transit does not.

The user can add the following dependency along side
[Transit-clj](https://github.com/cognitect/transit-clj) or
[Transit-cljs](https://github.com/cognitect/transit-cljs):

[![Clojars
Project](https://img.shields.io/clojars/v/dvlopt/rktree.transit.svg)](https://clojars.org/dvlopt/rktree.transit)

This package provides a read handler and a write handler for sorted maps:

```clojure
(require '[dvlopt.rktree.transit :as rktree.transit])

rktree.transit/read-handler

rktree.transit/write-handler
```


## Running tests

On the JVM, using [Kaocha](https://github.com/lambdaisland/kaocha):

```bash
$ ./bin/test/jvm/run
$ ./bin/test/jvm/watch
```
On NodeJS, using [Kaocha-CLJS](https://github.com/lambdaisland/kaocha-cljs):

```bash
$ ./bin/test/node/run
$ ./bin/test/node/watch
```

In the browser, using [Chui](https://github.com/lambdaisland/chui):
```
$ ./bin/test/browser/compile
# Then open ./resources/chui/index.html

# For testing an advanced build
$ ./bin/test/browser/advanced
```


## Development

Starting in Clojure JVM mode, mentioning an additional deps alias (here, a local
setup of NREPL):
```bash
$ ./bin/dev/clojure :nrepl
```

Starting in CLJS mode using Shadow-CLJS:
```bash
$ ./bin/dev/cljs
# Then open ./resources/public/index.html
```


## License

Copyright © 2020 Adam Helinski

Licensed under the term of the Mozilla Public License 2.0, see LICENSE.
