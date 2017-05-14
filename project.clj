(defproject fxc "0.1.0-SNAPSHOT"
  :description "FXC simple secret sharing library"
  :url "https://secrets.dyne.org"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.2"]
                 [ring/ring-defaults "0.2.3"]
                 [ring-middleware-accept "2.0.3"]
				 [ring/ring-core "1.6.0-RC3"]
				 [ring/ring-jetty-adapter "1.6.0-RC3"]
                 [json-html "0.4.0"]
                 [formidable "0.1.10"]
                 [markdown-clj "0.9.98"]
                 [org.clojars.dyne/fxc "0.3.0"]
                 [org.clojars.metal-slime/javafx2.2.0 "2.2.0"]]

  :jvm-opts ["-Djava.security.egd=file:/dev/random" ;use a proper random source
             "-XX:-OmitStackTraceInFastThrow" ; stacktrace JVM exceptions
             ]

  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler fxc.handler/app}
  :main fxc.handler
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
             :main fxc.app}})
