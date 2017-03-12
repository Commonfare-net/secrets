;; FXC - PIN Secret Sharing

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

(ns fxc.webpage
  (:require [hiccup.page :as page]))

(declare render-page)

(defn render-page [{:keys [section body] :as content}]
  (let [title "Simple Secret Sharing Service"
        desc "Social and decentralised management of passwords, free software by Dyne.org"
        url "https://secrets.dyne.org"]

    (page/html5
     [:head [:meta {:charset "utf-8"}]
      [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
      [:meta
       {:name "viewport"
        :content "width=device-width, initial-scale=1, maximum-scale=1"}]

      ;; social stuff
      [:meta {:name "description"  :content desc }]
      [:meta {:property "og:title" :content title }]
      [:meta {:property "og:description" :content desc }]
      [:meta {:property "og:type" :content "website" }]
      [:meta {:property "og:url" :content url }]
      [:meta {:property "og:image" :content (str url "/static/img/secret_ladies.jpg") }]

      [:meta {:name "twitter:card" :content "summary"}]
      [:meta {:name "twitter:site" :content "@DyneOrg"}]
      [:meta {:name "twitter:title" :content title }]
      [:meta {:name "twitter:description" :content desc }]
      [:meta {:name "twitter:image" :content (str url "/static/img/secret_ladies.jpg") }]

      [:title title]
      (page/include-css "/static/css/bootstrap.min.css")
      (page/include-css "/static/css/bootstrap-theme.min.css")
      (page/include-css "/static/css/gh-fork-ribbon.css")
      (page/include-css "/static/css/json-html.css")
      (page/include-css "/static/css/freecoin.css")]
     [:body {:class "fxc"}
      [:div {:class "github-fork-ribbon-wrapper right"}
       [:div {:class "github-fork-ribbon"}
        [:a {:href "https://github.com/PIENews/secrets"} "Fork me!   :^)"]]]

      [:nav {:class "navbar navbar-default navbar-static-top"}
       [:div {:class "container"}
        [:ul {:class "nav navbar-nav"}
         [:li [:a {:href "https://github.com/PIENews/secrets"} "About Secrets"]]
         [:li {:role "separator" :class "divider"} ]
         [:li [:a {:href "/"} "Split PIN" [:span {:class "sr-only"}"(current)"]]]
         [:li [:a {:href "/recover"} "Recover PIN" ]]
         [:li {:role "separator" :class "divider"} ]
         ]]]

      [:div {:class "container"}
       [:img {:src "/static/img/secret_ladies.jpg" :class "pull-right img-responsive" :style "width: 16em; border:1px solid #010a40"}]
       [:h1 "Simple Secret Sharing Service" ]
       [:h2 "Social and decentralised management of passwords"]
       [:h3 section]
       body

       [:footer {:style "margin-top: 3em"}
        [:hr]
        [:div {:class "pull-left footer"}
         [:a {:href "https://www.dyne.org"} [:img {:src "/static/img/software_by_dyne.png"
                                                   :alt   "Software by Dyne.org"
                                                   :title "Software by Dyne.org"}]]]
        [:div {:class "pull-right footer"}
         [:img {:src "static/img/AGPLv3.png" :style "margin-top: 3em"
                :alt "Affero GPLv3 License" :title "Affero GPLv3 License"} ]]
        ]]])))
