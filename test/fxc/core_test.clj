(ns fxc.core-test
  (:use midje.sweet)
  (:require  [fxc.core :refer :all]
             [fxc.random :as r]
             [fxc.secretshare :as ss]
             [fxc.marshalling :as ms]
             [hashids.core :as h]
             [clojure.pprint :as pp]))

(pp/pprint {"------------------------------------------" "FXC_CORE_TESTS"})

;; TODO: get some proper random
(def salt (str (for [x (range 0 6)] (r/digit (Integer/MAX_VALUE)))))
;; generate a random secret
(def password (str (h/encode {:salt salt}
                           (for [x (range 0 3)] (r/digit (Integer/MAX_VALUE))))))

(pp/pprint {:password password
            :salt salt
            :seq (str2seq password)})

(def intseq (str2seq password))

(fact "String to seq"
      (seq2str intseq) => password)

(def secrets (seq2secrets intseq))

(fact "Seq to secrets"
      (secrets2seq secrets) => intseq
      (fact "Secrets to seq to password"
            (seq2str (secrets2seq secrets)) => password))

(def raw-slices (secrets2numslices secrets))
(def encoded-slices (map ms/encode raw-slices))
(def decoded-slices (map ms/decode encoded-slices))

(pp/pprint {:raw-slices raw-slices
            :encoded-slices encoded-slices})

(fact "Vertical slices across secrets"
      (fact "are as many as the settings total"
            (count raw-slices) => (:total settings))
      (fact "are as many as the sequence of compressed integers"
            (count (first raw-slices)) => (count intseq))
      (fact "are decoded correctly to raw numeric format"
            raw-slices => decoded-slices))

