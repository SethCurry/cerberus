(ns cerberus.acp.schemas.session
  "Session configuration and update schema definitions for the Agent Client Protocol (ACP).
   Includes session config options (select dropdowns) and streaming session updates
   (message chunks, tool calls, plans, commands, etc.)."
  (:require [malli.core :as malli]
            [cerberus.acp.schemas.primitives :refer [Meta SessionConfigId SessionConfigGroupId
                                                     SessionConfigValueId SessionConfigOptionCategory
                                                     SessionModeId ToolCallId ToolKind ToolCallStatus]]
            [cerberus.acp.schemas.content :refer [ContentBlock]]
            [cerberus.acp.schemas.tools :refer [ToolCallContent ToolCallLocation]]
            [cerberus.acp.schemas.plan :refer [PlanEntry]]
            [cerberus.acp.schemas.commands :refer [AvailableCommand]]))

;; ===========================================================================
;; SESSION CONFIG TYPES
;; ===========================================================================

;; A possible value for a session configuration option.
(def SessionConfigSelectOption
  [:map
   Meta
   [:description {:optional true} [:maybe :string]]
   [:name :string]
   [:value SessionConfigValueId]])

;; A group of possible values for a session configuration option.
(def SessionConfigSelectGroup
  [:map
   Meta
   [:group SessionConfigGroupId]
   [:name :string]
   [:options [:vector SessionConfigSelectOption]]])

;; Possible values for a session configuration option.
(def SessionConfigSelectOptions
  [:or
   [:vector SessionConfigSelectOption]
   [:vector SessionConfigSelectGroup]])

;; A single-value selector (dropdown) session configuration option payload.
(def SessionConfigSelect
  [:map
   [:currentValue SessionConfigValueId]
   [:options SessionConfigSelectOptions]])

;; A session configuration option selector and its current state.
(def SessionConfigOption
  [:map
   Meta
   [:category {:optional true} [:maybe SessionConfigOptionCategory]]
   [:description {:optional true} [:maybe :string]]
   [:id SessionConfigId]
   [:name :string]
   [:type {:optional true} [:= "select"]]
   [:currentValue {:optional true} SessionConfigValueId]
   [:options {:optional true} SessionConfigSelectOptions]])

;; ===========================================================================
;; SESSION UPDATE SUB-TYPES
;; ===========================================================================

;; The current mode of the session has changed.
(def CurrentModeUpdate
  [:map
   Meta
   [:currentModeId SessionModeId]])

;; Session configuration options have been updated.
(def ConfigOptionUpdate
  [:map
   Meta
   [:configOptions [:vector SessionConfigOption]]])

;; Update to session metadata. All fields are optional.
(def SessionInfoUpdate
  [:map
   Meta
   [:title {:optional true} [:maybe :string]]
   [:updatedAt {:optional true} [:maybe :string]]])

;; Different types of updates that can be sent during session processing.
(def SessionUpdate
  [:or
   [:map {:title "user_message_chunk"}
    [:sessionUpdate [:= "user_message_chunk"]]
    Meta
    [:content ContentBlock]]
   [:map {:title "agent_message_chunk"}
    [:sessionUpdate [:= "agent_message_chunk"]]
    Meta
    [:content ContentBlock]]
   [:map {:title "agent_thought_chunk"}
    [:sessionUpdate [:= "agent_thought_chunk"]]
    Meta
    [:content ContentBlock]]
   [:map {:title "tool_call"}
    [:sessionUpdate [:= "tool_call"]]
    Meta
    [:content {:optional true} [:vector ToolCallContent]]
    [:kind {:optional true} ToolKind]
    [:locations {:optional true} [:vector ToolCallLocation]]
    [:rawInput {:optional true} :any]
    [:rawOutput {:optional true} :any]
    [:status {:optional true} ToolCallStatus]
    [:title :string]
    [:toolCallId ToolCallId]]
   [:map {:title "tool_call_update"}
    [:sessionUpdate [:= "tool_call_update"]]
    Meta
    [:content {:optional true} [:maybe [:vector ToolCallContent]]]
    [:kind {:optional true} [:maybe ToolKind]]
    [:locations {:optional true} [:maybe [:vector ToolCallLocation]]]
    [:rawInput {:optional true} :any]
    [:rawOutput {:optional true} :any]
    [:status {:optional true} [:maybe ToolCallStatus]]
    [:title {:optional true} [:maybe :string]]
    [:toolCallId ToolCallId]]
   [:map {:title "plan"}
    [:sessionUpdate [:= "plan"]]
    Meta
    [:entries [:vector PlanEntry]]]
   [:map {:title "available_commands_update"}
    [:sessionUpdate [:= "available_commands_update"]]
    Meta
    [:availableCommands [:vector AvailableCommand]]]
   [:map {:title "current_mode_update"}
    [:sessionUpdate [:= "current_mode_update"]]
    Meta
    [:currentModeId SessionModeId]]
   [:map {:title "config_option_update"}
    [:sessionUpdate [:= "config_option_update"]]
    Meta
    [:configOptions [:vector SessionConfigOption]]]
   [:map {:title "session_info_update"}
    [:sessionUpdate [:= "session_info_update"]]
    Meta
    [:title {:optional true} [:maybe :string]]
    [:updatedAt {:optional true} [:maybe :string]]]])
