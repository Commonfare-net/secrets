;; Secrets

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

(ns secrets.webpage
  (:require [secrets.form_helpers :as fh]
            [secrets.config :refer :all]
            [fxc.core :as fxc]
            [hiccup.page :as page]
            [json-html.core :as present]))

(declare render)
(declare render-page)
(declare render-head)
(declare render-navbar)
(declare render-footer)


(declare render-error)
(declare render-static)

(defn show-config [session]
  (present/edn->html (dissoc session
                             :salt :prime :length :entropy
                             :type "__anti-forgery-token")))



(defn check-input [session params form-spec]
  (if (nil? (:total session)) (render-error "Error: no Session.")
      (if (nil? (:secret params)) (render-error session "Error: no Params.")
          (let [input (fh/validate-form
                       (form-spec session) params)]
            (if (= (:status input) :error)
              (render-error session [:div [:h2 "form validation"]
                                     (:problems input)])
              (if (empty? (get-in input [:data :secret]))
                (render-error session "No input.")
                {:config session
                 :params (:data input)}))))))

(defn check-session [request]
  (let [session (:session request)]
    (cond
     (not (contains? session :config)) (conj session (config-read))
     (string?  (:config session)) session
     (false? (:config session)) fxc/settings)))


(defn check-params [request form-spec]
  (fh/validate-form
   (form-spec (:session request))
   (:params request)))

(defn render [body]
  {:headers {"Content-Type"
             "text/html; charset=utf-8"}
   :body (page/html5
          (render-head)
          [:body {:class "fxc static"}
           (render-navbar)

           [:div {:class "container"}
           ;;  [:img {:src "/static/img/secret_ladies.jpg"
           ;;         :class "pull-right img-responsive"
           ;;         :style "width: 16em; border:1px solid #010a40"}]
           ;;  [:h1 "Simple Secret Sharing Service" ] body]
            body]

           (render-footer)])})


(defn render-error
  ([]    (render-error {} "Unknown"))
  ([err] (render-error {} err))
  ([session error]
   {:headers {"Content-Type"
              "text/html; charset=utf-8"}
    :session session
    :body (page/html5
           (render-head)
           [:body {:class "fxc static"}
            (render-navbar)
            [:div {:class "container"}
             [:div {:class "error"}
              [:h1 "Error:"] [:h2 (drop 1 error)]]
             [:div {:class "config"}
              (show-config session)]]])}))

;; helper to switch including different html sections depending from run-mode
;; the atom is defined in config and changed at app start
(defn ^:private mode
 ([m res] (if (= @run-mode m) res))
 ([m res relse] (if (= @run-mode m) res relse)))

(defn render-head
  ([] (render-head
       "Simple Secret Sharing" ;; default title
       "Decentralised Social Management of Secrets" ;; default desc
       (mode :web
             "https://secrets.dyne.org" ;; url for web
			 "http://localhost:8080"))) ;; url for desk

  ([title desc url]
   [:head [:meta {:charset "utf-8"}]
    [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
    [:meta
     {:name "viewport"
      :content "width=device-width, initial-scale=1, maximum-scale=1"}]

    ;; social stuff
    [:meta {:name "description"  :content desc}]
    [:meta {:property "og:title" :content title}]
    [:meta {:property "og:description" :content desc}]
    [:meta {:property "og:type" :content "website"}]
    [:meta {:property "og:url" :content url}]
    [:meta {:property "og:image" :content (str url "/static/img/secret_ladies.jpg")}]

    [:meta {:name "twitter:card" :content "summary"}]
    [:meta {:name "twitter:site" :content "@DyneOrg"}]
    [:meta {:name "twitter:title" :content title}]
    [:meta {:name "twitter:description" :content desc}]
    [:meta {:name "twitter:image" :content (str url "/static/img/secret_ladies.jpg")}]

    [:title title]
    (page/include-css "/static/css/bootstrap.min.css")
    (page/include-css "/static/css/bootstrap-theme.min.css")
    (mode :web (page/include-css "/static/css/gh-fork-ribbon.css"))
    (mode :web (page/include-css "/static/css/webapp.css"))]))

(defn render-navbar []
 [:nav {:class "navbar navbar-default navbar-static-top"}

  (mode :web
		[:div {:class "github-fork-ribbon-wrapper right"}
		 [:div {:class "github-fork-ribbon"}
		  [:a {:href "https://github.com/PIENews/secrets"} "Fork me!   :^)"]]])

   [:div {:class "container"}
    [:ul {:class "nav navbar-nav"}
     [:li [:a {:href "/about"} "About Secrets"]]
     [:li {:role "separator" :class "divider"}]
     [:li [:a {:href "/share"} "Share Secrets"
           [:span {:class "sr-only"}"(current)"]]]
     [:li [:a {:href "/combine"} "Combine Secrets"]]
     [:li {:role "separator" :class "divider"}]]]])


(defn render-footer []
  [:footer {:class "row" :style "margin: 20px"}
   [:hr]
   [:div {:class "footer col-sm-2"
          :style "float: left"}
    [:img {:src "/static/img/ec_logo.png"
           :alt "R&D funded by the European Commission"
           :title "The research and development on Secrets as Free and Open Source Software has been funded by the European Commission."}]]

   [:div {:class "footer col-sm-2"
          :style "float: right"}
    [:a {:href "https://www.dyne.org"}
     [:img {:src "/static/img/swbydyne.png"
            :alt   "Software by Dyne.org"
            :title "Software by Dyne.org"}]]]

   [:div {:class "footer col-sm-2"
          :style "float: none; margin: 0 auto; clear: none"}
    [:img {:src "static/img/AGPLv3.png"
           :alt "Affero GPLv3 License"
           :title "Affero GPLv3 License"}]]

])



(defn render-static [body]
  (page/html5 (render-head)
              [:body {:class "fxc static"}

               (render-navbar)

               [:div {:class "container"} body]

               (render-footer)]))



(defn render-page [{:keys [section body] :as content}]
  (let [title "Simple Secret Sharing Service"
        desc "Decentralised Social Management of Secrets"
        url "https://secrets.dyne.org"]

    (page/html5

     (render-head)

     (render-navbar)

     [:div {:class "container-fluid"}
      [:img {:src "/static/img/secret_ladies.jpg" :class "pull-right img-responsive" :style "width: 16em; border:1px solid #010a40"}]
      [:h1 "Simple Secret Sharing Service"]
      [:h2 "Decentralised Social Management of Secrets"]
      [:h3 section]
      body]

     (render-footer))))
