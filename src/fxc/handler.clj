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
  (:require [compojure.core :refer :all]
            [compojure.route :as route]

            [hiccup.page :as page]

            [fxc.core :refer :all]
            [fxc.webpage :as web]
            [fxc.form_helpers :as fh]

            [formidable.parse :as fp]
            [formidable.core :as fc]

            [json-html.core :as present]
            [hiccup.page :as page]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))


(def generate-form-spec
  {:renderer :bootstrap3-stacked
   :fields [{:name "Secret:"   :type :text}]
   :validations [[:required [:field :value]]]
   :action "/generate"
   :method "post"})

(def recovery-form-spec
  {:renderer :bootstrap3-stacked
   :fields [{:name "Share 1"  :type :integer}
            {:name "Share 2"  :type :text}
            {:name "Share 3"  :type :text}]
   :validations [[:required ["1" "2" "3"]]]

   :action "/recover"
   :method "post"})


(defroutes app-routes

  (GET "/" []
      (web/render-page
       {:section "Split a Secret into Shares"
        :body [:div {:class "secrets row center"}
               [:div {:class "content input-form"}
                (fc/render-form generate-form-spec)]]}))

  (POST "/g*" {params :params}
    (let [pass   (params "Secret:")
          shares (encode settings pass)]

      (web/render-page
       {:section "Split Secret"
        :body [:div {:class "secrets row center"}
               [:div {:class "password"}
                "Your Secret:" [:div {:class "content"} pass]]

               [:div {:class "slices"}
                [:h3 (str "Split in " (:total settings)
                          " shared secrets, quorum "
                          (:quorum settings) ":")]
                [:ul (map #(conj [:li {:class "content"}] %)
                          shares)]]]})))


  (GET "/r*" []
    (web/render-page {:section "Recover a Shared Secret"
                      :body [:div {:class "recovery row center"}
                         [:div {:class "content input-form"}
                          (fc/render-form recovery-form-spec)
                          ]]}))

  (POST "/r*" {params :params}
    (web/render-page {:section "Recover Secret"
                      :body (let [para (fp/parse-params recovery-form-spec params)
                              converted (decode (vals para))]
                          (present/edn->html converted))}))

  ;; TODO: detect cryptographical conversion error: returned is the first share

  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
