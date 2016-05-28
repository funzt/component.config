(ns funzt.component.config.spec
  (:require [funzt.component.config :as config]
            [com.stuartsierra.component :as component]
            [clojure.spec :as s]
            [clojure.spec.gen :as gen]))

(s/def ::config/component-key
  (s/with-gen keyword?
    (fn []
      (s/gen
       #{:sample-component-1
         :sample-component-2
         :sample-component-3}))))

(s/def :setup/disabled
  (s/and (s/coll-of ::component-key #{})
         set?))

(s/def ::config/config
  (s/with-gen map?
    (fn []
      (gen/elements [{:port 8080}]))))

(s/def ::config/ctor
  (s/or :via-kw #{:config} ;; Keyword is not fn?, so this doesn't hold
                           ;; unless we specify this exlicitly
        :ctor-fn
        (s/fspec :args (s/cat :ctor-map (s/keys :req-un [::config/config]))
                 :ret #(satisfies? component/Lifecycle %)
                 :gen (fn []
                        (gen/return
                         (fn [config]
                           (reify
                             component/Lifecycle
                             (start [this]
                               (println "Component configured with"
                                        config "started."))
                             (stop [this]
                               (println "Component configured with"
                                        config "stopped.")))))))))

(s/def ::config/configs (s/keys :opt [:setup/disabled]))

(s/def ::config/ctors (s/map-of ::config/component-key ::ctor))

(s/fdef config/wrap-using
        :args (s/cat :ctor ::ctor
                     :deps (s/or :deps-map (s/map-of keyword? keyword?)
                                 :deps-vec (s/and (s/coll-of keyword? [])
                                                  vector?)))
        :ret ::ctor)

(s/fdef config/setup
        :args (s/cat :key->ctor ::config/ctors
                     :key->config ::config/configs)
        :ret #(satisfies? component/Lifecycle %))
