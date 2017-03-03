(ns fxc.handler
  (:require [compojure.core :refer :all]
            [compojure.route :as route]
            [clojure.pprint :as pp]
            [fxc.secretshare :as ssss]
            [fxc.random :as rand]
            [fxc.fxc :as fxc]
            [json-html.core :as present]
            [hiccup.page :as page]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]))

;; defaults
(def settings
  {;; TODO: custom total/quorum
   :total 9
   :quorum 5

   :prime 'prime4096

   :description "FXC (Freecoin Secret Sharing)"

   ;; versioning every secret
   :protocol "FXC1"

   :type "WEB"

   ;; TODO: custom column width
   :columns 2
   ;; random number generator settings
   :length 8
   :entropy 3.1})

(defroutes app-routes

  (GET "/" []

  (let [prob {:ah (:integer (rand/create (:length settings)))
              :al (:integer (rand/create (:length settings)))}
        main   (fxc/render-slice  settings
                                  (:type settings) (:ah prob) (:al prob) 0)
        slices (fxc/create-secret settings
                                  (:type settings) (:ah prob) (:al prob))]
    
    (present/edn->html {:secret main
                        :slices slices})))

  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (wrap-defaults app-routes site-defaults))
