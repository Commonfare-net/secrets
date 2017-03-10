(ns fxc.core-test
  (:use midje.sweet)
  (:require  [fxc.core :refer :all]
             [fxc.random :as r]
             [fxc.secretshare :as ss]
             [fxc.marshalling :as mm]
             [hashids.core :as h]
             [clojure.pprint :as pp]))

(pp/pprint {"------------------------------------------" "FXC_CORE_TESTS"})

;; generate a random secret
(def secret (str (h/encode {:salt (str (r/digit (Integer/MAX_VALUE)))}
                           (for [x (range 0 3)] (r/digit (Integer/MAX_VALUE))))))

(pp/pprint {:secret secret
            :seq (str2seq secret)})

(def dyneseq (str2seq secret))

(fact "String to seq"
      (seq2str (str2seq secret)) => secret)

(def dyneshares (seq2shares dyneseq))

(let [shares dyneshares
      back (shares2seq shares)]
  (pp/pprint {:orig shares})
  (pp/pprint {:back back}))

(fact "Seq to shares"
      (shares2seq dyneshares) => dyneseq)

(def dyneslices (shares2slices dyneshares))
(pp/pprint {:vertical_slices dyneslices})

(fact "Vertical slices across secrets"
      (fact "are as many as the settings total"
            (count dyneslices) => (:total settings))
      (fact "are as many as the sequence of compressed integers (minus the lenght indicator)"
            (count (first dyneslices)) => (count dyneseq)))

(pp/pprint (map mm/encode dyneslices))
