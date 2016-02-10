# funzt.component.config

A minimalist configuration framework for component systems of Stuart
Sierra's component lib.

This is mainly a design pattern/best practice how to do EDN based
configuration for component systems accompanyied by utility
functionality.

## Usage

Add the library `[org.funzt/component.config "0.1.0"]` to your project
dependencies.

Require the namespace `funzt.component.config`

Inside your source code, have a map with component constructors.  Each
constructor must accept a map `{:config <config>}` and return a
component.

```clj
(def key->ctor
  {:db map->Database
   :server (config/wrap-using map->Server [:db :global-config])
   :email-agent (config/wrap-using map->EmailAgent [:server :global-config])
   :global-config :config})
```

Wherever you store the config, e. g. in a file, have a EDN
configuration map.

```clj
(def config-map
  {:db {:connection-uri "datomic:mem://test-db"
        :schema :foo}
   :server {:port 8080}
   :global-config {:dev-mode? true}
   :setup/disabled #{:email-agent}})
```

Then simply create a component system using config/setup

```clj
(component/start-system (config/setup key->ctor config-map))
```

## License

Copyright © 2016 Leon Grapenthin & [funzt.jetzt](http://funzt.jetzt)
UG (haftungsbeschränkt)

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
