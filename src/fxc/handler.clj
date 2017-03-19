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

(ns fxc.handler
  (:require [clojure.java.io :as io]
            [compojure.core :refer :all]
            [compojure.handler :refer :all]
            [compojure.route :as route]
            [compojure.response :as response]
            [hiccup.page :as page]
            [hiccup.middleware :refer [wrap-base-url]]

            [fxc.core :refer :all]
            [fxc.webpage :as web]
            [fxc.config :refer :all]
            [fxc.form_helpers :as fh]

            [formidable.parse :as fp]
            [formidable.core :as fc]

            [markdown.core :as md]
            [hiccup.page :as page]
            [ring.middleware.session :refer :all]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))


(defn generate-form-spec [config]
  {:renderer :bootstrap3-stacked
   :fields [{:name :secret  :type :textarea :datatype :string
              }]
   :validations [[:required [:secret]]
                 [:max-length (:max config) :secret]]
   :action "/share"
   :method "post"})

(defn combine-form-spec [config]
  {:renderer :bootstrap3-stacked
   :fields [{:name :share_1  :type :text :datatype :string}
            {:name :share_2  :type :text :datatype :string}
            {:name :share_3  :type :text :datatype :string}]
   :validations [[:required [:share_1 :share_2 :share_3]]]
   :action "/combine"
   :method "post"})


(defn trunc
  [n s]
  (subs s 0 (min (count s) n)))

(defroutes app-routes

  (GET "/" request
       (conj {:session (web/check-session request)}
             (web/render
              (md/md-to-html-string
               (slurp (io/resource "public/static/README.md"))))))

  (GET "/about" request
       (conj {:session (web/check-session request)}
             (web/render
              (md/md-to-html-string
               (slurp (io/resource "public/static/README.md"))))))

  (GET "/share" request
       (let [config (web/check-session request)]
         (conj {:session config}
               (web/render
                [:div {:class "secrets row center"}
                 (str "Split a Secret to "
                      (:total config)
                      " shares with quorum "
                      (:quorum config))
                 [:div {:class "content input-form"}
                  (fc/render-form
                   (generate-form-spec config))]]))))


  (POST "/share" request
        (let [config (web/check-session request)
              params (web/check-params request generate-form-spec)]          
          (cond
            (not (contains? config :total))
              (web/render-error "Error: no Session.")
            (= (:status params) :error)
            (web/render-error config [:div [:h2 "form validation"]
                                      (:problems params)])
            :else 
            (if-let [input (:data params)]
              (cond  
                (not (contains? input :secret))
                (web/render-error config "Error: no Params.")
                (empty? (:secret input))
                (web/render-error config "No input.")
                :else
                ;; all checks passed
                
                (let [pass   (trunc (:max config) (:secret input))
                      shares (encode config pass)]
                  (conj {:session config}
                        (web/render
                         [:div {:class "results"}
                          [:h2 "Secret shared succesfully"]
                          [:div {:class "secrets row center"}
                           [:div {:class "slices"}
                            [:h3 (str "Split to " (:total config)
                                      " shared secrets, quorum "
                                      (:quorum config) ":")]
                            [:ul (map #(conj [:li {:class "content"}] %)
                                      shares)]]]]))))))))
  
  
  (GET "/combine" request
       (let [config (web/check-session request)]
         (conj {:session config}
               (web/render 
                [:div {:class "recovery row center"}
                 [:h2 "Combine a Shared Secret"]
                 [:div {:class "content input-form"}]
                (fc/render-form (combine-form-spec config))]))))

  (POST "/combine" request
        (let [config (web/check-session request)
              params (web/check-params request combine-form-spec)]          
          (cond
            (not (contains? config :total))
            (web/render-error "Error: no Session.")
            (= (:status params) :error)
            (web/render-error config [:div [:h2 "form validation"]
                                      (:problems params)])
            :else 
            (conj {:session config}
                  (web/render
                   [:div {:class "combined"}
                    [:h2 "Secret recovered:"]
                    (let [combined
                          (decode
                           config
                           (vals (:data params)))]
                      [:div {:class "password"}
                       "Your Secret: "
                       [:div {:class "content"} [:code combined ]
                        ]])])))))


  ;; TODO: detect cryptographical conversion error: returned is the first share
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  ((wrap-session
    (wrap-defaults app-routes site-defaults))))
