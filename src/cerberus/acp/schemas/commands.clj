(ns cerberus.acp.schemas.commands
  "Command schema definitions for the Agent Client Protocol (ACP).
   Commands represent available slash-commands that agents can suggest to clients."
  (:require [malli.core :as malli]
            [cerberus.acp.schemas.primitives :refer [Meta]]))

;; All text that was typed after the command name is provided as input.
(def UnstructuredCommandInput
  [:map
   Meta
   [:hint :string]])

;; The input specification for a command.
(def AvailableCommandInput
  [:or
   [:map {:title "unstructured"}
    Meta
    [:hint :string]]])

;; Information about a command.
(def AvailableCommand
  [:map
   Meta
   [:description :string]
   [:input {:optional true} [:maybe AvailableCommandInput]]
   [:name :string]])

;; Available commands are ready or have changed.
(def AvailableCommandsUpdate
  [:map
   Meta
   [:availableCommands [:vector AvailableCommand]]])
