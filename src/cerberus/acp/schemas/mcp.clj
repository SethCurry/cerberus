(ns cerberus.acp.schemas.mcp
  "MCP (Model Context Protocol) server configuration schema definitions.
   Includes environment variables, HTTP headers, and transport configurations."
  (:require [malli.core :as malli]
            [cerberus.acp.schemas.primitives :refer [Meta]]))

;; ===========================================================================
;; ENV / HTTP HELPERS
;; ===========================================================================

;; An environment variable to set when launching an MCP server.
(def EnvVariable
  [:map
   Meta
   [:name :string]
   [:value :string]])

;; An HTTP header to set when making requests to the MCP server.
(def HttpHeader
  [:map
   Meta
   [:name :string]
   [:value :string]])

;; ===========================================================================
;; MCP SERVER DEFINITIONS
;; ===========================================================================

;; HTTP transport configuration for MCP.
(def McpServerHttp
  [:map
   Meta
   [:headers [:vector HttpHeader]]
   [:name :string]
   [:url :string]])

;; SSE transport configuration for MCP.
(def McpServerSse
  [:map
   Meta
   [:headers [:vector HttpHeader]]
   [:name :string]
   [:url :string]])

;; Stdio transport configuration for MCP.
(def McpServerStdio
  [:map
   Meta
   [:args [:vector :string]]
   [:command :string]
   [:env [:vector EnvVariable]]
   [:name :string]])

;; Configuration for connecting to an MCP (Model Context Protocol) server.
(def McpServer
  [:or
   [:map {:title "http"}
    [:type [:= "http"]]
    Meta
    [:headers [:vector HttpHeader]]
    [:name :string]
    [:url :string]]
   [:map {:title "sse"}
    [:type [:= "sse"]]
    Meta
    [:headers [:vector HttpHeader]]
    [:name :string]
    [:url :string]]
   McpServerStdio])

;; Backward compatibility alias.
(def McpServerDefinition McpServer)
