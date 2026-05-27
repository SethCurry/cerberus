(ns cerberus.acp.core
  (:require [jsonista.core :as json]))

(defn read-input-message []
  (let [got-value (json/read-value
    json/keyword-keys-object-mapper
    (java.io.BufferedReader. *in*))]
  (t/log! {:level :debug
    :msg "read message from stding"
    :data {
      :message-in got-value
      }})
  got-value))

(defn write-output-message [msg]
  (json/write-value json/keyword-keys-object-mapper *out*))
