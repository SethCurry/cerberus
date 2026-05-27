(ns cerberus.acp.schemas.plan
  "Plan schema definitions for the Agent Client Protocol (ACP).
   Plans represent structured execution strategies for complex agent tasks."
  (:require [malli.core :as malli]
            [cerberus.acp.schemas.primitives :refer [Meta PlanEntryPriority PlanEntryStatus]]))

;; A single entry in the execution plan.
(def PlanEntry
  [:map
   Meta
   [:content :string]
   [:priority PlanEntryPriority]
   [:status PlanEntryStatus]])

;; An execution plan for accomplishing complex tasks.
(def Plan
  [:map
   Meta
   [:entries [:vector PlanEntry]]])
