(defproject secrets "0.4.0-SNAPSHOT"
  :description "Simple Secret Sharing"
  :url "https://secrets.dyne.org"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.6.0"]
                 [ring/ring-defaults "0.3.0"]
                 [ring-middleware-accept "2.0.3"]
				 [ring/ring-core "1.6.1"]
				 [ring/ring-jetty-adapter "1.6.1"]
                 [json-html "0.4.3"]
                 [formidable "0.1.10"]
                 [markdown-clj "0.9.99"]
                 [org.clojars.dyne/fxc "0.4.0"]
                 [org.clojars.metal-slime/javafx2.2.0 "2.2.0"]]

  :jvm-opts ["-Djava.security.egd=file:/dev/random" ;use a proper random source
             "-XX:-OmitStackTraceInFastThrow" ; stacktrace JVM exceptions
             ]

  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler secrets.handler/app}
  :main secrets.handler
  :target-path "target/%s"
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]
                        [midje "1.8.3"]
                        [kerodon "0.8.0"]]
         :plugins [[lein-midje "3.1.3"]]
         :aot :all
         :main fxc.handler}

   :uberjar {:aot  :all
             :main secrets.app}})
