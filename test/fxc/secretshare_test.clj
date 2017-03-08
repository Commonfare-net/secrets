(ns fxc.secretshare-test
  (:use midje.sweet)
  (:require
   [fxc.secretshare :as ssss]
   [fxc.random :as rand]
   [fxc.marshalling :refer :all]
   [kerodon.core :as k]
   [clojure.pprint :as pp]))

;; defaults
(def settings
  {:total (Integer. 5)
   :quorum (Integer. 3)

   :prime 'prime4096

   :description "FXC v1 (Freecoin Secret Sharing)"

   ;; versioning every secret
   :protocol "FXC1"

   :type "WEB"

   ;; random number generator settings
   :length 6
   :entropy 3.1})

(fact "Secret Sharing unit tests"
      (def pin (:integer (rand/create (:length settings))))

      (pp/pprint (str "PIN: " pin))

      (pp/pprint settings)

      (def shares (:shares (ssss/shamir-split settings pin)))

      (fact "PIN is split in shares"
            (pp/pprint shares))

      (fact "PIN is retrieved from all shares"
            (ssss/shamir-combine {:header settings
                                  :shares shares}) => pin)

      (fact "PIN is retrieved from quorum shares"
            (ssss/shamir-combine {:header settings
                                  :shares (take (:quorum settings) shares)})
            => pin)

      (def marsh (int2str-append-pos shares))

      (fact "Shares are marshalled into strings appending pos cipher"
            (fact "results in array of correct length"
                  (count marsh) => (:total settings))
            (fact "each string has its position as last cipher"
                  (loop [c 1 
                         m (first marsh)]
                    (if (< c (count marsh))
                      (str (last m)) => (str c)
                      (recur (inc c)
                             (nth marsh (inc c))))))
            (pp/pprint marsh))

      (fact "Marshalled strings are converted to integers"
            (def back (str2int-trim-pos marsh))

            (fact "resulting in array of correct length"
                  (count marsh) => (:total settings))

            (fact "combine correctly into the PIN"
                  (ssss/shamir-combine
                   {:header settings
                    :shares (take (:quorum settings) back)}))
            (pp/pprint back))
)

(defn debug [state]
  (clojure.pprint/pprint state)
  state)

(fact "Web integration tests (PIN)"
)
