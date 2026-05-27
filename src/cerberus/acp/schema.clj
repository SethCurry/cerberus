(ns cerberus.acp.schema
  "Malli schema definitions for the Agent Client Protocol (ACP).

  These schemas are derived from the ACP JSON Schema at doc/acp-schema.json.
  Each `def` corresponds to a `$defs` entry in the JSON schema."
  (:require [malli.core :as malli]))

;; ---------------------------------------------------------------------------
;; Helper predicates
;; ---------------------------------------------------------------------------

(def stop-reason?
  "Predicate: returns true if x is a valid StopReason value."
  (malli/validator [:enum "end_turn" "max_tokens" "max_turn_requests" "refusal" "cancelled"]))

;; ===========================================================================
;; 1. PRIMITIVE / SIMPLE TYPES (no dependencies)
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

;; ===========================================================================
;; 2. ANNOTATIONS (depends on Role)
;; ===========================================================================

;; Optional annotations for the client.
(def Annotations
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:audience {:optional true} [:maybe [:vector Role]]]
   [:lastModified {:optional true} [:maybe :string]]
   [:priority {:optional true} [:maybe :double]]])

;; ===========================================================================
;; 3. CONTENT TYPES (depends on Annotations, primitives)
;; ===========================================================================

;; Text provided to or from an LLM.
(def TextContent
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:annotations {:optional true} [:maybe Annotations]]
   [:text :string]])

;; An image provided to or from an LLM.
(def ImageContent
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:annotations {:optional true} [:maybe Annotations]]
   [:data :string]
   [:mimeType :string]
   [:uri {:optional true} [:maybe :string]]])

;; Audio provided to or from an LLM.
(def AudioContent
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:annotations {:optional true} [:maybe Annotations]]
   [:data :string]
   [:mimeType :string]])

;; A resource that the server is capable of reading.
(def ResourceLink
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:annotations {:optional true} [:maybe Annotations]]
   [:description {:optional true} [:maybe :string]]
   [:mimeType {:optional true} [:maybe :string]]
   [:name :string]
   [:size {:optional true} [:maybe :int]]
   [:title {:optional true} [:maybe :string]]
   [:uri :string]])

;; Text-based resource contents.
(def TextResourceContents
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:mimeType {:optional true} [:maybe :string]]
   [:text :string]
   [:uri :string]])

;; Binary resource contents.
(def BlobResourceContents
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:blob :string]
   [:mimeType {:optional true} [:maybe :string]]
   [:uri :string]])

;; Resource content that can be embedded in a message.
(def EmbeddedResourceResource
  [:or TextResourceContents BlobResourceContents])

;; The contents of a resource, embedded into a prompt or tool call result.
(def EmbeddedResource
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:annotations {:optional true} [:maybe Annotations]]
   [:resource EmbeddedResourceResource]])

;; Content blocks represent displayable information in the Agent Client Protocol.
(def ContentBlock
  [:or
   [:map {:title "text"}
    [:type [:= "text"]]
    [:_meta {:optional true} [:maybe :map]]
    [:annotations {:optional true} [:maybe Annotations]]
    [:text :string]]
   [:map {:title "image"}
    [:type [:= "image"]]
    [:_meta {:optional true} [:maybe :map]]
    [:annotations {:optional true} [:maybe Annotations]]
    [:data :string]
    [:mimeType :string]
    [:uri {:optional true} [:maybe :string]]]
   [:map {:title "audio"}
    [:type [:= "audio"]]
    [:_meta {:optional true} [:maybe :map]]
    [:annotations {:optional true} [:maybe Annotations]]
    [:data :string]
    [:mimeType :string]]
   [:map {:title "resource_link"}
    [:type [:= "resource_link"]]
    [:_meta {:optional true} [:maybe :map]]
    [:annotations {:optional true} [:maybe Annotations]]
    [:description {:optional true} [:maybe :string]]
    [:mimeType {:optional true} [:maybe :string]]
    [:name :string]
    [:size {:optional true} [:maybe :int]]
    [:title {:optional true} [:maybe :string]]
    [:uri :string]]
   [:map {:title "resource"}
    [:type [:= "resource"]]
    [:_meta {:optional true} [:maybe :map]]
    [:annotations {:optional true} [:maybe Annotations]]
    [:resource EmbeddedResourceResource]]])

;; Standard content block wrapper.
(def Content
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:content ContentBlock]])

;; A streamed item of content.
(def ContentChunk
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:content ContentBlock]])

;; ===========================================================================
;; 4. TOOL TYPES (depends on ContentBlock, primitives)
;; ===========================================================================

;; A diff representing file modifications.
(def Diff
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:newText :string]
   [:oldText {:optional true} [:maybe :string]]
   [:path :string]])

;; Embed a terminal created with terminal/create by its id.
(def Terminal
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:terminalId :string]])

;; Content produced by a tool call.
(def ToolCallContent
  [:or
   [:map {:title "content"}
    [:type [:= "content"]]
    [:_meta {:optional true} [:maybe :map]]
    [:content ContentBlock]]
   [:map {:title "diff"}
    [:type [:= "diff"]]
    [:_meta {:optional true} [:maybe :map]]
    [:newText :string]
    [:oldText {:optional true} [:maybe :string]]
    [:path :string]]
   [:map {:title "terminal"}
    [:type [:= "terminal"]]
    [:_meta {:optional true} [:maybe :map]]
    [:terminalId :string]]])

;; A file location being accessed or modified by a tool.
(def ToolCallLocation
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:line {:optional true} [:maybe :int]]
   [:path :string]])

;; Represents a tool call that the language model has requested.
(def ToolCall
  [:map
   [:_meta {:optional true} [:maybe :map]]
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
   [:_meta {:optional true} [:maybe :map]]
   [:content {:optional true} [:maybe [:vector ToolCallContent]]]
   [:kind {:optional true} [:maybe ToolKind]]
   [:locations {:optional true} [:maybe [:vector ToolCallLocation]]]
   [:rawInput {:optional true} :any]
   [:rawOutput {:optional true} :any]
   [:status {:optional true} [:maybe ToolCallStatus]]
   [:title {:optional true} [:maybe :string]]
   [:toolCallId ToolCallId]])

;; ===========================================================================
;; 5. PLAN TYPES (depends on primitives)
;; ===========================================================================

;; A single entry in the execution plan.
(def PlanEntry
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:content :string]
   [:priority PlanEntryPriority]
   [:status PlanEntryStatus]])

;; An execution plan for accomplishing complex tasks.
(def Plan
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:entries [:vector PlanEntry]]])

;; ===========================================================================
;; 6. COMMAND TYPES
;; ===========================================================================

;; All text that was typed after the command name is provided as input.
(def UnstructuredCommandInput
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:hint :string]])

;; The input specification for a command.
(def AvailableCommandInput
  [:or
   [:map {:title "unstructured"}
    [:_meta {:optional true} [:maybe :map]]
    [:hint :string]]])

;; Information about a command.
(def AvailableCommand
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:description :string]
   [:input {:optional true} [:maybe AvailableCommandInput]]
   [:name :string]])

;; Available commands are ready or have changed.
(def AvailableCommandsUpdate
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:availableCommands [:vector AvailableCommand]]])

;; ===========================================================================
;; 7. SESSION CONFIG TYPES (primitives only, no circular deps)
;; ===========================================================================

;; A possible value for a session configuration option.
(def SessionConfigSelectOption
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:description {:optional true} [:maybe :string]]
   [:name :string]
   [:value SessionConfigValueId]])

;; A group of possible values for a session configuration option.
(def SessionConfigSelectGroup
  [:map
   [:_meta {:optional true} [:maybe :map]]
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
   [:_meta {:optional true} [:maybe :map]]
   [:category {:optional true} [:maybe SessionConfigOptionCategory]]
   [:description {:optional true} [:maybe :string]]
   [:id SessionConfigId]
   [:name :string]
   [:type {:optional true} [:= "select"]]
   [:currentValue {:optional true} SessionConfigValueId]
   [:options {:optional true} SessionConfigSelectOptions]])

;; ===========================================================================
;; 8. SESSION UPDATE SUB-TYPES (depends on all above)
;; ===========================================================================

;; The current mode of the session has changed.
(def CurrentModeUpdate
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:currentModeId SessionModeId]])

;; Session configuration options have been updated.
(def ConfigOptionUpdate
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:configOptions [:vector SessionConfigOption]]])

;; Update to session metadata. All fields are optional.
(def SessionInfoUpdate
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:title {:optional true} [:maybe :string]]
   [:updatedAt {:optional true} [:maybe :string]]])

;; Different types of updates that can be sent during session processing.
(def SessionUpdate
  [:or
   [:map {:title "user_message_chunk"}
    [:sessionUpdate [:= "user_message_chunk"]]
    [:_meta {:optional true} [:maybe :map]]
    [:content ContentBlock]]
   [:map {:title "agent_message_chunk"}
    [:sessionUpdate [:= "agent_message_chunk"]]
    [:_meta {:optional true} [:maybe :map]]
    [:content ContentBlock]]
   [:map {:title "agent_thought_chunk"}
    [:sessionUpdate [:= "agent_thought_chunk"]]
    [:_meta {:optional true} [:maybe :map]]
    [:content ContentBlock]]
   [:map {:title "tool_call"}
    [:sessionUpdate [:= "tool_call"]]
    [:_meta {:optional true} [:maybe :map]]
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
    [:_meta {:optional true} [:maybe :map]]
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
    [:_meta {:optional true} [:maybe :map]]
    [:entries [:vector PlanEntry]]]
   [:map {:title "available_commands_update"}
    [:sessionUpdate [:= "available_commands_update"]]
    [:_meta {:optional true} [:maybe :map]]
    [:availableCommands [:vector AvailableCommand]]]
   [:map {:title "current_mode_update"}
    [:sessionUpdate [:= "current_mode_update"]]
    [:_meta {:optional true} [:maybe :map]]
    [:currentModeId SessionModeId]]
   [:map {:title "config_option_update"}
    [:sessionUpdate [:= "config_option_update"]]
    [:_meta {:optional true} [:maybe :map]]
    [:configOptions [:vector SessionConfigOption]]]
   [:map {:title "session_info_update"}
    [:sessionUpdate [:= "session_info_update"]]
    [:_meta {:optional true} [:maybe :map]]
    [:title {:optional true} [:maybe :string]]
    [:updatedAt {:optional true} [:maybe :string]]]])

;; ===========================================================================
;; 9. ENV / HTTP HELPERS (primitives only)
;; ===========================================================================

;; An environment variable to set when launching an MCP server.
(def EnvVariable
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:name :string]
   [:value :string]])

;; An HTTP header to set when making requests to the MCP server.
(def HttpHeader
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:name :string]
   [:value :string]])

;; ===========================================================================
;; 10. MCP SERVER DEFINITIONS
;; ===========================================================================

;; HTTP transport configuration for MCP.
(def McpServerHttp
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:headers [:vector HttpHeader]]
   [:name :string]
   [:url :string]])

;; SSE transport configuration for MCP.
(def McpServerSse
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:headers [:vector HttpHeader]]
   [:name :string]
   [:url :string]])

;; Stdio transport configuration for MCP.
(def McpServerStdio
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:args [:vector :string]]
   [:command :string]
   [:env [:vector EnvVariable]]
   [:name :string]])

;; Configuration for connecting to an MCP (Model Context Protocol) server.
(def McpServer
  [:or
   [:map {:title "http"}
    [:type [:= "http"]]
    [:_meta {:optional true} [:maybe :map]]
    [:headers [:vector HttpHeader]]
    [:name :string]
    [:url :string]]
   [:map {:title "sse"}
    [:type [:= "sse"]]
    [:_meta {:optional true} [:maybe :map]]
    [:headers [:vector HttpHeader]]
    [:name :string]
    [:url :string]]
   McpServerStdio])

;; Backward compatibility alias.
(def McpServerDefinition McpServer)

;; ===========================================================================
;; 11. CAPABILITIES
;; ===========================================================================

;; File system capabilities that a client may support.
(def FileSystemCapabilities
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:readTextFile {:optional true} :boolean]
   [:writeTextFile {:optional true} :boolean]])

;; Capabilities supported by the client.
(def ClientCapabilities
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:fs {:optional true} FileSystemCapabilities]
   [:terminal {:optional true} :boolean]])

;; MCP capabilities supported by the agent.
(def McpCapabilities
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:http {:optional true} :boolean]
   [:sse {:optional true} :boolean]])

;; Prompt capabilities supported by the agent.
(def PromptCapabilities
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:audio {:optional true} :boolean]
   [:embeddedContext {:optional true} :boolean]
   [:image {:optional true} :boolean]])

;; Logout capabilities supported by the agent.
(def LogoutCapabilities
  [:map
   [:_meta {:optional true} [:maybe :map]]])

;; Authentication-related capabilities supported by the agent.
(def AgentAuthCapabilities
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:logout {:optional true} [:maybe LogoutCapabilities]]])

;; Session close capabilities.
(def SessionCloseCapabilities
  [:map
   [:_meta {:optional true} [:maybe :map]]])

;; Session list capabilities.
(def SessionListCapabilities
  [:map
   [:_meta {:optional true} [:maybe :map]]])

;; Session resume capabilities.
(def SessionResumeCapabilities
  [:map
   [:_meta {:optional true} [:maybe :map]]])

;; Session capabilities supported by the agent.
(def SessionCapabilities
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:close {:optional true} [:maybe SessionCloseCapabilities]]
   [:list {:optional true} [:maybe SessionListCapabilities]]
   [:resume {:optional true} [:maybe SessionResumeCapabilities]]])

;; Capabilities supported by the agent.
(def AgentCapabilities
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:auth {:optional true} AgentAuthCapabilities]
   [:loadSession {:optional true} :boolean]
   [:mcpCapabilities {:optional true} McpCapabilities]
   [:promptCapabilities {:optional true} PromptCapabilities]
   [:sessionCapabilities {:optional true} SessionCapabilities]])

;; ===========================================================================
;; 12. IMPLEMENTATION / AUTH / MODES (depend on capabilities)
;; ===========================================================================

;; Metadata about the implementation of the client or agent.
(def Implementation
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:name :string]
   [:title {:optional true} [:maybe :string]]
   [:version :string]])

;; Agent handles authentication itself.
(def AuthMethodAgent
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:description {:optional true} [:maybe :string]]
   [:id :string]
   [:name :string]])

;; Describes an available authentication method.
(def AuthMethod
  [:or AuthMethodAgent])

;; A mode the agent can operate in.
(def SessionMode
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:description {:optional true} [:maybe :string]]
   [:id SessionModeId]
   [:name :string]])

;; The set of modes and the one currently active.
(def SessionModeState
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:availableModes [:vector SessionMode]]
   [:currentModeId SessionModeId]])

;; ===========================================================================
;; 13. PERMISSION TYPES
;; ===========================================================================

;; An option presented to the user when requesting permission.
(def PermissionOption
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:kind PermissionOptionKind]
   [:name :string]
   [:optionId PermissionOptionId]])

;; The user selected one of the provided options.
(def SelectedPermissionOutcome
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:optionId PermissionOptionId]])

;; The outcome of a permission request.
(def RequestPermissionOutcome
  [:or
   [:map {:title "cancelled"}
    [:outcome [:= "cancelled"]]
    [:_meta {:optional true} [:maybe :map]]]
   [:map {:title "selected"}
    [:outcome [:= "selected"]]
    [:_meta {:optional true} [:maybe :map]]
    [:optionId PermissionOptionId]]])

;; ===========================================================================
;; 14. TERMINAL / ERROR / EXT TYPES
;; ===========================================================================

;; Exit status of a terminal command.
(def TerminalExitStatus
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:exitCode {:optional true} [:maybe :int]]
   [:signal {:optional true} [:maybe :string]]])

;; JSON-RPC error object.
(def Error
  [:map
   [:code ErrorCode]
   [:data {:optional true} :any]
   [:message :string]])

;; Allows the Agent/Client to send an arbitrary notification not part of ACP spec.
(def ExtNotification :map)

;; Allows for sending an arbitrary request not part of the ACP spec.
(def ExtRequest :map)

;; Allows for sending an arbitrary response not part of the ACP spec.
(def ExtResponse :map)

;; ===========================================================================
;; 15. REQUEST / RESPONSE TYPES (top-level method params/results)
;; ===========================================================================

;; ---------- Initialize ----------

;; Request parameters for the initialize method.
(def InitializeRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:clientCapabilities {:optional true} ClientCapabilities]
   [:clientInfo {:optional true} [:maybe Implementation]]
   [:protocolVersion ProtocolVersion]])

;; Response to the initialize method.
(def InitializeResponse
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:agentCapabilities {:optional true} AgentCapabilities]
   [:agentInfo {:optional true} [:maybe Implementation]]
   [:authMethods {:optional true} [:vector AuthMethod]]
   [:protocolVersion ProtocolVersion]])

;; ---------- Authenticate ----------

(def AuthenticateRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:methodId :string]])

(def AuthenticateResponse
  [:map
   [:_meta {:optional true} [:maybe :map]]])

;; ---------- Logout ----------

(def LogoutRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]])

(def LogoutResponse
  [:map
   [:_meta {:optional true} [:maybe :map]]])

;; ---------- Session: New ----------

(def NewSessionRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:cwd :string]
   [:mcpServers [:vector McpServer]]])

(def NewSessionResponse
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:configOptions {:optional true} [:maybe [:vector SessionConfigOption]]]
   [:modes {:optional true} [:maybe SessionModeState]]
   [:sessionId SessionId]])

;; ---------- Session: Load ----------

(def LoadSessionRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:cwd :string]
   [:mcpServers [:vector McpServer]]
   [:sessionId SessionId]])

(def LoadSessionResponse
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:configOptions {:optional true} [:maybe [:vector SessionConfigOption]]]
   [:modes {:optional true} [:maybe SessionModeState]]])

;; ---------- Session: List ----------

(def ListSessionsRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:cursor {:optional true} [:maybe :string]]
   [:cwd {:optional true} [:maybe :string]]])

;; Information about a session returned by session/list.
(def SessionInfo
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:cwd :string]
   [:sessionId SessionId]
   [:title {:optional true} [:maybe :string]]
   [:updatedAt {:optional true} [:maybe :string]]])

(def ListSessionsResponse
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:nextCursor {:optional true} [:maybe :string]]
   [:sessions [:vector SessionInfo]]])

;; ---------- Session: Resume ----------

(def ResumeSessionRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:cwd :string]
   [:mcpServers {:optional true} [:vector McpServer]]
   [:sessionId SessionId]])

(def ResumeSessionResponse
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:configOptions {:optional true} [:maybe [:vector SessionConfigOption]]]
   [:modes {:optional true} [:maybe SessionModeState]]])

;; ---------- Session: Close ----------

(def CloseSessionRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:sessionId SessionId]])

(def CloseSessionResponse
  [:map
   [:_meta {:optional true} [:maybe :map]]])

;; ---------- Session: Set Mode ----------

(def SetSessionModeRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:modeId SessionModeId]
   [:sessionId SessionId]])

(def SetSessionModeResponse
  [:map
   [:_meta {:optional true} [:maybe :map]]])

;; ---------- Session: Set Config Option ----------

(def SetSessionConfigOptionRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:configId SessionConfigId]
   [:sessionId SessionId]
   [:value SessionConfigValueId]])

(def SetSessionConfigOptionResponse
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:configOptions [:vector SessionConfigOption]]])

;; ---------- Session: Cancel ----------

(def CancelNotification
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:sessionId SessionId]])

;; ---------- Session: Prompt ----------

(def PromptRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:prompt [:vector ContentBlock]]
   [:sessionId SessionId]])

(def PromptResponse
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:stopReason StopReason]])

;; ---------- Session: Notification (streaming) ----------

;; Notification containing a session update from the agent.
(def SessionNotification
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:sessionId SessionId]
   [:update SessionUpdate]])

;; ---------- Session: Request Permission ----------

(def RequestPermissionRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:options [:vector PermissionOption]]
   [:sessionId SessionId]
   [:toolCall ToolCallUpdate]])

(def RequestPermissionResponse
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:outcome RequestPermissionOutcome]])

;; ---------- File System ----------

(def ReadTextFileRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:limit {:optional true} [:maybe :int]]
   [:line {:optional true} [:maybe :int]]
   [:path :string]
   [:sessionId SessionId]])

(def ReadTextFileResponse
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:content :string]])

(def WriteTextFileRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:content :string]
   [:path :string]
   [:sessionId SessionId]])

(def WriteTextFileResponse
  [:map
   [:_meta {:optional true} [:maybe :map]]])

;; ---------- Terminal ----------

(def CreateTerminalRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:args {:optional true} [:vector :string]]
   [:command :string]
   [:cwd {:optional true} [:maybe :string]]
   [:env {:optional true} [:vector EnvVariable]]
   [:outputByteLimit {:optional true} [:maybe :int]]
   [:sessionId SessionId]])

(def CreateTerminalResponse
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:terminalId :string]])

(def TerminalOutputRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:sessionId SessionId]
   [:terminalId :string]])

(def TerminalOutputResponse
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:exitStatus {:optional true} [:maybe TerminalExitStatus]]
   [:output :string]
   [:truncated :boolean]])

(def ReleaseTerminalRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:sessionId SessionId]
   [:terminalId :string]])

(def ReleaseTerminalResponse
  [:map
   [:_meta {:optional true} [:maybe :map]]])

(def WaitForTerminalExitRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:sessionId SessionId]
   [:terminalId :string]])

(def WaitForTerminalExitResponse
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:exitCode {:optional true} [:maybe :int]]
   [:signal {:optional true} [:maybe :string]]])

(def KillTerminalRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:sessionId SessionId]
   [:terminalId :string]])

(def KillTerminalResponse
  [:map
   [:_meta {:optional true} [:maybe :map]]])

;; ===========================================================================
;; 16. INTERNAL ROUTING TYPES (x-docs-ignore in JSON schema)
;; ===========================================================================

;; Internal: All possible notifications that an agent can send to a client.
(def AgentNotification
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:method :string]
   [:params {:optional true}
    [:maybe
     [:or
      SessionNotification
      ExtNotification]]]])

;; Internal: All possible requests that an agent can send to a client.
(def AgentRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:id RequestId]
   [:method :string]
   [:params {:optional true}
    [:maybe
     [:or
      WriteTextFileRequest
      ReadTextFileRequest
      RequestPermissionRequest
      CreateTerminalRequest
      TerminalOutputRequest
      ReleaseTerminalRequest
      WaitForTerminalExitRequest
      KillTerminalRequest
      ExtRequest]]]])

;; Internal: All possible responses that an agent can send to a client.
(def AgentResponse
  [:or
   [:map {:title "Result"}
    [:_meta {:optional true} [:maybe :map]]
    [:id RequestId]
    [:result
     [:or
      InitializeResponse
      AuthenticateResponse
      LogoutResponse
      NewSessionResponse
      LoadSessionResponse
      ListSessionsResponse
      ResumeSessionResponse
      CloseSessionResponse
      SetSessionModeResponse
      SetSessionConfigOptionResponse
      PromptResponse
      ExtResponse]]]
   [:map {:title "Error"}
    [:_meta {:optional true} [:maybe :map]]
    [:id RequestId]
    [:error Error]]])

;; Internal: All possible notifications that a client can send to an agent.
(def ClientNotification
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:method :string]
   [:params {:optional true}
    [:maybe
     [:or
      CancelNotification
      ExtNotification]]]])

;; Internal: All possible requests that a client can send to an agent.
(def ClientRequest
  [:map
   [:_meta {:optional true} [:maybe :map]]
   [:id RequestId]
   [:method :string]
   [:params {:optional true}
    [:maybe
     [:or
      InitializeRequest
      AuthenticateRequest
      LogoutRequest
      NewSessionRequest
      LoadSessionRequest
      ListSessionsRequest
      ResumeSessionRequest
      CloseSessionRequest
      SetSessionModeRequest
      SetSessionConfigOptionRequest
      PromptRequest
      ExtRequest]]]])

;; Internal: All possible responses that a client can send to an agent.
(def ClientResponse
  [:or
   [:map {:title "Result"}
    [:_meta {:optional true} [:maybe :map]]
    [:id RequestId]
    [:result
     [:or
      WriteTextFileResponse
      ReadTextFileResponse
      RequestPermissionResponse
      CreateTerminalResponse
      TerminalOutputResponse
      ReleaseTerminalResponse
      WaitForTerminalExitResponse
      KillTerminalResponse
      ExtResponse]]]
   [:map {:title "Error"}
    [:_meta {:optional true} [:maybe :map]]
    [:id RequestId]
    [:error Error]]])

;; ===========================================================================
;; 17. TOP-LEVEL JSON-RPC ENVELOPE TYPES
;; ===========================================================================

;; JSON-RPC 2.0 Request sent by agent to client.
(def AgentRequestEnvelope
  [:map
   [:jsonrpc [:= "2.0"]]
   [:id RequestId]
   [:method :string]
   [:params {:optional true}
    [:maybe
     [:or
      WriteTextFileRequest
      ReadTextFileRequest
      RequestPermissionRequest
      CreateTerminalRequest
      TerminalOutputRequest
      ReleaseTerminalRequest
      WaitForTerminalExitRequest
      KillTerminalRequest
      ExtRequest]]]])

;; JSON-RPC 2.0 Notification sent by agent to client.
(def AgentNotificationEnvelope
  [:map
   [:jsonrpc [:= "2.0"]]
   [:method :string]
   [:params {:optional true}
    [:maybe
     [:or
      SessionNotification
      ExtNotification]]]])

;; JSON-RPC 2.0 Response sent by agent to client.
(def AgentResponseEnvelope
  [:or
   [:map {:title "Result"}
    [:jsonrpc [:= "2.0"]]
    [:id RequestId]
    [:result
     [:or
      InitializeResponse
      AuthenticateResponse
      LogoutResponse
      NewSessionResponse
      LoadSessionResponse
      ListSessionsResponse
      ResumeSessionResponse
      CloseSessionResponse
      SetSessionModeResponse
      SetSessionConfigOptionResponse
      PromptResponse
      ExtResponse]]]
   [:map {:title "Error"}
    [:jsonrpc [:= "2.0"]]
    [:id RequestId]
    [:error Error]]])

;; JSON-RPC 2.0 Request sent by client to agent.
(def ClientRequestEnvelope
  [:map
   [:jsonrpc [:= "2.0"]]
   [:id RequestId]
   [:method :string]
   [:params {:optional true}
    [:maybe
     [:or
      InitializeRequest
      AuthenticateRequest
      LogoutRequest
      NewSessionRequest
      LoadSessionRequest
      ListSessionsRequest
      ResumeSessionRequest
      CloseSessionRequest
      SetSessionModeRequest
      SetSessionConfigOptionRequest
      PromptRequest
      ExtRequest]]]])

;; JSON-RPC 2.0 Notification sent by client to agent.
(def ClientNotificationEnvelope
  [:map
   [:jsonrpc [:= "2.0"]]
   [:method :string]
   [:params {:optional true}
    [:maybe
     [:or
      CancelNotification
      ExtNotification]]]])

;; JSON-RPC 2.0 Response sent by client to agent.
(def ClientResponseEnvelope
  [:or
   [:map {:title "Result"}
    [:jsonrpc [:= "2.0"]]
    [:id RequestId]
    [:result
     [:or
      WriteTextFileResponse
      ReadTextFileResponse
      RequestPermissionResponse
      CreateTerminalResponse
      TerminalOutputResponse
      ReleaseTerminalResponse
      WaitForTerminalExitResponse
      KillTerminalResponse
      ExtResponse]]]
   [:map {:title "Error"}
    [:jsonrpc [:= "2.0"]]
    [:id RequestId]
    [:error Error]]])
