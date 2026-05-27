(ns cerberus.acp.core
  (:require [jsonista.core :as json]
    [cerberus.acp.schemas.api :as api]))

(defn read-input-message [buf]
  (let [got-value (json/read-value
    json/keyword-keys-object-mapper
    buf)]
  (t/log! {:level :debug
    :msg "read message from stding"
    :data {
      :message-in got-value
      }})
  got-value))

(defn write-output-message [msg]
  (json/write-value json/keyword-keys-object-mapper *out*))

(defn run-agent []
  (let [read-buf (java.io.BufferedReader. *in*)]
    (while true
      (let [got-message (read-input-message read-buf)
        message-schema (api/get-message-schema got-message)
        is-valid (malli/validate message-schema got-message)]
      (when (not is-valid)
        (throw (ex-info "got invalid message" {:validation is-valid :message got-message})))))))
