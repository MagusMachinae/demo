(ns frontend.ui.login-panel
  (:require
    [demo.backend.auth :as account]
    #?@(:cljs [[com.fulcrologic.semantic-ui.modules.modal.ui-modal :refer [ui-modal]]
               [com.fulcrologic.semantic-ui.modules.modal.ui-modal-header :refer [ui-modal-header]]
               [com.fulcrologic.semantic-ui.modules.modal.ui-modal-content :refer [ui-modal-content]]])
    [com.fulcrologic.fulcro.components :as comp :refer [defsc]]

    [com.fulcrologic.fulcro.dom :refer all]
    [com.fulcrologic.fulcro.mutations :as m]
    [taoensso.timbre :as log]))

    (defsc PasswordRecoveryPanel
      []
      {:query [:ui/username
               :ui/password]
       :initial-state {:ui/new-password1 ""
                       :ui/new-password2 ""}}
      (div (form
            (label "Enter your new password")
            (input {:type     "new-pass1"
                    :onChange (fn [evt] (m/set-string! this :ui/new-pass1 :event evt))
                    :value    (or username "")})
            (label "Re-enter your new password")
            (input {:type     "new-pass2"
                    :onChange (fn [evt] (m/set-string! this :ui/new-pass2 :event evt))
                    :value    (or username "")})
            (button {:onClick (fn [] (comp/transact! this [(account/change-password {:username username :password password})]))}))))

(defsc LoginPanel
  []
  {:query               [:ui/username
                         :ui/password]
   :initial-state       {:ui/username ""
                         :ui/password ""}

   ::auth/provider      :local
   ::auth/check-session `account/check-session
   ::auth/logout        `account/logout

   :ident               (fn [] [:component/id ::LoginForm])}
  (div (form
         (label "Username")
         (input {:type     "username"
                 :onChange (fn [evt] (m/set-string! this :ui/username :event evt))
                 :value    (or username "")})
         (label "Password")
         (input {:type     "password"
                 :onChange (fn [evt] (m/set-string! this :ui/username :event evt))
                 :value    (or password "")})
         (button {:onClick (fn [] (comp/transact! this [(account/login {:username username :password password})]))})
         (button {:onClick (fn [] (comp/transact! this [(account/change-password {:username username :password password})]))}))))
