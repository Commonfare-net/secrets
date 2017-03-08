;; Freecoin - digital social currency toolkit

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


(ns fxc.fxc
  (:require [fxc.secretshare :as ssss]
            [fxc.random :as rand]
            [fxc.utils :as util]
            [clojure.string :as str]))

(declare render-slice)
(declare extract-quorum)
(declare extract-pin)


(defn create-secret
  "Takes a configuration, the blockchain type and optionally two integers, creates a wallet address, returned as a string"
  ([conf type]
   (let [pin (:integer (rand/create (:length conf)))]
     (create-secret conf type pin)))

  ([conf type pin]
   {:pre  [(contains? conf :protocol)
           (contains? conf :length)
           (contains? conf :entropy)]
    :post [(= (:total conf) (count (:slices %)))]}

   (let [split (ssss/shamir-split conf pin)]
         
     {:config (assoc conf :type type)

      :slices (loop [share (first (:shares split))
                     res []
                     c 1]
                (if (< c (count (:shares split)))
                  (recur (nth (:shares split) c)
                         (merge res (render-slice conf type share c))
                         (inc c))
                  (merge res (render-slice conf type share c))))})))

(defn unlock-secret
  "Takes shares, returns the pin"
  [conf secret slice]
  {:pre [(contains? conf :quorum)
         (contains? secret :slices)]}
  (let [quorum (extract-quorum conf secret slice)
        pin (ssss/shamir-combine (:ah quorum))]
    (render-slice conf pin 0)))

(defn extract-quorum
  "Takes a config, a secret and a slice and tries to combine the secret and the slice in a collection of integers ready for shamir-combine. Returns a map {:ah :al} with the collections."
  [conf secret slice]
  {:pre  [(contains? conf :quorum)
          (contains? secret :slices)
          (seq slice)]}
  ;; TODO: fix some off-by-one problem here (assert fails with +1)
  ;; :post [(= (count (get-in % (:ah :shares))) (:quorum conf))]}

  (let [ordered (sort-by last (:slices secret))]
  ;; reconstruct a collection of slices
  (loop [num [(extract-pin conf slice)]
         c 1]

    (if (< (count num) (:quorum conf))

      (recur
       (conj num (extract-pin conf (nth ordered c)))
       (inc c))

      ;; return
      {:header {:_id (:_id secret)
                :quorum (int (:quorum conf))
                :total (int (:total conf))
                :prime (:prime conf)
                :description (:description conf)}
       :shares (map biginteger
                    (conj num 
                          (extract-pin conf (nth (:slices secret) c))))}))))

(defn render-slice [conf type share idx]
   (format "%s_%s_%s_%d" (:protocol conf) type share idx))

(defn extract-pin
  "Extract the numeric part in an fxc string."
  [conf addr]
  (try
    (let [toks (str/split (util/trunc addr 128) #"_")]
      (if-not (= (subs (first toks) 0 4) (:protocol conf))
        (throw (Exception.
                (format "Invalid FXC address: %s" (first toks))))
        (nth toks 2) ; second position
        ;; TODO: check that only numeric characters are present
        ))

    (catch Exception e
      (let [error (.getMessage e)]
        (util/log! 'ERR 'fxc/extract-pin error)))

    (finally)))

(defn extract-share
  "Extract the numeric part in a fxc share, last number is the position"
  [conf addr]
  (try
    (let [toks (str/split (util/trunc addr 128) #"_")]
      (if-not (= (subs (first toks) 0 4) (:protocol conf))
        (throw (Exception.
                (format "Invalid FXC address: %s" (first toks))))
        (str (nth toks 2) (nth toks 3))
        ;; TODO: check that only numeric characters are present
        ))

    (catch Exception e
      (let [error (.getMessage e)]
        (util/log! 'ERR 'fxc/extract-pin error)))

    (finally)))
