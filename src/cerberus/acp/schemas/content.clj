(ns cerberus.acp.schemas.content
  "Content and resource schema definitions for the Agent Client Protocol (ACP).
   Includes annotations, text/image/audio content blocks, resource references,
   and embedded resources."
  (:require [malli.core :as malli]
            [cerberus.acp.schemas.primitives :refer [Meta Role]]))

;; ===========================================================================
;; ANNOTATIONS
;; ===========================================================================

;; Optional annotations for the client.
(def Annotations
  [:map
   Meta
   [:audience {:optional true} [:maybe [:vector Role]]]
   [:lastModified {:optional true} [:maybe :string]]
   [:priority {:optional true} [:maybe :double]]])

;; ===========================================================================
;; CONTENT TYPES
;; ===========================================================================

;; Text provided to or from an LLM.
(def TextContent
  [:map
   Meta
   [:annotations {:optional true} [:maybe Annotations]]
   [:text :string]])

;; An image provided to or from an LLM.
(def ImageContent
  [:map
   Meta
   [:annotations {:optional true} [:maybe Annotations]]
   [:data :string]
   [:mimeType :string]
   [:uri {:optional true} [:maybe :string]]])

;; Audio provided to or from an LLM.
(def AudioContent
  [:map
   Meta
   [:annotations {:optional true} [:maybe Annotations]]
   [:data :string]
   [:mimeType :string]])

;; A resource that the server is capable of reading.
(def ResourceLink
  [:map
   Meta
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
   Meta
   [:mimeType {:optional true} [:maybe :string]]
   [:text :string]
   [:uri :string]])

;; Binary resource contents.
(def BlobResourceContents
  [:map
   Meta
   [:blob :string]
   [:mimeType {:optional true} [:maybe :string]]
   [:uri :string]])

;; Resource content that can be embedded in a message.
(def EmbeddedResourceResource
  [:or TextResourceContents BlobResourceContents])

;; The contents of a resource, embedded into a prompt or tool call result.
(def EmbeddedResource
  [:map
   Meta
   [:annotations {:optional true} [:maybe Annotations]]
   [:resource EmbeddedResourceResource]])

;; Content blocks represent displayable information in the Agent Client Protocol.
(def ContentBlock
  [:or
   [:map {:title "text"}
    [:type [:= "text"]]
    Meta
    [:annotations {:optional true} [:maybe Annotations]]
    [:text :string]]
   [:map {:title "image"}
    [:type [:= "image"]]
    Meta
    [:annotations {:optional true} [:maybe Annotations]]
    [:data :string]
    [:mimeType :string]
    [:uri {:optional true} [:maybe :string]]]
   [:map {:title "audio"}
    [:type [:= "audio"]]
    Meta
    [:annotations {:optional true} [:maybe Annotations]]
    [:data :string]
    [:mimeType :string]]
   [:map {:title "resource_link"}
    [:type [:= "resource_link"]]
    Meta
    [:annotations {:optional true} [:maybe Annotations]]
    [:description {:optional true} [:maybe :string]]
    [:mimeType {:optional true} [:maybe :string]]
    [:name :string]
    [:size {:optional true} [:maybe :int]]
    [:title {:optional true} [:maybe :string]]
    [:uri :string]]
   [:map {:title "resource"}
    [:type [:= "resource"]]
    Meta
    [:annotations {:optional true} [:maybe Annotations]]
    [:resource EmbeddedResourceResource]]])

;; Standard content block wrapper.
(def Content
  [:map
   Meta
   [:content ContentBlock]])

;; A streamed item of content.
(def ContentChunk
  [:map
   Meta
   [:content ContentBlock]])
