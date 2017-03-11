(ns fxc.secretshare-test
  (:use midje.sweet)
  (:require
   [fxc.secretshare :as ssss]
   [fxc.random :as rand]
   [fxc.marshalling :refer :all]
   [kerodon.core :as k]
   [clojure.pprint :as pp]))

(pp/pprint {"------------------------------------------" "SECRETSHARING_TESTS" })

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

(defn take-first-shares
  [shares]
  (take (get-in shares [:header :quorum]) (:shares shares)))

(defn take-last-shares
  [shares]
  (let [jump (- (get-in shares [:header :total])
                (get-in shares [:header :quorum]))]
    (drop jump (:shares shares))))

(defn take-scatter-shares
  [shares]
  (let [sh (:shares shares)]
    [(first sh) (nth sh 2) (nth sh 4)]))


      (def pin (:integer (rand/create (:length settings))))

(pp/pprint (str "PIN: " pin))

(pp/pprint settings)

(def rawsecrets (ssss/shamir-split settings pin))

(fact "PIN is split in numeric shares"

      (let [a (:shares rawsecrets)
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
              (ssss/shamir-combine rawsecrets) => pin)

        (fact "PIN is retrieved from numeric first quorum shares"
              (ssss/shamir-combine
               {:header settings
                :shares f})
              => pin)

        (fact "PIN is retrieved from numeric last quorum shares"
              (ssss/shamir-combine
               {:header settings
                :shares l})
              => pin)

        (fact "PIN is retrieved from numeric scattered quorum shares"
              (ssss/shamir-combine
               {:header settings
                :shares s})
              => pin)

        ))

(def secrets {:header (:header rawsecrets)
              :shares (encode-shares (:shares rawsecrets))})

(def decoded-secrets {:header (:header secrets)
                      :shares (decode-shares (:shares secrets))})

(pp/pprint {:encoded-secrets secrets
            :decoded-secrets decoded-secrets})

(fact "Encoded secrets are decoded correctly"

      (fact "same header"
            (:header secrets) => (:header decoded-secrets))

      (fact "resulting in array of correct length"
            (count (:shares decoded-secrets)) => (:total settings))

      (fact "combine correctly into the PIN"
            (ssss/shamir-combine decoded-secrets) => pin)

      )

(fact "PIN can be recovered"
      (let [f (decode-shares (take-first-shares secrets))
            l (decode-shares (take-last-shares secrets))
            s (decode-shares (take-scatter-shares secrets))]
        (fact "using first quorum shares"
              (pp/pprint {:first_quorum f})
              (ssss/shamir-combine
               {:header (:header decoded-secrets)
                :shares f}) => pin)
        (fact "using last quorum shares"
              (pp/pprint {:last_quorum l})
              (ssss/shamir-combine
               {:header (:header decoded-secrets)
                :shares l}) => pin)
        (fact "using scattered quorum shares"
              (pp/pprint {:scatter_quorum s})
              (ssss/shamir-combine
               {:header (:header decoded-secrets)
                :shares s}) => pin)))


