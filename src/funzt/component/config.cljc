(ns funzt.component.config
  (:require [com.stuartsierra.component :as component]))

(defn wrap-using
  "Return a component ctor that returns calls component/using on the
  return value of ctor with provided deps. "
  [ctor deps]
  (fn [m] (component/using (ctor m) deps)))

(defn setup
  "Return a component system with one component per key in key->ctor.
  To create the component, the ctor is called with a map

  {:config config}

  where config is the configuration found in key->config.

  The map argument is a convenience for Clojure's map->Record default
  constructor.  However, it can be made compatible with any component
  ctor by wrapping a fitting ctor around it or modifying it.

  Keys with the namespace \"setup\" are reserved options:

  :setup/disabled - Set of keys that will be ignored, i. e. even if
  they are in key->ctor"
  [key->ctor key->config]
  (component/map->SystemMap
   (into {}
         (comp (if-let [disabled-ks (:setup/disabled key->config)]
                 (remove (comp disabled-ks key))
                 identity)
               (map (fn [[k ctor]]
                      [k (ctor {:config (get key->config k)})])))
         key->ctor)))
