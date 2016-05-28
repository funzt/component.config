(defproject org.funzt/component.config "0.1.2-SNAPSHOT"
  :description "Configuration framework for component systems"
  :url "http://github.com/funzt/component.config"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0-alpha3"]
                 [com.stuartsierra/component "0.3.1"]]
  :profiles {:dev {:dependencies [[org.clojure/test.check "0.9.0"]]}})
