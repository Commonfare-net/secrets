(ns fxc.core-test
  (:use midje.sweet)
  (:require  [fxc.core :refer :all]
             [fxc.secretshare :as ss]
             [clojure.pprint :as pp]))

(pp/pprint {"------------------------------------------" "FXC_CORE_TESTS"})

(pp/pprint {:str "dyne.org"
            :seq (str2seq "dyne.org")})

(fact "String to seq"
      (seq2str (str2seq "dyne.org")) => "dyne.org")

(let [shares (seq2shares (str2seq "dyne.org"))
      back (shares2seq shares)]
  (pp/pprint {:orig shares})
  (pp/pprint {:back back}))

(def dyneseq (str2seq "dyne.org"))

(fact "Seq to shares"
      (shares2seq (seq2shares dyneseq)) => dyneseq)
