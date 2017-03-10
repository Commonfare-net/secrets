(ns fxc.marshalling-test
  (:use midje.sweet)
  (:require [clojure.pprint :as pp]
            [fxc.marshalling :as ms]
            [fxc.intcomp     :as ic ]))

(def dyneseq [100 121 110 101 46 111 114 103]) ;; "dyne.org" in ascii codes

(def longstring (str repeat "x" 512))

(pp/pprint {:dyneseq dyneseq
            :len (count dyneseq)})

(fact "Marshalling strings into a sequences of integers"
      (fact "from string to sequence"
            (ms/str2intseq "dyne.org") => dyneseq)
      (fact "from sequence to string"
            (ms/intseq2str dyneseq) => "dyne.org")
      (fact "round conversion" ;; redundant but ok
            (ms/intseq2str (ms/str2intseq "dyne.org")) => "dyne.org")
      )


(pp/pprint {:compressed (ic/compress dyneseq)})
;; (pp/pprint (ic/compress (ic/codec.binpack) dyneseq))
(pp/pprint (conj {:0 "Confirming FastPFOR128 is default, this should return same values"}
                 (ic/compress (ic/codec.pfor128) (ms/str2intseq dyneseq))))

(fact "Compressing integers"
      (fact "back and forth"

            (fact "sequence of integers"
                  (fact "default codec"
                        (let [com (ic/compress dyneseq)
                              dec (ic/decompress com)]
                          dec => dyneseq))
                  (fact "pfor128 codec"
                        (seq (:data (ic/decompress (ic/codec.pfor128)
                              (:data (ic/compress  (ic/codec.pfor128)
                                    dyneseq))))) => dyneseq))

            (fact "string marshalling"
                  (ms/intseq2str
                   (ic/decompress (ic/compress
                                   (ms/str2intseq "dyne.org")))) => "dyne.org")
      ))

;;(pp/pprint (map biginteger (seq (ic/compress dyneseq))))
(pp/pprint {:encoded (ms/encode dyneseq)})
;;(pp/pprint (ms/encode (seq (ic/compress dyneseq))))
(pp/pprint {:Integer/MAX_VALUE  Integer/MAX_VALUE})
(pp/pprint {:unsigned (ms/int2unsigned (ic/compress dyneseq))})

(pp/pprint
 (ms/encode (ms/int2unsigned (ic/compress dyneseq))))

(fact "Making all negative integers unsigned"
      ;; TODO: add a fuzzy test with very big integers
      (ms/unsigned2int (ms/int2unsigned (seq (ic/compress dyneseq)))) => (seq (ic/compress dyneseq)))

(fact "Hashing integers"
      (fact "sequence back and forth"
            (ms/decode (ms/encode dyneseq)) => dyneseq)
      (fact "compressed back and forth"
            (seq (ic/decompress (ms/unsigned2int (ms/decode
                                                  (ms/encode (ms/int2unsigned (ic/compress dyneseq)))))))
            => dyneseq))

