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
            [fxc.intcomp :as ic]))


(def hashids-conf
  {
   ;; this alphabet excludes ambiguous chars:
   ;; 1,0,I,O can be confused on some screens
   :alphabet "ABCDEFGHJKLMNPQRSTUVWXYZ23456789"
   ;; the salt should be a secret shared password
   ;; known to all possessors of the key pieces
   :salt "La gatta sul tetto che scotta"
   })



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

(defn encode
  "takes an intseq and encodes it with hashids"
  [o]
  (h/encode hashids-conf o))

(defn decode
  "takes an hash and decodes it with hashids"
  [o] (h/decode hashids-conf o))

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

(defn intseq2bigint
  "takes a sequence of integers and returns a big integer number"
  [s]
  (biginteger (apply str s)))

(defn int2str-append-pos
  "add a final cipher to each number in the shares collection which
  indicates its position, returns an array of strings"
  [shares]
  (loop [res []
         s (first shares)
         c 1]
    (if (< c (count shares))
      (recur (conj res (format "%d%d" s c))
             (nth shares c)
             (inc c))
      (conj res (format "%d%d" s c)))))

(defn str2int-trim-pos
  "remove the final cipher from each number in the collection of
  strings, returns an array of integers sorted by the removed cipher"
  [shares]
  (let [sorted (sort-by last shares)
        trimmed (map #(subs % 0 (dec (count %))) sorted)]
    (map biginteger trimmed)))
