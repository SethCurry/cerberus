(ns cerberus.acp.schemas.envelopes
  "JSON-RPC 2.0 envelope schema definitions for the Agent Client Protocol (ACP).
   Wraps method-specific params in the standard JSON-RPC request/response/notification
   format, including the mandatory jsonrpc version field."
  (:require [malli.core :as malli]
            [cerberus.acp.schemas.primitives :refer [RequestId]]
            [cerberus.acp.schemas.api :refer [SessionNotification ExtNotification ExtRequest ExtResponse
                                              JSONRPCError
                                              WriteTextFileRequest ReadTextFileRequest
                                              RequestPermissionRequest
                                              CreateTerminalRequest TerminalOutputRequest
                                              ReleaseTerminalRequest WaitForTerminalExitRequest
                                              KillTerminalRequest
                                              WriteTextFileResponse ReadTextFileResponse
                                              RequestPermissionResponse
                                              CreateTerminalResponse TerminalOutputResponse
                                              ReleaseTerminalResponse
                                              WaitForTerminalExitResponse KillTerminalResponse
                                              InitializeRequest InitializeResponse
                                              AuthenticateRequest AuthenticateResponse
                                              LogoutRequest LogoutResponse
                                              NewSessionRequest NewSessionResponse
                                              LoadSessionRequest LoadSessionResponse
                                              ListSessionsRequest ListSessionsResponse
                                              ResumeSessionRequest ResumeSessionResponse
                                              CloseSessionRequest CloseSessionResponse
                                              SetSessionModeRequest SetSessionModeResponse
                                              SetSessionConfigOptionRequest SetSessionConfigOptionResponse
                                              CancelNotification PromptRequest PromptResponse]]))

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
    [:error JSONRPCError]]])

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
    [:error JSONRPCError]]])
