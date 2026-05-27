(ns cerberus.acp.schemas.api-test
  (:require [clojure.test :refer :all]
            [malli.core :as malli]
            [cerberus.acp.schemas.api :as api]))

(deftest jsonrpc-error
  (testing "Valid, no data"
    (is (true? (malli/validate api/JSONRPCError {:code 3
                                                 :message "test message"}))))
  (testing "Missing message"
    (is (false? (malli/validate api/JSONRPCError {:code 3})))))
