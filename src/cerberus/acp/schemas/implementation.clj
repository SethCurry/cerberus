(ns cerberus.acp.schemas.implementation
  "Implementation metadata, authentication methods, and session mode schema
   definitions for the Agent Client Protocol (ACP)."
  (:require [malli.core :as malli]
            [cerberus.acp.schemas.primitives :refer [Meta SessionModeId]]))

;; ===========================================================================
;; IMPLEMENTATION
;; ===========================================================================

;; Metadata about the implementation of the client or agent.
(def Implementation
  [:map
   Meta
   [:name :string]
   [:title {:optional true} [:maybe :string]]
   [:version :string]])

;; ===========================================================================
;; AUTH METHODS
;; ===========================================================================

;; Agent handles authentication itself.
(def AuthMethodAgent
  [:map
   Meta
   [:description {:optional true} [:maybe :string]]
   [:id :string]
   [:name :string]])

;; Describes an available authentication method.
(def AuthMethod
  [:or AuthMethodAgent])

;; ===========================================================================
;; SESSION MODES
;; ===========================================================================

;; A mode the agent can operate in.
(def SessionMode
  [:map
   Meta
   [:description {:optional true} [:maybe :string]]
   [:id SessionModeId]
   [:name :string]])

;; The set of modes and the one currently active.
(def SessionModeState
  [:map
   Meta
   [:availableModes [:vector SessionMode]]
   [:currentModeId SessionModeId]])
