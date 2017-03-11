(ns fxc.secretshare-test
  (:use midje.sweet)
  (:require
   [fxc.secretshare :as ssss]
   [fxc.random :as rand]
   [fxc.core :refer :all]
   [fxc.marshalling :as ms]
   [kerodon.core :as k]
   [clojure.pprint :as pp]))

(pp/pprint {"------------------------------------------" "SECRETSHARING_TESTS" })

(defn take-first-shares
  [shares]
  (take (:quorum settings) shares))

(defn take-last-shares
  [shares]
  (let [jump (- (:total settings) (:quorum settings))]
    (drop jump shares)))

(defn take-scatter-shares
  [shares]
  (let [sh shares]
    [(first sh) (nth sh 2) (nth sh 4)]))


(def pin (:integer (rand/create (:length settings))))

(pp/pprint (str "PIN: " pin))

(pp/pprint settings)

(def rawsecrets (ssss/shamir-split settings pin))

(fact "PIN is split in numeric shares"

      (let [a rawsecrets
            f (take-first-shares rawsecrets)
            l (take-last-shares rawsecrets)
            s (take-scatter-shares rawsecrets)]
        ;;(- (:total settings) (:quorum settings))
        (pp/pprint {:rawsecrets rawsecrets
                    :all a
                    :first f
                    :last l
                    :scat s})

        (fact "PIN is retrieved from all numeric shares"
              (ssss/shamir-combine settings rawsecrets) => pin)

        (fact "PIN is retrieved from numeric first quorum shares"
              (ssss/shamir-combine settings f) => pin)

        (fact "PIN is retrieved from numeric last quorum shares"
              (ssss/shamir-combine settings l) => pin)

        (fact "PIN is retrieved from numeric scattered quorum shares"
              (ssss/shamir-combine settings s) => pin)

        (fact "PIN is retrieved from numeric shuffled quorum shares"
              (ssss/shamir-combine settings
                                   (take (:quorum settings)
                                         (shuffle rawsecrets))) => pin)

        ))
