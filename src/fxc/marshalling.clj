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
  (:require [clojure.string :as str]))

(defn parse-int [s]
  (Integer. (re-find  #"\d+" s )))


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
