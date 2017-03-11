;; Secrets

;; part of Decentralized Citizen Engagement Technologies (D-CENT)
;; R&D funded by the European Commission (FP7/CAPS 610349)

;; Based on Secret Share Java implementation by Tim Tiemens
;; Copyright (C) 2015-2017 Denis Roio <jaromil@dyne.org>

;; Shamir's Secret Sharing algorithm was invented by Adi Shamir
;; Shamir, Adi (1979), "How to share a secret", Communications of the ACM 22 (11): 612–613
;; Knuth, D. E. (1997), The Art of Computer Programming, II: Seminumerical Algorithms: 505

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

(ns fxc.secretshare
  (:gen-class)
  (:import [com.tiemens.secretshare.engine SecretShare]))

(defn prime384 []
  (SecretShare/getPrimeUsedFor384bitSecretPayload))

(defn prime192 []
  (SecretShare/getPrimeUsedFor192bitSecretPayload))

(defn prime4096 []
  (SecretShare/getPrimeUsedFor4096bigSecretPayload))

(defn get-prime [sym]
  (ns-resolve *ns* (symbol (str "fxc.secretshare/" sym))))


(defn shamir-set-header
  "Takes an header and sets it into Tiemen's structure"
  [head]
  (SecretShare.
   (com.tiemens.secretshare.engine.SecretShare$PublicInfo.
    (int (:total head))
    (:quorum head)
    ((get-prime (:prime head)))
    (:description head))))

(defn shamir-get-header
  "Takes Tiemen's share and extracts a header"
  [share]
  (let [pi (.getPublicInfo share)]
    {:_id (.getUuid pi)
     :quorum (.getK pi)
     :total (.getN pi)
     :prime (condp = (.getPrimeModulus pi)
              (prime192) 'prime192
              (prime384) 'prime384
              (prime4096) 'prime4096
              (str "UNKNOWN"))

     :description (.getDescription pi)}))

(defn shamir-get-shares
  "Takes Tiemen's share and extract a collection of shares"
  [si]
  (map (fn [_] (.getShare _)) si))

(defn map-shares
  "Takes a raw array of shares (big integers) and returns a
  bidimensional array with numbered keys for positions"
  [shares]
  (loop [[s & share] shares
         res []
         c 1]
    (let [res (conj res [c s])]
      (if (empty? share) res
          (recur share res (inc c))))))

(defn shamir-split
  "split an integer into shares according to conf
  return a structure { :header { :quorum :total :prime :description}
                       :shares [ [ pos share ] [ pos share] ... ]"
  [conf secnum]
  (let [si (.getShareInfos (.split (shamir-set-header conf) secnum))
        header (shamir-get-header (first si))
        shares (shamir-get-shares si)]

    (map-shares shares)))

(defn shamir-load
  "Loads a new share into the Shamir's engine, internal use in combine"
  [conf shares pos new]
  (conj shares
        (com.tiemens.secretshare.engine.SecretShare$ShareInfo.
         pos new
         (com.tiemens.secretshare.engine.SecretShare$PublicInfo.
          (:total conf)
          (:quorum conf)
          (SecretShare/getPrimeUsedFor4096bigSecretPayload)
          (:description conf)))))

(defn shamir-combine
  "Takes a secret (header and collection of integers) and returns the
  unlocked big integer. The collection must be ordered and have a nil
  in place for each missing share."
  [conf shares]
  {:pre [(coll? shares)
         (>= (count  shares)
             (:quorum conf))]

   :post [(integer? %)]}

  (loop [[i & slices] shares
         res []]
    (let [pos (biginteger (first i))
          sh  (biginteger (second i))]
      
      (if (empty? slices)
        (.getSecret
         (.combine (shamir-set-header conf)
                   (if-not (nil? sh)
                     (shamir-load conf res pos sh) res)))
        (recur slices
               (if-not (nil? sh)
                 (shamir-load conf res pos sh) res))))))

