;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at https://mozilla.org/MPL/2.0/.


(ns dvlopt.rktree.example

  "Exercepts from README."

  (:require [dvlopt.rktree :as rktree]))


;;;;;;;;;;


(comment

  (def my-tree
       (sorted-map 0 (sorted-map 1 {:a {:b 'leaf-1}})
                   5 {:a {:d {:e 'leaf-2}}}))



  (= 'leaf-1
     (rktree/get my-tree
                 [0 1]
                 [:a :b]))



  (= (rktree/pop my-tree)

     [(sorted-map 5 {:a {:d {:e 'leaf-2}}})
      [0 1]
      {:a {:b 'leaf-1}}])




  ;; <!> Following throws in CLJS, probably due to a bug in CLJS itself.


  (def my-tree-2
       (rktree/assoc my-tree
                     [0 1 0 0 0 5]
                     [:possible?]
                     true))



  ;; Notice that 'leaf has been re-prioritized from [0 1] to [0 1 0 0 0 0].
  ;; Order is actuall ymaintained as before, but we can account for the new
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
  )
