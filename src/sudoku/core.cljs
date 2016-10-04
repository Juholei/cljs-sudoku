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

(def selected-cell (reagent/atom {:coords [0 0]}))

(defn value-at [board coord]
  (get-in board coord))

(defn set-value-at [board coord new-value]
  (assoc-in board coord new-value))
;; -------------------------
;; Components

(defn number-selector []
  [:div (for [number (range 0 10)]
          [:button
            {:on-click #(swap! board set-value-at (:coords @selected-cell) number)}
            number])])

(defn cell [coords]
  (let [value (reagent/atom (value-at @board coords))]
    [:td {:on-click #(swap! selected-cell assoc :coords coords)}
       (when (not (zero? @value)) @value)]))

(defn sudoku-board []
  [:table [:tbody (for [row (range 0 9)]
            [:tr (for [column (range 0 9)]
                   [cell [row column]])])]])

;; -------------------------
;; Views

(defn home-page []
  [:div [:h1 "Sudoku"]
   [sudoku-board]
   [number-selector]
   [:h1 (apply concat (map str (map inc (:coords @selected-cell))))]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
