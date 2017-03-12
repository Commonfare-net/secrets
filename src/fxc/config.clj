;; FXC - Secret Sharing

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

(ns fxc.config
  (:require [clojure.java.io :as io]
            [cheshire.core :refer :all]))

(declare config-read)

(defn config-read
  "read configurations from standard locations, overriding defaults or
  system-wide with user specific paths."
  ([] (config-read {}))
  ([default]
   (let [home (System/getenv "HOME")
         pwd  (System/getenv "PWD")]
     (loop [[p & paths] ["/etc/secrets/config.json" 
                         (str home "/.secrets/config.json")
                         (str pwd "config.json")]
            res default ]
       (let [res (merge res
                        (if (.exists (io/as-file p))
                          (parse-stream (io/reader p) true)))]
         (if (empty? paths) res
             (recur paths res)))))))

(defn config-write
  "write configurations to file"
  [conf file]
  (generate-stream conf (io/writer file)
                   {:pretty true}))

