(ns frontend.ui.login-panel
  (:require
    [backend.auth :as account]
    #?@(:cljs [[com.fulcrologic.semantic-ui.modules.modal.ui-modal :refer [ui-modal]]
               [com.fulcrologic.semantic-ui.modules.modal.ui-modal-header :refer [ui-modal-header]]
               [com.fulcrologic.semantic-ui.modules.modal.ui-modal-content :refer [ui-modal-content]]])
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]
    [com.fulcrologic.rad.authorization :as auth]
    [com.fulcrologic.fulcro.dom :refer [div label input]]
    [com.fulcrologic.fulcro.mutations :as m]
    [taoensso.timbre :as log]))

(defsc LoginPanel
  []
  {:query               [:ui/username
                         :ui/password]
   :initial-state       {:ui/username ""
                         :ui/password ""}

   ::auth/provider      :local
   ::auth/check-session `account/check-session
   ::auth/logout        `account/logout

   :ident               (fn [] [:component/id ::LoginForm])})

(defsc PasswordRecoveryPanel)
