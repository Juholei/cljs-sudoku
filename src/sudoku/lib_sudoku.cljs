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

(defn valid-row? [board coord]
  (= all-values (row-values board coord)))

(defn valid-col? [board coord]
  (= all-values (col-values board coord)))

(defn valid-block? [board coord]
  (= all-values (block-values board coord)))

(defn valid-solution? [board coord]
  (and (valid-row? board coord)
       (valid-col? board coord)
       (valid-block? board coord)))

(defn contains-duplicates? [a-seq]
  (> (count a-seq)
     (count (set a-seq))))

(defn duplicate? [board coord value]
  (not (or (> (get (frequencies (filter pos? (row-values board coord))) value) 1)
            (> (get (frequencies (filter pos? (col-values board coord))) value) 1)
            (> (get (frequencies (filter pos? (block-values board coord))) value) 1))))