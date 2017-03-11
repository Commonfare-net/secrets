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

        ))

(def secrets (ms/encode-shares rawsecrets))

(def decoded-secrets (ms/decode-shares secrets))

(pp/pprint {:encoded-secrets secrets
            :decoded-secrets decoded-secrets})

(fact "Encoded secrets are decoded correctly"

      (fact "resulting in array of correct length"
            (count decoded-secrets) => (:total settings))

      (fact "combine correctly into the PIN"
            (ssss/shamir-combine settings decoded-secrets) => pin)

      )

(fact "PIN can be recovered"
      (let [f (ms/decode-shares (take-first-shares secrets))
            l (ms/decode-shares (take-last-shares secrets))
            s (ms/decode-shares (take-scatter-shares secrets))]
        (fact "using first quorum shares"
              (pp/pprint {:first_quorum f})
              (ssss/shamir-combine settings f) => pin)
        (fact "using last quorum shares"
              (pp/pprint {:last_quorum l})
              (ssss/shamir-combine settings l) => pin)
        (fact "using scattered quorum shares"
              (pp/pprint {:scatter_quorum s})
              (ssss/shamir-combine settings s) => pin)))
