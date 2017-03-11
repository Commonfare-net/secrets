;; FXC -  Secret Sharing

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

(ns fxc.core
  (:require [fxc.marshalling :as ms]
            [fxc.intcomp :as ic]
            [fxc.secretshare :as ss]))


;; defaults
(def settings
  {:total (Integer. 5)
   :quorum (Integer. 3)

   :prime 'prime4096

   :description "FXC v1 (Freecoin Secret Sharing)"

   ;; versioning every secret
   :protocol "FXC1"

   :type "WEB"

   ;; this alphabet excludes ambiguous chars:
   ;; 1,0,I,O can be confused on some screens
   :alphabet "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
   ;; the salt should be a secret shared password
   ;; known to all possessors of the key pieces
   :salt "La gatta sul tetto che scotta"

   ;; random number generator settings
   :length 6
   :entropy 3.1})

;; internal functions


(defn str2seq
  "Takes a string and returns a unique collection of big unsigned
  integers. First integer is the length of the original string."
  [s]
  {:pre [(string? s)]
   :post [(coll? %)]}
  (ms/int2unsigned (ic/compress (ms/str2intseq s))))

(defn seq2str
  "Takes a collection of big unsigned integers and returns a string."
  [s] {:pre [(coll? s)]
       :post [(string? %)]}
  (ms/intseq2str (ic/decompress (ms/unsigned2int s))))

(defn seq2secrets
  "Takes a sequence and computes secrets."
  [s] {:pre  [(coll? s)]
       :post [(coll? %)]}
  (loop [[i & slices] (drop 1 s)
         res []]
    (let [res (conj res (ss/shamir-split settings (biginteger i)))]
      (if (empty? slices)
        {:length (first s)
         :secrets res}
        (recur slices res)))))


(defn secrets2slices
  "Traverse secrets horizontally to harvest settings:total slices and
  returns a collection of integers."
  [secrets]
  (for [slinum (range 0 (:total settings))
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
  [slices]
  {:pre [(>= (count slices) (:quorum settings))
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
  (ms/decode-hash slice)
  )

(defn secrets2seq
  "Takes clear shares and returns a sequence"
  [s]
  (loop [[i & slices] (:secrets s)
         res []]
    (let [res (conj res (ss/shamir-combine settings i))]
      (if (empty? slices)
        (cons (:length s) res)
        (recur slices res)))))





;; public functions
(defn encode
  "Takes a string and returns multiple strings that can be used to
  retrieve the original according to settings."
  [conf pass] {:pre [(string? pass)
                     (map? conf)]
               :post [(coll? %)
                      (string? (first %))
                      (= (count %) (:total conf))]}

  (map #(ms/encode-hash settings %) (-> pass
                                       str2seq
                                       seq2secrets
                                       secrets2slices
                                       )))

(defn decode
  "Takes a collection of strings and returns the original secret
  according to the settings"
  [conf slices] {:pre [(coll? slices)
                       (map? conf)
                       (<= (count slices) (:total conf))]
                 :post [(string? %)]}

  (-> (map #(ms/decode-hash settings %) slices)
      slices2secrets
      secrets2seq
      seq2str))
