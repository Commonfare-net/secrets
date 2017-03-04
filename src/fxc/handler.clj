(ns fxc.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]

            [hiccup.page :as page]
            [fxc.form_helpers :as fh]
            [formidable.parse :as fp]
            [formidable.core :as fc]

            [fxc.secretshare :as ssss]
            [fxc.random :as rand]
            [fxc.fxc :as fxc]
            [json-html.core :as present]
            [hiccup.page :as page]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

;; defaults
(def settings
  {;; TODO: custom total/quorum
   :total 5
   :quorum 3

   :prime 'prime4096

   :description "FXC v1 (Freecoin Secret Sharing)"

   ;; versioning every secret
   :protocol "FXC1"

   :type "WEB"

   ;; TODO: custom column width
   :columns 2
   ;; random number generator settings
   :length 6
   :entropy 3.1})

(def recovery-form-spec
  {:renderer :bootstrap3-stacked
   :fields [{:name "1st: "  :type :text}
            {:name "2nd: "  :type :text}
            {:name "3rd: "  :type :text}]
   :validations [[:required [:field :value]]]
   :action "/recover"
   :method "post"})


(defn render-page [{:keys [title body] :as content}]
  (page/html5
   [:head [:meta {:charset "utf-8"}]
    [:meta {:http-equiv "X-UA-Compatible" :content "IE=edge,chrome=1"}]
    [:meta 
     {:name "viewport"
      :content "width=device-width, initial-scale=1, maximum-scale=1"}]
    [:title title]
    (page/include-css "/static/css/bootstrap.min.css")
    (page/include-css "/static/css/bootstrap-theme.min.css")
    (page/include-css "/static/css/freecoin.css")]
   [:body {:class "fxc"}
    [:div {:class "container"}
     [:h1 title]
     body]]))

(defroutes app-routes

  (GET "/" []

  (let [pin (:integer (rand/create (:length settings)))
        main   (fxc/render-slice  settings (:type settings) pin 0)
        slices (fxc/create-secret settings (:type settings) pin)]

    (render-page
     {:title "FXC.dyne.org - Shared PIN Generator"
      :body [:div {:class "secrets row center"}
             [:div {:class "password"}
              "Your PIN:" [:div {:class "content"}
                      (fxc/extract-pin settings main)]]
             [:div {:class "slices"}
              [:h3 (str "Split in " (:total settings)
                        " shared secrets, quorum "
                        (:quorum settings) ":")]
              [:ul
               (map #(conj [:li {:class "content"}] %)
                    (map #(fxc/extract-share settings %)
                    (:slices slices)))]]]})))
      
  (GET "/r*" []
       (render-page {:title "Shared PIN Recovery"
                     :body [:div {:class "recovery row center"}
                            [:div {:class "input-form"}
                            (fc/render-form recovery-form-spec)
                            ]]}))

  (POST "/r*" []
        (render-page {:title "Shared PIN Recovery"
                      :body "WIP"}))

  (route/resources "/"
                   "/r*")

  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
