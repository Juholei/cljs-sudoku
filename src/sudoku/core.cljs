(ns sudoku.core
    (:require [reagent.core :as reagent]))

(def board [[5 3 0 0 7 0 0 0 0]
            [6 0 0 1 9 5 0 0 0]
            [0 9 8 0 0 0 0 6 0]
            [8 0 0 0 6 0 0 0 3]
            [4 0 0 8 0 3 0 0 1]
            [7 0 0 0 2 0 0 0 6]
            [0 6 0 0 0 0 2 8 0]
            [0 0 0 4 1 9 0 0 5]
            [0 0 0 0 8 0 0 7 9]])

(defn value-at [board coord]
  (get-in board coord))
;; -------------------------
;; Components
(defn cell [coords]
  (let [value (value-at board coords)]
    [:td {:on-click #(js/console.log (first coords) (second coords))}
      (when (not (zero? value)) value)]))

(defn sudoku-board []
  [:table (for [row (range 0 10)]
            [:tr (for [column (range 0 10)]
                   [cell [row column]])])])

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
