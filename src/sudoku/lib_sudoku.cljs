(ns sudoku.lib-sudoku
  (:require [clojure.set :as set]))

(def all-values #{1 2 3 4 5 6 7 8 9})

(defn value-at [board coord]
  (get-in board coord))

(defn set-value-at [board coord new-value]
  (assoc-in board coord new-value))

(defn- row-values [board coord]
  (let [[row _] coord]
    (get-in board [row])))

(defn- col-values [board coord]
  (let [[_ col] coord
        column-values (fn [acc row]
                        (conj acc (get-in row [col])))]
    (reduce column-values '() board)))

(defn- block-top-left [coords]
  (let [[x y] coords]
    [(* 3 (int (/ x 3))) (* 3 (int (/ y 3)))]))

(defn- block-values [board coord]
  (let [[x y] (block-top-left coord)
        coords (for [offset-x [0 1 2]
                     offset-y [0 1 2]]
                 [(+ x offset-x) (+ y offset-y)])
        values (fn [acc xy]
                 (conj acc (value-at board xy)))]
    (reduce values '() coords)))

(defn contains-duplicates? [a-seq]
  (> (count a-seq)
     (count (set a-seq))))

(defn duplicate? [board coord value]
  (not (or (> (get (frequencies (filter pos? (row-values board coord))) value) 1)
           (> (get (frequencies (filter pos? (col-values board coord))) value) 1)
           (> (get (frequencies (filter pos? (block-values board coord))) value) 1))))

(defn- rows [board]
  (for [row (range 0 9)]
    (row-values board [row 0])))

(defn- cols [board]
  (for [col (range 0 9)]
    (col-values board [0 col])))

(defn- blocks [board]
  (for [row [0 3 6]
        col [0 3 6]]
    (block-values board [row col])))

(defn- valid-rows? [board]
  (every? true? (for [row (rows board)]
                  (= (set row) all-values))))

(defn- valid-cols? [board]
  (every? true? (for [col (cols board)]
                  (= (set col) all-values))))

(defn- valid-blocks? [board]
  (every? true? (for [block (blocks board)]
                  (= (set block) all-values))))

(defn valid-solution? [board]
  (and (valid-rows? board)
       (valid-cols? board)
       (valid-blocks? board)))
