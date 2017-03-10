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

   ;; random number generator settings
   :length 6
   :entropy 3.1})

(defn str2seq
  "Takes a string and returns a unique collection of big unsigned
  integers. First integer is the length of the original string."
  [s]
  (ms/int2unsigned (ic/compress (ms/str2intseq s))))

(defn seq2str
  "Takes a collection of big unsigned integers and returns a string."
  [s]
  (ms/intseq2str (ic/decompress (ms/unsigned2int s))))

(defn seq2shares
  "Takes a sequence and computes clear shares of it"
  [s]
  (loop [[i & slices] (drop 1 s)
         res []]
    (let [res (conj res (ss/shamir-split settings (biginteger i)))]
      (if (empty? slices)
        {:length (first s)
         :secrets res}
        (recur slices res)))))

(defn shares2seq
  "Takes clear shares and returns a sequence"
  [s]
  (loop [[i & slices] (:secrets s)
         res []]
    (let [res (conj res (ss/shamir-combine i))]
      (if (empty? slices) 
        (cons (:length s) res)
        (recur slices res)))))

