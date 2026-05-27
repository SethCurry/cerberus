(ns cerberus.acp.schemas.api
  "API method request/response schema definitions for the Agent Client Protocol (ACP).
   Includes all method parameter and result types for both client→agent and agent→client
   communication, plus shared types like Error and terminal exit status."
  (:require [malli.core :as malli]
            [cerberus.acp.schemas.primitives :refer [Meta ProtocolVersion StopReason SessionId
                                                     SessionModeId SessionConfigId SessionConfigValueId
                                                     ErrorCode RequestId]]
            [cerberus.acp.schemas.content :refer [ContentBlock]]
            [cerberus.acp.schemas.tools :refer [ToolCallUpdate]]
            [cerberus.acp.schemas.mcp :refer [McpServer EnvVariable]]
            [cerberus.acp.schemas.capabilities :refer [ClientCapabilities AgentCapabilities]]
            [cerberus.acp.schemas.implementation :refer [Implementation AuthMethod
                                                         SessionMode SessionModeState]]
            [cerberus.acp.schemas.permissions :refer [PermissionOption RequestPermissionOutcome]]
            [cerberus.acp.schemas.session :refer [SessionUpdate SessionConfigOption
                                                  SessionConfigSelectOptions]]))

;; ===========================================================================
;; TERMINAL / ERROR / EXT TYPES
;; ===========================================================================

;; Exit status of a terminal command.
(def TerminalExitStatus
  [:map
   Meta
   [:exitCode {:optional true} [:maybe :int]]
   [:signal {:optional true} [:maybe :string]]])

;; JSON-RPC error object.
(def JSONRPCError
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
;; REQUEST / RESPONSE TYPES
;; ===========================================================================

;; ---------- Initialize ----------

;; Request parameters for the initialize method.
(def InitializeRequest
  [:map
   Meta
   [:clientCapabilities {:optional true} ClientCapabilities]
   [:clientInfo {:optional true} [:maybe Implementation]]
   [:protocolVersion ProtocolVersion]])

;; Response to the initialize method.
(def InitializeResponse
  [:map
   Meta
   [:agentCapabilities {:optional true} AgentCapabilities]
   [:agentInfo {:optional true} [:maybe Implementation]]
   [:authMethods {:optional true} [:vector AuthMethod]]
   [:protocolVersion ProtocolVersion]])

;; ---------- Authenticate ----------

(def AuthenticateRequest
  [:map
   Meta
   [:methodId :string]])

(def AuthenticateResponse
  [:map
   Meta])

;; ---------- Logout ----------

(def LogoutRequest
  [:map
   Meta])

(def LogoutResponse
  [:map
   Meta])

;; ---------- Session: New ----------

(def NewSessionRequest
  [:map
   Meta
   [:cwd :string]
   [:mcpServers [:vector McpServer]]])

(def NewSessionResponse
  [:map
   Meta
   [:configOptions {:optional true} [:maybe [:vector SessionConfigOption]]]
   [:modes {:optional true} [:maybe SessionModeState]]
   [:sessionId SessionId]])

;; ---------- Session: Load ----------

(def LoadSessionRequest
  [:map
   Meta
   [:cwd :string]
   [:mcpServers [:vector McpServer]]
   [:sessionId SessionId]])

(def LoadSessionResponse
  [:map
   Meta
   [:configOptions {:optional true} [:maybe [:vector SessionConfigOption]]]
   [:modes {:optional true} [:maybe SessionModeState]]])

;; ---------- Session: List ----------

(def ListSessionsRequest
  [:map
   Meta
   [:cursor {:optional true} [:maybe :string]]
   [:cwd {:optional true} [:maybe :string]]])

;; Information about a session returned by session/list.
(def SessionInfo
  [:map
   Meta
   [:cwd :string]
   [:sessionId SessionId]
   [:title {:optional true} [:maybe :string]]
   [:updatedAt {:optional true} [:maybe :string]]])

(def ListSessionsResponse
  [:map
   Meta
   [:nextCursor {:optional true} [:maybe :string]]
   [:sessions [:vector SessionInfo]]])

;; ---------- Session: Resume ----------

(def ResumeSessionRequest
  [:map
   Meta
   [:cwd :string]
   [:mcpServers {:optional true} [:vector McpServer]]
   [:sessionId SessionId]])

(def ResumeSessionResponse
  [:map
   Meta
   [:configOptions {:optional true} [:maybe [:vector SessionConfigOption]]]
   [:modes {:optional true} [:maybe SessionModeState]]])

;; ---------- Session: Close ----------

(def CloseSessionRequest
  [:map
   Meta
   [:sessionId SessionId]])

(def CloseSessionResponse
  [:map
   Meta])

;; ---------- Session: Set Mode ----------

(def SetSessionModeRequest
  [:map
   Meta
   [:modeId SessionModeId]
   [:sessionId SessionId]])

(def SetSessionModeResponse
  [:map
   Meta])

;; ---------- Session: Set Config Option ----------

(def SetSessionConfigOptionRequest
  [:map
   Meta
   [:configId SessionConfigId]
   [:sessionId SessionId]
   [:value SessionConfigValueId]])

(def SetSessionConfigOptionResponse
  [:map
   Meta
   [:configOptions [:vector SessionConfigOption]]])

;; ---------- Session: Cancel ----------

(def CancelNotification
  [:map
   Meta
   [:sessionId SessionId]])

;; ---------- Session: Prompt ----------

(def PromptRequest
  [:map
   Meta
   [:prompt [:vector ContentBlock]]
   [:sessionId SessionId]])

(def PromptResponse
  [:map
   Meta
   [:stopReason StopReason]])

;; ---------- Session: Notification (streaming) ----------

;; Notification containing a session update from the agent.
(def SessionNotification
  [:map
   Meta
   [:sessionId SessionId]
   [:update SessionUpdate]])

;; ---------- Session: Request Permission ----------

(def RequestPermissionRequest
  [:map
   Meta
   [:options [:vector PermissionOption]]
   [:sessionId SessionId]
   [:toolCall ToolCallUpdate]])

(def RequestPermissionResponse
  [:map
   Meta
   [:outcome RequestPermissionOutcome]])

;; ---------- File System ----------

(def ReadTextFileRequest
  [:map
   Meta
   [:limit {:optional true} [:maybe :int]]
   [:line {:optional true} [:maybe :int]]
   [:path :string]
   [:sessionId SessionId]])

(def ReadTextFileResponse
  [:map
   Meta
   [:content :string]])

(def WriteTextFileRequest
  [:map
   Meta
   [:content :string]
   [:path :string]
   [:sessionId SessionId]])

(def WriteTextFileResponse
  [:map
   Meta])

;; ---------- Terminal ----------

(def CreateTerminalRequest
  [:map
   Meta
   [:args {:optional true} [:vector :string]]
   [:command :string]
   [:cwd {:optional true} [:maybe :string]]
   [:env {:optional true} [:vector EnvVariable]]
   [:outputByteLimit {:optional true} [:maybe :int]]
   [:sessionId SessionId]])

(def CreateTerminalResponse
  [:map
   Meta
   [:terminalId :string]])

(def TerminalOutputRequest
  [:map
   Meta
   [:sessionId SessionId]
   [:terminalId :string]])

(def TerminalOutputResponse
  [:map
   Meta
   [:exitStatus {:optional true} [:maybe TerminalExitStatus]]
   [:output :string]
   [:truncated :boolean]])

(def ReleaseTerminalRequest
  [:map
   Meta
   [:sessionId SessionId]
   [:terminalId :string]])

(def ReleaseTerminalResponse
  [:map
   Meta])

(def WaitForTerminalExitRequest
  [:map
   Meta
   [:sessionId SessionId]
   [:terminalId :string]])

(def WaitForTerminalExitResponse
  [:map
   Meta
   [:exitCode {:optional true} [:maybe :int]]
   [:signal {:optional true} [:maybe :string]]])

(def KillTerminalRequest
  [:map
   Meta
   [:sessionId SessionId]
   [:terminalId :string]])

(def KillTerminalResponse
  [:map
   Meta])

(defn get-message-schema [message-data]
    (let [message-method (get message-data :method)]
      (case message-method
        ;; Initialize
        "initialize" InitializeRequest
        ;; Authentication
        "authenticate" AuthenticateRequest
        ;; Logout
        "logout" LogoutRequest
        ;; Session methods
        "session/new" NewSessionRequest
        "session/load" LoadSessionRequest
        "session/list" ListSessionsRequest
        "session/resume" ResumeSessionRequest
        "session/close" CloseSessionRequest
        "session/prompt" PromptRequest
        "session/cancel" CancelNotification
        "session/update" SessionUpdate
        "session/request_permission" RequestPermissionRequest
        "session/set_mode" SetSessionModeRequest
        "session/set_config_option" SetSessionConfigOptionRequest
        ;; File system methods
        "fs/read_text_file" ReadTextFileRequest
        "fs/write_text_file" WriteTextFileRequest
        ;; Terminal methods
        "terminal/create" CreateTerminalRequest
        "terminal/output" TerminalOutputRequest
        "terminal/release" ReleaseTerminalRequest
        "terminal/wait_for_exit" WaitForTerminalExitRequest
        "terminal/kill" KillTerminalRequest
        (throw (ex-info "Unknown JSONRPC method"
          {:method message-method})))))
