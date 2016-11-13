(ns sudoku.core
  (:require [reagent.core :as reagent]
            [sudoku.lib-sudoku :as s]))

(def starting-board (s/generate-board))

(def board (reagent/atom starting-board))

(defn starting-value? [coords]
  (not (zero? (s/value-at starting-board coords))))

;; -------------------------
;; Components

(defn number-input [coords current-value]
  [:input.numberinput
   {:type "number"
    :pattern "[0-9]*"
    :inputmode "numeric"
    :value (when (not (zero? current-value)) current-value)
    :min 0
    :max 9
    :on-change (fn [e]
                 (let [new-value (.-target.value e)]
                   (when (< (count (str new-value)) 2)
                     (swap! board s/set-value-at coords
                            (int new-value)))))}])

(defn unchangeable-number [value]
  [:span.unchangeable value])

(defn cell [coords]
  (let [value (s/value-at @board coords)
        element (if (s/duplicate? @board coords value) :td :td.wrong)]
    [element (if (starting-value? coords)
               [unchangeable-number value]
               [number-input coords value])]))

(defn sudoku-board []
  [:div.sidebyside
   [:table
    [:tbody (for [row (range 0 9)]
              ^{:key row} [:tr (for [column (range 0 9)]
                                 ^{:key (str row column)}
                                 [cell [row column]])])]]])

(defn score-display []
  (fn []
    (when (s/valid-solution? @board)
      [:div.sidebyside [:h2 "You win!"]])))
;; -------------------------
;; Views

(defn home-page []
  [:div [:h1 "Sudoku"]
   [sudoku-board]
   [score-display]])

;; -------------------------
;; Initialize app

(defn mount-root []
  (reagent/render [home-page] (.getElementById js/document "app")))

(defn init! []
  (mount-root))
