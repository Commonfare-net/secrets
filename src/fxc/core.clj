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
  (:require [fxc.marshalling :as ms]))

;; defaults
(def settings
  {:total (Integer. 5)
   :quorum (Integer. 3)
   :max 256
   :prime 'prime4096

   :description "FXC v1 (Simple Secret Sharing, Freecoin component)"

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


;; public functions
(defn encode
  "Takes a string and returns multiple strings that can be used to
  retrieve the original according to settings."
  [conf pass] {:pre [(string? pass)
                     (map? conf)]
               :post [(coll? %)
                      (string? (first %))
                      (= (count %) (:total conf))]}

  (map #(ms/encode-hash settings %) (->> pass
                                        ms/str2seq
                                        (ms/seq2secrets settings)
                                        (ms/secrets2slices settings)
                                        )))

(defn decode
  "Takes a collection of strings and returns the original secret
  according to the settings"
  [conf slices] {:pre [(coll? slices)
                       (map? conf)
                       (<= (count slices) (:total conf))]
                 :post [(string? %)]}

  (->> (map #(ms/decode-hash settings %) slices)
      (ms/slices2secrets settings)
      (ms/secrets2seq settings)
      ms/seq2str))
