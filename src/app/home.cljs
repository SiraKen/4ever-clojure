(ns app.home
  (:require [app.data :as data]
            [app.state :as state :refer [db]]
            [reagent.core :as r]))

(def user-data (r/cursor db [:solutions]))

(def sort-by-solved (r/cursor db [:sort-by-solved]))

(defn sorted-problems []
  (let [data-state
        (map #(assoc % :solution (get @user-data (:id %)))
             data/problems)
        sorted (if (nil? @sort-by-solved)
                 (sort-by :id data-state)
                 (sort-by :solution #(not (nil? %)) data-state))]
    (if (false? @sort-by-solved) (reverse sorted) sorted)))

(defn get-problem-status [id]
  (let [{:keys [passed failed]}
        (get @user-data (js/parseInt id))
        progress (str passed "/" (+ passed failed))]
    (cond
      (and passed (zero? failed))
      [:span {:style {:color "green"}} (str progress " 合格！")]
      (not (nil? passed)) progress
      :else "-")))

(defn problem-list-item [{:keys [id title _tags difficulty]}]
  [:tr
   [:td id]
   [:td
    [:a {:href (state/href :problem/item {:id id})}
     title]]
   [:td difficulty]
   [:td (get-problem-status id)]])

(defn problem-list []
  [:<>
   [:h3 "問題 "
    [:small (str "(" (count data/problems) ")")]]
   (into [:table
          [:thead
           [:tr
            [:th {:on-click #(swap! sort-by-solved (fn [] nil))} "No."]
            [:th "名前"]
            [:th "難易度"]
            [:th
             {:on-click #(swap! sort-by-solved not)}
             (str "ステータス  " (case @sort-by-solved
                              true "🠕" false "🠗" nil ""))]]]
          [:tbody
           (for [problem (sorted-problems)]
             ^{:key (:id problem)}
             [problem-list-item problem])]])])

(defn view []
  [:div
   [:p
    "4clojureは永遠に！このウェブサイトは完全に静的で、sciを使ってコードを検証しています。
     提案やプルリクエストは"
    [:a {:href "https://github.com/oxalorg/4ever-clojure"}
     "github.com/oxalorg/4ever-clojure"]
    "まで！"]
   [:p
    "4ever-clojureは完全にブラウザで評価されることに注意してください。
     そのため、すべてのJavaの相互運用がうまくいくわけではありませんが、運が良ければJSでも同じように使えるものもあります。
     詳細は"
    [:a {:href "https://cljs.info/cheatsheet/"} "cljs-cheatsheet"]
    "をチェックしてください！"]
   [problem-list]])
