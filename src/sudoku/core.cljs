(ns sudoku.core
    (:require [reagent.core :as reagent]))

(def board (reagent/atom [[5 3 0 0 7 0 0 0 0]
                          [6 0 0 1 9 5 0 0 0]
                          [0 9 8 0 0 0 0 6 0]
                          [8 0 0 0 6 0 0 0 3]
                          [4 0 0 8 0 3 0 0 1]
                          [7 0 0 0 2 0 0 0 6]
                          [0 6 0 0 0 0 2 8 0]
                          [0 0 0 4 1 9 0 0 5]
                          [0 0 0 0 8 0 0 7 9]]))

(defn value-at [board coord]
  (get-in board coord))

(defn set-value-at [board coord new-value]
  (assoc-in board coord new-value))
;; -------------------------
;; Components
(defn cell [coords]
  (let [value (reagent/atom (value-at @board coords))]
    [:td {:on-click #(swap! board set-value-at coords (inc @value))}
       (when (not (zero? @value)) @value)]))

(defn sudoku-board []
  [:table [:tbody (for [row (range 0 9)]
            [:tr (for [column (range 0 9)]
                   [cell [row column]])])]])

;; -------------------------
;; Views

(defn home-page []
  [:div [:h1 "Sudoku"]
   [sudoku-board]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
