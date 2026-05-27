(ns cerberus.acp.schema
  (:require [malli.core :as malli]))

(defn stop-reason? [x]
  (if (some? #{x} '("end_turn"
    "max_tokens"
    "max_turn_requests"
    "refusal"
    "cancelled"))
  true
  false))

(def TextContent
  [:map
    [:type :string] ; always "text"
    [:text :string]])

(def InitializeRequest
  [:map
    [:jsonrpc :string]
    [:id :integer]
    [:method :string]
    [:params [:map
      [:protocolVersion :integer]
      [:clientCapabilities
        [:map
          [:terminal :boolean]
          [:fs [:map
            [:readTextFile :boolean]
            [:writeTextFile :boolean]]]]]
      [:clientInfo [:map
        [:name :string]
        [:title :string]
        [:version :string]]]]]])

(def InitializeResponse
  [:map
    [:jsonrpc :string]
    [:id :integer]
    [:result [:map
      [:protocolVersion :integer]
      [:agentCapabilities [:map
        [:loadSession :boolean]
        [:promptCapabilities [:map
          [:image :boolean]
          [:audio :boolean]
          [:embeddedContext :boolean]]]
        [:mcpCapabilities [:map
          [:http :boolean]
          [:sse :boolean]]]
        [:agentInfo [:map
          [:name :string]
          [:title :string]
          [:version :string]]]
        [:authMethods [:vector :string]]]]]]])


(def McpServerDefinition
  [:map
    [:name :string]
    [:command :string]
    [:args [:vector :string]]
    [:env [:vector [:map [:name :string]
                         [:value :string]]]]])


(def NewSessionRequest
  [:map
    [:jsonrpc :string]
    [:id :integer]
    [:method :string]
    [:params [:map
      [:cwd {:optional true} :string]
      [:mcpServers [:vector McpServerDefinition]]]]])

(def NewSessionResponse
  [:map
    [:jsonrpc :string]
    [:id :integer]
    [:result [:map
      [:sessionId :string]]]])

(def LoadSessionRequest
  [:map
    [:jsonrpc :string]
    [:id :integer]
    [:method :string]
    [:params [:map
      [:sessionId :string]
      [:cwd :string]
      [:mcpServers [:vector McpServerDefinition]]]]])

(def SessionUpdate
  [:map
    [:jsonrpc :string]
    [:method :string]
    [:params [:map
      [:sessionId :string]
      [:update [:map
        [:sessionUpdate :string]
        [:content :map]]]]]])

(def ListSessionsRequest
  [:map
    [:jsonrpc :string]
    [:id :integer]
    [:method :string]
    [:params [:map
      [:cwd :string]
      [:cursor {:optional true} :string]]]])

(def ListSessionsResponse
  [:map
    [:jsonrpc :string]
    [:id :integer]
    [:nextCursor :string]
    [:result [:map
      [:sessions [:vector
        [:map
          [:sessionId :string]
          [:cwd :string]
          [:title :string]
          [:updatedAt :string]
          [:_meta {:optional true} :map]]]]]]])

(def PromptRequest
  [:map
    [:jsonrpc :string]
    [:id :integer]
    [:method :string]
    [:params [:map
      [:sessionId :string]
      [:prompt [:vector [:map
        [:type :string]]]]]]])
