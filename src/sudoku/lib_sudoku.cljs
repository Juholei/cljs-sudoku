(ns sudoku.lib-sudoku
  (:require [clojure.set :as set]))

(def all-values #{1 2 3 4 5 6 7 8 9})

(defn value-at [board coord]
  (get-in board coord))

(defn set-value-at [board coord new-value]
  (assoc-in board coord new-value))

(defn- row-values [board coord]
  (let [[row _] coord]
    (set (get-in board [row]))))

(defn valid-row? [board coord]
  (= all-values (row-values board coord)))
