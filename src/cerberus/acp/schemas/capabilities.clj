(ns cerberus.acp.schemas.capabilities
  "Capability schema definitions for the Agent Client Protocol (ACP).
   Describes optional features that clients and agents may support."
  (:require [malli.core :as malli]
            [cerberus.acp.schemas.primitives :refer [Meta]]))

;; File system capabilities that a client may support.
(def FileSystemCapabilities
  [:map
   Meta
   [:readTextFile {:optional true} :boolean]
   [:writeTextFile {:optional true} :boolean]])

;; Capabilities supported by the client.
(def ClientCapabilities
  [:map
   Meta
   [:fs {:optional true} FileSystemCapabilities]
   [:terminal {:optional true} :boolean]])

;; MCP capabilities supported by the agent.
(def McpCapabilities
  [:map
   Meta
   [:http {:optional true} :boolean]
   [:sse {:optional true} :boolean]])

;; Prompt capabilities supported by the agent.
(def PromptCapabilities
  [:map
   Meta
   [:audio {:optional true} :boolean]
   [:embeddedContext {:optional true} :boolean]
   [:image {:optional true} :boolean]])

;; Logout capabilities supported by the agent.
(def LogoutCapabilities
  [:map
   Meta])

;; Authentication-related capabilities supported by the agent.
(def AgentAuthCapabilities
  [:map
   Meta
   [:logout {:optional true} [:maybe LogoutCapabilities]]])

;; Session close capabilities.
(def SessionCloseCapabilities
  [:map
   Meta])

;; Session list capabilities.
(def SessionListCapabilities
  [:map
   Meta])

;; Session resume capabilities.
(def SessionResumeCapabilities
  [:map
   Meta])

;; Session capabilities supported by the agent.
(def SessionCapabilities
  [:map
   Meta
   [:close {:optional true} [:maybe SessionCloseCapabilities]]
   [:list {:optional true} [:maybe SessionListCapabilities]]
   [:resume {:optional true} [:maybe SessionResumeCapabilities]]])

;; Capabilities supported by the agent.
(def AgentCapabilities
  [:map
   Meta
   [:auth {:optional true} AgentAuthCapabilities]
   [:loadSession {:optional true} :boolean]
   [:mcpCapabilities {:optional true} McpCapabilities]
   [:promptCapabilities {:optional true} PromptCapabilities]
   [:sessionCapabilities {:optional true} SessionCapabilities]])
