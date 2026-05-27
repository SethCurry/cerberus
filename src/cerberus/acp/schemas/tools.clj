(ns cerberus.acp.schemas.tools
  "Tool call schema definitions for the Agent Client Protocol (ACP).
   Models tool invocations, their content output, and lifecycle updates."
  (:require [malli.core :as malli]
            [cerberus.acp.schemas.primitives :refer [Meta ToolCallId ToolKind ToolCallStatus]]
            [cerberus.acp.schemas.content :refer [ContentBlock]]))

;; A diff representing file modifications.
(def Diff
  [:map
   Meta
   [:newText :string]
   [:oldText {:optional true} [:maybe :string]]
   [:path :string]])

;; Embed a terminal created with terminal/create by its id.
(def Terminal
  [:map
   Meta
   [:terminalId :string]])

;; Content produced by a tool call.
(def ToolCallContent
  [:or
   [:map {:title "content"}
    [:type [:= "content"]]
    Meta
    [:content ContentBlock]]
   [:map {:title "diff"}
    [:type [:= "diff"]]
    Meta
    [:newText :string]
    [:oldText {:optional true} [:maybe :string]]
    [:path :string]]
   [:map {:title "terminal"}
    [:type [:= "terminal"]]
    Meta
    [:terminalId :string]]])

;; A file location being accessed or modified by a tool.
(def ToolCallLocation
  [:map
   Meta
   [:line {:optional true} [:maybe :int]]
   [:path :string]])

;; Represents a tool call that the language model has requested.
(def ToolCall
  [:map
   Meta
   [:content {:optional true} [:vector ToolCallContent]]
   [:kind {:optional true} ToolKind]
   [:locations {:optional true} [:vector ToolCallLocation]]
   [:rawInput {:optional true} :any]
   [:rawOutput {:optional true} :any]
   [:status {:optional true} ToolCallStatus]
   [:title :string]
   [:toolCallId ToolCallId]])

;; An update to an existing tool call.
(def ToolCallUpdate
  [:map
   Meta
   [:content {:optional true} [:maybe [:vector ToolCallContent]]]
   [:kind {:optional true} [:maybe ToolKind]]
   [:locations {:optional true} [:maybe [:vector ToolCallLocation]]]
   [:rawInput {:optional true} :any]
   [:rawOutput {:optional true} :any]
   [:status {:optional true} [:maybe ToolCallStatus]]
   [:title {:optional true} [:maybe :string]]
   [:toolCallId ToolCallId]])
