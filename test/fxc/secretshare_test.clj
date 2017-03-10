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

(fact "Secret Sharing unit tests"
      (def pin (:integer (rand/create (:length settings))))

      (pp/pprint (str "PIN: " pin))

      (pp/pprint settings)

      (def numshares (ssss/shamir-split settings pin))

      (fact "PIN is split in numeric shares"

      (let [a (:shares numshares)
            f (take (:quorum settings) (:shares numshares))
            l (cons (biginteger 0)
                    (drop 1 (:shares numshares)))]
        ;;(- (:total settings) (:quorum settings))
        (pp/pprint {:all a
                    :first f
                    :last l})

        (fact "PIN is retrieved from all numeric shares"
              (ssss/shamir-combine numshares) => pin)

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
        ))
      (def shares (int2str-append-pos (:shares numshares)))

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

      (def back (str2int-trim-pos shares))

      (fact "Marshalled strings are converted to integers"

            (fact "resulting in array of correct length"
                  (count marsh) => (:total settings))

            (fact "combine correctly into the PIN"
                  (ssss/shamir-combine
                   {:header settings
                    :shares (take (:quorum settings) back)}) => pin)
            (pp/pprint back))

      (fact "PIN can be recovered"
            (let [f (take (:quorum settings) back)
                  l (drop (- (:total settings) (:quorum settings)) back)
                  s [(nth back 0) (nth back 2) (nth back 4)]]
              (fact "using first quorum shares"
                    (pp/pprint {:first_quorum f})
                    (ssss/shamir-combine
                     {:header settings
                      :shares f}) => pin)
              (fact "using last quorum shares"
                    (pp/pprint {:last_quorum l})
                    (ssss/shamir-combine
                     {:header settings
                      :shares l}) => pin)
              (fact "using scattered quorum shares"
                    (pp/pprint {:scatter_quorum s})
                    (ssss/shamir-combine
                     {:header settings
                      :shares s}) => pin)))
)

(defn debug [state]
  (clojure.pprint/pprint state)
  state)

(fact "Web integration tests (PIN)"
)
