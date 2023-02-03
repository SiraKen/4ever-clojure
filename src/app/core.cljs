(ns app.core
  (:require [app.routes :as routes]
            [reagent.dom :as rdom]
            [reitit.frontend.easy :as rfe]))

(def notification
  [:div {:style {:background-color "#D6D0FD"
                 :position "absolute"
                 :top 0
                 :left 0
                 :width "100%"
                 :text-align "center"}}
   [:div {:style {:padding "0.5rem"}}
    [:small
     "各問題のページから解答アーカイブが閲覧できます🎉 Alanさんに大感謝です！"]]])

(defn header []
  [:header
   [:h1 "4ever-clojure"]
   [:p
    [:small
     [:a {:href (rfe/href :home)
          :data-reitit-handle-click false} "ホーム"]
     " | "
     [:a {:href "https://github.com/oxalorg/4ever-clojure"} "GitHub"]
     " | "
     [:a {:href "https://twitter.com/oxalorg"} "@oxalorg"]
     "と"
     [:a {:href "https://twitter.com/borkdude"} "@borkdude"]
     "より❤を込めて"
     " | "
     "翻訳: "
     [:a {:href "https://twitter.com/shirasawa_kento"} "@shirasawa_kento"]]]
   notification])

(defn main []
  [:div
   [header]
   (when-let [match @routes/match]
     (let [view (:view (:data match))]
       [view match]))])

(defn ^:dev/after-load mount []
  (rdom/render
   [main]
   (js/document.getElementById "app")))

(defn init! []
  (routes/init!)
  (mount))

(init!)
