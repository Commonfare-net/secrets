;; FXC - PIN Secret Sharingdigital  social currency toolkit

;; part of Decentralized Citizen Engagement Technologies (D-CENT)
;; R&D funded by the European Commission (FP7/CAPS 610349)

;; Copyright (C) 2015-2017 Dyne.org foundation

;; Sourcecode designed, written and maintained by
;; Denis Roio <jaromil@dyne.org>

;; This program is free software: you can redistribute it and/or modify
;; it under the terms of the GNU Affero General Public License as published by
;; the Free Software Foundation, either version 3 of the License, or
;; (at your option) any later version.

;; This program is distributed in the hope that it will be useful,
;; but WITHOUT ANY WARRANTY; without even the implied warranty of
;; MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
;; GNU Affero General Public License for more details.

;; You should have received a copy of the GNU Affero General Public License
;; along with this program.  If not, see <http://www.gnu.org/licenses/>.

(ns fxc.marshalling
  (:require [clojure.string :as str]
            [hashids.core :as h]
            [fxc.intcomp :as ic]
            [fxc.secretshare :as ss]))


;; TODO: verify this under fuzzying
(defn int2unsigned
  "takes a collection of integers and converts it to unsigned
  notation, saves the first element which is just the length of the
  original size before compression"
  [i]
  (cons (first i) (map #(+ (biginteger %) (Integer/MAX_VALUE)) (drop 1 i))))

(defn unsigned2int
  [i]
  (cons (first i) (map #(- (biginteger %) (Integer/MAX_VALUE)) (drop 1 i))))

(defn encode-hash
  "takes an intseq and encodes it with hashids"
  [conf o] {:pre  [(coll? o)]
            :post [(string? %)]}
  (h/encode conf o))

(defn decode-hash
  "takes an hash and decodes it with hashids"
  [conf o] {:pre  [(string? o)]
            :post [(coll? %)]}
  (h/decode conf o))

;; (defn parse-int [s]
;;   (Integer. (re-find  #"\d+" s )))

(defn str2intseq
  "takes a string and returns a sequence of integer ascii codes"
  [s]
  (map #(int %) (seq s)))

(defn intseq2str
  "takes a sequence of integer ascii codes and returns a string"
  [s]
  (apply str (map #(char %) s)))

;; internal functions


(defn str2seq
  "Takes a string and returns a unique collection of big unsigned
  integers. First integer is the length of the original string."
  [s]
  {:pre [(string? s)]
   :post [(coll? %)]}
  (int2unsigned (ic/compress (str2intseq s))))

(defn seq2str
  "Takes a collection of big unsigned integers and returns a string."
  [s] {:pre [(coll? s)]
       :post [(string? %)]}
  (intseq2str (ic/decompress (unsigned2int s))))

(defn seq2secrets
  "Takes a sequence and computes secrets."
  [conf s] {:pre  [(coll? s)]
       :post [(coll? %)]}
  (loop [[i & slices] (drop 1 s)
         res []]
    (let [res (conj res (ss/shamir-split conf (biginteger i)))]
      (if (empty? slices)
        {:length (first s)
         :secrets res}
        (recur slices res)))))


(defn secrets2slices
  "Traverse secrets horizontally to harvest settings:total slices and
  returns a collection of integers."
  [conf secrets]
  (for [slinum (range 0 (:total conf))
        :let [slice (loop [[verti & slices] (:secrets secrets)
                           res[]]
                      (let [num (second (nth verti slinum))
                            res (conj res num)]
                        (if (empty? slices)
                          res
                          (recur slices res))))]]
    (cons (:length secrets) (conj slice (inc slinum)))))

(defn slices2secrets
  "Takes horizontal slices (decoded) and returns vertically aggregated
  secrets ready for processing by shamir-combine."
  [conf slices]
  {:pre [(>= (count slices) (:quorum conf))
         (integer? (first (last slices)))]
   :post [(coll? %)]}
  ;; iterate over the length of elements in slices
  ;; two is subtracted to remove the original pass len and position
  {:length (first (first slices))
   :secrets (for [c (range 1 (dec (count (first slices))))]
              (loop [[s & sli] (sort-by last slices) ;; TODO: sort
                     res []  ]
                (let [res (conj res [(last s) (nth s c)])]
                  (if (empty? sli) res
                      (recur sli res)))))})


(defn slice2seq
  "Gets a sliced strings, decodes and orders them according to
  position, then returns a sequence of integers"
  [slice]
  (decode-hash slice)
  )

(defn secrets2seq
  "Takes clear shares and returns a sequence"
  [conf s]
  (loop [[i & slices] (:secrets s)
         res []]
    (let [res (conj res (ss/shamir-combine conf i))]
      (if (empty? slices)
        (cons (:length s) res)
        (recur slices res)))))




