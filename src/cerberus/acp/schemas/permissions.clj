(ns cerberus.acp.schemas.permissions
  "Permission schema definitions for the Agent Client Protocol (ACP).
   Models the permission request/response flow between agents and clients."
  (:require [malli.core :as malli]
            [cerberus.acp.schemas.primitives :refer [Meta PermissionOptionId PermissionOptionKind]]))

;; An option presented to the user when requesting permission.
(def PermissionOption
  [:map
   Meta
   [:kind PermissionOptionKind]
   [:name :string]
   [:optionId PermissionOptionId]])

;; The user selected one of the provided options.
(def SelectedPermissionOutcome
  [:map
   Meta
   [:optionId PermissionOptionId]])

;; The outcome of a permission request.
(def RequestPermissionOutcome
  [:or
   [:map {:title "cancelled"}
    [:outcome [:= "cancelled"]]
    Meta]
   [:map {:title "selected"}
    [:outcome [:= "selected"]]
    Meta
    [:optionId PermissionOptionId]]])
