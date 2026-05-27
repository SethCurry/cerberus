(ns cerberus.acp.schemas.routing
  "Internal routing schema definitions for the Agent Client Protocol (ACP).
   Union types that aggregate all possible requests, responses, and notifications
   an agent or client may send. Used for message dispatch."
  (:require [malli.core :as malli]
            [cerberus.acp.schemas.primitives :refer [RequestId Meta]]
            [cerberus.acp.schemas.api :refer [SessionNotification ExtNotification ExtRequest ExtResponse
                                              WriteTextFileRequest ReadTextFileRequest
                                              RequestPermissionRequest RequestPermissionResponse
                                              CreateTerminalRequest TerminalOutputRequest
                                              ReleaseTerminalRequest WaitForTerminalExitRequest
                                              KillTerminalRequest CreateTerminalResponse
                                              TerminalOutputResponse ReleaseTerminalResponse
                                              WaitForTerminalExitResponse KillTerminalResponse
                                              WriteTextFileResponse ReadTextFileResponse
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
                                              CancelNotification PromptRequest PromptResponse
                                              Error]]))

;; Internal: All possible notifications that an agent can send to a client.
(def AgentNotification
  [:map
   Meta
   [:method :string]
   [:params {:optional true}
    [:maybe
     [:or
      SessionNotification
      ExtNotification]]]])

;; Internal: All possible requests that an agent can send to a client.
(def AgentRequest
  [:map
   Meta
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
    Meta
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
    Meta
    [:id RequestId]
    [:error Error]]])

;; Internal: All possible notifications that a client can send to an agent.
(def ClientNotification
  [:map
   Meta
   [:method :string]
   [:params {:optional true}
    [:maybe
     [:or
      CancelNotification
      ExtNotification]]]])

;; Internal: All possible requests that a client can send to an agent.
(def ClientRequest
  [:map
   Meta
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
    Meta
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
    Meta
    [:id RequestId]
    [:error Error]]])
