(ns cerberus.core
  (:require [taoensso.telemere :as t])
  (:gen-class))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (t/set-min-level! :debug)
  (println "Hello, World!"))
