;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns dvlopt.rktree.transit

  "Providing serialization for Transit which does not differentiate sorted maps from unsorted ones."

  {:author "Adam Helinski"}

  (:require [cognitect.transit :as transit])
  #?(:clj (:import clojure.lang.PersistentTreeMap)))


;;;;;;;;;;


(def read-handler

  "Map containing a read handler for sorted maps."

  {"sorted-map" (transit/read-handler (partial reduce-kv
                                               assoc
                                               (sorted-map)))})



(def write-handler

  "Map containing a write handler for sorted maps."

  {PersistentTreeMap (transit/write-handler (fn tag [_] "sorted-map")
                                              (partial reduce-kv
                                                       assoc
                                                       {}))})
