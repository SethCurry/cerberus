(ns cerberus.acp.schemas.primitives
  "Primitive scalar types and enums used across the Agent Client Protocol (ACP) schema."
  (:require [malli.core :as malli]))

;; ---------------------------------------------------------------------------
;; Helper predicates
;; ---------------------------------------------------------------------------

(def stop-reason?
  "Predicate: returns true if x is a valid StopReason value."
  (malli/validator [:enum "end_turn" "max_tokens" "max_turn_requests" "refusal" "cancelled"]))

;; ===========================================================================
;; PRIMITIVE / SIMPLE TYPES
;; ===========================================================================

;; Unique identifier for a permission option.
(def PermissionOptionId :string)

;; The type of permission option being presented to the user.
(def PermissionOptionKind
  [:enum "allow_once" "allow_always" "reject_once" "reject_always"])

;; Protocol version identifier.
(def ProtocolVersion :int)

;; JSON RPC Request Id.
(def RequestId [:or nil? :int :string])

;; The sender or recipient of messages and data in a conversation.
(def Role [:enum "assistant" "user"])

;; Unique identifier for a conversation session between a client and agent.
(def SessionId :string)

;; Unique identifier for a Session Mode.
(def SessionModeId :string)

;; Unique identifier for a session configuration option.
(def SessionConfigId :string)

;; Unique identifier for a session configuration option value group.
(def SessionConfigGroupId :string)

;; Unique identifier for a session configuration option value.
(def SessionConfigValueId :string)

;; Unique identifier for a tool call within a session.
(def ToolCallId :string)

;; Categories of tools that can be invoked.
(def ToolKind
  [:enum "read" "edit" "delete" "move" "search" "execute" "think" "fetch" "switch_mode" "other"])

;; Execution status of a tool call.
(def ToolCallStatus
  [:enum "pending" "in_progress" "completed" "failed"])

;; Priority levels for plan entries.
(def PlanEntryPriority
  [:enum "high" "medium" "low"])

;; Status of a plan entry in the execution flow.
(def PlanEntryStatus
  [:enum "pending" "in_progress" "completed"])

;; Reasons why an agent stops processing a prompt turn.
(def StopReason
  [:enum "end_turn" "max_tokens" "max_turn_requests" "refusal" "cancelled"])

;; Predefined error codes for common JSON-RPC and ACP-specific errors.
(def ErrorCode
  [:or
   [:enum -32700]
   [:enum -32600]
   [:enum -32601]
   [:enum -32602]
   [:enum -32603]
   [:enum -32000]
   [:enum -32002]
   :int])

;; Semantic category for a session configuration option.
(def SessionConfigOptionCategory
  [:or
   [:enum "mode"]
   [:enum "model"]
   [:enum "thought_level"]
   :string])

(def Meta [:_meta {:optional true} [:maybe :map]])
