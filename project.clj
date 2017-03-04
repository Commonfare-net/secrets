(defproject fxc "0.1.0-SNAPSHOT"
  :description "FXC shared secret server"
  :url "http://fxc.io"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [compojure "1.5.1"]
                 [ring/ring-defaults "0.2.1"]
                 [org.clojure/math.numeric-tower "0.0.4"]
                 [com.tiemens/secretshare "1.4.2"]
                 [json-html "0.4.0"]
                 [formidable "0.1.10"]
                 ]

  :jvm-opts ["-Djava.security.egd=file:/dev/random" ;use a proper random source
             "-XX:-OmitStackTraceInFastThrow" ; stacktrace JVM exceptions
             ]

  :plugins [[lein-ring "0.9.7"]]
  :ring {:handler fxc.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.0"]]}})
