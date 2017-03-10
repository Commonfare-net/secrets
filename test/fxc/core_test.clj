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
(def secret (str (h/encode {:salt salt}
                           (for [x (range 0 3)] (r/digit (Integer/MAX_VALUE))))))

(pp/pprint {:secret secret
            :salt salt
            :seq (str2seq secret)})

(def dyneseq (str2seq secret))

(fact "String to seq"
      (seq2str (str2seq secret)) => secret)

(def dyneshares (seq2shares dyneseq))

(let [shares dyneshares
      back (shares2seq shares)]
  (pp/pprint {:orig shares})
  (pp/pprint {:back back}))

(fact "Shares to seq"
      (shares2seq dyneshares) => dyneseq
      (seq2str (shares2seq dyneshares)) => secret)

(def dynenumslices (shares2numslices dyneshares))
(def dyneslices (map ms/encode dynenumslices))

(pp/pprint {:vertical_slices dynenumslices})

(fact "Vertical slices across secrets"
      (fact "are as many as the settings total"
            (count dynenumslices) => (:total settings))
      (fact "are as many as the sequence of compressed integers"
            (count (first dynenumslices)) => (count dyneseq)))

(pp/pprint {:slices dyneslices
            :1stseq (ms/decode (first dyneslices))})
